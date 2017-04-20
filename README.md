# TALC: Tool Assisted License Compliance

TALC is a prototype of a license compliance system which was written as part of my master thesis. It takes SPDX files as input and creates a compliance report. During its analysis phase TALC tries to recover a module dependency graph based on STATIC_LINK and DYNAMIC_LINK relationshipts and connects each module with software connector [1]. The recovered dependency graph is then used to determine the license compatibility between all licenses in a module and all licenses which are accumulated through the software connectors. For license compatibility analysis a license compatibility graph [2], which implementation was incorporated from the 
[SPDX License Compatibility RESTful Service](https://github.com/dpasch01/spdx-compat-tools), is used.

## Limitations

Currently TALC has only limited applicability:
- No support for dual licensing
- Only a limited set of FOSS licenses is recognized by the compatibility analysis, unknown licenses are mapped to a default proprietary license in the graph
- Only a simple heuristic is used to recover the dependency graph
- No supported for circular dependencies

## Compiling

TALC is a Java application and Maven is used as build system. To build from source use:

```
mvn package
```

## References

[1] Alspaugh, Thomas A., Hazeline U. Asuncion und Walt Scacchi (2010). Software Licenses in Context: The Challenge of Heterogeneously-Licensed Systems. In: Journal of the Association for Information Systems 11.

[2] Kapitsaki, Georgia M., Frederik Kramer and Nikolaos D. Tselikas (2016). Automating the License Compatibility Process in Open Source Software with SPDX. In: Journal of Systems and Software. DOI: [10.1016/j.jss.2016.06.064](http://dx.doi.org/10.1016/j.jss.2016.06.064).

## License

Copyright 2017 Thomas Ascher
Licensed under the GNU General Public License v3.0 or higher
