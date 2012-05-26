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
import java.util.Date;
import java.util.List;

/**
 * Holds data of one post.
 * 
 * @author sangupta
 *
 */
public class Post {

	/**
	 * The title of the post
	 */
	private String title;
	
	/**
	 * the URL of the post without the domain
	 */
	private String url;
	
	/**
	 * The date assigned to this post
	 */
	private Date date;
	
	/**
	 * A unique identifier to the POST, useful in RSS feeds
	 */
	private String id;
	
	/**
	 * The list of categories to which this post belongs
	 */
	private final List<String> categories = new ArrayList<String>();
	
	/**
	 * A list of tags attached to this post, specified in YAML Front Matter
	 */
	private final List<String> tags = new ArrayList<String>();
	
	// Usual accessors follow

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
	 * @return the tags
	 */
	public List<String> getTags() {
		return tags;
	}

}
