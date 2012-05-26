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

package com.sangupta.shire.site;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.sangupta.shire.ExecutionOptions;

/**
 * Handles backup of the previously generated site.
 * 
 * @author sangupta
 * @since Feb 23, 2012
 */
public class SiteBackup {

	private static final String BACKUP_FOLDER_EXTENSION = ".backup";
	
	/**
	 * Holds the file handle to the site backup 
	 */
	private static File backupFolder = null;
	
	/**
	 * Method to restore the _site.backup folder that we took in this very session.
	 */
	private static void restoreSiteBackup() {
		if(backupFolder != null) {
			// build up the name of the original folder
			String path = backupFolder.getAbsolutePath();
			path = StringUtils.left(path, path.length() - BACKUP_FOLDER_EXTENSION.length());
			File original = new File(path);
			
			// delete the currently made site folder
			if(original.exists()) {
				FileUtils.deleteQuietly(original);
			}
			
			// restore the backup
			try {
				FileUtils.moveDirectory(backupFolder, original);
			} catch (IOException e) {
				System.out.println("Unable to restore the original site backup.");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method to delete the _site.backup folder that we took a while back.
	 * 
	 */
	private static void deleteSiteBackup() {
		if(backupFolder != null) {
			try {
				FileUtils.deleteDirectory(backupFolder);
				backupFolder = null;
			} catch (IOException e) {
				System.out.println("Unable to delete the older site backup at: " + backupFolder.getAbsolutePath());
				e.printStackTrace();
			}
		}
	}

	/**
	 * Method that creates the backup of the _site folder, if present.
	 * 
	 */
	public static void backupOlderSite(ExecutionOptions options) {
		File siteFolder = new File(options.getParentFolder(), options.getSiteFolderName());
		if(!siteFolder.exists()) {
			return;
		}
		
		if(!siteFolder.isDirectory()) {
			return;
		}
		
		backupFolder = new File(siteFolder.getAbsolutePath() + BACKUP_FOLDER_EXTENSION);
		
		try {
			FileUtils.moveDirectory(siteFolder, backupFolder);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The method performs the house keeping based on the success attribute of
	 * the site generation.
	 * 
	 * @param success
	 */
	public static void performHouseKeeping(boolean success) {
		// if we have been successful in creating the site
		// delete the backup
		// else, restore the backup
		if(success) {
			SiteBackup.deleteSiteBackup();
		} else {
			SiteBackup.restoreSiteBackup();
		}
	}
}
