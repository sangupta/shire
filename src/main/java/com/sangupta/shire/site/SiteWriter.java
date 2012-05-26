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
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.sangupta.shire.ExecutionOptions;
import com.sangupta.shire.util.HtmlUtils;

/**
 * Class that writes a given {@link ProcessableSiteFile} to the exported site.
 * 
 * @author sangupta
 * @since Feb 23, 2012
 */
public class SiteWriter {
	
	/**
	 * Holds the applicable execution options
	 */
	private final ExecutionOptions options;
	
	/**
	 * Flag that helps us in lazy initializing the site writer.
	 */
	private boolean initialized = false;

	/**
	 * Holds the path to the root folder of the input site
	 */
	private final String rootFolderPath;
	
	/**
	 * File handle to the root of export folder
	 */
	private File siteFolder = null;
	
	/**
	 * Constructor
	 * 
	 * @param options
	 */
	public SiteWriter(ExecutionOptions options) {
		this.options = options;
		this.rootFolderPath = this.options.getParentFolder().getAbsolutePath();
	}

	/**
	 * Create a new site folder, if it does not exists.
	 * 
	 */
	private void createSiteExportFolder() {
		initialized = true;

		File file = new File(options.getParentFolder(), options.getSiteFolderName());
		if(file.exists() && file.isDirectory()) {
			throw new IllegalStateException("Site folder already exists... looks like the backup failed. aborting!");
		}
		
		boolean success = file.mkdirs();
		if(!success) {
			throw new RuntimeException("Unable to create site export directory.");
		}
		
		this.siteFolder = file;
	}

	/**
	 * Exports the given file to the site export location
	 * creating all child folders that may be necessary.
	 * 
	 * @param siteFile
	 */
	public void export(ProcessableSiteFile siteFile, String pageContents) {
		// check if we have initialized the _site folder or not
		if(!initialized) {
			createSiteExportFolder();
		}
		
		// start the export process
		String path = siteFile.getExportPath(this.rootFolderPath);
		path = this.siteFolder.getAbsolutePath() + File.separator + path;
		
		File exportFile = new File(path);

		pageContents = HtmlUtils.tidyHtml(pageContents);

		try {
			FileUtils.writeStringToFile(exportFile, pageContents);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void export(SiteResource siteResource) {
		if(!initialized) {
			createSiteExportFolder();
		}
		
		// start the export process
		String path = siteResource.getExportPath(this.rootFolderPath);
		path = this.siteFolder.getAbsolutePath() + File.separator + path;
		
		File exportFile = new File(path);

		try {
			FileUtils.copyFile(siteResource.input, exportFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
