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
)

//
// the main entry point to Shire.
func main() {
	// configure application using command line flags
	appConfig, err := app.ReadAppArguments()
	if err != nil {
		panic(err)
	}
	fmt.Println(appConfig)

	// read shire.json config file
	siteConfig, err := config.ReadConfig(appConfig)
	if err != nil {
		panic(err)
	}
	fmt.Println(siteConfig)

	// depending on what we need to do, we will branch out from here
	// for now, we are only building the site
	site.BuildSite(appConfig, siteConfig)
}
