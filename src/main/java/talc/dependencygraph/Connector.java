// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.dependencygraph;

import org.spdx.rdfparser.license.AnyLicenseInfo;

public interface Connector {
	AnyLicenseInfo filter(AnyLicenseInfo licenseInfo);

	ConnectorType getType();
}
