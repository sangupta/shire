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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.sangupta.shire.Shire;
import com.sangupta.shire.domain.RenderableResource;
import com.sangupta.shire.util.ShireUtils;

/**
 * Holds data model for a given page/blog post - the page which is under
 * processing by the layout.
 * 
 * @author sangupta
 *
 */
public class Page implements Comparable<Page> {
	
	/**
	 * The unrendered content of the page
	 */
	private String content;
	
	/**
	 * The title of the page
	 */
	private String title;
	
	/**
	 * The URL of the page without the domain
	 */
	private String url;
	
	/**
	 * The date assigned to this post
	 */
	private Date date;
	
	/**
	 * Unique pageID assigned to this post
	 */
	private String id;
	
	/**
	 * Indicates if publishing of this page is to be put on hold.
	 */
	private boolean published = true;
	
	/**
	 * The categories the page in
	 */
	private final List<TagOrCategory> categories = new ArrayList<TagOrCategory>();
	
	/**
	 * The tags this page belongs to
	 */
	private final List<TagOrCategory> tags = new ArrayList<TagOrCategory>();
	
	/**
	 * Reference to the page's front matter
	 */
	private Properties frontMatter;
	
	/**
	 * Identifies whether the post is part of a blog or not
	 */
	private boolean blogPost;
	
	/**
	 * Reference to next entry in this blog
	 */
	private Page nextEntry;
	
	/**
	 * Reference to previous entry in this blog
	 */
	private Page previousEntry;
	
	public Page(RenderableResource resource, Shire shire) {
		// no need to initialize properties, if the resource is null
		// this is usually done for resources that are generated on the fly 
		if(resource == null) {
			return;
		}
		
		this.frontMatter = resource.getFrontMatter();

		this.setDate(resource.getPublishDate());
		this.setTitle(resource.getFrontMatterProperty("title"));
		
		if(shire != null) {
			String pageURL = shire.getSiteWriter().getURL(resource);
			this.setUrl(pageURL);
		}
		
		try {
			this.setContent(resource.getConvertedContent());
		} catch (IOException e) {
			throw new RuntimeException("Unable to build post out of this resource", e);
		}
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String title = this.getPageProperty(FrontMatterConstants.PAGE_TITLE);
		if(title == null) {
			return super.toString();
		}
		
		return title;
	}
	
	/**
	 * Return the value of configuration property with the given name if
	 * mentioned in the page's front matter data.
	 *  
	 * @param key
	 * @return
	 */
	public String getPageProperty(String key) {
		if(this.frontMatter == null) {
			return null;
		}
		
		return this.frontMatter.getProperty(key);
	}
	
	/**
	 * @param pageFrontMatter
	 * @param blogPath
	 */
	public void mergeFrontMatter(String blogPath, Shire shire) {
		// read the title from the front matter
		this.setTitle(frontMatter.getProperty(FrontMatterConstants.PAGE_TITLE));

		// read the date from the front matter
		String date = frontMatter.getProperty("date");
		if(date != null) {
			this.setDate(ShireUtils.parsePostDate(date));
		}
		
		// read the tags from the front matter
		String tags = frontMatter.getProperty("tags");
		if(tags != null && !tags.trim().isEmpty()) {
			String[] tokens = StringUtils.split(tags, FrontMatterConstants.TAG_CATEGORY_SEPARATOR);
			for(String tag : tokens) {
				if(!("".equals(tag.trim()))) {
					String tagPath = shire.getSiteWriter().createBasePath(blogPath + "/" + FrontMatterConstants.TAGS + "/" + tag);
					this.tags.add(new TagOrCategory(tag, tagPath));
				}
			}
		}
		
		// read the categories from the fron matter
		String categories = frontMatter.getProperty("categories");
		if(categories != null && !categories.trim().isEmpty()) {
			String[] tokens = StringUtils.split(categories, " ;,");
			for(String category : tokens) {
				if(!("".equals(category.trim()))) {
					String catPath = shire.getSiteWriter().createBasePath(blogPath + "/" + FrontMatterConstants.CATEGORIES + "/" + category);
					this.categories.add(new TagOrCategory(category, catPath));
				}
			}
		}
		
		// check for published flags
		String published = frontMatter.getProperty("published");
		if("false".equalsIgnoreCase(published)) {
			this.published = false;
		}
	}

	/**
	 * All properties of the page have been set and are
	 * ready for post-processing.
	 */
	public void postProcessProperties() {
		this.id = ShireUtils.createUniquePageID(this.url);
		updateCategoriesFromUrl();
	}

	/**
	 * Update the categories as extracted from the url.
	 */
	private void updateCategoriesFromUrl() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Page other) {
		if(other == null) {
			return -1;
		}
		
		return this.id.compareToIgnoreCase(other.id);
	}

	// Usual accessors follow

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the categories
	 */
	public List<TagOrCategory> getCategories() {
		if(this.categories.isEmpty()) {
			return null;
		}
		
		return this.categories;
	}

	/**
	 * @return the tags
	 */
	public List<TagOrCategory> getTags() {
		if(this.tags.isEmpty()) {
			return null;
		}
		
		return this.tags;
	}

	/**
	 * @return the published
	 */
	public boolean isPublished() {
		return published;
	}

	/**
	 * @param published the published to set
	 */
	public void setPublished(boolean published) {
		this.published = published;
	}

	/**
	 * @return the blogPost
	 */
	public boolean isBlogPost() {
		return blogPost;
	}

	/**
	 * @param blogPost the blogPost to set
	 */
	public void setBlogPost(boolean blogPost) {
		this.blogPost = blogPost;
	}

	/**
	 * @return the nextEntry
	 */
	public Page getNextEntry() {
		return nextEntry;
	}

	/**
	 * @param nextEntry the nextEntry to set
	 */
	public void setNextEntry(Page nextEntry) {
		this.nextEntry = nextEntry;
	}

	/**
	 * @return the previousEntry
	 */
	public Page getPreviousEntry() {
		return previousEntry;
	}

	/**
	 * @param previousEntry the previousEntry to set
	 */
	public void setPreviousEntry(Page previousEntry) {
		this.previousEntry = previousEntry;
	}

}
