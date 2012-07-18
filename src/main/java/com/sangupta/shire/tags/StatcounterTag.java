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
public class StatcounterTag extends AbstractCustomTag {

	/**
	 * @see org.apache.velocity.runtime.directive.Directive#getName()
	 */
	@Override
	public String getName() {
		return "statcounter";
	}

	/**
	 * @see org.apache.velocity.runtime.directive.Directive#getType()
	 */
	@Override
	public int getType() {
		return LINE;
	}
	
	/**
	 * @see com.sangupta.shire.tags.AbstractCustomTag#doTag()
	 */
	@Override
	protected boolean doTag() throws IOException {
		String project = getArgument(0);
		String invisible = getArgument(1);
		String security = getArgument(2);
		
		writeLine("<!-- Start of StatCounter Code for Default Guide -->");
		writeLine("<script type=\"text/javascript\">");
		writeLine("var sc_project=" + project + ";"); 
		writeLine("var sc_invisible=" + invisible + ";"); 
		writeLine("var sc_security='" + security + "';"); 
		writeLine("</script>");
		writeLine("<script type=\"text/javascript\" src=\"http://www.statcounter.com/counter/counter.js\"></script>");
		writeLine("<noscript>");
		writeLine("<div class=\"statcounter\"><a title=\"joomla stats\" href=\"http://statcounter.com/joomla/\" target=\"_blank\"><img class=\"statcounter\" src=\"http://c.statcounter.com/5505086/0/e8417bfa/1/\" alt=\"joomla stats\" /></a></div>");
		writeLine("</noscript>");
		writeLine("<!-- End of StatCounter Code for Default Guide -->");
		
		return true;
	}

}
