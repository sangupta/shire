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

package com.sangupta.shire.domain;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * An abtsraction for common methods over a given site-resource.
 * 
 * @author sangupta
 *
 */
public abstract class AbstractResource implements Resource {

	protected File fileHandle;
	
	protected String path;
	
	public AbstractResource(File fileHandle, String rootPath) {
		this.fileHandle = fileHandle;
		this.path = getBasePath(rootPath);
	}
	
	/**
	 * @see com.sangupta.shire.domain.Resource#getPublishDate()
	 */
	@Override
	public Date getPublishDate() {
		return getFileDate();
	}
	
	/**
	 * Construct a URL of the page's export path
	 * 
	 * @param siteFile
	 * @return
	 */
	public String getUrl() {
		String path = this.getExportPath();
		path = StringUtils.replaceChars(path, '\\', '/');
		return path;
	}

	/**
	 * Return the date the file was last modified on disk.
	 * 
	 * @return
	 */
	@Override
	public Date getFileDate() {
		return new Date(this.fileHandle.lastModified());
	}
	
	/**
	 * @return the fileHandle
	 */
	@Override
	public File getFileHandle() {
		return fileHandle;
	}

	/**
	 * Return the file name of the internal file handle.
	 * 
	 * @return
	 */
	@Override
	public String getFileName() {
		return this.fileHandle.getName();
	}
	
	/**
	 * @return
	 */
	public String getExportPath() {
		return this.path;
	}

	private String getBasePath(String basePath) {
		if(fileHandle == null) {
			return null;
		}
		
		// its not there, let's construct from base path
		String path = this.fileHandle.getAbsolutePath();
		
		if(path.startsWith(basePath)) {
			path = path.substring(basePath.length());
		}
		
		return path;
	}

}
