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

package site

type Template struct {
	AbsPath   string // absolute path to the string
	IndexFile string // the index file in the template folder
}

func readTemplateFromFolder(id string, absFolder string) (*Template, error) {
	return nil, nil
}
