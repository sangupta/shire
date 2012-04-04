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

import com.sangupta.shire.ExecutionOptions;

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
	private final static String[] probableExtensions = new String[] { "png", "jpg", "css", "js", "otf", "zip" };
	
	/**
	 * Handle to the root folder of input site
	 */
	private final File folder;
	
	/**
	 * Currently set execution options
	 */
	private final ExecutionOptions options;
	
	private final List<File> processableFiles = new ArrayList<File>();
	
	private final List<File> nonProcessableFiles = new ArrayList<File>();
	
	/**
	 * Constructor
	 * 
	 * @param options
	 */
	public SiteDirectory(ExecutionOptions options) {
		this.options = options;
		this.folder = options.getParentFolder();
		
		// go ahead and scan the folder
		scanRootFolder();
	}
	
	/**
	 * Method to scan the input directory and find files that need
	 * to be exported either by processing or without.
	 */
	private void scanRootFolder() {
		crawlDirectory(this.folder, 0);
	}

	/**
	 * Crawl this directory and find files in it.
	 * 
	 * @param folderToCrawl
	 */
	private void crawlDirectory(final File folderToCrawl, final int level) {
		String name = folderToCrawl.getName();
		
		final boolean rootFolder = (level <= 1);
		
		if(rootFolder) {
			if(name.equals(this.options.getIncludesFolderName())) {
				return;
			}
			
			if(name.equals(this.options.getLayoutsFolderName())) {
				return;
			}
			
			if(name.equals(this.options.getSiteFolderName())) {
				return;
			}
		}
		
		if(name.equals("CVS")) {
			return;
		}
		
		if(name.equals(".svn")) {
			return;
		}
		
		if(name.equals(".git")) {
			return;
		}
		
		File[] filesInFolder = folderToCrawl.listFiles();
		for(File file : filesInFolder) {
			if(file.isDirectory()) {
				crawlDirectory(file, level + 1);
			} else if(file.isFile()) {
				// check if we are in root folder
				// if yes, skip the files that do not make any sense
				// like _config.yml
				if(rootFolder) {
					if(!rootFileAllowed(file)) {
						continue;
					}
				}
				
				// see where the file falls in
				if(fileAllowed(file)) {
					processableFiles.add(file);
				} else {
					nonProcessableFiles.add(file);
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
		if(this.options.getConfigFile().equals(file)) {
			return false;
		}
		
		return true;
	}

	/**
	 * Check if this file is actually processable or not.
	 * 
	 * @param file
	 * @return
	 */
	private boolean fileAllowed(File file) {
		String name = file.getName();
		
		String extension = null;
		int index = name.lastIndexOf('.');
		if(index != -1) {
			extension = name.substring(index + 1);
			
			for(String ext : probableExtensions) {
				if(ext.equals(extension)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	// Usual accessors follow

	/**
	 * @return the processableFiles
	 */
	public List<File> getProcessableFiles() {
		return processableFiles;
	}

	/**
	 * @return the nonProcessableFiles
	 */
	public List<File> getNonProcessableFiles() {
		return nonProcessableFiles;
	}

}
