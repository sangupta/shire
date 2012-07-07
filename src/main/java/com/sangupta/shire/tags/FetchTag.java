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

import com.sangupta.shire.util.HttpUtil;
import com.sangupta.shire.util.HttpUtil.WebResponse;

/**
 * @author sangupta
 *
 */
public class FetchTag extends AbstractCustomTag {

	@Override
	public String getName() {
		return "curl";
	}

	@Override
	public int getType() {
		return LINE;
	}

	@Override
	public boolean doTag() throws IOException {
		String url = getArgument(0);
		
		if(url != null) {
			WebResponse webResponse = HttpUtil.getUrlResponse(url);
			if(webResponse != null && webResponse.isSuccess()) {
				String contents = webResponse.getResponseBody();
				
				this.writer.write(contents);
			}
		}
		
		return true;
	}

}
