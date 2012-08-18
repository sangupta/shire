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

package com.sangupta.shire.ant;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.shire.Shire;

/**
 * Apache ANT task that helps invoke Shire from the command line. Invocation
 * from ANT however does not allow for server mode.
 *  
 * @author sangupta
 * @since 0.3.0
 */
public class ShireAntTask extends Task {

	/**
	 * Path to the configuration file
	 */
	private String configFile;
	
	/**
	 * Here we go with the task execution, pretty self-explanatory.
	 * 
	 * @see org.apache.tools.ant.Task#execute()
	 */
	public void execute() throws BuildException {
		// test for config file presence
		if(AssertUtils.isEmpty(this.configFile)) {
			throw new BuildException("Configuration file must be specified.");
		}
		
		File config = new File(this.configFile);
		
		// run shire
		Shire.executeShire(config);
	}
	
	// Usual accessors follow

	/**
	 * @return the configFile
	 */
	public String getConfigFile() {
		return configFile;
	}

	/**
	 * @param configFile the configFile to set
	 */
	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
}
