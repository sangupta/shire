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

import java.util.ArrayList;
import java.util.List;

import com.sangupta.shire.core.Generator;
import com.sangupta.shire.domain.GeneratedResource;
import com.sangupta.shire.model.Post;
import com.sangupta.shire.model.TemplateData;

/**
 * Generator that creates a sitemap of all pages in the website.
 * 
 * @author sangupta
 *
 */
public class SiteMapGenerator implements Generator {

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
	public List<GeneratedResource> execute(TemplateData model) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("<xml>\n");
		builder.append("<pages>\n");
		
		List<Post> posts = model.getSite().getPages();
		if(posts != null && posts.size() > 0) {
			for(Post post : posts) {
				builder.append("<page url=\"");
				builder.append(post.getUrl());
				builder.append("\" />\n");
			}
		}

		builder.append("</pages>\n");
		
		GeneratedResource resource = new GeneratedResource("/sitemap.xml", builder.toString());
		
		List<GeneratedResource> resources = new ArrayList<GeneratedResource>();
		resources.add(resource);
		return resources;
	}

}
