Shire is a blog-aware static site generator modelled around the famous [Jekyll](https://github.com/mojombo/jekyll). It also tends to pull in features from the [Octopress](https://github.com/imathis/octopress). The project is under heavy development as this also serves my [home site](http://www.sangupta.com) :)

**Current Version: 0.2.0**

Usage
-----

The project is still under development and hence, is not usable directly via the command line. 

To use sync the code base and set it up as a project in your favorite IDE. Shire is a Maven-based JAVA project. Once the project is setup, open the JAVA class `Shire` and in the `main` method, setup the path to your YAML config file.

Features
--------

**0.2.0**

* Support for running mulitple blogs by adding a simple `.blog` file to the folder
* Support for pagination, and generation of tags and category pages
* Added velocity tags for Google Analytics and StatCounter
* Fixed issue with file extension of generated markdown source files
* List of posts now available in global `site` object
* Added a `sitemap.xml` generator for search engines

**0.1.0**

* Support for Apache Velocity Templates
* Support for multiple includes and layouts
* Support for YAML and XML based configuration 

Downloads
---------
You can download the JAR from the [Downloads](https://github.com/sangupta/shire/downloads) page.

Alternatively, you may download the builds from Maven Central repository using:

```xml
<dependency>
    <groupId>com.sangupta</groupId>
    <artifactId>shire</artifactId>
    <version>0.2.0</version>
</dependency>
```

Dependencies
------------

Currently, Shire does not depend on any third-party binary.

Continuous Integration
----------------------
The library is continuously integrated and unit tested using the *Travis CI system.

Current status of branch `MASTER`: [![Build Status](https://secure.travis-ci.org/sangupta/shire.png?branch=master)](http://travis-ci.org/sangupta/shire)

The library is tested against

* Oracle JDK 7
* Oracle JDK 6
* Open JDK 7
* Open JDK 6

Versioning
----------

For transparency and insight into our release cycle, and for striving to maintain backward compatibility, Bootstrap will be maintained under the Semantic Versioning guidelines as much as possible.

Releases will be numbered with the follow format:

`<major>.<minor>.<patch>`

And constructed with the following guidelines:

* Breaking backward compatibility bumps the major
* New additions without breaking backward compatibility bumps the minor
* Bug fixes and misc changes bump the patch

For more information on SemVer, please visit http://semver.org/.

License
-------

Copyright 2012, Sandeep Gupta

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
