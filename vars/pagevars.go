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

package vars

//
// the variables model that is populated
// to merge with the page content, before it
// is merged with template
//
type PageVars struct {
	Aliases     []string // other URLs that this page may have
	Content     string   // the actual content
	Description string   // the description of this page
	IsHomePage  bool     // is this page a home page?
	Keywords    []string // keywords associted with this page
	LinkTitle   string
	PermaLink   string
	RawContent  string
	Title       string
	Summary     string
}
