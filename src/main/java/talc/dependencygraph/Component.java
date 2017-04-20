// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.dependencygraph;

import org.spdx.rdfparser.model.SpdxElement;

import talc.license.ComponentLicenseInfo;

public interface Component {

	void addContent(SpdxElement content);

	ComponentLicenseInfo extractLicenseInfo();

	ConnectorType getConnectorTypeTo(Component module);

	SpdxElement getDocumentElement();

	String getID();

	String getName();

	boolean isFile();

	boolean isPackage();

	void setDocumentElement(SpdxElement documentElement);
}
