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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.sangupta.shire.config.YmlConfigReader;

/**
 * @author sangupta
 *
 */
public class FrontMatterUtils {
	
	/**
	 * Given a file handle, detects if the file has front matter specified or not. If yes,
	 * the provided properties object is filled with the front matter.
	 * 
	 * @param file
	 * @param properties
	 * @return
	 * @throws IOException 
	 */
	public static int checkFileHasFrontMatter(File file, Properties properties) throws IOException {
		FileReader fileReader = null;
		BufferedReader reader = null;
		int linesRead = 0;

		try {
			fileReader = new FileReader(file);
			reader = new BufferedReader(fileReader);
	
			String line;
			boolean found = false;
			while((line = reader.readLine()) != null) {
				linesRead++;
	
				// start reading the file from top
				if(!found) {
					if("".equals(line.trim())) {
						continue;
					}
					
					// check the first non-empty line
					if(!line.startsWith("---")) {
						return -1;
					}
					
					// we had the front matter
					found = true;
					continue;
				}
				
				// front matter has been found and this is the line
				// that may have the property
				if(line.startsWith("---")) {
					// this is the end of new params
					break;
				}
				
				// extract the param
				String[] tokens = YmlConfigReader.readLine(line);
				if(tokens != null) {
					properties.put(tokens[0], tokens[1]);
				}
			}
		} finally {
			if(fileReader != null) {
				fileReader.close();
			}
			
			if(reader != null) {
				reader.close();
			}
		}
		
		return linesRead;
	}

}
