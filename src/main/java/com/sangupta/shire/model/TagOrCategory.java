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
import java.util.List;

/**
 * Holds data about one given tag or category. As there is no functional
 * difference between the two, the encapsulating model object is the same.
 * 
 * @author sangupta
 *
 */
public class TagOrCategory implements Comparable<TagOrCategory> {
	
	/**
	 * The name of this tag/category
	 */
	private String name;
	
	/**
	 * Base URL for this tag or category
	 */
	private String baseURL;
	
	/**
	 * The URL of this tag/category
	 */
	private String url;
	
	/**
	 * Holds all posts of this category
	 */
	private List<Page> posts;
	
	/**
	 * Default constructor
	 */
	public TagOrCategory() {
		
	}
	
	/**
	 * Convenience constructor
	 * 
	 * @param name
	 * @param url
	 */
	public TagOrCategory(String name, String baseURL) {
		this.name = name;
		this.baseURL = baseURL;
		this.url = baseURL + "/index.html";
	}

	/**
	 * Add the post to the list
	 * 
	 * @param post
	 */
	public void addPost(Page post) {
		if(this.posts == null) {
			this.posts = new ArrayList<Page>();
		}
		
		this.posts.add(post);
	}
	
	public int getNumPosts() {
		if(this.posts == null) {
			return 0;
		}
		
		return this.posts.size();
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		
		if(!(obj instanceof TagOrCategory)) {
			return false;
		}
		
		TagOrCategory cat = (TagOrCategory) obj;
		return this.name.equals(cat.getName());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.name;
	}
	
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(TagOrCategory other) {
		if(other == null) {
			return -1;
		}
		
		return this.name.compareToIgnoreCase(other.name);
	}

	// Usual accessors follow

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the posts
	 */
	public List<Page> getPosts() {
		return posts;
	}

	/**
	 * @return the baseURL
	 */
	public String getBaseURL() {
		return baseURL;
	}

}
