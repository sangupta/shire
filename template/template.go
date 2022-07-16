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

package template

import (
	"errors"
)

//
// Define the contract for any structure that holds
// a parsed template, and provides a way to merge the
// parsed template with actual data
//
type ParsedTemplate interface {
	MergeData()
}

//
// Holds the details for a given template
type Template struct {
	Id        string          // the provided template id
	AbsPath   string          // absolute path to the string
	IndexFile string          // the index file in the template folder
	Markup    TemplateMarkup  // defines the kind of markup the template uses
	Content   *ParsedTemplate // the parsed template that we can merge with
}

//
// Scan the given folder for a template with given
// index file. This method only return the `Template`
// object that can later be used, to read actual
// template contents and/or for data-merge to produce pages.
//
func ScanTemplateInFolder(id string, absFolder string) (*Template, error) {
	return nil, nil
}

//
// This function reads the actual template data
// from disk and includes all imports to be ready
// for a merge call to be made.
//
func (template *Template) ReadTemplate() {
	// check markup type
	switch template.Markup {
	case HtmlTemplate:
		template.Content = parseHtmlTemplate(template.AbsPath)
		return

	default:
		panic(errors.New("Unknown markup in template"))
	}
}

func parseHtmlTemplate(filePath string) *ParsedTemplate {
	// fileBytes, err := ioutil.ReadFile(filePath)
	// if err != nil {
	// 	panic(err)
	// }

	// reader := bytes.NewReader(fileBytes)

	// // create html doc
	// doc, err := goquery.NewDocumentFromReader(reader)
	// if err != nil {
	// 	panic(err)
	// }

	// // find all imports and import them too
	// parsed := &template.HtmlParsedTemplate{
	// 	Document: doc,
	// }

	// return parsed

	return nil
}
