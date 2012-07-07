/**
 *
 * Shire - Blog aware static site generator 
 * Copyright (c) 2012, Sandeep Gupta
 * 
 * http://www.sangupta/projects/shire
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.sangupta.shire.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.sangupta.shire.Shire;

/**
 * @author sangupta
 *
 */
public class HttpUtil {
	
	/**
	 * Time in seconds in which connections held by {@link HttpUtil} and {@link AuthenticatedHttpUtil}
	 * will be timed out and the call rejected.
	 */
	public static final int CONNECTION_TIME_OUT = 300;
	
	/**
	 * Time in seconds in which calls via {@link HttpUtil} and {@link AuthenticatedHttpUtil} will be failed
	 * if no packets are received on the socket.
	 */
	public static final int SOCKET_TIME_OUT = 180;
	
	/**
	 * The last modified header name as per HTTP standards.
	 */
	public static final String LAST_MODIFIED_HEADER = "Last-Modified";
	
	/**
	 * Number of entries to cache when fetching URLs via the {@link HttpUtil} and {@link AuthenticatedHttpUtil}
	 */
	public static final int MAX_CACHE_ENTRIES = 10000;
	
	/**
	 * The maximum size of the entry in bytes which will be cached by the {@link HttpUtil} and {@link AuthenticatedHttpUtil}
	 */
	public static final int MAX_CACHE_ENTRY_SIZE = 32768;
	
	/**
	 * Defines the maximum number of total connections we will maintain
	 */
	private static final int MAX_CONNECTIONS_PER_ROUTE = 100;

	/**
	 * Defines the maximum number of connections per route that can be opened.
	 */
	private static final int MAX_TOTAL_CONNECTIONS = 500;

    // Create an HttpClient with the ThreadSafeClientConnManager.
    // This connection manager must be used if more than one thread will
    // be using the HttpClient.
    private static ThreadSafeClientConnManager HTTP_CONNECTION_MANAGER = null;

    private static HttpClient httpClient = null;
    
    private static final Log logger = LogFactory.getLog(HttpUtil.class);
    
    static {
    	HTTP_CONNECTION_MANAGER = new ThreadSafeClientConnManager();
    	HTTP_CONNECTION_MANAGER.setMaxTotal(MAX_TOTAL_CONNECTIONS);
    	HTTP_CONNECTION_MANAGER.setDefaultMaxPerRoute(MAX_CONNECTIONS_PER_ROUTE);
    	
    	CacheConfig cacheConfig = new CacheConfig();  
    	cacheConfig.setMaxCacheEntries(MAX_CACHE_ENTRIES);
    	cacheConfig.setMaxObjectSizeBytes(MAX_CACHE_ENTRY_SIZE);
    	
    	httpClient = new CachingHttpClient(new DefaultHttpClient(HTTP_CONNECTION_MANAGER), cacheConfig);
    	
    	SSLSocketFactory sf = (SSLSocketFactory)httpClient.getConnectionManager().getSchemeRegistry().getScheme("https").getSchemeSocketFactory();
    	sf.setHostnameVerifier(new AllowAllHostnameVerifier());
    	
    	HttpParams httpParams = httpClient.getParams();
    	httpParams.setIntParameter("http.socket.timeout", SOCKET_TIME_OUT * 1000);
    	httpParams.setIntParameter("http.connection.timeout", CONNECTION_TIME_OUT * 1000);
    }

    public static WebResponse getUrlResponse(String url) {
    	if(!Shire.getExecutionOptions().isUseCache()) {
    		return getResponseWithoutCacheCheck(url);
    	}
    	
    	return WebResponseCacheUtil.doWithCache(url);
	}
    
    public static WebResponse getResponseWithoutCacheCheck(String url) {
    	WebResponse webResponse = null;
		try {
	    	HttpGet get = new HttpGet(url);
	    	HttpResponse response = httpClient.execute(get);
	    	HttpEntity entity = response.getEntity();
	    	
	    	webResponse = new WebResponse();
	    	webResponse.setResponseBody(EntityUtils.toString(entity));
	    	webResponse.setResponseCode(response.getStatusLine().getStatusCode());
	    	Header[] headers = response.getAllHeaders();
	    	if(headers != null && headers.length > 0) {
	    		for(Header header : headers) {
	    			String name = header.getName();
	    			String value = header.getValue();
	    			webResponse.addHeader(name, value);
	    		}
	    	}
	    	
	    	String lastModifiedHeader = extractLastModifiedHeader(response); 
	    	if(lastModifiedHeader != null) {
	    		try {
	    			webResponse.setLastModifiedDate(DateUtils.parseDate(lastModifiedHeader));
	    		} catch(Exception e) {
	    			logger.error("Unable to parse last modified header: " + lastModifiedHeader, e);
	    		}
	    	}
	    	
	    	if(entity.getContentEncoding() != null) {
	    		webResponse.setContentEncoding(entity.getContentEncoding().getValue());
	    	}
	    	if(entity.getContentType() != null) {
	    		webResponse.setContentType(entity.getContentType().getValue());
	    	}
	    	webResponse.setContentLength(entity.getContentLength());
		} catch (ClientProtocolException e) {
			logger.error("Unable to fetch URL " + url, e);
		} catch (IOException e) {
			logger.error("Unable to fetch URL " + url, e);
		}
    	
		return webResponse;
    }
    
    private static String extractLastModifiedHeader(HttpResponse response) {
    	Header[] headers = response.getHeaders(LAST_MODIFIED_HEADER);
    	if(headers != null && headers.length > 0) {
			return headers[0].getValue();
    	}
    	
    	return null;
    }

	/**
	 * Close all expired connections as we would no longer be needing them
	 */
	public static void performHouseKeeping() {
		if(HTTP_CONNECTION_MANAGER != null) {
			HTTP_CONNECTION_MANAGER.closeExpiredConnections();
		}
	}

	public static class WebResponse implements Serializable {
    	
    	/**
		 * Generated via Eclipse
		 */
		private static final long serialVersionUID = -7444927096731647636L;

		private String responseBody;
    	
    	private int responseCode = -1;
    	
    	private Map<String, String> responseHeaders = new HashMap<String, String>();
    	
    	private String contentEncoding;
    	
    	private String contentType;
    	
    	private long contentLength;
    	
    	private Date lastModifiedDate;
    	
    	public void addHeader(String name, String value) {
    		responseHeaders.put(name, value);
    	}
    	
    	public boolean isSuccess() {
    		return responseCode == 200;
    	}
    	
    	/**
    	 * @return the responseBody
    	 */
    	public String getResponseBody() {
    		return responseBody;
    	}

    	/**
    	 * @param responseBody the responseBody to set
    	 */
    	public void setResponseBody(String responseBody) {
    		this.responseBody = responseBody;
    	}

    	/**
    	 * @return the responseCode
    	 */
    	public int getResponseCode() {
    		return responseCode;
    	}

    	/**
    	 * @param responseCode the responseCode to set
    	 */
    	public void setResponseCode(int responseCode) {
    		this.responseCode = responseCode;
    	}

    	/**
    	 * @return the responseHeaders
    	 */
    	public Map<String, String> getResponseHeaders() {
    		return responseHeaders;
    	}

    	/**
    	 * @param responseHeaders the responseHeaders to set
    	 */
    	public void setResponseHeaders(Map<String, String> responseHeaders) {
    		this.responseHeaders = responseHeaders;
    	}

    	/**
    	 * @return the contentEncoding
    	 */
    	public String getContentEncoding() {
    		return contentEncoding;
    	}

    	/**
    	 * @param contentEncoding the contentEncoding to set
    	 */
    	public void setContentEncoding(String contentEncoding) {
    		this.contentEncoding = contentEncoding;
    	}

    	/**
    	 * @return the contentType
    	 */
    	public String getContentType() {
    		return contentType;
    	}

    	/**
    	 * @param contentType the contentType to set
    	 */
    	public void setContentType(String contentType) {
    		this.contentType = contentType;
    	}

    	/**
    	 * @return the contentLength
    	 */
    	public long getContentLength() {
    		return contentLength;
    	}

    	/**
    	 * @param contentLength the contentLength to set
    	 */
    	public void setContentLength(long contentLength) {
    		this.contentLength = contentLength;
    	}

    	/**
    	 * @return the lastModifiedDate
    	 */
    	public Date getLastModifiedDate() {
    		return lastModifiedDate;
    	}

    	/**
    	 * @param lastModifiedDate the lastModifiedDate to set
    	 */
    	public void setLastModifiedDate(Date lastModifiedDate) {
    		this.lastModifiedDate = lastModifiedDate;
    	}

    }
}
