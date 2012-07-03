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

package com.sangupta.shire.generators;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.sangupta.shire.core.Generator;
import com.sangupta.shire.domain.GeneratedResource;
import com.sangupta.shire.domain.RenderableResource;
import com.sangupta.shire.model.TemplateData;
import com.sangupta.shire.site.SiteWriter;

/**
 * Generator that creates a sitemap of all pages in the website.
 * 
 * @author sangupta
 *
 */
public class SiteMapGenerator implements Generator {
	
	/**
	 * @see com.sangupta.shire.core.Generator#getName()
	 */
	@Override
	public String getName() {
		return "Sitemap.xml";
	}

	/**
	 * @see com.sangupta.shire.core.Generator#runBeforeResourceProcessing()
	 */
	@Override
	public boolean runBeforeResourceProcessing() {
		return false;
	}

	/**
	 * @see com.sangupta.shire.core.Generator#execute(com.sangupta.shire.model.TemplateData)
	 */
	@Override
	public void execute(TemplateData model, List<RenderableResource> resources, List<File> dotFiles) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		builder.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
		String url = model.getSite().getUrl();

		if(resources != null && resources.size() > 0) {
			for(RenderableResource resource : resources) {
				builder.append("  <url>\n");
				
				builder.append("    <loc>");
				builder.append(url);
				if(!resource.getUrl().startsWith("/")) {
					builder.append("/");
				}
				builder.append(resource.getUrl());
				builder.append("</loc>\n");
				
				builder.append("    <lastmod>");
				builder.append(DateFormatUtils.format(resource.getPublishDate(), "yyyy-MM-dd"));
				builder.append("</lastmod>\n");
				
				builder.append("    <changefreq>");
				builder.append(SiteMapChangeFrequency.monthly);
				builder.append("</changefreq>\n");
				
				builder.append("    <priority>");
				builder.append("0.5");
				builder.append("</priority>\n");
				
				builder.append("  </url>\n");
			}
		}

		builder.append("</urlset>\n");
		
		// create a resource of this file
		GeneratedResource resource = new GeneratedResource("/sitemap.xml", builder.toString());
		
		// export it
		SiteWriter.export(resource);
	}

	/**
	 * Enumeration defining the sitemap change frequency.
	 * 
	 * @author sangupta
	 *
	 */
	public static enum SiteMapChangeFrequency {
		
		always,
		
		hourly,
		
		daily,
		
		weekly,
		
		monthly,
		
		yearly,
		
		never;
		
	}
}
