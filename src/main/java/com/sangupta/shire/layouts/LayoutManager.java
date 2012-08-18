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

import com.sangupta.makeup.Makeup;
import com.sangupta.makeup.layouts.Layout;
import com.sangupta.makeup.layouts.LayoutType;
import com.sangupta.shire.ExecutionOptions;
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
		
		layout = Makeup.getLayout(options.getLayoutType());
		
		// set it
		layout.initialize( new File[] { layouts, includes }, Makeup.getKnownCustomTags());
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
			modifiedContent = layout.layoutWithTemplateCode(content, dataModel);
		} else {
			modifiedContent = content;
		}
		
		// layout the contents
		dataModel.put("content", modifiedContent);
		return layout.layout(layoutName, dataModel);
	}

	/**
	 * Create a data model out of this {@link TemplateData} object that can be
	 * used with the {@link Layout}.
	 * 
	 * @param data
	 *            the template data that needs to be converted
	 * 
	 * @return the converted data model as a {@link Map}
	 * 
	 * @throws <code>NullPointerException</code> if the provided
	 *         {@link TemplateData} object is null
	 * 
	 */
	private static Map<String, Object> getDataModel(final TemplateData data) {
		final Map<String, Object> model = new HashMap<String, Object>();
		
		model.put("site", data.getSite());
		model.put("page", data.getPage());
		model.put("paginator", data.getPaginator());
		
		return model;
	}

}
