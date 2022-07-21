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
	"io/ioutil"
	"path/filepath"
	"strconv"

	"github.com/sangupta/shire/app"
	"github.com/sangupta/shire/logger"
	"github.com/sangupta/shire/template"
	"github.com/sangupta/shire/utils"

	mapset "github.com/deckarep/golang-set/v2"
)

type SiteData struct {
	Templates       map[string]*template.Template // map of templateID and template data
	TemplateFolders mapset.Set[string]            // Set of all folders that contain a template
	PageFolders     []*utils.FileAsset            // all child folders that are scanned for pages/posts
	AllPages        []*utils.FileAsset            // reference to all files that act as pages/posts
	Pages           map[string]*Page              // map of each page
}

// start building the site
func ReadAndBuildSiteData(appConfig *app.AppConfig, siteConfig *SiteConfig) (*SiteData, error) {
	siteData := SiteData{
		Templates:       make(map[string]*template.Template),
		TemplateFolders: mapset.NewSet[string](),
	}

	// scan and read all templates
	scanTemplates(appConfig, siteConfig, &siteData)

	// scan and read all page files
	scanPages(appConfig, siteConfig, &siteData)

	// read front matter for each post/page in the site
	scanPagesForMetadataAndContent(appConfig, siteConfig, &siteData)

	// we are all set
	return &siteData, nil
}

// This function scans for all templates in the site
func scanTemplates(appConfig *app.AppConfig, siteConfig *SiteConfig, siteData *SiteData) {
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
	logger.Info("Scanning for template in folder: " + folder)
	template, err := template.ScanTemplateInFolder(templateId, folder)
	if err != nil {
		panic(err)
	}

	// store this template data in map
	siteData.TemplateFolders.Add(templateFolder)
	siteData.Templates[templateId] = template
}

// this function scans for all files that will act as pages/posts in our site
func scanPages(appConfig *app.AppConfig, siteConfig *SiteConfig, siteData *SiteData) {
	// first we read all folders that we need
	// read all folders from the base folder
	// and exclude all template folders from them
	contentFolder := filepath.Join(appConfig.BaseFolder, siteConfig.ContentRoot)
	logger.Info("Scanning for folders in content root: " + contentFolder)

	folders, err := utils.ListChildFolders(contentFolder, true)
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
	logger.Info("Total content folders found: " + strconv.Itoa(len(folders)))

	// now we can go ahead and read all files from these folders
	readAllPagesForSite(appConfig, siteConfig, siteData, folders)
}

// Function to read all pages from all folders that contain posts/pages
func readAllPagesForSite(appConfig *app.AppConfig, siteConfig *SiteConfig, siteData *SiteData, folders []*utils.FileAsset) {
	// for each folder, get all files in the folder
	files := make([]*utils.FileAsset, 0)
	for _, folder := range folders {
		path := filepath.Join(folder.Path, folder.Name)
		logger.Info("Scanning for content in folder: " + path)
		filesInFolder, err := utils.ListFilesExcludingFolders(path, false)
		if err != nil {
			panic(err)
		}

		logger.Info("  files found in folder: " + strconv.Itoa(len(filesInFolder)))
		files = append(files, filesInFolder...)
	}

	// once we have read all files, we need to set this in site data
	siteData.AllPages = files
}

// this function reads each content page
// and extract its front-matter and content
func scanPagesForMetadataAndContent(appConfig *app.AppConfig, siteConfig *SiteConfig, siteData *SiteData) {
	// initialize page map
	siteData.Pages = make(map[string]*Page, len(siteData.AllPages))

	// for each content page
	for _, file := range siteData.AllPages {
		filePath := filepath.Join(file.Path, file.Name)

		// read file content
		logger.Info("Reading file contents: " + filePath)
		fileContent, err := ioutil.ReadFile(filePath)
		if err != nil {
			panic(err)
		}

		// parse page contents and extract page object
		page, err := parsePage(filePath, fileContent)
		if err != nil {
			panic(err)
		}

		// put in page map
		siteData.Pages[filePath] = page
	}
}
