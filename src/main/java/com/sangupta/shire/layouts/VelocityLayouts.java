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
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.FileResourceLoader;

import com.sangupta.shire.core.Layout;
import com.sangupta.shire.tags.DateTag;
import com.sangupta.shire.tags.FetchTag;
import com.sangupta.shire.tags.HrefTag;
import com.sangupta.shire.tags.MarkdownTag;

/**
 * Class that allows layout based on Apache Velocity templating engine.
 * 
 * @author sangupta
 *
 */
public class VelocityLayouts implements Layout {
	
	private final VelocityEngine engine = new VelocityEngine();

	@Override
	public void initialize(File layouts, File includes) {
		Properties properties = new Properties();
		
		properties.setProperty(VelocityEngine.RESOURCE_LOADER, "file");
		properties.setProperty("file" + VelocityEngine.RESOURCE_LOADER + ".class", FileResourceLoader.class.getName());
		
		final String directives = registerCustomTags();
		properties.setProperty("userdirective", directives);
		
		String templatePath = null;
		if(includes != null) {
			templatePath = layouts.getAbsolutePath() + ", " + includes.getAbsolutePath();
		} else {
			templatePath = layouts.getAbsolutePath();
		}
		
		properties.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, templatePath);
		
		engine.init(properties);
		
	}

	/**
	 * 
	 */
	private String registerCustomTags() {
		final String[] customDirectives = { MarkdownTag.class.getName(), 
											FetchTag.class.getName(),
											HrefTag.class.getName(),
											DateTag.class.getName()
										  };
		
		StringBuilder builder = new StringBuilder();
		for(String directive : customDirectives) {
			builder.append(directive).append(",");
		}
		builder.deleteCharAt(builder.length() - 1);
		
		return builder.toString();		
	}

	@Override
	public String layoutContent(String layoutName, String content, final Map<String, Object> data) {
		// find if the template is available or not
		Template template = null;
		
		try {
			template = engine.getTemplate(layoutName);
		} catch(ResourceNotFoundException e) {
			e.printStackTrace();
		} catch(ParseErrorException e) {
			e.printStackTrace();
		}
		
		// unable to find the template
		// log this as information
		// return the plain contents
		if(template == null) {
			return content;
		}
		
		// template was found
		// build up the context
		VelocityContext context = new VelocityContext(data);
		context.put("content", content);
		
		// generate the final output
		StringWriter stringWriter = new StringWriter();
		template.merge(context, stringWriter);
		
		// return the build up data
		return stringWriter.toString();
	}

	@Override
	public String processTemplate(String templateCode, Map<String, Object> dataModel) {
		VelocityContext context = new VelocityContext(dataModel);
		StringWriter stringWriter = new StringWriter();
		engine.evaluate(context, stringWriter, "stringtemplate", templateCode);
		
		return stringWriter.toString();
	}

}
