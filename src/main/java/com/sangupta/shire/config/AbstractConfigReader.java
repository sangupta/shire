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
import java.util.Properties;

import com.sangupta.shire.core.ConfigReader;

/**
 * Abstract configuration reader that implements some basic checks to the passed
 * parameters.
 * 
 * @author sangupta
 *
 */
public abstract class AbstractConfigReader implements ConfigReader {

	public Properties readConfigFile(File configFile) {
		if(configFile == null) {
			return null;
		}
		
		if(!configFile.exists()) {
			return null;
		}
		
		if(configFile.isDirectory()) {
			return null;
		}
		
		Properties properties = null;
		try {
			properties = readFile(configFile);
		} catch(IOException e) {
			
		}
		
		return properties;
	}
	
	protected abstract Properties readFile(File configFile) throws IOException;

}
