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

package main

import (
	"encoding/json"
	"fmt"
	"time"

	"github.com/sangupta/shire/app"
	"github.com/sangupta/shire/lhtml"
	"github.com/sangupta/shire/logger"
	"github.com/sangupta/shire/site"

	"golang.org/x/net/html"
)

func main() {
	htmlString := "<html><head><pageTitle></pageTitle><shire:page /></head><body>hello world<div>world</div><div>may be</div></body></html>"
	htmlString = "<html class='test'><head /><title id='title'>Hello World</title>Hello, World!</html>"
	htmlString = "<html class='test' class='test1' expr:m='hello'>Hello World <shire:pageTitle /></html>"
	// htmlString = "Hello world <div>sangupta</div>"

	doc, err := lhtml.ParseHtmlString(htmlString)
	if err != nil {
		fmt.Println(err)
		return
	}

	if doc == nil {
		fmt.Println("document is nil")
		return
	}

	j, _ := json.MarshalIndent(doc, "", "  ")
	fmt.Print("Size of json bytes: ")
	fmt.Println(len(j))
	fmt.Println(string(j))

	// visitor := func(node *html.Node) {
	// 	if node.Parent != nil {
	// 		fmt.Println(node.Data + " with parent " + node.Parent.Data)
	// 	} else {
	// 		fmt.Println(node.Data)
	// 	}
	// }

	// doc.Traverse(visitor)
}

func run(n *html.Node) {
	if n == nil {
		return
	}

	for c := n.FirstChild; c != nil; c = c.NextSibling {
		if c.Type == html.ElementNode {
			fmt.Println(c.Data)
		}

		run(c)
	}
}

//
// the main entry point to Shire.
func main1() {
	start := time.Now()
	runShire()
	duration := time.Since(start)

	// print thanks message
	fmt.Println("\nThanks for using shire, ran for " + duration.String() + ". Goodbye!")
}

func runShire() {
	// configure application using command line flags
	appConfig, err := app.ReadAppArguments()
	if err != nil {
		panic(err)
	}
	logger.LogObject(appConfig)

	// read shire.json config file
	siteConfig, err := site.ReadConfig(appConfig)
	if err != nil {
		panic(err)
	}
	logger.LogObject(siteConfig)

	// for any step of the following, the first is to read and build site data
	// `build`: build the prod mode site
	// `serve`: build and serve site
	// `test`: build in non-prod mode
	// `watch`: test and serve site
	// `publish`: build and publish the site
	siteData, err := site.ReadAndBuildSiteData(appConfig, siteConfig)
	if err != nil {
		panic(err)
	}

	// depending on what we need to do, we will branch out from here
	// for now, we are only building the site
	site.BuildSite(siteConfig, siteData)
}
