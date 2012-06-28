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

package com.sangupta.shire.model;

import java.util.Comparator;

/**
 * @author sangupta
 *
 */
public class PostComparatorOnNames implements Comparator<Post> {

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Post p1, Post p2) {
		if(p1.getTitle() == null && p2.getTitle() == null) {
			return p1.getUrl().compareTo(p2.getUrl());
		}
		
		if(p1.getTitle() == null) {
			return 1;
		}
		
		if(p2.getTitle() == null) {
			return -1;
		}
		
		return p1.getTitle().compareTo(p2.getTitle());
	}

}
