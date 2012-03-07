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

package com.sangupta.shire.converters;

import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;

import com.sangupta.shire.core.Converter;

/**
 * Converts markdown format to HTML
 * 
 * @author sangupta
 *
 */
public class MarkdownConverter implements Converter {
	
	public static PegDownProcessor pegDownProcessor;

	@Override
	public String convert(String content) {
		if(pegDownProcessor == null) {
			initialize();
		}
		
		return pegDownProcessor.markdownToHtml(content);
	}

	private synchronized void initialize() {
		if(pegDownProcessor == null) {
			pegDownProcessor = new PegDownProcessor(Extensions.ALL);
		}
	}

}
