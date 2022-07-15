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

type BuildType int64

//
// Defines the enums to be used as BuildType.
// Strongly-type the output file formats needed.
//
const (
	HtmlFile BuildType = iota
	JsonFile
	PdfFile
)
