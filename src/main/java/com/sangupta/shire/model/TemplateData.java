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

package com.sangupta.shire.model;

import java.util.Properties;

import org.apache.commons.lang3.BooleanUtils;

/**
 * Stores the template data that is passed on to the page
 * for rendering. Refer to https://github.com/mojombo/jekyll/wiki/Template-Data
 * for more details.
 * 
 * @author sangupta
 *
 */
public class TemplateData {
	
	private Site site;
	
	private Page page;
	
	private Paginator paginator;
	
	public TemplateData(Properties configuration) {
		this.site = new Site();
		this.page = new Page();
		this.paginator = new Paginator();
		
		if(configuration != null) {
			mergeWithTemplateData(configuration);
		}
	}
	
	/**
	 * Merge the properties of the config file with the site-wide data.
	 * 
	 * @param configuration
	 */
	private void mergeWithTemplateData(Properties configuration) {
		this.site.setDebug(BooleanUtils.toBoolean(configuration.getProperty("debug")));
		this.site.setUrl(configuration.getProperty("url"));
	}

	/**
	 * Merge the front-matter of the page with this template data.
	 * 
	 * @param pageFrontMatter
	 */
	public void mergePageFrontMatter(Properties pageFrontMatter) {
		this.page = new Page(); 
		
		if(pageFrontMatter == null) {
			return;
		}
		
		page.mergeFrontMatter(pageFrontMatter);
	}
	
	/**
	 * Extract all tags and categories from this list of resources.
	 * 
	 * @param resources
	 */
	public void extractFromResources() {
		// TODO: fix this
	}

	// Usual accessors follow

	/**
	 * @return the site
	 */
	public Site getSite() {
		return site;
	}

	/**
	 * @param site the site to set
	 */
	public void setSite(Site site) {
		this.site = site;
	}

	/**
	 * @return the page
	 */
	public Page getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(Page page) {
		this.page = page;
	}

	/**
	 * @return the paginator
	 */
	public Paginator getPaginator() {
		return paginator;
	}

	/**
	 * @param paginator the paginator to set
	 */
	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

}
