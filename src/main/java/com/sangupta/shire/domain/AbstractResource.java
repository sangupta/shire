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

/**
 * An abtsraction for common methods over a given site-resource.
 * 
 * @author sangupta
 *
 */
public abstract class AbstractResource implements Resource {

	/**
	 * Absolute file handle to the source file
	 */
	protected File fileHandle;
	
	/**
	 * The extracted base path of the resource from the base
	 * source folder - this path when suffixed to the base export
	 * path makes the path at which the resource needs to be
	 * written.
	 */
	protected String path;
	
	/**
	 * Constructor
	 * 
	 * @param fileHandle
	 * @param rootPath
	 */
	public AbstractResource(File fileHandle) {
		this.fileHandle = fileHandle;
	}

	/**
	 * @see com.sangupta.shire.domain.Resource#getPublishDate()
	 */
	@Override
	public Date getPublishDate() {
		return getFileDate();
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
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	
}
