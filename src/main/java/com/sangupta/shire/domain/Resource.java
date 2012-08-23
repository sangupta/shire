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
 * Includes information on one resource in the site.
 * 
 * @author sangupta
 *
 */
public interface Resource {

	/**
	 * Return if the file has front matter associated with it or not.
	 * 
	 * @return
	 */
	public boolean hasFrontMatter();
	
	/**
	 * Return the entire front matter associated with this resource.
	 * 
	 * @return
	 */
	public Properties getFrontMatter();

	/**
	 * Return the internal file handle.
	 * 
	 * @return
	 */
	public File getFileHandle();
	
	/**
	 * Return the filename of the file referenced by this resource.
	 * 
	 * @return
	 */
	public String getFileName();
	
	/**
	 * Return the date the file was last modified on disk.
	 * 
	 * @return
	 */
	public Date getFileDate();
	
	/**
	 * The publishing date for this resource, either the one specified
	 * in front matter, or the file's last modified date.
	 * 
	 * @return
	 */
	public Date getPublishDate();
	
}
