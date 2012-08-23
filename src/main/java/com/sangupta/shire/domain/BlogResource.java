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
import java.util.List;

import org.apache.commons.io.FileUtils;

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
	private String basePath;
	
	/**
	 * The name for this blog
	 */
	private String blogName;
	
	/**
	 * All renderable resources associated with this blog
	 */
	private final List<RenderableResource> resources = new ArrayList<RenderableResource>();
	
	/**
	 * Constructor for a blog resource.
	 * 
	 * @param blogFile
	 */
	public BlogResource(File blogFile) {
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
	}
	
	/**
	 * Add the file as a blog resource in this blog.
	 * 
	 * @param resource
	 */
	public void addResource(RenderableResource resource) {
		this.resources.add(resource);
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
	 * @param basePath the basePath to set
	 */
	public void setBasePath(String basePath) {
		this.basePath = basePath;
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
	 * @return the resources
	 */
	public List<RenderableResource> getResources() {
		return resources;
	}

}
