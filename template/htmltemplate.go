package template

import "github.com/PuerkitoBio/goquery"

type HtmlParsedTemplate struct {
	Document *goquery.Document
}

func (template HtmlParsedTemplate) Merge() {

}
