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
	 * blog domain and its child 
	 */
	private final List<Post> posts = new ArrayList<Post>();
	
	/**
	 * A complete list of all pages in the site
	 */
	private final List<Post> pages = new ArrayList<Post>();
	
	/**
	 * If the page being processed is a Post, this contains a list of up to ten related Posts
	 */
	private final List<Post> relatedPosts = new ArrayList<Post>();

	/**
	 * The list of all Posts in category
	 */
	private final List<Category> categories = new ArrayList<Category>();
	
	/**
	 * The list of all Posts with tag
	 */
	private final List<Tag> tags = new ArrayList<Tag>();
	
	/**
	 * Is the site in debug mode
	 */
	private boolean debug = true;
	
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
	public void addPage(Post page) {
		this.pages.add(page);
	}
	
	/**
	 * Sort the entire list of pages based on their title, followed by path
	 * and filename.
	 */
	public void sortPages() {
		Collections.sort(this.pages, new PostComparatorOnNames());
	}
	
	/**
	 * Add the post to the list of available posts
	 * 
	 * @param post
	 */
	public void addPost(Post post) {
		this.posts.add(post);
	}
	
	/**
	 * Sort all the given posts in their natural order - the
	 * reverse chronological order.
	 */
	public void sortPosts() {
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
	public List<Post> getPosts() {
		return posts;
	}

	/**
	 * @return the relatedPosts
	 */
	public List<Post> getRelatedPosts() {
		return relatedPosts;
	}

	/**
	 * @return the categories
	 */
	public List<Category> getCategories() {
		return categories;
	}

	/**
	 * @return the tags
	 */
	public List<Tag> getTags() {
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
	public List<Post> getPages() {
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

}
