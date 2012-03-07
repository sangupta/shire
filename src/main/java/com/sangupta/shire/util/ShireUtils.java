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

}
