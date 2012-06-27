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
	 * The current time
	 */
	private Date time = null;
	
	/**
	 * Default layout name to be used in case the file had front matter, but no layout name has been specified
	 */
	private String defaultLayoutName = null;
	
	/**
	 * a reverse chronological list of all posts
	 */
	private final List<Post> posts = new ArrayList<Post>();
	
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
	 * Add the post to the list of available posts
	 * 
	 * @param post
	 */
	public void addPost(Post post) {
		this.posts.add(post);
	}
	
	public void sortPosts() {
		Collections.sort(this.posts);
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

}
