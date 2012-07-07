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
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * Renders the date in the given format or the default format.
 * 
 * @author sangupta
 *
 */
public class DateTag extends AbstractCustomTag {

	@Override
	public String getName() {
		return "date";
	}

	@Override
	public int getType() {
		return LINE;
	}

	@Override
	public boolean doTag() throws IOException {
		Date date = null;
		String format = null;
		
		date = getArgument(0);
		format = getArgument(1);
		
		if(date == null) {
			return false;
		}
		
		if(format == null || format.trim().isEmpty()) {
			format = "dd MMM yyyy";
		}
		
		String formattedDate = DateFormatUtils.format(date, format);
		writer.write(formattedDate);
		
		return true;
	}

}
