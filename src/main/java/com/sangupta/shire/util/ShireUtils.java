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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sangupta.shire.ExecutionOptions;

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
		DateFormat format = new SimpleDateFormat("yyyyy-MM-dd hh:mm:ss");
		try {
			return format.parse(date.trim());
		} catch (ParseException e) {
			// unable to parse date on post
			e.printStackTrace();
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

}
