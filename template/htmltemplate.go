package template

import (
	"bytes"
	"errors"
	"io/ioutil"
	"path/filepath"
	"strconv"

	"github.com/sangupta/shire/logger"

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
	doc, err := getDocument(filePath)
	if err != nil {
		panic(err)
	}

	l := doc.Find("page:title").Length()
	logger.Debug("length: " + strconv.Itoa(l))

	html, err := doc.Html()
	logger.Debug(html)

	// finally create an object for it
	parsed := &HtmlParsedTemplate{
		Document: doc,
	}

	return parsed
}

func getDocument(filePath string) (*goquery.Document, error) {
	logger.Debug("Reading template file: " + filePath)
	fileBytes, err := ioutil.ReadFile(filePath)
	if err != nil {
		return nil, err
	}

	// create a reader
	reader := bytes.NewReader(fileBytes)

	// create html doc
	doc, err := goquery.NewDocumentFromReader(reader)
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
func includeImports(doc *goquery.Document, path string) {
	logger.Debug("Processing any includes in file: " + path)
	selection := doc.Find("shireInclude")
	if selection == nil || selection.Length() == 0 {
		logger.Debug("No <shire:include> found in file: " + path)
		return
	}

	// go over each file
	for _, node := range selection.Nodes {
		src := getAttribute(node, "src")
		if src == "" {
			logger.Warn("File specifies shire:import but does not have 'src' attribute: " + path)
			continue
		}

		// the include source was found
		includePath := filepath.Join(filepath.Dir(path), src)
		doc, err := getDocument(includePath)
		// html, err := doc.Html()
		// logger.Debug(html)
		if err != nil {
			panic(errors.New("Unable to parse include file: " + includePath))
		}

		// replace
		parent := node.Parent

		body := doc.Find("body")
		bodyChildren := body.Children()

		if bodyChildren.Length() > 0 {
			for _, child := range bodyChildren.Nodes {
				child.Parent.RemoveChild(child)
				parent.InsertBefore(child, node)
			}
			parent.RemoveChild(node)
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
