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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.sangupta.shire.Shire;
import com.sangupta.shire.domain.BlogResource;
import com.sangupta.shire.domain.NonRenderableResource;
import com.sangupta.shire.domain.RenderableResource;
import com.sangupta.shire.model.FrontMatterConstants;
import com.sangupta.shire.util.FrontMatterUtils;

/**
 * Allows reading of the source site directory to process content.
 * 
 * @author sangupta
 * @since Feb 23, 2012
 */
public class SiteDirectory {
	
	/**
	 * List of extensions that need to be copied as-is
	 */
	private final static String[] probableBinaryExtensions = new String[] { "png", "jpg", "css", "js", "otf", "zip" };
	
	/**
	 * List of folders that need to be ignored when scanning
	 */
	private final List<String> ignorableFolders = new ArrayList<String>();
	
	/**
	 * Files in the root folder of the site that need to be ignored
	 */
	private final List<File> ignorableFiles = new ArrayList<File>();
	
	/**
	 * Holds the list of all renderable resources that are available in the site
	 */
	private final List<RenderableResource> renderableResources = new ArrayList<RenderableResource>();
	
	/**
	 * Holds the list of all non-renderable resources that are available in the site
	 */
	private final List<NonRenderableResource> nonRenderableResources = new ArrayList<NonRenderableResource>();
	
	/**
	 * Holds a list of all blogs that have been found in this site
	 */
	private final List<BlogResource> blogs = new ArrayList<BlogResource>();
	
	/**
	 * Holds the list of all directories that have been marked to be considered
	 * as a blog.
	 */
	private final List<File> dotFiles = new ArrayList<File>();
	
	/**
	 * The shire object representing this site
	 */
	private final Shire shire;
	
	/**
	 * Constructor
	 * 
	 * @param options
	 */
	public SiteDirectory(Shire shire) {
		this.shire = shire;
		this.ignorableFolders.add(shire.getOptions().getIncludesFolderName());
		this.ignorableFolders.add(shire.getOptions().getLayoutsFolderName());
		this.ignorableFolders.add(shire.getOptions().getSiteFolderName());
		
		this.ignorableFiles.add(shire.getOptions().getConfigFile());
		
		// go ahead and scan the folder
		scanRootFolder(shire.getOptions().getParentFolder());
	}
	
	/**
	 * Method to scan the input directory and find files that need
	 * to be exported either by processing or without.
	 */
	private void scanRootFolder(File folder) {
		crawlDirectory(folder, 0);
	}

	/**
	 * Crawl this directory and find files in it.
	 * 
	 * @param folderToCrawl
	 */
	private void crawlDirectory(final File folderToCrawl, final int level) {
		String name = folderToCrawl.getName();
		
		final boolean rootFolder = (level <= 1);
		
		if(rootFolder && this.ignorableFolders.contains(name)) {
			return;
		}
		
		if(name.equals("CVS") || name.equals(".svn") || name.equals(".git")) {
			return;
		}
		
		// read all files
		File[] filesInFolder = folderToCrawl.listFiles();
		
		// start building
		for(File file : filesInFolder) {
			if(file.isDirectory()) {
				crawlDirectory(file, level + 1);
				continue;
			}
			
			if(file.isFile()) {
				// check if we are in root folder
				// if yes, skip the files that do not make any sense
				// like _config.yml
				if(rootFolder) {
					if(!rootFileAllowed(file)) {
						continue;
					}
				}
				
				// check for blog signal
				if(file.getName().startsWith(".")) {
					this.dotFiles.add(file.getAbsoluteFile());
				}
				
				// check for blog file
				if(file.getName().equals(".blog")) {
					this.blogs.add(new BlogResource(file));
				}
				
				// see where the file falls in
				if(fileAllowed(file)) {
					// check if the file has front-matter or not
					Properties properties = new Properties();
					try {
						int frontMatter = FrontMatterUtils.checkFileHasFrontMatter(file, properties);
						if(frontMatter > 0) {
							// check if the resource has been published or not
							String published = properties.getProperty(FrontMatterConstants.PUBLISHED);
							if(published == null || !("false".equalsIgnoreCase(published))) {
								this.renderableResources.add(new RenderableResource(file, properties, frontMatter, this.shire));
							}
						}
					} catch(Exception e) {
						e.printStackTrace();
						// as we are unable to process this resource
						// move it to non-processable one
						this.nonRenderableResources.add(new NonRenderableResource(file));
					}
				} else {
					this.nonRenderableResources.add(new NonRenderableResource(file));
				}
			}
		}
	}
	
	/**
	 * Check if this file in the root folder is allowed or not.
	 * 
	 * @param file
	 * @return
	 */
	private boolean rootFileAllowed(File file) {
		if(this.ignorableFiles.contains(file)) {
			return false;
		}
		
		return true;
	}

	/**
	 * Check if this file is actually processable or not. We skip
	 * all files that start with a dot (.) for these are usually not published
	 * to web.
	 * 
	 * @param file
	 * @return
	 */
	private boolean fileAllowed(File file) {
		String name = file.getName();
		
		if(name.startsWith(".")) {
			return false;
		}
		
		String extension = null;
		int index = name.lastIndexOf('.');
		if(index != -1) {
			extension = name.substring(index + 1);
			
			for(String ext : probableBinaryExtensions) {
				if(ext.equals(extension)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	// Usual accessors follow

	/**
	 * @return the dotFiles
	 */
	public List<File> getDotFiles() {
		return dotFiles;
	}

	/**
	 * @return the renderableResources
	 */
	public List<RenderableResource> getRenderableResources() {
		return renderableResources;
	}

	/**
	 * @return the nonRenderableResources
	 */
	public List<NonRenderableResource> getNonRenderableResources() {
		return nonRenderableResources;
	}

	/**
	 * @return the blogs
	 */
	public List<BlogResource> getBlogs() {
		return blogs;
	}

}
