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

package app

type AppConfig struct {
	BaseFolder string // the base folder where shire.config.json exists
	LogFile    string // path to log file that we may use
	LogLevel   string // log level at which we emit logs
}

func ReadAppArguments() (*AppConfig, error) {
	config := AppConfig{
		BaseFolder: "/Users/sangupta/git/sangupta/shire/test-site",
		LogFile:    "shire.log.txt",
		LogLevel:   "none",
	}

	return &config, nil
}
