package vars

//
// the variables model that is populated
// to merge with the page content, before it
// is merged with template
//
type PageVars struct {
	Aliases     []string // other URLs that this page may have
	Content     string   // the actual content
	Description string   // the description of this page
	IsHomePage  bool     // is this page a home page?
	Keywords    []string // keywords associted with this page
	LinkTitle   string
	PermaLink   string
	RawContent  string
	Title       string
	Summary     string
}
