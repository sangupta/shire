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
	"fmt"
	app "shire/app"
	"shire/logger"
	site "shire/site"
	"time"
)

//
// the main entry point to Shire.
func main() {
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
