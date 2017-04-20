// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.analysis.result;

public class LicenseComplianceConflict {

	private LicenseComplianceInformation first;

	private LicenseComplianceInformation second;

	public LicenseComplianceConflict(LicenseComplianceInformation first, LicenseComplianceInformation second) {
		setFirst(first);
		setSecond(second);
	}

	public LicenseComplianceInformation getFirst() {
		return first;
	}

	public LicenseComplianceInformation getSecond() {
		return second;
	}

	public void setFirst(LicenseComplianceInformation first) {
		this.first = first;
	}

	public void setSecond(LicenseComplianceInformation second) {
		this.second = second;
	}

	@Override
	public String toString() {
		return getFirst().toString() + " with " + getSecond().toString();
	}
}
