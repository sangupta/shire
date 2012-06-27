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

/**
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
	 * @return the fileHandle
	 */
	@Override
	public File getFileHandle() {
		return fileHandle;
	}

	/**
	 * @return
	 */
	public String getExportPath() {
		return this.path;
	}

	private String getBasePath(String basePath) {
		// its not there, let's construct from base path
		String path = this.fileHandle.getAbsolutePath();
		
		if(path.startsWith(basePath)) {
			path = path.substring(basePath.length());
		}
		
		return path;
	}

}
