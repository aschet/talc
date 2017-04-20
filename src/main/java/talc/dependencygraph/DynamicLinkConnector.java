// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.dependencygraph;

import java.util.List;

import org.spdx.rdfparser.license.AnyLicenseInfo;

import talc.license.AnyLicenseInfoOperations;
import talc.license.AnyLicenseInfoOperationsImpl;

public class DynamicLinkConnector extends ConnectorImpl {

	@Override
	public AnyLicenseInfo filter(AnyLicenseInfo licenseInfo) {
		AnyLicenseInfoOperations licenseOps = new AnyLicenseInfoOperationsImpl();

		// NOTE: prevent licenses which are not limited by dynamic linking,
		// this should not be hard coded values
		List<String> containedLicenseIDs = licenseOps.getContainedLicenses(licenseInfo, true);
		containedLicenseIDs.remove("AGPL-3.0");
		containedLicenseIDs.remove("GPL-2.0");
		containedLicenseIDs.remove("GPL-3.0");

		AnyLicenseInfo newLicenseInfo = licenseInfo.clone();
		return licenseOps.filtered(newLicenseInfo, containedLicenseIDs, true);
	}

	@Override
	public ConnectorType getType() {
		return ConnectorType.DYNAMIC_LINK;
	}
}
