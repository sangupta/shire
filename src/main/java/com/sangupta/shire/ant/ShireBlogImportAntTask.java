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

package com.sangupta.shire.ant;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.sangupta.blogparser.BlogType;
import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.shire.tools.ShireBlogImporter;

/**
 * Apache ANT task to run blog import tool.
 * 
 * @author sangupta
 * @since 0.3.0
 */
public class ShireBlogImportAntTask extends Task {
	
	/**
	 * Export file (usually XML) that contains the blog data
	 */
	private String importFile;
	
	/**
	 * Layout name to be used in each file
	 */
	private String layoutName;
	
	/**
	 * Folder where output must be written
	 */
	private String outputFolder;
	
	/**
	 * Type of blog export mechanism
	 */
	private String provider;
	
	/**
	 * 
	 * @see org.apache.tools.ant.Task#execute()
	 */
	public void execute() throws BuildException {
		// start basic validation
		if(AssertUtils.isEmpty(this.importFile)) {
			throw new BuildException("Import file must be specified.");
		}
		
		if(AssertUtils.isEmpty(this.layoutName)) {
			throw new BuildException("Default layout name must be specified.");
		}
		
		if(AssertUtils.isEmpty(this.outputFolder)) {
			throw new BuildException("Output folder is a must.");
		}
		
		if(AssertUtils.isEmpty(this.provider)) {
			throw new BuildException("Provider must be specified.");
		}
		
		// Start advanced validation
		File importXML = new File(this.importFile);
		if(!(importXML.exists() && importXML.isFile())) {
			throw new BuildException("Import file either does not exists or is a directory.");
		}
		
		File exportFolder = new File(this.outputFolder);
		if(!(exportFolder.exists() && exportFolder.isDirectory())) {
			throw new BuildException("Output folder already exists.");
		}
		
		BlogType blogType = null;
		try {
			blogType = BlogType.valueOf(this.provider);
		} catch(Throwable t) {
			// eat up
		}
		
		if(blogType == null) {
			throw new BuildException("Provider must be one of: Blogger, Wordpress, MovableType.");
		}
		
		// all set and done
		// run the import tool
		ShireBlogImporter.executeImporter(importXML, blogType, exportFolder, layoutName);
	}
	
	// Usual accessors follow

	/**
	 * @return the importFile
	 */
	public String getImportFile() {
		return importFile;
	}

	/**
	 * @param importFile the importFile to set
	 */
	public void setImportFile(String importFile) {
		this.importFile = importFile;
	}

	/**
	 * @return the layoutName
	 */
	public String getLayoutName() {
		return layoutName;
	}

	/**
	 * @param layoutName the layoutName to set
	 */
	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}

	/**
	 * @return the outputFolder
	 */
	public String getOutputFolder() {
		return outputFolder;
	}

	/**
	 * @param outputFolder the outputFolder to set
	 */
	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}

	/**
	 * @return the provider
	 */
	public String getProvider() {
		return provider;
	}

	/**
	 * @param provider the provider to set
	 */
	public void setProvider(String provider) {
		this.provider = provider;
	}

}
