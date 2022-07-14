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
	config "shire/config"
	site "shire/site"
	"shire/utils"
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
	utils.LogObject(appConfig)

	// read shire.json config file
	siteConfig, err := config.ReadConfig(appConfig)
	if err != nil {
		panic(err)
	}
	utils.LogObject(siteConfig)

	// depending on what we need to do, we will branch out from here
	// for now, we are only building the site
	site.BuildSite(appConfig, siteConfig)
}
