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
	"strconv"
	"strings"
	"time"

	"github.com/sangupta/shire/logger"
)

//
// Defines a single page/post content in the site
//
type Page struct {
	AbsPath  string           // absolute path to this page
	Metadata *PageFrontMatter // front matter data associated wih this page
	content  string           // actual page contents
}

func (page *Page) GetContent() string {
	return page.content
}

//
// Parse the given file contents into a `Page` object. This
// includes parsing the page front-matter, and optionally, storing
// and/or parsing the page contents. At no point in the system
// should we assume that page contents are available. Outside
// methods should use `GetContent()` method to ensure that they
// always get the content, either from cache, or from disk.
//
func parsePage(filePath string, fileContent []byte) (*Page, error) {
	time.Now()
	// convert to lines
	lines := strings.Split(string(fileContent), "\n")

	// extract the front matter
	logger.Debug("Reading front-matter from file: " + filePath)
	frontMatterEndIndex := -1
	for index, line := range lines {
		if strings.HasPrefix(line, "---") {
			if index == 0 {
				// first line we skip
				continue
			}

			frontMatterEndIndex = index
			break
		}
	}

	// did we find front matter?
	logger.Debug("Front-matter ends at line: " + strconv.Itoa(frontMatterEndIndex))

	// now parse the front-matter
	var metadata *PageFrontMatter
	metadata, err := parsePageFrontMatter(filePath, lines, frontMatterEndIndex)
	if err != nil {
		panic(err)
	}

	// build page content
	pageContent := strings.Join(lines[frontMatterEndIndex+1:], "\n")

	// create page
	page := Page{
		AbsPath:  filePath,
		content:  pageContent,
		Metadata: metadata,
	}
	return &page, nil
}

//
// Method returns the build types that the page specifies.
// These can be configured at the site level, and can then
// be overridden in the page itself. As both site and page
// can change over time, this method exists to provide exact
// values just before rendering.
//
func (page *Page) GetBuildTypes(siteConfig *SiteConfig) []BuildType {
	return []BuildType{HtmlFile}
}
