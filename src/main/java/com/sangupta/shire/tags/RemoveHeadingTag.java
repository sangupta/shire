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

/**
 * @author sangupta
 *
 */
public class RemoveHeadingTag extends AbstractCustomTag {

	/**
	 * @see org.apache.velocity.runtime.directive.Directive#getName()
	 */
	@Override
	public String getName() {
		return "removeHeading";
	}

	/**
	 * @see org.apache.velocity.runtime.directive.Directive#getType()
	 */
	@Override
	public int getType() {
		return LINE;
	}

	/**
	 * 
	 * @see com.sangupta.shire.tags.AbstractCustomTag#doTag()
	 */
	@Override
	public boolean doTag() throws IOException {
		String data = null;
		data = getArgument(0);
		
		if(data != null) {
			writer.write(remove(data, new String[] { "h1", "h2", "h3" }));
		}
		
		return true;
	}

	/**
	 * @param string
	 * @return
	 */
	private String remove(String data, String[] tags) {
		for(String tag : tags) {
			if(data.startsWith("<" + tag + ">")) {
				int index = data.indexOf("</" + tag + ">");
				if(index != -1) {
					return data.substring(index + 3 + tag.length());
				}
			}
		}
		
		return data;
	}

}
