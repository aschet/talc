// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.analysis.result;

import java.util.ArrayList;
import java.util.List;

public class ComponentComplianceInformationImpl implements ComponentComplianceInformation {

	private boolean compliancy;

	private List<LicenseComplianceInformation> containedLicenses = new ArrayList<>();

	private List<LicenseComplianceConflict> licenseConflicts = new ArrayList<>();

	private String name;

	private List<LicenseComplianceInformation> primaryLicenses = new ArrayList<>();

	@Override
	public void addContainedLicense(LicenseComplianceInformation containedLicense) {
		containedLicenses.add(containedLicense);

	}

	@Override
	public void addLicenseConflict(LicenseComplianceConflict conflict) {
		licenseConflicts.add(conflict);
	}

	@Override
	public void addPrimaryLicense(LicenseComplianceInformation primaryLicense) {
		primaryLicenses.add(primaryLicense);
	}

	@Override
	public List<LicenseComplianceInformation> getContainedLicenses() {
		return containedLicenses;
	}

	@Override
	public List<LicenseComplianceConflict> getLicenseConflicts() {
		return licenseConflicts;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<LicenseComplianceInformation> getPrimaryLicenses() {
		return primaryLicenses;
	}

	@Override
	public boolean isCompliant() {
		return compliancy;
	}

	@Override
	public void setCompliant(boolean compliant) {
		compliancy = compliant;
	}

	@Override
	public void setContainedLicenses(List<LicenseComplianceInformation> containedLicenses) {
		this.containedLicenses = containedLicenses;
	}

	@Override
	public void setLicenseConflicts(List<LicenseComplianceConflict> conflicts) {
		licenseConflicts = conflicts;

	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setPrimaryLicenses(List<LicenseComplianceInformation> primaryLicenses) {
		this.primaryLicenses = primaryLicenses;
	}
}
