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
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import com.sangupta.shire.config.PropertyConfigReader;
import com.sangupta.shire.config.YmlConfigReader;
import com.sangupta.shire.core.Generator;
import com.sangupta.shire.layouts.LayoutManager;
import com.sangupta.shire.site.ProcessableSiteFile;
import com.sangupta.shire.site.SiteBackup;
import com.sangupta.shire.site.SiteDirectory;
import com.sangupta.shire.site.SiteResource;
import com.sangupta.shire.site.SiteWriter;

/**
 * Command line application porting the awesome Jekyll framework
 * to Java, adding a lot of functionality along side.
 * 
 * @author sangupta
 *
 */
public class Shire {
	
	/**
	 * Execution options as read from the config file.
	 */
	private ExecutionOptions options = null;

	/**
	 * Manager which reads all layouts
	 */
	private LayoutManager layoutManager = null;
	
	/**
	 * Manager to take site backups
	 */
	private SiteBackup siteBackup = null;
	
	/**
	 * Manager to handle export of site
	 */
	private SiteWriter siteWriter = null;
	
	/**
	 * List of registered generators
	 */
	private List<Generator> generators = null;
	
	/**
	 * Directory scanner for the input site
	 */
	private SiteDirectory siteDirectory;

	/**
	 * Main entry point to the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String configFile = "E:/git/sangupta-static/_config.yml";

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
		Shire shire = new Shire(config);
		
		// build the site
		shire.buildSite();
	}
	
	private Shire(File config) {
		this.options = new ExecutionOptions(config.getParentFile(), config);
	}
	
	private void buildSite() {
		// read the config file
		final Properties configuration = readConfigFile(this.options.getConfigFile());
		
		// build the options object
		this.options.initialize(configuration);

		// initialize all sub-systems
		this.siteBackup = new SiteBackup(options);
		this.layoutManager = new LayoutManager(options);
		this.siteWriter = new SiteWriter(options);
		this.siteDirectory = new SiteDirectory(options);
		
		// read all available layouts
		// as given in _layouts folder
		// and in _includes folder
		this.layoutManager.readLayoutsAndIncludes();
		
		// rename the older _site folder
		// to create a backup
		// we will delete it, if all goes all well
		this.siteBackup.backupOlderSite();
		
		boolean success = true;
		
		// build a list of all folders
		// exclude the _includes, _layouts, _plugins and _site folders
		Collection<File> files = this.siteDirectory.getProcessableFiles();
		
		System.out.println("Found " + files.size() + " files to process.");
		
		// start processing each file
		for(File file : files) {
			renderPage(file);
		}
		
		// call all generators
		// and pass these the needed values
		invokeGenerators();
		
		// copy all resources
		// that is the non-processable files
		copyResources(this.siteDirectory.getNonProcessableFiles());
		
		// if we have been successful in creating the site
		// delete the backup
		// else, restore the backup
		if(success) {
			this.siteBackup.deleteSiteBackup();
		} else {
			this.siteBackup.restoreSiteBackup();
		}
	}
	
	/**
	 * Method that copies all resources from the input directory
	 * to output directory without processing.
	 * 
	 * @param nonProcessableFiles
	 */
	public void copyResources(List<File> nonProcessableFiles) {
		if(nonProcessableFiles == null || nonProcessableFiles.size() == 0) {
			return;
		}
		
		for(File file : nonProcessableFiles) {
			// check for files with a starting DOT
			if(file.getName().startsWith(".")) {
				continue;
			}
			
			// copy the resource
			SiteResource resource = new SiteResource(file);
			this.siteWriter.export(resource);
		}
	}


	/**
	 * Method that invokes all available generators on site.
	 * 
	 */
	private void invokeGenerators() {
		if(this.generators == null || this.generators.size() == 0) {
			// nothing to do
			return;
		}
		
		for(Generator generator : generators) {
			generator.execute();
		}
	}

	/**
	 * Method that processes one given file and renders it
	 * to the destination site.
	 * 
	 * @param file
	 */
	private void renderPage(File file) {
		ProcessableSiteFile siteFile = new ProcessableSiteFile(file);
		
		// if site has front matter
		// process the contents
		if(siteFile.hasFrontMatter()) {
			// process the front matter
			siteFile.processFrontMatter();
		}
		
		// process the file contents
		siteFile.processContent(this.layoutManager);
		
		// now that we are done
		// we need to write the file back to the destination
		this.siteWriter.export(siteFile);
	}

	/**
	 * Read the configuration parameters from the config file.
	 * 
	 * @param file
	 * @return
	 */
	private Properties readConfigFile(File file) {
		String fileName = file.getName();
		if("_config.yml".equals(fileName)) {
			return new YmlConfigReader().readConfigFile(file);
		}
		
		if(fileName.endsWith(".properties")) {
			return new PropertyConfigReader().readConfigFile(file);
		}
		
		return null;
	}

}
