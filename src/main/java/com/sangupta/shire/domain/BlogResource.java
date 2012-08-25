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

package com.sangupta.shire.domain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.shire.Shire;
import com.sangupta.shire.model.FrontMatterConstants;
import com.sangupta.shire.model.Page;
import com.sangupta.shire.model.TagOrCategory;

/**
 * Defines data for one blog as found in this site.
 * 
 * @author sangupta
 *
 */
public class BlogResource {
	
	/**
	 * The absolute base path for this blog resource
	 */
	private final String basePath;
	
	/**
	 * The name for this blog
	 */
	private final String blogName;
	
	/**
	 * The baseURL for this blog
	 */
	private final String baseURL;
	
	/**
	 * All renderable resources associated with this blog
	 */
	private final List<RenderableResource> resources = new ArrayList<RenderableResource>();
	
	/**
	 * All tags that posts of this blog are tagged with
	 */
	private final List<TagOrCategory> tags = new ArrayList<TagOrCategory>();
	
	/**
	 * All categories that posts of this blog are put in
	 */
	private final List<TagOrCategory> categories = new ArrayList<TagOrCategory>();
	
	/**
	 * Constructor for a blog resource.
	 * 
	 * @param blogFile
	 */
	public BlogResource(File blogFile, Shire shire) {
		if(blogFile == null) {
			throw new IllegalArgumentException("Cannot work on a null file.");
		}
		
		if(!(blogFile.getName().equals(".blog"))) {
			throw new IllegalArgumentException("A blog is represented by .blog file.");
		}
		
		try {
			String name = FileUtils.readFileToString(blogFile);
			this.blogName = name;
		} catch (IOException e) {
			throw new IllegalArgumentException("Cannot read blog file.");
		}
		
		this.basePath = blogFile.getAbsoluteFile().getParentFile().getAbsolutePath();
		this.baseURL = shire.getSiteWriter().createBasePath(this.basePath);
	}
	
	/**
	 * Add the file as a blog resource in this blog.
	 * 
	 * @param resource
	 */
	public void addResource(RenderableResource resource) {
		this.resources.add(resource);
	}
	
	public void buildBlog() {
		// extract tags from this resource
		Collection<TagOrCategory> tags = extractAllTagsOrCategories(FrontMatterConstants.TAGS);
		if(tags != null) {
			this.tags.addAll(tags);
		}
		
		// extract categories from this resource
		Collection<TagOrCategory> categories = extractAllTagsOrCategories(FrontMatterConstants.CATEGORIES);
		if(categories != null) {
			this.categories.addAll(categories);
		}
	}
	
	/**
	 * @param list
	 * @return
	 */
	private Collection<TagOrCategory> extractAllTagsOrCategories(final String propertyName) {
		Map<String, TagOrCategory> result = new HashMap<String, TagOrCategory>();
		
		// create a single re-usable object to prevent excessive
		// garbage collection
		TagOrCategory tagOrCategory; // = new TagOrCategory();
		
		// build up a list of all details
		for(RenderableResource resource : this.resources) {
			String categoryLine = resource.getFrontMatterProperty(propertyName);
			if(AssertUtils.isEmpty(categoryLine)) {
				continue;
			}
			
			String[] tokens = StringUtils.split(categoryLine, FrontMatterConstants.TAG_CATEGORY_SEPARATOR);
			for(String token : tokens) {
				token = token.trim();
				if(!token.isEmpty()) {
					tagOrCategory = result.get(token);
					if(tagOrCategory == null) {
						tagOrCategory = new TagOrCategory(token, propertyName, this);
						result.put(token, tagOrCategory);
					}
					
					Page post = resource.getResourcePost();
					
					tagOrCategory.addPost(post);
					
					if(propertyName.equals(FrontMatterConstants.TAGS)) {
						post.getTags().add(tagOrCategory);
					} else {
						post.getCategories().add(tagOrCategory);
					}
				}
			}
		}
		
		return result.values();
	}
	
	/**
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof BlogResource)) {
			return false;
		}
		
		BlogResource other = (BlogResource) obj;
		return this.basePath.equals(other.basePath);
	}
	
	/**
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.basePath.hashCode();
	}
	
	/**
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Blog: " + this.getBlogName();
	}
	
	// Usual accessors follow

	/**
	 * @return the basePath
	 */
	public String getBasePath() {
		return basePath;
	}

	/**
	 * @return the blogName
	 */
	public String getBlogName() {
		return blogName;
	}

	/**
	 * @return the resources
	 */
	public List<RenderableResource> getResources() {
		return resources;
	}

	/**
	 * @return the tags
	 */
	public List<TagOrCategory> getTags() {
		return tags;
	}

	/**
	 * @return the categories
	 */
	public List<TagOrCategory> getCategories() {
		return categories;
	}

	/**
	 * @return the baseURL
	 */
	public String getBaseURL() {
		return baseURL;
	}

}
