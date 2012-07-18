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

package com.sangupta.shire;

import java.io.File;
import java.util.Properties;

import com.sangupta.shire.config.PropertyConfigReader;
import com.sangupta.shire.config.YmlConfigReader;
import com.sangupta.shire.layouts.LayoutType;

/**
 * Stores execution options as defined by the configuration file
 * or the command line parameters.
 * 
 * @author sangupta
 * @since Feb 23, 2012
 */
public class ExecutionOptions {
	
	/**
	 * Parent folder of the site being processed.
	 */
	private File parentFolder = null;
	
	/**
	 * Configuration file of the site being processed.
	 */
	private File configFile = null;
	
	/**
	 * The configuration data as read from the configuration file
	 */
	private Properties configuration = null;

	/**
	 * The default name of the includes folder
	 */
	private String includesFolderName = "_includes";
	
	/**
	 * The default name of the layouts folder
	 */
	private String layoutsFolderName = "_layouts";
	
	/**
	 * The default name of the plug-ins folder
	 */
	private String pluginsFolderName = "_plugins";
	
	/**
	 * The default name of the export folder
	 */
	private String siteFolderName = "_site";
	
	/**
	 * The default name of the cache folder
	 */
	private String cacheFolderName = "_cache";
	
	/**
	 * The layout type being used as specified or auto detected.
	 */
	private LayoutType layoutType = LayoutType.AutoDetect;
	
	/**
	 * Disables custom plugins when set to true
	 */
	private boolean safe = false;
	
	/**
	 * Indicates if we are running in debug mode
	 */
	private boolean debug = false;
	
	/**
	 * Defines whether to use cache of all previous internet requests
	 * made from any of the plugins or core. All plugins should honour this
	 * setting
	 */
	private boolean useCache = true;
	
	/**
	 * Constructor
	 * 
	 * @param parentDir
	 * @param configFile
	 */
	public ExecutionOptions(File configFile) {
		this.configFile = configFile.getAbsoluteFile();
		initialize();
	}
	
	/**
	 * Read the configuration parameters from the config file.
	 * 
	 * @param file
	 * @return
	 */
	private Properties readConfigFile() {
		String fileName = this.configFile.getName();
		if("_config.yml".equals(fileName)) {
			return new YmlConfigReader().readConfigFile(this.configFile);
		}
		
		if(fileName.endsWith(".properties")) {
			return new PropertyConfigReader().readConfigFile(this.configFile);
		}
		
		return null;
	}
	
	private void initialize() {
		configuration = readConfigFile();
		
		// TODO: for now the site configuration options are being ignored, we need to fix this
		
		this.parentFolder = this.configFile.getParentFile().getAbsoluteFile();
	}

	// Usual accessors follow

	/**
	 * @return the includesFolderName
	 */
	public String getIncludesFolderName() {
		return includesFolderName;
	}

	/**
	 * @return the layoutsFolderName
	 */
	public String getLayoutsFolderName() {
		return layoutsFolderName;
	}

	/**
	 * @return the pluginsFolderName
	 */
	public String getPluginsFolderName() {
		return pluginsFolderName;
	}

	/**
	 * @return the siteFolderName
	 */
	public String getSiteFolderName() {
		return siteFolderName;
	}

	/**
	 * @return the layoutType
	 */
	public LayoutType getLayoutType() {
		return layoutType;
	}

	/**
	 * @return the parentFolder
	 */
	public File getParentFolder() {
		return parentFolder;
	}

	/**
	 * @return the configFile
	 */
	public File getConfigFile() {
		return configFile;
	}

	/**
	 * @param layoutType the layoutType to set
	 */
	public void setLayoutType(LayoutType layoutType) {
		this.layoutType = layoutType;
	}

	/**
	 * @return the safe
	 */
	public boolean isSafe() {
		return safe;
	}

	/**
	 * @return the debug
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * @return the configuration
	 */
	public Properties getConfiguration() {
		return configuration;
	}

	/**
	 * @return the useCache
	 */
	public boolean isUseCache() {
		return useCache;
	}

	/**
	 * @return the cacheFolderName
	 */
	public String getCacheFolderName() {
		return cacheFolderName;
	}

}
