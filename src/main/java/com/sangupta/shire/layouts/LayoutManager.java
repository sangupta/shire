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

package com.sangupta.shire.layouts;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.sangupta.shire.ExecutionOptions;
import com.sangupta.shire.core.Layout;
import com.sangupta.shire.model.TemplateData;
import com.sangupta.shire.util.ShireUtils;

/**
 * Handles layout for the pages that need transformation.
 * 
 * @author sangupta
 * @since Feb 23, 2012
 */
public class LayoutManager {
	
	private ExecutionOptions options = null;
	
	private Layout layout = null;
	
	public LayoutManager(ExecutionOptions options) {
		this.options = options;

		LayoutType layoutType = this.options.getLayoutType();
		switch(layoutType) {
			case AutoDetect:
				layoutType = autoDetectLayoutType();
				break;
				
			default:
				// do nothing
				break;
		}
		
		// reset-the layout type option
		this.options.setLayoutType(layoutType);
		
		// check for unknown
		if(layoutType == LayoutType.Unknown) {
			throw new IllegalArgumentException("Unknown layout type");
		}
	}
	
	/**
	 * Method that detects layout type based on files inside the
	 * layouts folder.
	 * 
	 * @return
	 */
	private LayoutType autoDetectLayoutType() {
		return LayoutType.Velocity;
	}

	/**
	 * Method to read the list of all layouts available in the library
	 * and figure out the layout type if not specified.
	 * 
	 */
	public void readLayoutsAndIncludes() {
		final File layouts = ShireUtils.getFolder(this.options, this.options.getLayoutsFolderName());
		final File includes = ShireUtils.getFolder(this.options, this.options.getIncludesFolderName());
		
		// initialize the proper layout handler
		switch (this.options.getLayoutType()) {
			case Velocity:
				this.layout = new VelocityLayouts();
				break;
				
			case DjangoLiquid:
				// this.layout = new DjangoLayouts();
				break;
	
			default:
				break;
		}
		
		// set it
		this.layout.initialize(layouts, includes);
	}
	
	public String layoutContent(String layoutName, final String content, final TemplateData templateData) {
		if(StringUtils.isEmpty(layoutName)) {
			return content;
		}
		
		if("nil".equals(layoutName)) {
			return content;
		}
		
		if(!layoutName.endsWith(".html")) {
			layoutName = layoutName + ".html";
		}
		
		// build up the final data model
		final Map<String, Object> dataModel = getDataModel(templateData);
		
		// parse the contents of the page itself
		String modifiedContent = this.layout.processTemplate(content, dataModel);
		
		// layout the contents
		return this.layout.layoutContent(layoutName, modifiedContent, dataModel);
	}

	private Map<String, Object> getDataModel(final TemplateData data) {
		final Map<String, Object> model = new HashMap<String, Object>();
		
		model.put("site", data.getSite());
		model.put("page", data.getPage());
		model.put("paginator", data.getPaginator());
		
		return model;
	}

}
