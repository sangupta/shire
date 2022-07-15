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
	"shire/utils"
)

//
// This method builds the entire site.
//
func BuildSite(siteConfig *SiteConfig, siteData *SiteData) {
	// read all templates that have been used
	for _, template := range siteData.Templates {
		template.ReadTemplate()
	}

	// now for each page, build page
	for _, pageFile := range siteData.AllPages {

		// find page object
		pageObject := siteData.Pages[pageFile.Id]

		buildPage(siteConfig, siteData, pageFile, pageObject)
	}
}

//
// Build a single page using the provided template. Building
// a page involves the following steps:
//
// 1. Find the template attached to this page. This can change
// due to a configuration change (like template id mapped to
// a different template, or changing the default template id)
// or a change in page (page binds to a new template now)
//
// 2. Find valid build types for this page. We can specify build
// types at the site level, or at the page level. Again types
// may change due to change in config/page.
//
// 3. Once we have the build types, we build the page. This involves
// rendering each type of output that has been requested.
//
func buildPage(siteConfig *SiteConfig, siteData *SiteData, pageFile *utils.FileAsset, pageObject *Page) {
	// find the template that this page uses
	templateToUse := pageObject.Metadata.TemplateId
	if templateToUse == "" && siteConfig.DefaultTemplate == "" {
		utils.Warn("Page has no template and no default template specified: " + pageObject.AbsPath)
		return
	}

	template := siteData.Templates[templateToUse]
	if template == nil {
		utils.Error("No template found as specified in page: " + pageObject.AbsPath)
		return
	}

	// get valid build types for this page
	buildTypes := pageObject.GetBuildTypes(siteConfig)
	if len(buildTypes) == 0 {
		utils.Info("Page did not return any build type: " + pageObject.AbsPath)
		return
	}

	// now for each build type, we execute the right renderer
	for _, buildType := range buildTypes {
		switch buildType {
		case HtmlFile:
			renderHtmlPage()

		case JsonFile:
			renderJsonPage()
		}
	}
}

func renderHtmlPage() {

}

func renderJsonPage() {

}
