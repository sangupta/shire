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

package com.sangupta.shire.site;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.sangupta.shire.ExecutionOptions;
import com.sangupta.shire.converters.Converters;
import com.sangupta.shire.core.Converter;
import com.sangupta.shire.core.Generator;
import com.sangupta.shire.layouts.LayoutManager;
import com.sangupta.shire.model.TemplateData;

public class SiteBuilder {

	/**
	 * Options to be used for site building
	 */
	private ExecutionOptions options;
	
	/**
	 * Manager which reads all layouts
	 */
	private LayoutManager layoutManager = null;
	
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
	 * Holds the template data for the site, and the page
	 */
	private TemplateData templateData = null;
	
	/**
	 * Constructor
	 * 
	 * @param options
	 */
	public SiteBuilder(ExecutionOptions options) {
		if(options == null) {
			throw new IllegalArgumentException("Execution options cannot be null.");
		}
		
		this.options = options;
		
		initialize();
	}
	
	/**
	 * Initialize all the sub-systems of this application
	 * 
	 * @param options
	 */
	private void initialize() {
		// initialize all sub-systems
		this.layoutManager = new LayoutManager(options);
		this.siteWriter = new SiteWriter(options);
		this.siteDirectory = new SiteDirectory(options);
	}

	/**
	 * Process the site.
	 * 
	 */
	public void buildSite() {
		// read all available layouts
		// as given in _layouts folder
		// and in _includes folder
		this.layoutManager.readLayoutsAndIncludes();
		
		// rename the older _site folder
		// to create a backup
		// we will delete it, if all goes all well
		SiteBackup.backupOlderSite(options);
		
		boolean success = false;
		try {
			processSite();
			success = true;
		} catch(Exception e) {
			e.printStackTrace();
		}

		SiteBackup.performHouseKeeping(success);
	}
	
	private void processSite() {
		// build the template data
		templateData = new TemplateData(this.options.getConfiguration());
		
		// build a list of all folders
		// exclude the _includes, _layouts, _plugins and _site folders
		renderPages(this.siteDirectory.getProcessableFiles());
		
		// call all generators
		// and pass these the needed values
		invokeGenerators();
		
		// copy all resources
		// that is the non-processable files
		copyResources(this.siteDirectory.getNonProcessableFiles());
	}
	
	private void renderPages(Collection<File> files) {
		System.out.println("Found " + files.size() + " files to process.");
		
		// start processing each file
		for(File file : files) {
			renderPage(file, templateData);
		}
	}
	
	/**
	 * Method that processes one given file and renders it
	 * to the destination site.
	 * 
	 * @param file
	 */
	private void renderPage(File file, TemplateData templateData) {
		System.out.println("Processing " + file.getAbsolutePath() + "...");
		ProcessableSiteFile siteFile = new ProcessableSiteFile(file);
		
		String layoutName = null;
		Properties frontMatter = null;

		// if site has front matter
		if(siteFile.hasFrontMatter()) {
			// process the front matter
			frontMatter = siteFile.getFrontMatter();

			layoutName = frontMatter.getProperty("layout");
			if(StringUtils.isEmpty(layoutName)) {
				layoutName = templateData.getSite().getDefaultLayoutName();
			}
		}
		
		templateData.mergePageFrontMatter(frontMatter);
		
		if(StringUtils.isNotEmpty(layoutName)) {
			String content = siteFile.getContent();

			// find out the right converter for the file's content
			// markdown, Wiki, or HTML, or something else
			Converter converter = Converters.getConverter(siteFile.getFileName());
			
			// convert the content first
			content = converter.convert(content);
			
			content = this.layoutManager.layoutContent(layoutName, content, templateData);
			
			// now that we are done
			// we need to write the file back to the destination
			this.siteWriter.export(siteFile, content);
			
			return;
		}
		
		System.out.println("No layout name specified for file... copying it as a resource");
		// TODO: implement resource copy
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
			System.out.println("Copying resource " + file.getAbsolutePath() + "...");
			
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

}
