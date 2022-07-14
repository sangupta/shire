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
	"shire/utils"
	"strconv"
	"strings"
)

//
// Defines a single page/post content in the site
//
type Page struct {
	AbsPath  string           // absolute path to this page
	Metadata *PageFrontMatter // front matter data associated wih this page
	Content  string           // actual page contents
}

//
// Defines the front matter associated with a page
//
type PageFrontMatter struct {
	Title string // the title to page
	Date  string // the page date
}

func parsePage(filePath string, fileContent []byte) (*Page, error) {
	// convert to lines
	lines := strings.Split(string(fileContent), "\n")

	// extract the front matter
	utils.Debug("Reading front-matter from file: " + filePath)
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
	utils.Debug("Front-matter ends at line: " + strconv.Itoa(frontMatterEndIndex))

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
		Content:  pageContent,
		Metadata: metadata,
	}
	return &page, nil
}

func parsePageFrontMatter(filePath string, lines []string, endIndex int) (*PageFrontMatter, error) {
	metadata := PageFrontMatter{
		Title: filepath.Base(filePath),
	}

	if endIndex <= 0 {
		return &metadata, nil
	}

	return &metadata, nil
}
