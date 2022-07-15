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

import "path/filepath"

//
// Defines the front matter associated with a page
//
type PageFrontMatter struct {
	Syntax       FrontMatterSyntax // the syntax of front matter
	Title        string            // the title to page
	Date         string            // the page date
	TemplateId   string            // the template that this page uses
	Draft        bool              // can this page be published?
	PublishEpoch int64             // time when this page should publish
	ExpiryEpoch  int64             // time when this page will expire
	PageFormat   PageMarkup        // specifies if page uses markdown, html or some other format
	LinkTitle    string            // (optional) title to use when linking this page, instead of title
	Series       string            // does this post belong to a series?
	Summary      string            // summary for this page pre-defined by user
	Url          string            // the URL to use when publishing this file
}

//
// Parse the lines read from the page file into
// the `PageFrontMatter` object. Based on the type
// of front-matter syntax (toml/json/yaml) etc, this
// invokes the right parser.
//
func parsePageFrontMatter(filePath string, lines []string, endIndex int) (*PageFrontMatter, error) {
	metadata := PageFrontMatter{
		Title: filepath.Base(filePath),
	}

	if endIndex <= 0 {
		return &metadata, nil
	}

	return &metadata, nil
}
