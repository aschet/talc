// Copyright 2016 Demetris Paschalides
// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-2.0+

package ac.ucy.cs.spdx.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * LicenseExpression class provides us with an object containing the declared
 * license or licenses of an SPDXPackage, and whether or not those licenses are
 * conjunctive or disjunctive.
 *
 * @version 1.2
 * @author dpasch01
 */
public class LicenseExpression {
	public static final String DUAL_LICENSE = "Dual-license";
	public static final String FSF = "FSF";
	public static final String FSF_REF = "FSF-ref";
	public static final String NO_LICENSE_FOUND = "No_license_found";
	public static final String NOT_PUBLIC_DOMAIN = "NOT-public-domain";
	public static final String OTHER = "See-doc(OTHER)";
	public static final String RESTRICTED_RIGHTS = "Restricted-rights";
	public static final String SAME_AS = "Same-license-as";
	public static final String SEE_FILE = "See-file";
	public static final String TRADEMARK_REF = "Trademark-ref";
	public static final String UNCLASSIFIED = "UnclassifiedLicense";
	public static final String UNRAR_RESTRICTION = "unRAR restriction";
	public static String[] ignoredLicenseList = { "Public-domain", "Public-domain-ref", SAME_AS, NO_LICENSE_FOUND,
			DUAL_LICENSE, UNCLASSIFIED, TRADEMARK_REF, OTHER, SEE_FILE, FSF, FSF_REF, RESTRICTED_RIGHTS,
			UNRAR_RESTRICTION, NOT_PUBLIC_DOMAIN };
	
	public static LicenseExpression parseExpression(String licenseExp, HashMap<String, String> referencedLicenses) {
		LicenseExpression expObject = new LicenseExpression();
		String[] exportedLicenses;

		licenseExp = licenseExp.replace(")", "");
		licenseExp = licenseExp.replace("(", "");

		if (licenseExp.contains(" OR ")) {
			expObject.disjunctive = true;
			exportedLicenses = licenseExp.split(" OR ");
		} else {
			expObject.disjunctive = false;
			exportedLicenses = licenseExp.split(" AND ");
		}

		expObject.addLicenses(exportedLicenses, referencedLicenses);
		return expObject;
	}

	private boolean disjunctive;

	private ArrayList<String> licenses;

	public LicenseExpression() {
		this.licenses = new ArrayList<String>();
		this.disjunctive = false;
	}

	public void addLicenses(String[] licenses, HashMap<String, String> referencedLicenses) {
		int index = 0;
		for (String referenced : licenses) {
			if (referenced.contains("LicenseRef")) {
				licenses[index] = referenced;
			}
			index++;
		}
		this.licenses.addAll(Arrays.asList(licenses));
	}

	public ArrayList<String> getLicenses() {
		return this.licenses;
	}

	public String[] getLicensesArray() {
		return this.licenses.toArray(new String[licenses.size()]);
	}

	public boolean isDisjunctive() {
		return disjunctive;
	}

}
