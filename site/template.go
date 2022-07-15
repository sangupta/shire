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
// Holds the details for a given template
type Template struct {
	Id        string         // the provided template id
	AbsPath   string         // absolute path to the string
	IndexFile string         // the index file in the template folder
	Markup    TemplateMarkup // defines the kind of markup the template uses
}

//
// Scan the given folder for a template with given
// index file. This method only return the `Template`
// object that can later be used, to read actual
// template contents and/or for data-merge to produce pages.
//
func scanTemplateInFolder(id string, absFolder string) (*Template, error) {
	return nil, nil
}

//
// This function reads the actual template data
// from disk and (optionally) parses it to be ready
// for a merge call to be made.
//
func (template *Template) ReadTemplate() {

}
