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

type FrontMatterSyntax int64

//
// Defines the enums to be used as FrontMatterSyntax.
// Strongly-type the syntax that we need to recognize
// when parsing page front-matter.
//
const (
	JsonSyntax FrontMatterSyntax = iota
	YamlSyntax
	TomlSyntax
)
