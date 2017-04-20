// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.analysis.result;

import java.util.List;

public interface ComponentComplianceInformation {
	void addContainedLicense(LicenseComplianceInformation containedLicense);

	void addLicenseConflict(LicenseComplianceConflict conflict);

	void addPrimaryLicense(LicenseComplianceInformation primaryLicense);

	List<LicenseComplianceInformation> getContainedLicenses();

	List<LicenseComplianceConflict> getLicenseConflicts();

	String getName();

	List<LicenseComplianceInformation> getPrimaryLicenses();

	boolean isCompliant();

	void setCompliant(boolean compliant);

	void setContainedLicenses(List<LicenseComplianceInformation> containedLicenses);

	void setLicenseConflicts(List<LicenseComplianceConflict> conflicts);

	void setName(String name);

	void setPrimaryLicenses(List<LicenseComplianceInformation> primaryLicenses);
}
