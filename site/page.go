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
}

func ReadPageMetadata(filePath string) (*Page, error) {
	return nil, nil
}
