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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;

import com.sangupta.shire.Shire;
import com.sangupta.shire.util.HttpUtil.WebResponse;

/**
 * @author sangupta
 *
 */
public class WebResponseCacheUtil {
	
	/**
	 * Holds the reference to the cache folder
	 */
	private static File cacheDir = null;
	
	/**
	 * Method that retrieves data from the cache for a given URL, and if not
	 * available will hit the URL to fetch the URL.
	 * 
	 * @param url
	 * @return
	 */
	public static WebResponse doWithCache(String url) {
		WebResponse response = getFromCache(url);
		if(response != null) {
			return response;
		}
		
		// no response - hit the web
		response = HttpUtil.getResponseWithoutCacheCheck(url);
		if(response == null) {
			return null;
		}
		
		// save it out
		saveInCache(url, response);
		
		return response;
	}
	
	/**
	 * Retrieve the data from cache for the given URL if available.
	 * 
	 * @param url
	 * @return
	 */
	public static WebResponse getFromCache(String url) {
		File cache = getCache(url);
		if(cache == null) {
			return null;
		}
		
		if(!cache.exists()) {
			return null;
		}
		
		WebResponse response = null;
		try {
			response = (WebResponse) SerializationUtils.deserialize(FileUtils.readFileToByteArray(cache));
		} catch (FileNotFoundException e) {
			// do nothing
		} catch (IOException e) {
			// do nothing
			e.printStackTrace();
		}
		
		return response;
	}
	
	public static void saveInCache(String url, WebResponse response) {
		if(response == null) {
			return;
		}
		
		File cache = getCache(url);
		if(cache == null) {
			return;
		}
		
		try {
			FileUtils.writeByteArrayToFile(cache, SerializationUtils.serialize(response));
		} catch (IOException e) {
			// do nothing
		}
	}
	
	private static File getCache(String url) {
		if(url == null) {
			return null;
		}
		
		if(cacheDir == null) {
			buildCacheDir();
		}
		
		// get the md5
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Unable to generate the digest");
			return null;
		}
		String md5 = asHex(md.digest(url.getBytes()));
		
		File cache = new File(cacheDir, md5);
		return cache;
	}

	/**
	 * @param digest
	 * @return
	 */
	private static String asHex(byte[] bytes) {
		if(bytes == null) {
			return null;
		}
		
		StringBuilder hexString = new StringBuilder();
		
		for(int index = 0; index < bytes.length; index++) {
			String hex = Integer.toHexString(0xFF & bytes[index]);
			if (hex.length() == 1) {
			    // could use a for loop, but we're only dealing with a single byte
			    hexString.append('0');
			}
			hexString.append(hex);
		}
		
		return hexString.toString();
	}

	/**
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
