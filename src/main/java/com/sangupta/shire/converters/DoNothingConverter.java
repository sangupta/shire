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

import java.util.Map;

import com.sangupta.shire.core.Converter;
import com.sangupta.shire.model.TemplateData;

/**
 * Converter that does nothing. Used when we cannot detect the
 * type of the input file.
 * 
 * @author sangupta
 * @since Feb 24, 2012
 */
public final class DoNothingConverter implements Converter {
	
	/**
	 * @see com.sangupta.shire.core.Converter#getName()
	 */
	@Override
	public String getName() {
		return "Do Nothing";
	}

	@Override
	public final String convert(String content, TemplateData templateData) {
		return content;
	}

	/**
	 * @see com.sangupta.shire.core.Converter#getExtensionMappings()
	 */
	@Override
	public Map<String, String> getExtensionMappings() {
		return null;
	}

}
