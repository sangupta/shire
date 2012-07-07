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
public class GoogleAnalyticsTag extends AbstractCustomTag {

	/**
	 * @see org.apache.velocity.runtime.directive.Directive#getName()
	 */
	@Override
	public String getName() {
		return "googleAnalytics";
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
		String account = getArgument(0);
		String domain = getArgument(1);
		
		
		writeLine("<!-- Add Google Analytics Tracking -->");		
		writeLine("<script type=\"text/javascript\">");
		writeLine("var _gaq = _gaq || [];");
		writeLine("_gaq.push(['_setAccount', '" + account + "']);");
		writeLine("_gaq.push(['_setDomainName', '" + domain + "']);");
		writeLine("_gaq.push(['_trackPageview']);");
		writeLine("\n");
		writeLine("(function() {");
		writeLine("var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;");
		writeLine("ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';");
		writeLine("var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);");
		writeLine("})();");
		writeLine("</script>");
		
		return true;
	}

}
