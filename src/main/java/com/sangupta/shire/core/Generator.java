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

package com.sangupta.shire.core;

import java.util.List;

import com.sangupta.shire.domain.GeneratedResource;
import com.sangupta.shire.model.TemplateData;

/**
 * Contract for implementations that allow generation of additional
 * content based on rules.
 * 
 * @author sangupta
 *
 */
public interface Generator {
	
	/**
	 * Return the human-understandable name of this generator.
	 * 
	 * @return
	 */
	public String getName();
	
	/**
	 * Indicates if this generator needs to be run before processing
	 * any resource of the site.
	 * 
	 * @return
	 */
	public boolean runBeforeResourceProcessing();
	
	/**
	 * Run the generator providing it the entire site wide template data and
	 * return a list of all generated resources that were added by this 
	 * generator as part of its execution.
	 */
	public List<GeneratedResource> execute(TemplateData templateData);

}
