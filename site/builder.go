/**
 * Shire
 * https://github.com/sangupta/shire
 *
 * MIT License.
 * Copyright (c) 2022, Sandeep Gupta.
 *
 * Use of this source code is governed by a MIT style license
 * that can be found in LICENSE file in the code repository.
 */

package site

import (
	"path/filepath"
	app "shire/app"
	config "shire/config"
	"shire/utils"
	"strconv"

	mapset "github.com/deckarep/golang-set/v2"
)

type SiteData struct {
	Templates       map[string]*Template // map of templateID and template data
	TemplateFolders mapset.Set[string]   // Set of all folders that contain a template
	PageFolders     []*utils.FileAsset   // all child folders that are scanned for pages/posts
	AllPages        []*utils.FileAsset   // reference to all files that act as pages/posts
}

// start building the site
func BuildSite(appConfig *app.AppConfig, siteConfig *config.ShireConfig) {
	siteData := SiteData{
		Templates:       make(map[string]*Template),
		TemplateFolders: mapset.NewSet[string](),
	}

	// scan and read all templates
	scanTemplates(appConfig, siteConfig, &siteData)

	// scan and read all page files
	scanPages(appConfig, siteConfig, &siteData)

	// read front matter for each post/page in the site
	populatePageMetadata(appConfig, siteConfig, &siteData)
}

// This function scans for all templates in the site
func scanTemplates(appConfig *app.AppConfig, siteConfig *config.ShireConfig, siteData *SiteData) {
	// scan for all individual templates
	if siteConfig.Templates != nil {
		for _, template := range siteConfig.Templates {
			addTemplateDataToSite(siteData, template.TemplateId, appConfig.BaseFolder, template.Folder)
		}
	}
}

// Read one single template with given id from the given folder
// and then push it into `SiteData` templates map
func addTemplateDataToSite(siteData *SiteData, templateId string, baseFolder string, templateFolder string) {
	folder := filepath.Join(baseFolder, templateFolder)
	utils.Log("Scanning for template in folder: " + folder)
	template, err := readTemplateFromFolder(templateId, folder)
	if err != nil {
		panic(err)
	}

	// store this template data in map
	siteData.TemplateFolders.Add(templateFolder)
	siteData.Templates[templateId] = template
}

// this function scans for all files that will act as pages/posts in our site
func scanPages(appConfig *app.AppConfig, siteConfig *config.ShireConfig, siteData *SiteData) {
	// first we read all folders that we need
	// read all folders from the base folder
	// and exclude all template folders from them
	contentFolder := filepath.Join(appConfig.BaseFolder, siteConfig.ContentRoot)
	utils.Log("Scanning for folders in content root: " + contentFolder)

	folders, err := utils.ListChildFolders(contentFolder)
	if err != nil {
		panic(err)
	}

	// remove template folders
	for index, folder := range folders {
		if siteData.TemplateFolders.Contains(folder.Name) {
			utils.RemoveFromSliceAtIndex(folders, index)
		}
	}
	siteData.PageFolders = folders
	utils.Log("Total content folders found: " + strconv.Itoa(len(folders)))

	// now we can go ahead and read all files from these folders
	readAllPagesForSite(appConfig, siteConfig, siteData, folders)
}

// Function to read all pages from all folders that contain posts/pages
func readAllPagesForSite(appConfig *app.AppConfig, siteConfig *config.ShireConfig, siteData *SiteData, folders []*utils.FileAsset) {
	// for each folder, get all files in the folder
	files := make([]*utils.FileAsset, 0)
	for _, folder := range folders {
		path := filepath.Join(folder.Path, folder.Name)
		utils.Log("Scanning for content in folder: " + path)
		filesInFolder, err := utils.ListFilesExcludingFolders(path, false)
		if err != nil {
			panic(err)
		}

		utils.Log("  files found in folder: " + strconv.Itoa(len(filesInFolder)))
		files = append(files, filesInFolder...)
	}

	// once we have read all files, we need to set this in site data
	siteData.AllPages = files
}

// this function reads the front matter from each post/page
func populatePageMetadata(appConfig *app.AppConfig, siteConfig *config.ShireConfig, siteData *SiteData) {
	// for _, file := range siteData.AllPages {
	// 	filePath := filepath.Join(appConfig.BaseFolder, file.Path, file.Name)
	// 	metadata, err := ReadPageMetadata(filePath)
	// 	if err != nil {
	// 		panic(err)
	// 	}

	// }
}
