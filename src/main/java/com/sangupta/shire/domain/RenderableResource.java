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
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.sangupta.makeup.Makeup;
import com.sangupta.makeup.converters.Converter;
import com.sangupta.shire.Shire;
import com.sangupta.shire.model.Page;

/**
 * @author sangupta
 *
 */
public class RenderableResource extends AbstractResource implements Comparable<RenderableResource> {
	
	/**
	 * Store the front matter as extracted from the file.
	 */
	private Properties frontMatter;
	
	/**
	 * Line index at which the front matter has ended. This helps us
	 * skip when re-reading the file's content when rendering
	 */
	private int matterEndingLine;
	
	/**
	 * Stores the original un-processed content of this resource, except
	 * the front matter
	 */
	private String originalContent = null;
	
	/**
	 * Stores the converted content - content returned after running
	 * the converter over the {@link #originalContent}.
	 */
	private String convertedContent = null;
	
	/**
	 * Internal handle to the post
	 */
	private final Page post; // = new Page();
	
	/**
	 * Reference to the blog instance that this post may be a part of
	 */
	private BlogResource blog = null;

	/**
	 * Constructor
	 * 
	 * @param fileHandle
	 * @param path
	 * @param frontMatter
	 * @param matterEndingLine
	 */
	public RenderableResource(File fileHandle, Properties frontMatter, int matterEndingLine, Shire shire) {
		super(fileHandle);
		this.frontMatter = frontMatter;
		this.matterEndingLine = matterEndingLine;
		
		this.post = new Page(this, shire);
	}
	
	public int compareTo(RenderableResource other) {
		if(other == null) {
			return -1; // we are better
		}
		
		int value = this.getPublishDate().compareTo(other.getPublishDate());
		return (0 - value); // descending order
	}
	
	/**
	 * Mark this resource as being part of a blog
	 * 
	 * @param blog
	 */
	public void markAsBlog(BlogResource blog) {
		this.blog = blog;
	}
	
	/**
	 * Update the extension of this resource - based on the mapping
	 * as provided by the converted used.
	 * 
	 * @param extensionMappings
	 */
	public void updateExtension(List<String> extensionMappings) {
		if(extensionMappings == null) {
			return;
		}
		
		for(String extension : extensionMappings) {
			if(this.path.endsWith(extension)) {
				this.path = StringUtils.left(this.path, this.path.length() - extension.length()) + ".html";
				return;
			}
		}
	}
	
	/**
	 * @return the originalContent
	 * @throws IOException 
	 */
	public String getOriginalContent() throws IOException {
		if(originalContent == null) {
			List<String> lines = FileUtils.readLines(this.fileHandle);
			this.originalContent = StringUtils.join(lines.subList(this.matterEndingLine, lines.size()), '\n');
		}
		
		return originalContent;
	}

	/**
	 * @return the convertedContent
	 * @throws IOException 
	 */
	public String getConvertedContent() throws IOException {
		if(this.convertedContent == null) {
			String content = getOriginalContent();
			
			// find out the right converter for the file's content
			// markdown, Wiki, or HTML, or something else
			Converter converter = Makeup.getConverter(this.getFileName());
			
			// convert the content first
			this.convertedContent = converter.convert(content, this.frontMatter);
			
			// also update the extension mappings
			updateExtension(converter.getProbableExtensions());
		}
		
		return convertedContent;
	}
	
	public Page getResourcePost() {
		return this.post;
	}

	/**
	 * Return the value of the property with the given name from the
	 * front matter associated with this resource.
	 * 
	 * @param propertyName
	 * @return
	 */
	public String getFrontMatterProperty(String propertyName) {
		if(this.frontMatter == null) {
			return null;
		}
		
		return this.frontMatter.getProperty(propertyName);
	}
	
	/**
	 * @see com.sangupta.shire.domain.AbstractResource#getPublishDate()
	 */
	@Override
	public Date getPublishDate() {
		if(this.post != null && this.post.getDate() != null) {
			return this.post.getDate();
		}
		
		return super.getPublishDate();
	}

	/**
	 * @see com.sangupta.shire.domain.Resource#hasFrontMatter()
	 */
	@Override
	public boolean hasFrontMatter() {
		return true;
	}
	
	// Usual accessors follow

	/**
	 * @see com.sangupta.shire.domain.Resource#getFrontMatter()
	 */
	@Override
	public Properties getFrontMatter() {
		return this.frontMatter;
	}

	/**
	 * @return the matterEndingLine
	 */
	public int getMatterEndingLine() {
		return matterEndingLine;
	}

	/**
	 * @return the blog
	 */
	public BlogResource getBlog() {
		return blog;
	}

}
