// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.license;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spdx.rdfparser.InvalidSPDXAnalysisException;
import org.spdx.rdfparser.license.AnyLicenseInfo;
import org.spdx.rdfparser.model.SpdxFile;
import org.spdx.rdfparser.model.SpdxPackage;

import talc.dependencygraph.Component;
import talc.dependencygraph.Connector;

public class ComponentLicenseInfoImpl implements ComponentLicenseInfo {

	public static ComponentLicenseInfo create(Component component) {

		if (component.isFile()) {
			SpdxFile file = (SpdxFile) component.getDocumentElement();
			ComponentLicenseInfoImpl licenseInfo = new ComponentLicenseInfoImpl();
			licenseInfo.concludedLicense = file.getLicenseConcluded();
			licenseInfo.declaredLicense = file.getLicenseConcluded();
			licenseInfo.containedLicenses = new ArrayList<AnyLicenseInfo>(
					Arrays.asList(file.getLicenseInfoFromFiles()));
			licenseInfo.origin = component;
			return licenseInfo;
		} else if (component.isPackage()) {
			SpdxPackage pkg = (SpdxPackage) component.getDocumentElement();
			ComponentLicenseInfoImpl licenseInfo = new ComponentLicenseInfoImpl();
			licenseInfo.concludedLicense = pkg.getLicenseConcluded();
			try {
				licenseInfo.declaredLicense = pkg.getLicenseDeclared();
			} catch (InvalidSPDXAnalysisException e) {
				licenseInfo.declaredLicense = licenseInfo.concludedLicense;
			}
			licenseInfo.containedLicenses = new ArrayList<AnyLicenseInfo>(Arrays.asList(pkg.getLicenseInfoFromFiles()));
			if (licenseInfo.containedLicenses.isEmpty()) {
				licenseInfo.containedLicenses.add(licenseInfo.declaredLicense);
			}
			licenseInfo.origin = component;
			return licenseInfo;
		} else {
			throw new UnknownComponentTypeException();
		}
	}

	private AnyLicenseInfo concludedLicense;

	private List<AnyLicenseInfo> containedLicenses;

	private AnyLicenseInfo declaredLicense;

	private Map<AnyLicenseInfo, Component> mergeInfo = new HashMap<>();

	private Component origin;

	@Override
	public boolean containsLicense(AnyLicenseInfo license) {
		List<AnyLicenseInfo> allLicenses = new ArrayList<>();
		allLicenses.addAll(containedLicenses);
		allLicenses.add(getConcludedLicense());
		allLicenses.add(getDeclaredLicense());

		return allLicenses.contains(license);
	}

	@Override
	public AnyLicenseInfo getConcludedLicense() {
		return concludedLicense;
	}

	@Override
	public List<AnyLicenseInfo> getContainedLicenses() {
		return containedLicenses;
	}

	@Override
	public AnyLicenseInfo getDeclaredLicense() {
		return declaredLicense;
	}

	@Override
	public Component getOrigin() {
		return origin;
	}

	@Override
	public Component getOriginOfLicense(AnyLicenseInfo license) {
		return mergeInfo.getOrDefault(license, getOrigin());
	}

	@Override
	public void mergeWith(ComponentLicenseInfo other, Connector filter) {
		// NOTE: does not handle dual licensing

		AnyLicenseInfoOperations licenseOps = new AnyLicenseInfoOperationsImpl();

		for (AnyLicenseInfo licenseInfo : other.getContainedLicenses()) {
			AnyLicenseInfo filteredLicenseInfo = filter.filter(licenseInfo.clone());
			if (licenseOps.isValidLicense(filteredLicenseInfo) && !containsLicense(filteredLicenseInfo)) {
				containedLicenses.add(filteredLicenseInfo);
				mergeInfo.put(filteredLicenseInfo, other.getOriginOfLicense(licenseInfo));
			}
		}
	}

	@Override
	public String toString() {
		if (getOrigin() != null) {
			return getOrigin().getName();
		} else {
			return "unknown";
		}
	}
}
