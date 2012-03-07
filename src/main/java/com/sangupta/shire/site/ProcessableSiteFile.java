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

package com.sangupta.shire.site;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.sangupta.shire.config.YmlConfigReader;
import com.sangupta.shire.converters.Converters;
import com.sangupta.shire.core.Converter;
import com.sangupta.shire.layouts.LayoutManager;

/**
 * Holds a set of input and output files of site that will be processed.
 * 
 * @author sangupta
 * @since Feb 23, 2012
 */
public class ProcessableSiteFile extends AbstractSiteFile {

	/**
	 * Global YML reader for the front matter
	 */
	private static final YmlConfigReader ymlConfigReader = new YmlConfigReader(); 
	
	/**
	 * Holds the lines of the given text file.
	 */
	private List<String> lines = null;
	
	/**
	 * Holds the index of the YML's first separator
	 */
	private int ymlSeparatorStart = -1;
	
	/**
	 * Holds the index of the YML's second separator
	 */
	private int ymlSeparatorEnd = -1;
	
	/**
	 * Holds the properties read from the YML front matter, if present
	 */
	private Properties ymlProperties = null;
	
	/**
	 * Contains the processed content once everything has been done
	 */
	private String processedContent = null;
	
	/**
	 * Constructor
	 * 
	 * @param inputFile
	 */
	public ProcessableSiteFile(File inputFile) {
		super(inputFile);
		
		readInputFile();
	}
	
	/**
	 * Read the input file.
	 *  
	 */
	private void readInputFile() {
		try {
			this.lines = FileUtils.readLines(this.input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns true if the input file does have YML Front Matter included.
	 * 
	 * @return
	 */
	public boolean hasFrontMatter() {
		boolean first = false;
		boolean second = false;
		
		if(lines != null) {
			int lineIndex = 0;
			for(String line : lines) {
				// ignore empty lines
				if(line.trim().equals("")) {
					continue;
				}
				
				// find the first non-empty line
				// does it contain three-dashes
				if("---".equals(line)) {
					if(!first) {
						first = true;
						this.ymlSeparatorStart = lineIndex;
					} else {
						second = true;
						this.ymlSeparatorEnd = lineIndex;
					}
				}
				
				// the line is not YML Front Matter section starter
				if(!first) {
					return false;
				}
				
				// YML Front Matter found
				if(first && second) {
					return true;
				}
				
				lineIndex++;
			}
		}
		
		return false;
	}
	
	/**
	 * Process the content of the file using the technology used
	 * to write the pages (Markdown etc).
	 * 
	 */
	public void processContent(LayoutManager layoutManager) {
		// fetch the contents of the file
		String content;
		if(this.ymlSeparatorEnd >= 0) {
			content = StringUtils.join(this.lines.subList(this.ymlSeparatorEnd + 1, this.lines.size()), '\n');
		} else {
			content = StringUtils.join(this.lines, '\n');
		}
				
		// find out the layout name that needs to be used
		String layoutName = null;
		if(this.ymlProperties != null) {
			layoutName = this.ymlProperties.getProperty("layout");
		}
		
		// find out the right converter for the file's content
		// markdown, Wiki, or HTML, or something else
		Converter converter = Converters.getConverter(this.input.getName());
		
		// convert the content first
		String convertedContent = converter.convert(content);
		
		// now if we have the name of the layout
		// we need to merge this content into the layout
		if(StringUtils.isNotEmpty(layoutName)) {
			this.processedContent = layoutManager.layoutContent(layoutName, convertedContent, this.ymlProperties);
		}
		
		// as we are done processing conent
		// we can clear the input list
		this.lines = null;
	}

	/**
	 * Process the front matter and initialize the layout manager
	 * for the same.
	 */
	public void processFrontMatter() {
		if(this.ymlSeparatorStart >= 0) {
			this.ymlProperties = ymlConfigReader.readLines(this.lines.subList(this.ymlSeparatorStart + 1, this.ymlSeparatorEnd));
		}
	}
	
	@Override
	public String getExportPath(String rootPath) {
		// see if we have a permalink
		if(this.ymlProperties != null) {
			String permalink = this.ymlProperties.getProperty("permalink");
			if(StringUtils.isNotEmpty(permalink)) {
				return permalink;
			}
		}
		
		// do the default
		return super.getExportPath(rootPath);
	}
	
	/**
	 * Method that indicates that export of file is now complete.
	 * 
	 */
	public void exportComplete() {
		this.processedContent = null;
	}
	
	// Usual accessors follow

	/**
	 * @return the processedContent
	 */
	public String getProcessedContent() {
		return processedContent;
	}

}
