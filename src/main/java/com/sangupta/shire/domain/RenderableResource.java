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
import java.util.Properties;

/**
 * @author sangupta
 *
 */
public class RenderableResource extends AbstractResource {
	
	/**
	 * Store the front matter as extracted from the file.
	 */
	private Properties frontMatter;
	
	/**
	 * Line index at which the front matter has ended. This helps us
	 * skip when re-reading the file's content when rendering
	 */
	private int matterEndingLine;

	/**
	 * Constructor
	 * 
	 * @param fileHandle
	 * @param path
	 * @param frontMatter
	 * @param matterEndingLine
	 */
	public RenderableResource(File fileHandle, String path, Properties frontMatter, int matterEndingLine) {
		super(fileHandle, path);
		this.frontMatter = frontMatter;
		this.matterEndingLine = matterEndingLine;
	}
	
	public String getFrontMatterProperty(String propertyName) {
		if(this.frontMatter == null) {
			return null;
		}
		
		return this.frontMatter.getProperty(propertyName);
	}
	
	/**
	 * @see com.sangupta.shire.domain.AbstractResource#getPublishDate()
	 */
	@Override
	public Date getPublishDate() {
		// TODO: also need to check for front matter
		return super.getPublishDate();
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
