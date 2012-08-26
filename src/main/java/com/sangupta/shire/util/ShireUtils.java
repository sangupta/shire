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
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.sangupta.shire.ExecutionOptions;
import com.sangupta.shire.Shire;

/**
 * Collection of utility functions.
 * 
 * @author sangupta
 * @since Feb 23, 2012
 */
public class ShireUtils {
	
	public static File getFolder(ExecutionOptions options, String folderName) {
		File file = new File(options.getParentFolder(), folderName);
		
		if(file.exists() && file.isDirectory()) {
			return file;
		}
		
		return null;
	}
	
	public static Date parsePostDate(String date) {
		// try parsing in the format
		// dow mon dd hh:mm:ss zzz yyyy
		try {
			return DateUtils.parseDateStrictly(date.trim(), "EEE MMM dd HH:mm:ss zzz yyyy", "yyyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "dd-MM-yyyy");
		} catch (ParseException e) {
			// eat up
		}
		
		return null;
	}

	/**
	 * Method that creates a unique ID for this post.
	 * For now, we remove the last extension
	 */
	public static String createUniquePageID(String url) {
		int index = url.lastIndexOf('.');
		if(index != -1) {
			return url.substring(0, index);
		}
		
		return url;
	}
	
	public static int getConfigPropertyAsInt(Shire shire, String property, final int defaultValue) {
		String value = shire.getOptions().getConfigProperty(property);
		if(value == null) {
			return defaultValue;
		}
		
		try {
			int val = Integer.parseInt(value);
			return val;
		} catch(NumberFormatException e) {
			
		}
		
		return defaultValue;
	}
	
	private static final String NORMALIZATION_CHARS = " ";
	
	public static String normalizePathOrUrl(String path) {
		return StringUtils.replaceChars(path, NORMALIZATION_CHARS, "_");
	}

}
