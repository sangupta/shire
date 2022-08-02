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
	"io/ioutil"
	"path/filepath"

	"github.com/sangupta/lhtml"
	"github.com/sangupta/shire/logger"
)

//
// Structure to implement ParsedTemplate interface.
//
type HtmlParsedTemplate struct {
	Document *lhtml.HtmlElements
}

//
// Parse and return the HTML template for given file
//
func parseHtmlTemplate(filePath string) ParsedTemplate {
	doc, err := getDocument(filePath)
	if err != nil {
		panic(err)
	}

	// finally create an object for it
	parsed := &HtmlParsedTemplate{
		Document: doc,
	}

	return parsed
}

func getDocument(filePath string) (*lhtml.HtmlElements, error) {
	logger.Debug("Reading template file: " + filePath)
	fileBytes, err := ioutil.ReadFile(filePath)
	if err != nil {
		return nil, err
	}

	// create html doc
	doc, err := lhtml.ParseHtmlString(string(fileBytes))
	if err != nil {
		return nil, err
	}

	// find all imports and import them too
	includeImports(doc, filePath)

	return doc, nil
}

//
// Include all imports specified in this document
//
func includeImports(doc *lhtml.HtmlElements, path string) {
	logger.Debug("Processing any includes in file: " + path)
	importNodes := doc.GetElementsByName("shireInclude")
	if importNodes.Length() == 0 {
		logger.Debug("No <shire:include> found in file: " + path)
		return
	}

	// go over each file
	for _, importNode := range importNodes.Nodes() {
		src := getAttribute(importNode, "src")
		if src == "" {
			logger.Warn("File specifies shire:import but does not have 'src' attribute: " + path)
			continue
		}

		// the include source was found
		includePath := filepath.Join(filepath.Dir(path), src)
		importedChildDocument, err := getDocument(includePath)
		// html, err := doc.Html()
		// logger.Debug(html)
		if err != nil {
			panic(errors.New("Unable to parse include file: " + includePath))
		}

		// replace
		parent := importNode.Parent()

		importedBody := importedChildDocument.GetElementsByName("body")
		if importedBody != nil && importedBody.Length() > 0 {
			bodyChildren := importedBody.Get(0)

			if bodyChildren.NumChildren() > 0 {
				for _, importedBodyChild := range bodyChildren.Children() {
					parent.InsertBeforeChild(importNode, importedBodyChild)
				}

				importNode.RemoveMe()
			}
		}
	}
}

//
// For a given node, find the required attribute
//
func getAttribute(node *lhtml.HtmlNode, name string) string {
	for _, attribute := range node.Attributes {
		if attribute.Name == name {
			return attribute.Value
		}
	}
	return ""
}

//
// Given this parsed template, merge it with required data
//
func (template HtmlParsedTemplate) MergeData() {

}
