// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.license;

import java.util.List;

import org.spdx.rdfparser.license.AnyLicenseInfo;
import org.spdx.rdfparser.model.SpdxDocument;

public interface AnyLicenseInfoOperations {
	AnyLicenseInfo filtered(AnyLicenseInfo licenseInfo, List<String> licenseIDs, boolean matchWithOperators);

	AnyLicenseInfo filtered(AnyLicenseInfo licenseInfo, String licenseIDs, boolean matchWithOperators);

	List<String> getContainedLicenses(AnyLicenseInfo licenseInfo, boolean removeOperators);

	String getLicenseName(String licenseID, SpdxDocument document);

	boolean isLicenseSet(AnyLicenseInfo licenseInfo);

	boolean isValidLicense(AnyLicenseInfo licenseInfo);
}
