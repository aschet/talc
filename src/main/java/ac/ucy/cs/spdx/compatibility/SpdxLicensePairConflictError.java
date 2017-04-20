// Copyright 2016 Demetris Paschalides
// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-2.0+

package ac.ucy.cs.spdx.compatibility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jgrapht.alg.util.Pair;
import org.spdx.rdfparser.InvalidSPDXAnalysisException;

import ac.ucy.cs.spdx.license.License;
import ac.ucy.cs.spdx.parser.CaptureLicense;
import ac.ucy.cs.spdx.parser.LicenseExpression;

public class SpdxLicensePairConflictError {
	private boolean areCompatible = true;
	private LicenseCompatibility compatibility;
	private boolean concludedExists = true;
	private boolean declaredExistsInExtracted = false;
	private List<String> declaredLicenses;
	private List<String> extractedLicenses;
	private List<Pair<String, String>> incompatiblePairs = new ArrayList<>();
	private boolean isAdjustable = true;
	private List<License> proposedLicenses;

	private CaptureLicense spdxCaptured;

	public SpdxLicensePairConflictError(CaptureLicense spdxCaptured) throws InvalidSPDXAnalysisException {

		compatibility = new LicenseCompatibility();
		LicenseExpression expression = spdxCaptured.getLicenseExpression();

		this.declaredLicenses = new ArrayList<String>();
		for (String declared : expression.getLicenses()) {
			if (!Arrays.asList(LicenseExpression.ignoredLicenseList).contains(declared)) {
				this.declaredLicenses.add(declared);
			}
		}

		this.extractedLicenses = new ArrayList<String>();
		this.extractedLicenses.addAll(spdxCaptured.getExtractedLicensesList());

		for (String licenseIdentifier : spdxCaptured.getExtractedLicensesList()) {
			for (String declared : this.getDeclaredLicenses()) {
				if (declared.equals(licenseIdentifier)) {
					this.setDeclaredExistsInExtracted(true);
					break;
				}
				if (this.isDeclaredExistsInExtracted()) {
					break;
				}
			}
			if (this.isDeclaredExistsInExtracted()) {
				break;
			}
		}

		if (spdxCaptured.getConcludedLicense().toString().equals("NONE")) {
			this.setConcludedExists(false);
		}

		this.proposedLicenses = new ArrayList<License>();

		Map<String, String> referenced = spdxCaptured.getReferencedLicenses();
		String[] referencedLicenseIDs = referenced.values().toArray(new String[referenced.values().size()]);

		if (!compatibility.areCompatible(referencedLicenseIDs)) {

			incompatiblePairs = compatibility.getIncompatiblePairs(referencedLicenseIDs);

			this.setCompatible(false);
			this.proposedLicenses.addAll(compatibility.proposeLicense(referencedLicenseIDs));
			if (this.proposedLicenses.isEmpty()) {
				setAdjustable(false);

			}
		}

		this.setSpdxCaptured(spdxCaptured);
	}

	public boolean areCompatible() {
		return areCompatible;
	}

	public List<String> getDeclaredLicenses() {
		return declaredLicenses;
	}

	public String[] getDeclaredLicensesArray() {
		return (String[]) declaredLicenses.toArray();
	}

	public List<String> getExtractedLicenses() {
		return extractedLicenses;
	}

	public List<Pair<String, String>> getIncompatiblePairs() {
		return incompatiblePairs;
	}

	public List<License> getProposedLicenses() {
		return proposedLicenses;
	}

	public CaptureLicense getSpdxCaptured() {
		return spdxCaptured;
	}

	public boolean isAdjustable() {
		return isAdjustable;
	}

	public boolean isConcludedExists() {
		return concludedExists;
	}

	public boolean isDeclaredExistsInExtracted() {
		return declaredExistsInExtracted;
	}

	public void setAdjustable(boolean isAdjustable) {
		this.isAdjustable = isAdjustable;
	}

	public void setCompatible(boolean areCompatible) {
		this.areCompatible = areCompatible;
	}

	public void setConcludedExists(boolean concludedExists) {
		this.concludedExists = concludedExists;
	}

	public void setDeclaredExistsInExtracted(boolean declaredExistsInExtracted) {
		this.declaredExistsInExtracted = declaredExistsInExtracted;
	}

	public void setDeclaredLicenses(ArrayList<String> declaredLicenses) {
		this.declaredLicenses = declaredLicenses;
	}

	public void setExtractedLicenses(ArrayList<String> extractedLicenses) {
		this.extractedLicenses = extractedLicenses;
	}

	public void setProposedLicenses(ArrayList<License> proposedLicenses) {
		this.proposedLicenses = proposedLicenses;
	}

	public void setSpdxCaptured(CaptureLicense spdxCaptured) {
		this.spdxCaptured = spdxCaptured;
	}

	@Override
	public String toString() {
		StringBuilder errorEcho = new StringBuilder();
		errorEcho.append(spdxCaptured.getFileName());
		if (this.areCompatible) {
			errorEcho.append(" has no violations.");
		} else {
			errorEcho.append(" has violations considering the declared licenses compatibility.\n");
			if (this.isAdjustable) {
				errorEcho.append("The violations are adjustable and the proposed licenses are:\n");
				for (License license : this.proposedLicenses) {
					errorEcho.append(license.getIdentifier() + "\n");
				}
			} else {
				errorEcho.append("The violations are not adjustable, please reconsider your declared licenses.");
			}
		}

		return errorEcho.toString();
	}

}
