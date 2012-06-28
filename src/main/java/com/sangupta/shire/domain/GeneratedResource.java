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

import java.util.Properties;

/**
 * @author sangupta
 *
 */
public class GeneratedResource extends AbstractResource {
	
	/**
	 * The content of this resource
	 */
	private String content;
	
	public GeneratedResource(String path, String content) {
		super(null, null);
		this.path = path;
		this.content = content;
	}

	/**
	 * @see com.sangupta.shire.domain.Resource#hasFrontMatter()
	 */
	@Override
	public boolean hasFrontMatter() {
		return false;
	}

	/**
	 * @see com.sangupta.shire.domain.Resource#getFrontMatter()
	 */
	@Override
	public Properties getFrontMatter() {
		return null;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

}