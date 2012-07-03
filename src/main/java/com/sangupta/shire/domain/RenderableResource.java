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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.sangupta.shire.converters.Converters;
import com.sangupta.shire.core.Converter;
import com.sangupta.shire.model.TemplateData;

/**
 * @author sangupta
 *
 */
public class RenderableResource extends AbstractResource {
	
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
	 * Constructor
	 * 
	 * @param fileHandle
	 * @param path
	 * @param frontMatter
	 * @param matterEndingLine
	 */
	public RenderableResource(File fileHandle, String path, Properties frontMatter, int matterEndingLine) {
		super(fileHandle, path);
		this.frontMatter = frontMatter;
		this.matterEndingLine = matterEndingLine;
	}
	
	/**
	 * Update the extension of this resource - based on the mapping
	 * as provided by the converted used.
	 * 
	 * @param extensionMappings
	 */
	public void updateExtension(Map<String, String> extensionMappings) {
		if(extensionMappings == null) {
			return;
		}
		
		for(Entry<String, String> entry : extensionMappings.entrySet()) {
			if(this.path.endsWith(entry.getKey())) {
				this.path = StringUtils.left(this.path, this.path.length() - entry.getKey().length()) + entry.getValue();
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
	public String getConvertedContent(TemplateData templateData) throws IOException {
		if(this.convertedContent == null) {
			String content = getOriginalContent();
			
			// find out the right converter for the file's content
			// markdown, Wiki, or HTML, or something else
			Converter converter = Converters.getConverter(this.getFileName());
			
			// convert the content first
			this.convertedContent = converter.convert(content, templateData);
			
			// also update the extension mappings
			updateExtension(converter.getExtensionMappings());
		}
		
		return convertedContent;
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
		// TODO: also need to check for front matter
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

}
