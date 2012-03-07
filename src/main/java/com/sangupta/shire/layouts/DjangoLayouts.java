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
import java.io.IOException;
import java.util.Map;

import net.asfun.jangod.template.TemplateEngine;

import com.sangupta.shire.core.Layout;

/**
 * Converts django/liquid markup to HTML.
 * May use Jangod framework for the same.
 * 
 * @author sangupta
 *
 */
public class DjangoLayouts implements Layout {
	
	private final TemplateEngine engine = new TemplateEngine();

	@Override
	public void initialize(File layouts, File includes) {
		engine.getConfiguration().setWorkspace(layouts.getAbsolutePath());
	}

	@Override
	public String layoutContent(String layoutName, final String content, final Map<String, Object> data) {
		data.put("content", content);
		try {
			return engine.process(layoutName, data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return content;
	}

	@Override
	public String processTemplate(String templateCode, Map<String, Object> dataModel) {
		return templateCode;
	}

}
