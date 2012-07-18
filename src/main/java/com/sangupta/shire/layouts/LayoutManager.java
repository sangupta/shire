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
	
	private static ExecutionOptions options = null;
	
	private static Layout layout = null;
	
	public static void initialize(ExecutionOptions options) {
		LayoutManager.options = options;

		LayoutType layoutType = options.getLayoutType();
		switch(layoutType) {
			case AutoDetect:
				layoutType = autoDetectLayoutType();
				break;
				
			default:
				// do nothing
				break;
		}
		
		// reset-the layout type option
		options.setLayoutType(layoutType);
		
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
	private static LayoutType autoDetectLayoutType() {
		return LayoutType.Velocity;
	}

	/**
	 * Method to read the list of all layouts available in the library
	 * and figure out the layout type if not specified.
	 * 
	 */
	public static void readLayoutsAndIncludes() {
		final File layouts = ShireUtils.getFolder(options, options.getLayoutsFolderName());
		final File includes = ShireUtils.getFolder(options, options.getIncludesFolderName());
		
		// initialize the proper layout handler
		switch (options.getLayoutType()) {
			case Velocity:
				layout = new VelocityLayouts();
				break;
				
			case DjangoLiquid:
				// this.layout = new DjangoLayouts();
				break;
	
			default:
				break;
		}
		
		// set it
		layout.initialize(layouts, includes);
	}
	
	public static String layoutContent(String layoutName, final String content, final TemplateData templateData) {
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
		// only if they are null
		// content would be null for post pages - where multiple posts are laid out
		String modifiedContent;
		if(content != null) {
			modifiedContent = layout.processTemplate(content, dataModel);
		} else {
			modifiedContent = content;
		}
		
		// layout the contents
		return layout.layoutContent(layoutName, modifiedContent, dataModel);
	}

	private static Map<String, Object> getDataModel(final TemplateData data) {
		final Map<String, Object> model = new HashMap<String, Object>();
		
		model.put("site", data.getSite());
		model.put("page", data.getPage());
		model.put("paginator", data.getPaginator());
		
		return model;
	}

}
