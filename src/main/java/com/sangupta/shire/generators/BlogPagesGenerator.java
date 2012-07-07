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

import org.apache.commons.io.FileUtils;

import com.sangupta.shire.core.Generator;
import com.sangupta.shire.domain.GeneratedResource;
import com.sangupta.shire.domain.RenderableResource;
import com.sangupta.shire.layouts.LayoutManager;
import com.sangupta.shire.model.FrontMatterConstants;
import com.sangupta.shire.model.Page;
import com.sangupta.shire.model.Paginator;
import com.sangupta.shire.model.ResourceComparatorOnDate;
import com.sangupta.shire.model.TagOrCategory;
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
		
		if(blogFolders.isEmpty()) {
			// nothind to do
			return;
		}
		
		// start building pages for each individual blog in there
		try {
			processBlogResources(blogFolders, resources, model);
		} catch (IOException e) {
			System.out.println("Unable to generate blog pages for pagination, categorites, tags, and year-month archives.");
			e.printStackTrace();
		}
	}

	/**
	 * Process all resources that are marked under the <code>.blog</code> folder
	 * to generate additional pages.
	 *  
	 * @param blogFolders
	 * @param resources
	 * @param siteWriter 
	 * @param model 
	 * @throws IOException 
	 */
	private void processBlogResources(List<String> blogFolders, List<RenderableResource> resources, TemplateData model) throws IOException {
		Map<String, List<RenderableResource>> pages = getBlogPages(blogFolders, resources);
		
		// start processing each blog individually
		for(String blog : blogFolders) {
			List<RenderableResource> list = pages.get(blog);
			processBlog(blog, list, model);
		}
	}
	
	/**
	 * @param blogPath the absolute base path to the root of the blog folder
	 * @param list the list of all posts in this blog
	 * @throws IOException 
	 */
	private void processBlog(final String blogPath, List<RenderableResource> list, TemplateData model) throws IOException {
		if(list == null || list.isEmpty()) {
			return;
		}
		
		// fetch the blog name from the folder
		final String blogName = FileUtils.readFileToString(new File(blogPath + "/.blog"));
		
		// sort the list in reverse chronological order
		Collections.sort(list, new ResourceComparatorOnDate()); 
		
		// extract all individual tag names, category names, and dates for archives
		List<TagOrCategory> tags = extractAllTagsOrCategories(list, FrontMatterConstants.TAGS, blogPath);
		List<TagOrCategory> categories = extractAllTagsOrCategories(list, FrontMatterConstants.CATEGORIES, blogPath);
		
		// add these to model
		model.getSite().setTags(tags);
		model.getSite().setCategories(categories);
		
		// build all the pagination pages
		List<Page> allPages = new ArrayList<Page>();
		for(RenderableResource rr : list) {
			allPages.add(rr.getResourcePost());
		}
		
		doPaginationPages(blogName, blogPath, allPages, model);
		
		// build all the date pages - archives
		
		// build all tag and categories pages
		for(TagOrCategory tag : tags) {
			doPaginationPages(blogName, tag.getBasePath(), tag.getPosts(), model);
		}
		for(TagOrCategory cat : categories) {
			doPaginationPages(blogName, cat.getBasePath(), cat.getPosts(), model);
		}
	}

	/**
	 * @param list
	 * @return
	 */
	private List<TagOrCategory> extractAllTagsOrCategories(List<RenderableResource> list, final String propertyName, final String blogPath) {
		List<TagOrCategory> result = new ArrayList<TagOrCategory>();
		
		// create a single re-usable object to prevent excessive
		// garbage collection
		TagOrCategory tagOrCategory = new TagOrCategory();
		
		// build up a list of all details
		for(RenderableResource resource : list) {
			String categoryLine = resource.getFrontMatterProperty(propertyName);
			if(categoryLine == null) {
				continue;
			}
			
			String[] tokens = categoryLine.split(FrontMatterConstants.TAG_CATEGORY_SEPARATOR);
			for(String token : tokens) {
				token = token.trim();
				if(!token.isEmpty()) {
					tagOrCategory.setName(token);
					
					int index = result.indexOf(tagOrCategory);
					if(index == -1) {
						tagOrCategory = new TagOrCategory(token, buildTagOrCategoryUrl(blogPath, propertyName, token));
						result.add(tagOrCategory);
					} else {
						tagOrCategory = result.get(index);
					}
					
					tagOrCategory.addPost(resource.getResourcePost());
				}
			}
		}
		
		return result;
	}

	/**
	 * @param category
	 * @return
	 */
	private String buildTagOrCategoryUrl(final String blogPath, final String folder, final String name) {
		return blogPath + "/" + folder + "/" + name;
	}

	/**
	 * @param blogName
	 * @param basePath
	 * @param list
	 * @param model
	 */
	private void doPaginationPages(final String blogName, final String basePath, List<Page> list, TemplateData model) {
		// create the home page with the top 5 entries
		int batches = (list.size() / BATCH_SIZE) + 1;
		for(int index = 0; index < batches; index++) {
			int lastIndex = (index + 1) * BATCH_SIZE;
			if(lastIndex > list.size()) {
				lastIndex = list.size();
			}
			
			List<Page> pages = list.subList(index * BATCH_SIZE, lastIndex);
			
			String name;
			// decide the name
			if(index == 0) {
				name = basePath + "/index.html";
			} else {
				name = basePath + "/page" + index + ".html";
			}
			
			int nextPage = index + 1;
			if(index == (batches - 1)) {
				nextPage = -1;
			}
			
			try {
				pushBatchPage(blogName, pages, model, name, index, nextPage);
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
	private void pushBatchPage(String blogName, List<Page> pages, TemplateData model, String name, int currentPage, int nextPage) throws IOException {
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
		Page page = new Page();
		page.setTitle(blogName);
		model.setPage(page);
		
		// build the data for each page
		String content;
		for(Page post : pages) {
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
