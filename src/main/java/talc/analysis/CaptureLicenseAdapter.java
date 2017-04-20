// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.spdx.rdfparser.InvalidSPDXAnalysisException;
import org.spdx.rdfparser.license.AnyLicenseInfo;
import org.spdx.rdfparser.license.ExtractedLicenseInfo;
import org.spdx.rdfparser.model.SpdxDocument;

import ac.ucy.cs.spdx.parser.CaptureLicense;
import ac.ucy.cs.spdx.parser.LicenseExpression;
import talc.license.AnyLicenseInfoOperations;
import talc.license.AnyLicenseInfoOperationsImpl;
import talc.license.ComponentLicenseInfo;

public class CaptureLicenseAdapter implements CaptureLicense {

	private SpdxDocument document;

	private ComponentLicenseInfo licenseInfo;

	public CaptureLicenseAdapter(SpdxDocument document, ComponentLicenseInfo licenseInfo) {
		this.document = document;
		this.licenseInfo = licenseInfo;
	}

	@Override
	public AnyLicenseInfo getConcludedLicense() {
		return licenseInfo.getConcludedLicense();
	}

	@Override
	public List<String> getExtractedLicensesList() throws InvalidSPDXAnalysisException {
		List<String> licenseIDs = new ArrayList<String>();
		AnyLicenseInfoOperations licenseOps = new AnyLicenseInfoOperationsImpl();

		for (AnyLicenseInfo license : licenseInfo.getContainedLicenses()) {
			List<String> currentLicenseIDs = licenseOps.getContainedLicenses(license, false);
			licenseIDs.addAll(currentLicenseIDs);
		}

		return licenseIDs;
	}

	@Override
	public String getFileName() {
		return licenseInfo.getOrigin().getName();
	}

	@Override
	public LicenseExpression getLicenseExpression() {
		return LicenseExpression.parseExpression(licenseInfo.getDeclaredLicense().toString(), getReferencedLicenses());
	}

	@Override
	public HashMap<String, String> getReferencedLicenses() {
		HashMap<String, String> referencedLicenses = new HashMap<String, String>();
		List<String> containingLicenses;

		try {
			containingLicenses = getExtractedLicensesList();
		} catch (InvalidSPDXAnalysisException e1) {
			return referencedLicenses;
		}

		for (String licenseID : containingLicenses) {
			referencedLicenses.put(licenseID, licenseID);
		}

		try {
			for (ExtractedLicenseInfo extractedLicense : document.getExtractedLicenseInfos()) {
				String id = extractedLicense.getLicenseId();

				if (containingLicenses.contains(id)) {
					referencedLicenses.put(id, id);
				}

			}
		} catch (InvalidSPDXAnalysisException e) {
			return referencedLicenses;
		}

		return referencedLicenses;
	}
}
