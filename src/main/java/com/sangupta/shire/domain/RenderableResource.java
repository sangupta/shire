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
import java.util.Properties;

/**
 * @author sangupta
 *
 */
public class RenderableResource extends AbstractResource {
	
	private Properties frontMatter;
	
	private int matterEndingLine;

	public RenderableResource(File fileHandle, String path, Properties frontMatter, int matterEndingLine) {
		super(fileHandle, path);
		this.frontMatter = frontMatter;
		this.matterEndingLine = matterEndingLine;
	}
	
	public String getFileName() {
		return this.fileHandle.getName();
	}

	/**
	 * @see com.sangupta.shire.domain.Resource#hasFrontMatter()
	 */
	@Override
	public boolean hasFrontMatter() {
		return true;
	}

	/**
	 * @see com.sangupta.shire.domain.Resource#getFrontMatter()
	 */
	@Override
	public Properties getFrontMatter() {
		return this.frontMatter;
	}

	/**
	 * @return the matterEndingLine
	 */
	public int getMatterEndingLine() {
		return matterEndingLine;
	}

}
