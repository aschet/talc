// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.dependencygraph;

import org.spdx.rdfparser.license.AnyLicenseInfo;

public class StaticLinkConnector extends ConnectorImpl {

	@Override
	public AnyLicenseInfo filter(AnyLicenseInfo licenseInfo) {
		return licenseInfo.clone();
	}

	@Override
	public ConnectorType getType() {
		return ConnectorType.STATIC_LINK;
	}

}
