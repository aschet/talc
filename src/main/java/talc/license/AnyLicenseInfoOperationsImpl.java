// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.license;

import java.util.ArrayList;
import java.util.List;

import org.spdx.rdfparser.InvalidSPDXAnalysisException;
import org.spdx.rdfparser.license.AnyLicenseInfo;
import org.spdx.rdfparser.license.ExtractedLicenseInfo;
import org.spdx.rdfparser.license.LicenseInfoFactory;
import org.spdx.rdfparser.license.LicenseSet;
import org.spdx.rdfparser.license.OrLaterOperator;
import org.spdx.rdfparser.license.SpdxNoAssertionLicense;
import org.spdx.rdfparser.license.SpdxNoneLicense;
import org.spdx.rdfparser.license.WithExceptionOperator;
import org.spdx.rdfparser.model.SpdxDocument;

public class AnyLicenseInfoOperationsImpl implements AnyLicenseInfoOperations {

	boolean doesMatch(AnyLicenseInfo licenseInfo, List<String> licenseIDs, boolean matchWithOperators) {
		String licenseText = getLicenseText(licenseInfo, matchWithOperators);
		for (String licenseID : licenseIDs) {
			if (licenseText.equals(licenseID)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public AnyLicenseInfo filtered(AnyLicenseInfo licenseInfo, List<String> licenseIDs, boolean matchWithOperators) {
		if (!isLicenseSet(licenseInfo)) {
			if (doesMatch(licenseInfo, licenseIDs, matchWithOperators)) {
				return new SpdxNoneLicense();
			} else {
				return licenseInfo;
			}
		} else {
			LicenseSet licenseSet = (LicenseSet) licenseInfo;
			List<AnyLicenseInfo> modifiedSet = new ArrayList<>();

			for (AnyLicenseInfo setLicenseInfo : licenseSet.getMembers()) {
				AnyLicenseInfo modifiedLicense = filtered(setLicenseInfo, licenseIDs, matchWithOperators);
				if (isValidLicense(modifiedLicense)) {
					modifiedSet.add(modifiedLicense);
				}
			}

			if (modifiedSet.isEmpty()) {
				return new SpdxNoneLicense();
			} else {
				try {
					if (modifiedSet.size() > 1) {
						licenseSet.setMembers(modifiedSet.toArray(new AnyLicenseInfo[modifiedSet.size()]));
						return licenseSet;
					} else {
						return modifiedSet.get(0);
					}
				} catch (InvalidSPDXAnalysisException e) {
					return new SpdxNoneLicense();
				}
			}
		}
	}

	@Override
	public AnyLicenseInfo filtered(AnyLicenseInfo licenseInfo, String licenseID, boolean matchWithOperators) {
		List<String> licenseIDs = new ArrayList<>();
		licenseIDs.add(licenseID);
		return filtered(licenseInfo, licenseIDs, matchWithOperators);
	}

	@Override
	public List<String> getContainedLicenses(AnyLicenseInfo licenseInfo, boolean removeOperators) {
		List<String> licenses = new ArrayList<String>();

		if (!isLicenseSet(licenseInfo)) {
			if (isValidLicense(licenseInfo)) {
				licenses.add(getLicenseText(licenseInfo, removeOperators));
			}
		} else {
			LicenseSet licenseSet = (LicenseSet) licenseInfo;
			for (AnyLicenseInfo setLicenseInfo : licenseSet.getMembers()) {
				licenses.addAll(getContainedLicenses(setLicenseInfo, removeOperators));
			}
		}

		return licenses;
	}

	@Override
	public String getLicenseName(String licenseID, SpdxDocument document) {
		if (LicenseInfoFactory.isSpdxListedLicenseID(licenseID)) {
			try {
				return LicenseInfoFactory.getListedLicenseById(licenseID).getName();
			} catch (InvalidSPDXAnalysisException e) {
				return licenseID;
			}
		}

		try {
			for (ExtractedLicenseInfo license : document.getExtractedLicenseInfos()) {
				if (license.getLicenseId().equals(licenseID)) {
					return license.getName();
				}
			}
		} catch (InvalidSPDXAnalysisException e) {
			return licenseID;
		}

		return licenseID;
	}

	String getLicenseText(AnyLicenseInfo licenseInfo, boolean removeOperators) {
		if (!removeOperators) {
			return licenseInfo.toString();
		} else {
			if (licenseInfo instanceof WithExceptionOperator) {
				return getLicenseText(((WithExceptionOperator) licenseInfo).getLicense(), removeOperators);
			} else if (licenseInfo instanceof OrLaterOperator) {
				return getLicenseText(((OrLaterOperator) licenseInfo).getLicense(), removeOperators);
			} else {
				return getLicenseText(licenseInfo, false);
			}
		}
	}

	@Override
	public boolean isLicenseSet(AnyLicenseInfo licenseInfo) {
		return licenseInfo instanceof LicenseSet;
	}

	@Override
	public boolean isValidLicense(AnyLicenseInfo licenseInfo) {
		if (licenseInfo instanceof SpdxNoneLicense || licenseInfo instanceof SpdxNoAssertionLicense) {
			return false;
		} else {
			return true;
		}
	}
}
