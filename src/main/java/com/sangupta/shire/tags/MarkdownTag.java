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

package com.sangupta.shire.tags;

import java.io.IOException;

import com.sangupta.shire.converters.MarkdownConverter;

public class MarkdownTag extends AbstractCustomTag {

	@Override
	public String getName() {
		return "markdown";
	}

	@Override
	public int getType() {
		return LINE;
	}

	@Override
	public boolean doTag() throws IOException {
		String value = getArgument(0);
		
		if(value != null) {
			MarkdownConverter converter = new MarkdownConverter();
			String html = converter.convert(value);
			writer.write(html);
		}
		
		return true;
	}

}
