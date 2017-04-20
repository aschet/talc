// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.license;

import java.util.List;

import org.spdx.rdfparser.license.AnyLicenseInfo;

import talc.dependencygraph.Component;
import talc.dependencygraph.Connector;

public interface ComponentLicenseInfo {

	boolean containsLicense(AnyLicenseInfo license);

	AnyLicenseInfo getConcludedLicense();

	List<AnyLicenseInfo> getContainedLicenses();

	AnyLicenseInfo getDeclaredLicense();

	Component getOrigin();

	Component getOriginOfLicense(AnyLicenseInfo license);

	void mergeWith(ComponentLicenseInfo other, Connector filter);
}