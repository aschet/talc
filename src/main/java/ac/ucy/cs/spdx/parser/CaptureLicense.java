// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-2.0+

package ac.ucy.cs.spdx.parser;

import java.util.HashMap;
import java.util.List;

import org.spdx.rdfparser.InvalidSPDXAnalysisException;
import org.spdx.rdfparser.license.AnyLicenseInfo;

public interface CaptureLicense {

	AnyLicenseInfo getConcludedLicense();

	List<String> getExtractedLicensesList() throws InvalidSPDXAnalysisException;

	String getFileName();

	LicenseExpression getLicenseExpression();

	HashMap<String, String> getReferencedLicenses();

}