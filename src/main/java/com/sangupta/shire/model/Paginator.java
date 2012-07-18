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

import java.util.List;

/**
 * Holds data about the paginator.
 * 
 * @author sangupta
 *
 */
public class Paginator {
	
	/**
	 * number of posts per page
	 */
	private int perPage;
	
	/**
	 * Posts available for that page
	 */
	private List<Page> posts;
	
	/**
	 * Total number of posts
	 */
	private int totalPosts;
	
	/**
	 * Total number of pages
	 */
	private int totalPages;
	
	/**
	 * Number of current page
	 */
	private int page;
	
	/**
	 * Number of previous page
	 */
	private int previousPage;
	
	/**
	 * Number of next page
	 */
	private int nextPage;

	/**
	 * @return the perPage
	 */
	public int getPerPage() {
		return perPage;
	}

	/**
	 * @param perPage the perPage to set
	 */
	public void setPerPage(int perPage) {
		this.perPage = perPage;
	}

	/**
	 * @return the totalPosts
	 */
	public int getTotalPosts() {
		return totalPosts;
	}

	/**
	 * @param totalPosts the totalPosts to set
	 */
	public void setTotalPosts(int totalPosts) {
		this.totalPosts = totalPosts;
	}

	/**
	 * @return the totalPages
	 */
	public int getTotalPages() {
		return totalPages;
	}

	/**
	 * @param totalPages the totalPages to set
	 */
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * @return the previousPage
	 */
	public int getPreviousPage() {
		return previousPage;
	}

	/**
	 * @param previousPage the previousPage to set
	 */
	public void setPreviousPage(int previousPage) {
		this.previousPage = previousPage;
	}

	/**
	 * @return the nextPage
	 */
	public int getNextPage() {
		return nextPage;
	}

	/**
	 * @param nextPage the nextPage to set
	 */
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	/**
	 * @return the posts
	 */
	public List<Page> getPosts() {
		return posts;
	}

	/**
	 * @param posts the posts to set
	 */
	public void setPosts(List<Page> posts) {
		this.posts = posts;
	}

}
