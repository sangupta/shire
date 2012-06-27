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

package com.sangupta.shire.config;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Configuration reader via YML formatted files/front matter.
 * 
 * @author sangupta
 *
 */
public class YmlConfigReader extends AbstractConfigReader {

	@Override
	protected Properties readFile(File configFile) throws IOException {
		// read all lines
		List<String> lines = FileUtils.readLines(configFile);

		// read and return the lines
		return readLines(lines);
	}
	
	@Override
	public Properties readLines(Collection<String> lines) {
		Properties properties = new Properties();
		
		// extract properties
		for(String line : lines) {
			String[] tokens = readLine(line);
			
			if(tokens != null) {
				properties.put(tokens[0], tokens[1]);
			}
		}
		
		return properties;
	}
	
	public static String[] readLine(String line) {
		// check for empty lines
		if(StringUtils.isEmpty(line)) {
			return null;
		}
		
		// clean spaces
		line = line.trim();
		
		// check for comments
		if(line.startsWith("#")) {
			return null;
		}
		
		// check for corrupt property file
		if(line.startsWith(":")) {
			throw new RuntimeException("Corrupt YML file, property cannot begin with a colon");
		}
		
		// read the property
		int index = line.indexOf(':');
		
		String propertyName = line.substring(0, index).trim();
		
		String propertyValue = "";
		if((index + 1) < line.length()) {
			propertyValue = line.substring(index + 1).trim();
		}
		
		String[] tokens = { propertyName, propertyValue };
		
		return tokens;
	}
}
