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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sangupta.shire.core.Generator;
import com.sangupta.shire.domain.GeneratedResource;
import com.sangupta.shire.domain.RenderableResource;
import com.sangupta.shire.layouts.LayoutManager;
import com.sangupta.shire.model.Page;
import com.sangupta.shire.model.Paginator;
import com.sangupta.shire.model.ResourceComparatorOnDate;
import com.sangupta.shire.model.TemplateData;
import com.sangupta.shire.site.SiteWriter;

/**
 * @author sangupta
 *
 */
public class BlogPagesGenerator implements Generator {
	
	private static final int BATCH_SIZE = 5;
	
	/**
	 *
	 * @see com.sangupta.shire.core.Generator#getName()
	 */
	@Override
	public String getName() {
		return "Blog pages";
	}

	/**
	 *
	 * @see com.sangupta.shire.core.Generator#runBeforeResourceProcessing()
	 */
	@Override
	public boolean runBeforeResourceProcessing() {
		return false;
	}

	/**
	 *
	 * @see com.sangupta.shire.core.Generator#execute(com.sangupta.shire.model.TemplateData, java.util.List, com.sangupta.shire.site.SiteWriter)
	 */
	@Override
	public void execute(TemplateData model, List<RenderableResource> resources, List<File> dotFiles) {
		// check if we have some blog folders in there
		List<String> blogFolders = new ArrayList<String>();
		for(File dotFile : dotFiles) {
			if(dotFile.getName().equals(".blog")) {
				blogFolders.add(dotFile.getParentFile().getAbsolutePath());
			}
		}
		
		processBlogResources(blogFolders, resources, model);
	}

	/**
	 * Process all resources that are marked under the <code>.blog</code> folder
	 * to generate additional pages.
	 *  
	 * @param blogFolders
	 * @param resources
	 * @param siteWriter 
	 * @param model 
	 */
	private void processBlogResources(List<String> blogFolders, List<RenderableResource> resources, TemplateData model) {
		Map<String, List<RenderableResource>> pages = getBlogPages(blogFolders, resources);
		
		// start processing each blog individually
		for(String blog : blogFolders) {
			List<RenderableResource> list = pages.get(blog);
			processBlog(blog, list, model);
		}
	}
	
	/**
	 * @param blog the absolute base path to the root of the blog folder
	 * @param list the list of all posts in this blog
	 */
	private void processBlog(final String blog, List<RenderableResource> list, TemplateData model) {
		if(list == null || list.isEmpty()) {
			return;
		}
		
		// sort the list in reverse chronological order
		Collections.sort(list, new ResourceComparatorOnDate());  
		
		// create the home page with the top 5 entries
		int batches = (list.size() / BATCH_SIZE) + 1;
		for(int index = 0; index < batches; index++) {
			int lastIndex = (index + 1) * BATCH_SIZE;
			if(lastIndex > list.size()) {
				lastIndex = list.size();
			}
			
			List<RenderableResource> pages = list.subList(index * BATCH_SIZE, lastIndex);
			
			String name;
			// decide the name
			if(index == 0) {
				name = blog + "/index.html";
			} else {
				name = blog + "/page/" + index + ".html";
			}
			
			int nextPage = index + 1;
			if(index == (batches - 1)) {
				nextPage = -1;
			}
			
			try {
				pushBatchPage(pages, model, name, index, nextPage);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param pages
	 * @param model
	 * @param siteWriter
	 * @param name
	 * @throws IOException 
	 */
	private void pushBatchPage(List<RenderableResource> pages, TemplateData model, String name, int currentPage, int nextPage) throws IOException {
		final String layoutName = "post-listing.html";
		
		// build the model template
		Paginator paginator = new  Paginator();
		List<Page> posts = new ArrayList<Page>();
		
		model.setPaginator(paginator);
		paginator.setPosts(posts);
		
		paginator.setPerPage(BATCH_SIZE);
		paginator.setPage(currentPage);
		paginator.setNextPage(nextPage);
		paginator.setPreviousPage(currentPage - 1);
		paginator.setTotalPosts(pages.size());
		
		// clear up the page data
		model.setPage(null);
		
		// build the data for each page
		String content;
		for(RenderableResource resource : pages) {
			content = resource.getConvertedContent(model);
			
			Page post = new Page();
			post.setDate(resource.getPublishDate());
			post.setContent(content);
			
			posts.add(post);
		}
		
		// put the model into the actual template
		content = LayoutManager.layoutContent(layoutName, null, model); 
		
		// create the resource to export
		GeneratedResource resource = new GeneratedResource(SiteWriter.createBasePath(name), content);
		
		// export the page
		SiteWriter.export(resource);
	}

	/**
	 * Construct a list of all post pages per blog.
	 * 
	 * @param blogFolders
	 * @param resources
	 * @return
	 */
	private Map<String, List<RenderableResource>> getBlogPages(List<String> blogFolders, List<RenderableResource> resources) {
		Map<String, List<RenderableResource>> blogPages = new HashMap<String, List<RenderableResource>>();
		
		for(String blogFolder : blogFolders) {
			List<RenderableResource> list = new ArrayList<RenderableResource>();
			blogPages.put(blogFolder, list);
			
			for(RenderableResource renderableResource : resources) {
				String filePath = renderableResource.getFileHandle().getAbsolutePath();
				if(filePath.startsWith(blogFolder)) {
					list.add(renderableResource);
				}
			}
		}
		
		return blogPages;
	}

}
