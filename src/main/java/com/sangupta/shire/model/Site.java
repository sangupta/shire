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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Holds global data of the site.
 * 
 * @author sangupta
 *
 */
public class Site {
	
	/**
	 * The absolute base URL of this site. This is used at places
	 * such as in generation of sitemap.xml files. 
	 */
	private String url;
	
	/**
	 * The current time
	 */
	private Date time = null;
	
	/**
	 * Default layout name to be used in case the file had front matter, but no layout name has been specified
	 */
	private String defaultLayoutName = null;
	
	/**
	 * a reverse chronological list of all posts in the same
	 * blog domain and its child. This is particularly useful
	 * when creating the blog archives
	 */
	private final List<Page> posts = new ArrayList<Page>();
	
	/**
	 * A complete list of all pages in the site. This is useful
	 * for generating the entire sitemaps.
	 */
	private final List<Page> pages = new ArrayList<Page>();
	
	/**
	 * If the page being processed is a Post, this contains a list of up to ten related Posts
	 */
	private final List<Page> relatedPosts = new ArrayList<Page>();
	
	/**
	 * Holds the list of top N recent posts from all blogs in this site 
	 */
	private final List<Page> recentPosts = new ArrayList<Page>();

	/**
	 * The list of all Posts in category
	 */
	private List<TagOrCategory> categories;
	
	/**
	 * The list of all Posts with tag
	 */
	private List<TagOrCategory> tags;
	
	/**
	 * Is the site in debug mode
	 */
	private boolean debug = true;
	
	/**
	 * The baseURL of the current site or the blog that we are publishing.
	 */
	private String baseURL;
	
	/**
	 * Name of the blog, if currently one is available
	 */
	private String blogName;
	
	/**
	 * Default constructor
	 */
	public Site() {
		this.time = new Date();
	}
	
	/**
	 * Add the resource as a page to the list of all pages
	 * in the site.
	 * 
	 * @param page
	 */
	public void addPage(Page page) {
		this.pages.add(page);
	}
	
	/**
	 * Add all resources as a page to the list of all pages
	 * in the site.
	 * 
	 * @param pages
	 */
	public void addAllPages(List<Page> pages) {
		this.pages.addAll(pages);
		
		this.sortPages();
	}
	
	/**
	 * Sort the entire list of pages based on their title, followed by path
	 * and filename.
	 */
	private void sortPages() {
		Collections.sort(this.pages, new PostComparatorOnNames());
	}
	
	/**
	 * Add the post to the list of available posts
	 * 
	 * @param post
	 */
	public void addPost(Page post) {
		this.posts.add(post);
	}
	
	/**
	 * Add all posts to the list of available posts in this domain
	 * 
	 * @param posts
	 */
	public void addAllPosts(List<Page> posts) {
		this.posts.addAll(posts);
		
		this.sortPosts();
	}
	
	public void addRecentPosts(List<Page> posts) {
		this.recentPosts.addAll(posts);
	}
	
	/**
	 * Sort all the given posts in their natural order - the
	 * reverse chronological order.
	 */
	private void sortPosts() {
		Collections.sort(this.posts, new PostComparatorOnDate());
	}
	
	// Usual accessors follow

	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * @return the posts
	 */
	public List<Page> getPosts() {
		return posts;
	}

	/**
	 * @return the relatedPosts
	 */
	public List<Page> getRelatedPosts() {
		return relatedPosts;
	}

	/**
	 * @return the categories
	 */
	public List<TagOrCategory> getCategories() {
		return categories;
	}

	/**
	 * @return the tags
	 */
	public List<TagOrCategory> getTags() {
		return tags;
	}

	/**
	 * @return the defaultLayoutName
	 */
	public String getDefaultLayoutName() {
		return defaultLayoutName;
	}

	/**
	 * @param defaultLayoutName the defaultLayoutName to set
	 */
	public void setDefaultLayoutName(String defaultLayoutName) {
		this.defaultLayoutName = defaultLayoutName;
	}

	/**
	 * @return the debug
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * @param debug the debug to set
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * @return the pages
	 */
	public List<Page> getPages() {
		return pages;
	}

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
	 * @param categories the categories to set
	 */
	public void setCategories(List<TagOrCategory> categories) {
		if(categories != null) {
			Collections.sort(categories);
		}
		
		this.categories = categories;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(List<TagOrCategory> tags) {
		if(tags != null) {
			Collections.sort(tags);
		}
		
		this.tags = tags;
	}

	/**
	 * @return the baseURL
	 */
	public String getBaseURL() {
		return baseURL;
	}

	/**
	 * @param baseURL the baseURL to set
	 */
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	/**
	 * @return the blogName
	 */
	public String getBlogName() {
		return blogName;
	}

	/**
	 * @param blogName the blogName to set
	 */
	public void setBlogName(String blogName) {
		this.blogName = blogName;
	}

	/**
	 * @return the recentPosts
	 */
	public List<Page> getRecentPosts() {
		return recentPosts;
	}

}
