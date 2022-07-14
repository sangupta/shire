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
	config "shire/config"
	"shire/utils"
)

//
// This method builds the entire site.
//
func BuildSite(siteConfig *config.SiteConfig, siteData *SiteData) {
	// read all templates that have been used
	for _, template := range siteData.Templates {
		template.ReadTemplate()
	}

	// now for each page, build page
	for _, pageFile := range siteData.AllPages {

		// find page object
		pageObject := siteData.Pages[pageFile.Id]

		// find the template that this page uses
		templateToUse := pageObject.Metadata.TemplateId
		template := siteData.Templates[templateToUse]

		buildPage(siteConfig, pageFile, template, pageObject)
	}
}

//
// Build a single page using the provided template
//
func buildPage(siteConfig *config.SiteConfig, pageFile *utils.FileAsset, template *Template, page *Page) {

}
