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

type TemplateMarkup int64

//
// Defines the enums to be used as Markup.
// Strongly-type the template format the page uses.
//
const (
	HtmlTemplate TemplateMarkup = iota
	Velocity
)

func (tm TemplateMarkup) String() string {
	switch tm {
	case HtmlTemplate:
		return "html"

	case Velocity:
		return "velocity"

	default:
		return "unknown"
	}
}
