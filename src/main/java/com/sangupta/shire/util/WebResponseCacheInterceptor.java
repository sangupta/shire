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

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;

import com.sangupta.jerry.http.WebInvocationInterceptor;
import com.sangupta.jerry.http.WebRequestMethod;
import com.sangupta.jerry.http.WebResponse;
import com.sangupta.jerry.util.CryptoUtil;
import com.sangupta.shire.Shire;

/**
 * A caching interceptor that stores all web invocation request results on disk in cache.
 * This is particularly useful when you are debugging an application. 
 * 
 * @author sangupta
 *
 */
public class WebResponseCacheInterceptor implements WebInvocationInterceptor {
	
	/**
	 * Holds the reference to the cache folder
	 */
	private static File cacheDir = null;

	/**
	 * Holds the thread local value whether we need to continue invocation or not
	 */
	private ThreadLocal<Boolean> continueInvocation = new ThreadLocal<Boolean>();
	
	/**
	 * Holds the URL that is supplied to this instance in before invocation method
	 */
	private ThreadLocal<String> url = new ThreadLocal<String>();
	
	/**
	 * @see com.sangupta.jerry.http.WebInvocationInterceptor#beforeInvocation(java.lang.String, com.sangupta.jerry.http.WebRequestMethod)
	 */
	@Override
	public WebResponse beforeInvocation(String url, WebRequestMethod method) {
		// set url so that it can be used in afterInvocation method 
		this.url.set(url);
		this.continueInvocation.set(true);
		
		// we only cache GET requests
		if(method != WebRequestMethod.GET) {
			return null;
		}
		
		// check in cache
		WebResponse response = getFromCache(url);
		
		// decide
		if(response == null) {
		} else {
			continueInvocation.set(false);
		}
		
		return response;
	}

	/**
	 * @see com.sangupta.jerry.http.WebInvocationInterceptor#continueInvocation()
	 */
	@Override
	public boolean continueInvocation() {
		return continueInvocation.get();
	}
	
	/**
	 * @see com.sangupta.jerry.http.WebInvocationInterceptor#afterInvocation(com.sangupta.jerry.http.WebResponse)
	 */
	@Override
	public WebResponse afterInvocation(WebResponse response) {
		// if we have reached here, this means that the response needs
		// to be saved in the cache dir
		File cache = getCacheFile(this.url.get());
		if(cache != null) {
			try {
				FileUtils.writeByteArrayToFile(cache, SerializationUtils.serialize(response));
			} catch (IOException e) {
				// eat up
			}
		}
		
		return response;
	}
	
	/**
	 * Get {@link WebResponse} as previously saved from the cache dir, if
	 * available
	 * 
	 * @param url
	 *            the URL that needs to be hit
	 * 
	 * @return the response as available
	 */
	private static WebResponse getFromCache(String url) {
		File cache = getCacheFile(url);
		if(cache == null || !cache.exists()) {
			return null;
		}
		
		try {
			WebResponse response = (WebResponse) SerializationUtils.deserialize(FileUtils.readFileToByteArray(cache));
			return response;
		} catch (IOException e) {
			// eat up
		}
		
		return null;
	}

	/**
	 * Generate the cache file object that points to the file handle
	 * which should provide cache for this request.
	 * 
	 * @param url
	 * @return
	 */
	private static File getCacheFile(String url) {
		if(url == null) {
			return null;
		}
		
		if(cacheDir == null) {
			buildCacheDir();
		}
		
		String md5 = CryptoUtil.getMD5Hex(url);
		File cache = new File(cacheDir, md5);
		
		return cache;
	}

	/**
	 * Create the cache dir, if not present
	 * 
	 */
	private synchronized static void buildCacheDir() {
		if(cacheDir != null) {
			return;
		}
		
		cacheDir = new File(Shire.getExecutionOptions().getParentFolder(), Shire.getExecutionOptions().getCacheFolderName());
		if(!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
	}
}
