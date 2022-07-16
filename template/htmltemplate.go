package template

import (
	"bytes"
	"io/ioutil"
	"shire/utils"

	"github.com/PuerkitoBio/goquery"
	"golang.org/x/net/html"
)

//
// Structure to implement ParsedTemplate interface.
//
type HtmlParsedTemplate struct {
	Document *goquery.Document
}

//
// Parse and return the HTML template for given file
//
func parseHtmlTemplate(filePath string) ParsedTemplate {
	fileBytes, err := ioutil.ReadFile(filePath)
	if err != nil {
		panic(err)
	}

	// create a reader
	reader := bytes.NewReader(fileBytes)

	// create html doc
	doc, err := goquery.NewDocumentFromReader(reader)
	if err != nil {
		panic(err)
	}

	// find all imports and import them too
	includeImports(doc, filePath)

	// finally create an object for it
	parsed := &HtmlParsedTemplate{
		Document: doc,
	}

	return parsed
}

//
// Include all imports specified in this document
//
func includeImports(doc *goquery.Document, path string) {
	selection := doc.Find("shire:include")
	if selection == nil {
		return
	}

	for _, node := range selection.Nodes {
		src := getAttribute(node, "src")
		if src == "" {
			utils.Warn("File specifies shire:import but does not have 'src' attribute: " + path)
			continue
		}
	}
}

//
// For a given node, find the required attribute
//
func getAttribute(node *html.Node, name string) string {
	for _, attribute := range node.Attr {
		if attribute.Key == name {
			return attribute.Val
		}
	}
	return ""
}

//
// Given this parsed template, merge it with required data
//
func (template HtmlParsedTemplate) MergeData() {

}
