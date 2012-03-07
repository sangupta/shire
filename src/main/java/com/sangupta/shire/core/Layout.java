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

import java.io.File;
import java.util.Map;

/**
 * Contract for implementing classes that define a type of layout, basically
 * a templating engine that is supported like Apache Velocity, or Django Liquid
 * Markup.
 *  
 * @author sangupta
 *
 */
public interface Layout {
	
	public void initialize(File layouts, File includes);
	
	public String processTemplate(String templateCode, Map<String, Object> dataModel);
	
	public String layoutContent(String layoutName, String content, Map<String, Object> dataModel);

}
