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

package shire

import (
	"encoding/json"
	"io/ioutil"
	"path/filepath"
	"shire/app"
	"shire/utils"
)

// holds information about site author
type Author struct {
	Name  string
	Email string
	Url   string
}

//
// Value object that holds information about templates as
// configured for the site.
//
type TemplateConfig struct {
	TemplateId string // unique id
	Folder     string // folder with respect to base folder
	IndexFile  string // (optional) index file to use. default is `index.html`
}

type OutputOptions struct {
	Folder string // path where site output is to be written
	Html   bool   // output html files
	Json   bool   // output json files?
	Pdf    bool   // output pdf files?
}

type BuildOptions struct {
	Drafts  bool // do we build draft pages
	Expired bool // do we build expired pages
	Future  bool // do we build future pages
}

//
// Options used for publishing the site
type PublishOptions struct {
}

// defines shire configuration
type SiteConfig struct {
	BaseUrl         string           // the base url to the site
	Title           string           // the site wide title to use, if present
	ContentRoot     string           // the root folder under which all content is scanned from
	Author          Author           // the author to the site
	DefaultTemplate string           // the ID of the default template, if any
	Templates       []TemplateConfig // array of templates that can be used
	Output          OutputOptions    // options to cutomize site output
	Build           BuildOptions     // options for building
	Publish         PublishOptions   // options for publishing
}

// read and return the config instance
func ReadConfig(appConfig *app.AppConfig) (*SiteConfig, error) {
	// create the basic configuration for the site
	// including sensible defaults
	config := SiteConfig{
		Output: OutputOptions{
			Folder: "site",
		},
		DefaultTemplate: "template",
	}

	// now read file from disk
	configFile := filepath.Join(appConfig.BaseFolder, "shire.config.json")
	utils.Info("Reading config file: " + configFile)
	jsonFile, err := ioutil.ReadFile(configFile)
	if err != nil {
		panic(err)
	}

	// deserialize from JSON
	err = json.Unmarshal(jsonFile, &config)
	if err != nil {
		panic(err)
	}

	// all done
	return &config, nil
}
