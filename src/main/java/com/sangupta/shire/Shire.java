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

package com.sangupta.shire;

import java.io.File;

import com.sangupta.shire.site.SiteBuilder;

/**
 * Command line application porting the awesome Jekyll framework
 * to Java, adding a lot of functionality along side.
 * 
 * @author sangupta
 *
 */
public class Shire {
	
	/**
	 * Singleton instance of the execution options
	 */
	private static ExecutionOptions options = null;
	
	/**
	 * Main entry point to the application. This method analyzes the command line parameters
	 * and sets up the execution options over which the site builder will run.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String configFile = "c:/projects/git/sangupta-static/_config.yml";

		// check if the config file is present or not
		File config = new File(configFile);
		
		if(!config.exists()) {
			System.out.println("Config file does not exists.");
			return;
		}
		
		if(!config.isFile()) {
			System.out.println("Config file does not represent a file.");
			return;
		}
		
		// start jekyll
		options = new ExecutionOptions(config);

		// read the config file
		// build the site
		new SiteBuilder(options).buildSite();
		
		System.out.println("\n\nDone building the site!");
	}
	
	/**
	 * Retrieve the options object globally.
	 * 
	 * @return
	 */
	public static ExecutionOptions getExecutionOptions() {
		return options;
	}
	
}
