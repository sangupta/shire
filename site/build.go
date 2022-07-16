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
	"shire/logger"
	"shire/utils"
)

//
// This method builds the entire site. This assumes
// that a valid `SiteConfig` is available. It also
// requires that the scanning based on this `SiteConfig`
// is complete, and that all data has been populated in
// `SiteData` object.
//
func BuildSite(siteConfig *SiteConfig, siteData *SiteData) {
	// read all templates that have been used
	for _, template := range siteData.Templates {
		template.ReadTemplate()
	}

	// build the siteModel object that will be used
	// when merging
	// siteModel := buildSiteModel(siteConfig, siteData)

	// now for each page, build page
	for _, pageFile := range siteData.AllPages {

		// find page object
		pageObject := siteData.Pages[pageFile.Id]

		// now build this page
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
		logger.Warn("Page has no template and no default template specified: " + pageObject.AbsPath)
		return
	}

	template := siteData.Templates[templateToUse]
	if template == nil {
		logger.Error("No template found as specified in page: " + pageObject.AbsPath)
		return
	}

	// get valid build types for this page
	buildTypes := pageObject.GetBuildTypes(siteConfig)
	if len(buildTypes) == 0 {
		logger.Info("Page did not return any build type: " + pageObject.AbsPath)
		return
	}

	// find out what we need to invoke
	templateFormat := template.Markup
	pageFormat := pageObject.Metadata.PageFormat
	logger.Debug("Page format: " + pageFormat.String() + " and template format: " + templateFormat.String() + " for page: " + pageObject.AbsPath + "")

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
