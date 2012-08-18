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

package com.sangupta.shire.tools;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.sangupta.blogparser.BlogParser;
import com.sangupta.blogparser.BlogType;
import com.sangupta.blogparser.domain.Blog;
import com.sangupta.blogparser.domain.BlogPage;
import com.sangupta.blogparser.domain.BlogPost;

/**
 * Command line tool to import an existing blog from services like Blogger, Wordpress 
 * and MovableType to the Shire platform.
 * 
 * @author sangupta
 * @since 0.3.0
 */
public class ShireBlogImporter {
	
	/**
	 * The platform dependent new line character
	 */
	private static final String NEW_LINE = System.getProperty("line.separator");
	
	/**
	 * Main method that takes command line arguments and exports the blog.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length != 4) {
			usage();
		}
		
		String importFile = args[0]; // "C:/Users/sangupta/Desktop/blog-08-17-2012.xml";
		String provider = args[1]; // "Blogger";
		String exportPath = args[2]; // "c:/testExport";
		String layoutName = args[3]; // "master.html";
		
		ExportOptions options = new ExportOptions();
		options.blogType = BlogType.valueOf(provider);
		options.exportPath = new File(exportPath);
		options.importFile = new File(importFile);
		options.layoutName = layoutName;
		
		new ShireBlogImporter(options).exportBlog();
	}
	
	/**
	 * Output the usage format for the command-line tool and exit.
	 * 
	 */
	private static void usage() {
		System.out.println("$ java com.sangupta.shire.tools.ShireBlogImport <importFile> <type> <exportPath> <layoutName>");
		System.out.println("    ");
		System.out.println("    <importFile>     The import file, usually XML, as exported from the previous blog service");
		System.out.println("    <type>           The blog format type: one of Blogger, Wordpress, MovableType");
		System.out.println("    <exportPath>     The folder where all files will be written, should not exist");
		System.out.println("    <layoutName>     The layout name that will be added to all posts and pages");
		
		System.exit(0);
	}

	/**
	 * The options as extracted from the 
	 */
	private ExportOptions options;
	
	/**
	 * Constructor with {@link ExportOptions}
	 * 
	 * @param options
	 */
	public ShireBlogImporter(ExportOptions options) {
		this.options = options;
	}
	
	/**
	 * Export the blog with the configured parameters
	 * 
	 */
	public void exportBlog() {
		if(this.options.importFile == null || !this.options.importFile.exists()) {
			System.out.println("Input file does not exists... exiting!");
			return;
		}
		
		if(this.options.blogType == null) {
			System.out.println("Unable to decipher the blog export format... exiting!");
			return;
		}
		
		if(this.options.exportPath == null) {
			System.out.println("No export path provided... exiting!");
			return;
		}
		
		if(this.options.exportPath.exists() && this.options.exportPath.isDirectory()) {
			System.out.println("Export path already exists... exiting!");
			return;
		}
		
		if(!this.options.exportPath.exists()) {
			boolean success = this.options.exportPath.mkdirs();
			if(!success) {
				System.out.println("Unable to create output folder... exiting!");
				return;
			}
		}
		
		startExport();
	}

	/**
	 * Start exporting the entire blog writing each page and post in a new file.
	 *
	 * @throws RuntimeException in case I/O fails
	 *  
	 */
	protected void startExport() {
		String blogData;
		try {
			blogData = FileUtils.readFileToString(this.options.importFile);
		} catch (IOException e) {
			// unable to read data
			System.out.println("Unable to read blog data from input file... exiting!");
			return;
		}
		
		Blog blog = BlogParser.parse(blogData, this.options.blogType);
		if(blog == null) {
			System.out.println("Unable to parse blog data... exiting!");
			return;
		}
		
		try {
			// start exporting each post
			if(blog.getPosts() != null) {
				for(BlogPost post : blog.getPosts()) {
					writeEachPost(post);
				}
			}
			
			// start exporting each page
			if(blog.getPages() != null) {
				for(BlogPage page : blog.getPages()) {
					writeEachPage(page);
				}
			}
			
			System.out.println("Done exporting the blog successfully!");
		} catch(IOException e) {
			throw new RuntimeException("Unable to export blog files.", e);
		}
	}

	/**
	 * Write one blog page to the export folder.
	 * 
	 * @param page the page that needs to be written.
	 * 
	 * @throws IOException in case I/O fails. 
	 */
	protected void writeEachPage(BlogPage page) throws IOException {
		StringWriter writer = new StringWriter();
		
		if(page.getUrl() == null) {
			// skip where we do not have a url
			System.out.println("Skipping page with empty URL.");
			return;
		}
		
		String url = removeSchemeAndDomain(page.getUrl());
		
		// start writing the front matter
		appendLine(writer, "---");
		appendLine(writer, "layout: ", this.options.layoutName);
		appendLine(writer, "title: " + page.getTitle());
		appendLine(writer, "permalink: " + url);
		appendLine(writer, "date: " + page.getPublishedOn());
		appendLine(writer, "---");
		
		// write the page html
		appendLine(writer, page.getContent());
		
		// write this text to file
		int index = url.lastIndexOf('/');
		String fileName = url.substring(index + 1);
		FileUtils.writeStringToFile(new File(this.options.exportPath, fileName), writer.toString());
	}

	/**
	 * Write one blog post to the export folder.
	 * 
	 * @param post the post that needs to be written.
	 * 
	 * @throws IOException in case I/O fails
	 *  
	 */
	protected void writeEachPost(BlogPost post) throws IOException {
		StringWriter writer = new StringWriter();
		
		if(post.getUrl() == null) {
			// skip where we do not have a url
			System.out.println("Skipping post with empty URL.");
			return;
		}
		
		String url = removeSchemeAndDomain(post.getUrl());
		
		// start writing the front matter
		appendLine(writer, "---");
		appendLine(writer, "layout: ", this.options.layoutName);
		appendLine(writer, "title: " + post.getTitle());
		appendLine(writer, "permalink: " + url);
		appendLine(writer, "date: " + post.getPublishedOn());
		
		// check for tags
		if(post.getTags() != null) {
			appendLine(writer, "tags: ", post.getTags(), ';');
		}
		
		// check for categories
		if(post.getCategories() != null) {
			appendLine(writer, "categories: ", post.getTags(), ';');
		}
		
		// close front matter
		appendLine(writer, "---");
		
		// write the page html
		appendLine(writer, post.getContent());
		
		// write this text to file
		int index = url.lastIndexOf('/');
		String fileName = url.substring(index + 1);
		FileUtils.writeStringToFile(new File(this.options.exportPath, fileName), writer.toString());
	}
	
	/**
	 * Appenda  new line with the given text
	 * 
	 * @param writer to which the text is appended
	 * 
	 * @param text the text that needs to be added
	 */
	protected void appendLine(StringWriter writer, String text) {
		writer.append(text);
		writer.append(NEW_LINE);
	}
	
	/**
	 * Append a new line with all the given items
	 * 
	 * @param writer to which the text is appended
	 * 
	 * @param items all the items that need to be added
	 * 
	 */
	protected void appendLine(StringWriter writer, String... items) {
		for(int index = 0; index < items.length; index++) {
			writer.append(items[index]);
		}
		writer.append(NEW_LINE);
	}
	
	/**
	 * Append a line with the given prefix and the list of string values that need to be separated
	 * by the given character.
	 * 
	 * @param writer to which the text is appended
	 * 
	 * @param prefix the prefix to append
	 * 
	 * @param values the list of values that need to be added
	 * 
	 * @param separator the separator to be used between values
	 * 
	 */
	protected void appendLine(StringWriter writer, String prefix, List<String> values, char separator) {
		writer.append(prefix);
		for(int index = 0; index < values.size(); index++) {
			String value = values.get(index);
			
			if(index > 0) {
				writer.write(separator);
			}
			writer.write(value);
		}
		
		writer.append(NEW_LINE);
	}
	
	/**
	 * Utility function to remove domain name from the given URL.
	 * 
	 * @param url
	 * @return
	 */
	private String removeSchemeAndDomain(String url) {
		int index = url.indexOf("://");
		index = url.indexOf('/', index + 3);
		return url.substring(index + 1);
	}
	
	/**
	 * Defines the export options to be used.
	 * 
	 * @author sangupta
	 *
	 */
	private static class ExportOptions {
		
		/**
		 * The XML file that needs to be imported and contains the blog data
		 */
		private File importFile;
		
		/**
		 * The blog format
		 */
		private BlogType blogType;
		
		/**
		 * The folder path where entries will be exported
		 */
		private File exportPath;
		
		/**
		 * The layout name to be used
		 */
		private String layoutName;
		
	}
	
}
