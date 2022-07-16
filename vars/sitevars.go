package vars

import "shire/site"

//
// the variables model that is populated
// to merge with template.
//
type SiteVars struct {
	AllPages      []*site.Page // array of pages that are in this site
	BaseUrl       string       // the base url for this site
	BuildDrafts   bool         // are we building drafts?
	Copyright     string       // copyright string if any
	Home          *site.Page   // home page reference
	IsShireServer bool         // are we serving using shire's built in server?
	Title         string       // site title
}
