# TALC: Tool Assisted License Compliance

TALC is a prototype of a license compliance system which was written as part of my master thesis. It takes SPDX files in the RDF format as input and creates a compliance report. During its analysis phase TALC tries to recover a module dependency graph based on STATIC_LINK and DYNAMIC_LINK relationshipts and connects each module with a software connector [1]. The recovered dependency graph is then used to determine the license compatibility between all licenses in a module and all licenses which are accumulated through the software connectors. For license compatibility analysis a license compatibility graph [2], which implementation was incorporated from the [SPDX License Compatibility RESTful Service](https://github.com/dpasch01/spdx-compat-tools), is used.

Based on the expirence gained by implementing this prototype the following libraries were created:
- [spdx-license-expression-tools](https://github.com/aschet/spdx-license-expression-tools)
- [spdx-license-compat](https://github.com/aschet/spdx-license-compat)

## Limitations

Currently TALC has only limited applicability:
- No support for dual licensing.
- Only a limited set of FOSS licenses is recognized by the compatibility analysis, unknown licenses are mapped to a default proprietary license in the graph.
- Only a simple heuristic is used to recover the dependency graph.
- No supported for circular dependencies.
- Creating SPDX documents with linking relationships is currently not supported by most compliance tools.
- No support for multiple SPDX documents per analysis.

## Compiling

TALC is a Java application and Maven is used as build system. To build from source use:

```
mvn package
```

## References

[1] Alspaugh, Thomas A., Hazeline U. Asuncion and Walt Scacchi (2010). Software Licenses in Context: The Challenge of Heterogeneously-Licensed Systems. In: Journal of the Association for Information Systems 11.

[2] Kapitsaki, Georgia M., Frederik Kramer and Nikolaos D. Tselikas (2016). Automating the License Compatibility Process in Open Source Software with SPDX. In: Journal of Systems and Software. DOI: [10.1016/j.jss.2016.06.064](http://dx.doi.org/10.1016/j.jss.2016.06.064).

## License

Copyright 2017 Thomas Ascher, licensed under the GNU General Public License v3.0 or higher

## Sample Report

```
Free Software Compliance Report
===============================

Overall compliance of architecture: non compliant

* LIB_1_1: compliant
* LIB_2_1: compliant
* LIB_3.py: compliant
* LIB_1: compliant
* LIB_2: compliant
* LIB_2_2: compliant
* APP: non compliant

LIB_1_1
-------

Primary Licenses:
* BSD 2-clause FreeBSD License

Contained Licenses:
* BSD 2-clause FreeBSD License

LIB_2_1
-------

Primary Licenses:
* GNU General Public License v3.0 or later

Contained Licenses:
* GNU General Public License v3.0 or later
* BSD 2-clause FreeBSD License

LIB_3.py
--------

Primary Licenses:
* GNU General Public License v2.0 only

Contained Licenses:
* GNU General Public License v2.0 only

LIB_1
-----

Primary Licenses:
* GNU Lesser General Public License v3.0 or later

Contained Licenses:
* GNU Lesser General Public License v3.0 or later
* BSD 2-clause FreeBSD License (via LIB_1_1)

LIB_2
-----

Primary Licenses:
* GNU Lesser General Public License v3.0 or later

Contained Licenses:
* GNU Lesser General Public License v3.0 or later
* GNU General Public License v3.0 or later (via LIB_2_1)
* MIT License (via LIB_2_2)

LIB_2_2
-------

Primary Licenses:
* MIT License

Contained Licenses:
* MIT License

APP
---

Primary Licenses:
* Ashsoft Proprietary Software License

Contained Licenses:
* Ashsoft Proprietary Software License
* GNU General Public License v2.0 only (via LIB_3.py)
* GNU Lesser General Public License v3.0 or later (via LIB_2)
* GNU General Public License v3.0 or later (via LIB_2_1)
* MIT License (via LIB_2_2)

License Conflicts:
* Ashsoft Proprietary Software License with GNU General Public License v2.0 only
* Ashsoft Proprietary Software License with GNU General Public License v3.0 or later
* GNU Lesser General Public License v3.0 or later with GNU General Public License v2.0 only
* GNU General Public License v2.0 only with GNU General Public License v3.0 or later

License Obligations
-------------------

GNU General Public License v2.0 only:
* The source code has to be made publicly available.
* The original copyright must be retained.
* No further sublicing is permitted.
* The full text of license has to be included.

BSD 2-clause FreeBSD License:
* The original copyright must be retained.
* The full text of license has to be included.

GNU General Public License v3.0 or later:
* Installation instructions have to be included when integrated into firmware.
* The source code has to be made publicly available.
* The full text of license has to be included.
* Inclusion will result in implicit patent grants for third parties.
* No further sublicing is permitted.
* The original copyright must be retained.

MIT License:
* The full text of license has to be included.
* The original copyright must be retained.

Ashsoft Proprietary Software License:
* Please refer to the license text for more information.

GNU Lesser General Public License v3.0 or later:
* Inclusion will result in implicit patent grants for third parties.
* Installation instructions have to be included when integrated into firmware.
* The original copyright must be retained.
* No further sublicing is permitted.
* The source code has to be made publicly available.
* The full text of license has to be included.
```

