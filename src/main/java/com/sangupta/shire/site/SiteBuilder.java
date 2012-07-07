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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.sangupta.shire.ExecutionOptions;
import com.sangupta.shire.core.Generator;
import com.sangupta.shire.domain.NonRenderableResource;
import com.sangupta.shire.domain.RenderableResource;
import com.sangupta.shire.generators.BlogPagesGenerator;
import com.sangupta.shire.generators.SiteMapGenerator;
import com.sangupta.shire.layouts.LayoutManager;
import com.sangupta.shire.model.Page;
import com.sangupta.shire.model.TemplateData;

public class SiteBuilder {

	/**
	 * Options to be used for site building
	 */
	private ExecutionOptions options;
	
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
		LayoutManager.initialize(options);
		SiteWriter.initialize(options);
		this.siteDirectory = new SiteDirectory(options);

		// add all default generators
		this.generators = new ArrayList<Generator>();
		this.generators.add(new SiteMapGenerator());
		this.generators.add(new BlogPagesGenerator());
	}

	/**
	 * Process the site.
	 * 
	 */
	public void buildSite() {
		// read all available layouts
		// as given in _layouts folder
		// and in _includes folder
		LayoutManager.readLayoutsAndIncludes();
		
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
		this.templateData = new TemplateData(this.options.getConfiguration());
		
		// build a list of all tags and categories
		// that will be used site-wide
		this.templateData.extractFromResources(this.siteDirectory.getRenderableResources());
		
		// export all non-renderable resources
		exportNonRenderableResources(this.siteDirectory.getNonRenderableResources());

		// build a list of all folders
		// exclude the _includes, _layouts, _plugins and _site folders
		processResources(this.siteDirectory.getRenderableResources());
		
		// call all generators
		// and pass these the needed values
		invokeGenerators();
		
	}
	
	/**
	 * @param nonRenderableResources
	 */
	private void exportNonRenderableResources(List<NonRenderableResource> nonRenderableResources) {
		for(NonRenderableResource nr : nonRenderableResources) {
			SiteWriter.export(nr);
		}
	}

	/**
	 * Process each individual page from the site folder and export it.
	 * 
	 * @param resources
	 */
	private void processResources(List<RenderableResource> resources) {
		System.out.println("Found " + resources.size() + " files to process.");
		
		// start processing each file
		for(RenderableResource resource : resources) {
			// process the resource as needed
			try {
				renderPage(resource, templateData);
			} catch (IOException e) {
				System.out.println("Unable to process site resource: " + resource.getExportPath());
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method that processes one given file and renders it
	 * to the destination site.
	 * 
	 * @param file
	 * @throws IOException 
	 */
	private void renderPage(RenderableResource resource, TemplateData templateData) throws IOException {
		System.out.println("Processing " + resource.getExportPath() + "...");
		
		String layoutName = null;
		Properties frontMatter = null;

		// if site has front matter
		if(resource.hasFrontMatter()) {
			// process the front matter
			frontMatter = resource.getFrontMatter();

			layoutName = frontMatter.getProperty("layout");
			
			// if the layout is null, only then check for default layout naming convention
			// if the layout is empty - that indicates we do not want any layout applied
			if(layoutName == null) {
				layoutName = templateData.getSite().getDefaultLayoutName();
			}
		}
		
		// this also makes sure that the page
		// attribute has been created
		templateData.mergePageFrontMatter(frontMatter);
		
		if(layoutName != null) {
			String content = resource.getOriginalContent();
			
			// add the unrendered content
			Page page = templateData.getPage();
			page.setContent(content);
			page.setUrl(resource.getUrl());
			
			page.postProcessProperties();
			
			// now see if the page actually needs to be published
			if(!page.isPublished()) {
				// processing of this page was stopped
				return;
			}

			// get the converted content in HTML
			content = resource.getConvertedContent();
			
			// work out this HTML content with the current model data
			content = LayoutManager.layoutContent(layoutName, content, templateData);
			
			// now that we are done
			// we need to write the file back to the destination
			SiteWriter.export(resource, content);
			
			return;
		}
		
		System.out.println("No layout name specified for file... copying it as a resource");
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
			System.out.println("Invoking generator: " + generator.getName());
			generator.execute(templateData, this.siteDirectory.getRenderableResources(), this.siteDirectory.getDotFiles());
		}
	}

}
