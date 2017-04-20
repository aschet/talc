// Copyright 2016 Demetris Paschalides
// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-2.0+

package ac.ucy.cs.spdx.compatibility;

import java.util.ArrayList;
import java.util.Arrays;

import ac.ucy.cs.spdx.license.License;
import ac.ucy.cs.spdx.parser.LicenseExpression;

public class ViolationAnalysisInfo {
	static String[] getMultipleDeclaredLicenses(SpdxLicensePairConflictError... unaryErrors) {
		ArrayList<String> multipleDeclared = new ArrayList<String>();
		for (SpdxLicensePairConflictError unaryError : unaryErrors) {
			for (String declared : unaryError.getDeclaredLicenses()) {
				if (!multipleDeclared.contains(declared)) {
					if (!Arrays.asList(LicenseExpression.ignoredLicenseList).contains(declared)) {
						multipleDeclared.add(declared);
					}
				}
			}

		}

		return multipleDeclared.toArray(new String[multipleDeclared.size()]);
	}

	private LicenseCompatibility compatibility;
	private boolean isAdjustFeasible;
	private ArrayList<License> proposedLicenses;

	private ArrayList<SpdxLicensePairConflictError> unarySpdxErrors;

	private boolean violationDetected;

	public ViolationAnalysisInfo(SpdxLicensePairConflictError... unaryErrors) {

		compatibility = new LicenseCompatibility();
		ArrayList<SpdxLicensePairConflictError> conjunctiveBucket = new ArrayList<SpdxLicensePairConflictError>();
		ArrayList<SpdxLicensePairConflictError> disjunctiveBucket = new ArrayList<SpdxLicensePairConflictError>();
		ArrayList<String> visitedBucket = new ArrayList<String>();

		for (SpdxLicensePairConflictError indexError : unaryErrors) {
			if (indexError.getSpdxCaptured().getLicenseExpression().isDisjunctive()) {
				disjunctiveBucket.add(indexError);
			} else {
				conjunctiveBucket.add(indexError);
			}
		}

		SpdxLicensePairConflictError[] disjunctiveArray = new SpdxLicensePairConflictError[disjunctiveBucket.size()];

		this.unarySpdxErrors = new ArrayList<SpdxLicensePairConflictError>();
		this.unarySpdxErrors.addAll(Arrays.asList(unaryErrors));
		this.proposedLicenses = new ArrayList<License>();

		if (disjunctiveBucket.isEmpty()) {
			String[] declared = getMultipleDeclaredLicenses(unaryErrors);

			if (compatibility.areCompatible(declared)) {
				this.setViolationDetected(false);
			} else {
				this.setViolationDetected(true);
				proposedLicenses.addAll(compatibility.proposeLicense(declared));
				if (proposedLicenses.isEmpty()) {
					this.setAdjustFeasible(false);
				} else {
					this.setAdjustFeasible(true);
				}
			}

		} else {
			while (visitedBucket
					.size() != getMultipleDeclaredLicenses(disjunctiveBucket.toArray(disjunctiveArray)).length) {
				ArrayList<String> traverseBucket = new ArrayList<String>();
				for (SpdxLicensePairConflictError disjunctivePair : disjunctiveBucket) {
					for (String traverseLicense : disjunctivePair.getDeclaredLicenses()) {
						if (visitedBucket.contains(traverseLicense)) {
							continue;
						} else {
							visitedBucket.add(traverseLicense);
							traverseBucket.add(traverseLicense);
							break;
						}
					}
				}

				traverseBucket.addAll(Arrays.asList(getMultipleDeclaredLicenses(
						conjunctiveBucket.toArray(new SpdxLicensePairConflictError[conjunctiveBucket.size()]))));

				if (compatibility.areCompatible(traverseBucket.toArray(new String[traverseBucket.size()]))) {
					this.setViolationDetected(false);
					break;
				} else {
					this.setViolationDetected(true);
					proposedLicenses.addAll(
							compatibility.proposeLicense(traverseBucket.toArray(new String[traverseBucket.size()])));
					if (proposedLicenses.isEmpty()) {
						this.setAdjustFeasible(false);
					} else {
						this.setAdjustFeasible(true);
					}
				}
			}
		}
	}

	public ArrayList<License> getProposedLicenses() {
		return proposedLicenses;
	}

	public ArrayList<SpdxLicensePairConflictError> getUnarySpdxErrors() {
		return unarySpdxErrors;
	}

	public boolean isAdjustFeasible() {
		return isAdjustFeasible;
	}

	public boolean isViolationDetected() {
		return violationDetected;
	}

	public void setAdjustFeasible(boolean isAdjustFeasible) {
		this.isAdjustFeasible = isAdjustFeasible;
	}

	public void setProposedLicenses(ArrayList<License> proposedLicenses) {
		this.proposedLicenses = proposedLicenses;
	}

	public void setUnarySpdxErrors(ArrayList<SpdxLicensePairConflictError> unarySpdxErrors) {
		this.unarySpdxErrors = unarySpdxErrors;
	}

	public void setViolationDetected(boolean violationDetected) {
		this.violationDetected = violationDetected;
	}

	@Override
	public String toString() {
		for (SpdxLicensePairConflictError error : this.unarySpdxErrors) {
			if (!error.areCompatible()) {
				return error.toString();
			}
		}

		StringBuilder violationEcho = new StringBuilder();
		for (SpdxLicensePairConflictError error : this.getUnarySpdxErrors()) {
			violationEcho.append(error.getSpdxCaptured().getFileName() + ", ");
		}
		violationEcho.delete(violationEcho.length() - 2, violationEcho.length() - 1);

		if (this.violationDetected) {
			violationEcho.append(" has/have some compatibility issues.\n");
			if (this.isAdjustFeasible) {
				violationEcho.append(
						"The violation is adjustable and the proposed licenses that can be used for the matter are : \n");
				for (License l : this.proposedLicenses) {
					violationEcho.append(l.getLicenseName() + "\n");
				}

			} else {
				violationEcho.append(
						"The violation is not adjustable and there are no proposed licenses that can be used for the matter.\nPlease review your licenses again.");
			}
		} else {
			violationEcho.append(" has/have no violation of compatibility issues.");
		}

		return violationEcho.toString();
	}
}
