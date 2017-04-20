// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.analysis.result;

import talc.license.obligation.DefaultObligationSet;
import talc.license.obligation.ObligationSet;

public class LicenseComplianceInformationImpl implements LicenseComplianceInformation {

	String licenseName;

	ObligationSet obligations = new DefaultObligationSet();

	String originName = "";

	@Override
	public String getLicenseName() {
		return licenseName;
	}

	@Override
	public ObligationSet getObligations() {
		return obligations;
	}

	@Override
	public String getOriginName() {
		return originName;
	}

	@Override
	public void setLicenseName(String licenseName) {
		this.licenseName = licenseName;

	}

	@Override
	public void setObligations(ObligationSet obligations) {
		this.obligations = obligations;
	}

	@Override
	public void setOriginName(String originName) {
		this.originName = originName;
	}

	@Override
	public String toString() {
		String result = getLicenseName();
		if (!getOriginName().isEmpty()) {
			result += " (via " + getOriginName() + ")";
		}
		return result;
	}
}
