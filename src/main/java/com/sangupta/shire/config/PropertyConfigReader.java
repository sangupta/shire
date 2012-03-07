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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * Configuration reader that utilizes Java property file format.
 * 
 * @author sangupta
 *
 */
public class PropertyConfigReader extends AbstractConfigReader {

	@Override
	protected Properties readFile(File configFile) throws IOException {
		Properties properties = new Properties();
		properties.load(new BufferedInputStream(new FileInputStream(configFile)));
		return properties;
	}

	@Override
	public Properties readLines(Collection<String> lines) {
		String content = StringUtils.join(lines, '\n');
		
		Properties properties = new Properties();
		try {
			properties.load(new StringReader(content));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return properties;
	}

}
