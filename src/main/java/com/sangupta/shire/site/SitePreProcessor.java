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

package com.sangupta.shire.site;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sangupta.shire.Shire;
import com.sangupta.shire.domain.BlogResource;
import com.sangupta.shire.domain.RenderableResource;
import com.sangupta.shire.model.ConfigurationConstants;
import com.sangupta.shire.model.Page;
import com.sangupta.shire.model.PostComparatorOnDate;
import com.sangupta.shire.util.ShireUtils;

/**
 * A pre-processor that runs on a site and fills in the missing fields
 * for various objects so that they need not be pre-computed.
 * 
 * @author sangupta
 *
 */
public class SitePreProcessor {
	
	/**
	 * Reference to the site's paradise, the shire.
	 */
	private Shire shire = null;

	/**
	 * Constructor
	 * 
	 * @param shire
	 */
	public SitePreProcessor(Shire shire) {
		this.shire = shire;
	}
	
	/**
	 * Pre-process the site.
	 * 
	 */
	public void preProcess() {
		// add all resources to each blog, as applicable
		collectAllBlogResources();
		
		// build the blogs
		for(BlogResource blog : this.shire.getSiteDirectory().getBlogs()) {
			blog.buildBlog();
		}
		// create all resource pages
		List<RenderableResource> renderableResources = this.shire.getSiteDirectory().getRenderableResources();
		for(RenderableResource rr : renderableResources) {
			rr.getResourcePost().mergeFrontMatter(this.shire);
		}
		
		// sort and ...
		sortAndSetNextPreviousLinks();
		
		// select top N posts from all blogs
		List<Page> topPosts = new ArrayList<Page>();
		for(BlogResource blog : this.shire.getSiteDirectory().getBlogs()) {
			topPosts.addAll(blog.getAllPosts());
		}
		Collections.sort(topPosts, new PostComparatorOnDate());
		int top = ShireUtils.getConfigPropertyAsInt(shire, ConfigurationConstants.NUM_RECENT_POSTS, 10);
		if(top > topPosts.size()) {
			top = topPosts.size();
		}
		
		topPosts = topPosts.subList(0, top);
		this.shire.getTemplateData().getSite().addRecentPosts(topPosts);
	}
	
	/**
	 * Collect all blog resources, sort and then fix up next/previus entry links
	 */
	private void collectAllBlogResources() {
		List<RenderableResource> renderableResources = this.shire.getSiteDirectory().getRenderableResources();
		List<BlogResource> blogs = this.shire.getSiteDirectory().getBlogs();
		
		// now we have a list of all renderable resources with us
		// find out all resources, that are part of a blog
		// and assign them accordingly
		for(RenderableResource resource : renderableResources) {
			for(BlogResource blog : blogs) {
				if(resourceIsInBlog(resource, blog)) {
					resource.markAsBlog(blog);
					blog.addResource(resource);
				}
			}
		}
	}
	
	/**
	 * Sort and apply next/previous links
	 */
	private void sortAndSetNextPreviousLinks() {
		List<RenderableResource> renderableResources = null;
		List<BlogResource> blogs = this.shire.getSiteDirectory().getBlogs();
		
		// sort them out
		for(BlogResource blog : blogs) {
			renderableResources = blog.getResources();
			Collections.sort(renderableResources);
			
			// now set next/previous entry links
			final int size = renderableResources.size();
			for(int index = 0; index < size; index++) {
				RenderableResource resource = renderableResources.get(index);
				if(index > 0) {
					resource.getResourcePost().setPreviousEntry(renderableResources.get(index - 1).getResourcePost());
				}
				if((index + 1) < size) {
					resource.getResourcePost().setNextEntry(renderableResources.get(index + 1).getResourcePost());
				}
			}
		}
	}
	
	/**
	 * Finds whether the resource is part of this blog or not.
	 * 
	 * @param resource
	 * @param blog
	 * @return
	 */
	private boolean resourceIsInBlog(RenderableResource resource, BlogResource blog) {
		String basePath = blog.getBasePath() + File.separatorChar;
		String filePath = resource.getFileHandle().getAbsoluteFile().getAbsolutePath();
		if(filePath.startsWith(basePath)) {
			return true;
		}
		
		return false;
	}
}
