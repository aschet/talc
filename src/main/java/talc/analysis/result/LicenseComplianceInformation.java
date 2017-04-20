// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.analysis.result;

import talc.license.obligation.ObligationSet;

public interface LicenseComplianceInformation {
	String getLicenseName();

	ObligationSet getObligations();

	String getOriginName();

	void setLicenseName(String licenseName);

	void setObligations(ObligationSet obligations);

	void setOriginName(String originName);
}
