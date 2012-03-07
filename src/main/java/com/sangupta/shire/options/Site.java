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

package com.sangupta.shire.options;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Site {
	
	/**
	 * the current time
	 */
	private final Date time = new Date();
	
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

}
