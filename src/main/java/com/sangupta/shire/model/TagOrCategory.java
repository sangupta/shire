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

import com.sangupta.shire.domain.BlogResource;
import com.sangupta.shire.util.ShireUtils;

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
	private final String name;
	
	/**
	 * Holds all posts of this category
	 */
	private List<Page> posts;
	
	/**
	 * Reference to the parent of this tag
	 */
	private final BlogResource parent;
	
	/**
	 * Return the type of this tag or category
	 */
	private final String type;
	
	/**
	 * Convenience constructor
	 * 
	 * @param name
	 * @param url
	 */
	public TagOrCategory(String name, String type, BlogResource parent) {
		this.name = name;
		this.type = type;
		this.parent = parent;
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
	
	/**
	 * Return the number of posts in the tag or category
	 * 
	 * @return
	 */
	public int getNumPosts() {
		if(this.posts == null) {
			return 0;
		}
		
		return this.posts.size();
	}
	
	/**
	 * Return the base path for this tag or category.
	 * 
	 * @return
	 */
	public String getBasePath() {
		return ShireUtils.normalizePathOrUrl(this.parent.getBaseURL() + "/" + this.type + "/" + this.name);
	}
	
	/**
	 * Return the URL to this tag or category.
	 * 
	 * @return
	 */
	public String getUrl() {
		return getBasePath() + "/index.html";
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
	 * @return the posts
	 */
	public List<Page> getPosts() {
		return posts;
	}

	/**
	 * @return the parent
	 */
	public BlogResource getParent() {
		return parent;
	}

}
