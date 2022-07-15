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

type PageMarkup int64

//
// Defines the enums to be used as Markup.
// Strongly-type the content format the page uses.
//
const (
	HtmlPage PageMarkup = iota
	MarkdownPage
	ReStructuredPage
)

func (pm PageMarkup) String() string {
	switch pm {
	case HtmlPage:
		return "html"

	case MarkdownPage:
		return "markdown"

	case ReStructuredPage:
		return "restructured"

	default:
		return "unknown"
	}
}
