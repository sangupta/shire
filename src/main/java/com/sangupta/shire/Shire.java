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

import net.htmlparser.jericho.Config;
import net.htmlparser.jericho.LoggerProvider;

import com.sangupta.jerry.http.WebInvoker;
import com.sangupta.shire.layouts.LayoutManager;
import com.sangupta.shire.model.TemplateData;
import com.sangupta.shire.site.SiteBackup;
import com.sangupta.shire.site.SiteBuilder;
import com.sangupta.shire.site.SiteDirectory;
import com.sangupta.shire.site.SitePreProcessor;
import com.sangupta.shire.site.SiteWriter;
import com.sangupta.shire.util.WebResponseCacheInterceptor;

/**
 * Command line application porting the awesome Jekyll framework
 * to Java, adding a lot of functionality along side.
 * 
 * @author sangupta
 *
 */
public class Shire {
	
	/**
	 * Turn off the Jericho HTML parser logging.
	 */
	static {
		Config.LoggerProvider = LoggerProvider.DISABLED;
	}
	
	/**
	 * Execution options per site
	 */
	private ExecutionOptions options = null;
	
	/**
	 * Site directory for this site
	 */
	private SiteDirectory siteDirectory = null;
	
	/**
	 * Holds the site backup for one site
	 */
	private SiteBackup siteBackup = null;
	
	/**
	 * Site writer for this site
	 */
	private SiteWriter siteWriter = null;
	
	/**
	 * Pre-processor for this site
	 */
	private SitePreProcessor preProcessor = null;
	
	/**
	 * Layout manager for this site
	 */
	private LayoutManager layoutManager = null;
	
	/**
	 * Template data associated with this site
	 */
	private TemplateData templateData = null;
	
	public Shire(File configFile) {
		this.options = new ExecutionOptions(configFile);

		// initialize all sub-systems
		this.siteBackup = new SiteBackup(options);
		
		this.layoutManager = new LayoutManager(options);
		
		this.siteWriter = new SiteWriter(options);
		
		this.siteDirectory = new SiteDirectory(this);
		
		this.preProcessor = new SitePreProcessor(this);

		this.templateData = new TemplateData(this.options.getConfiguration());
		
		// add web interceptor if needed
		if(this.options.isUseCache()) {
			WebInvoker.interceptor = new WebResponseCacheInterceptor(this);
		}
	}
	
	/**
	 * Main entry point to the application. This method analyzes the command line parameters
	 * and sets up the execution options over which the site builder will run.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
//		if(args.length == 0) {
//			usage();
//		}
		
		String configFile = "c:/projects/git/sangupta-static/_config.yml"; // args[0];

		// check if the config file is present or not
		File config = new File(configFile);

		executeShire(config);
	}
	
	/**
	 * Start the shire process over the config file.
	 * 
	 * @param config
	 */
	public static void executeShire(File config) {
		if(!config.exists()) {
			System.out.println("Config file does not exists.");
			return;
		}
		
		if(!config.isFile()) {
			System.out.println("Config file does not represent a file.");
			return;
		}
		
		// start shire/jekyll
		Shire shire = new Shire(config);
		
		// read the config file
		// build the site
		SiteBuilder builder = new SiteBuilder(shire);
		builder.buildSite();
		
		System.out.println("\n\nDone building the site!");
	}
	
	/**
	 * Print the usage information and quit.
	 */
	private static void usage() {
		System.out.println("$ java -jar shire.jar <config_file>");
		System.out.println("      <config_file>   the YAML configuration file that needs to be processed.");
		System.exit(0);
	}
	
	// Usual accessors follow

	/**
	 * @return the options
	 */
	public ExecutionOptions getOptions() {
		return options;
	}

	/**
	 * @return the siteDirectory
	 */
	public SiteDirectory getSiteDirectory() {
		return siteDirectory;
	}

	/**
	 * @return the siteWriter
	 */
	public SiteWriter getSiteWriter() {
		return siteWriter;
	}

	/**
	 * @return the layoutManager
	 */
	public LayoutManager getLayoutManager() {
		return layoutManager;
	}

	/**
	 * @return the templateData
	 */
	public TemplateData getTemplateData() {
		return templateData;
	}

	/**
	 * @return the siteBackup
	 */
	public SiteBackup getSiteBackup() {
		return siteBackup;
	}

	/**
	 * @return the preProcessor
	 */
	public SitePreProcessor getPreProcessor() {
		return preProcessor;
	}

}
