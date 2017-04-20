// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-2.0+

package ac.ucy.cs.spdx.license;

import java.util.ArrayList;
import java.util.HashSet;

import ac.ucy.cs.spdx.exception.LicenseAlreadyExistsException;

public class LicenseRegistry {
	private static LicenseRegistry instance;

	public static synchronized LicenseRegistry getInstance() {
		if (instance == null) {
			instance = new LicenseRegistry();
		}
		return instance;
	}

	private HashSet<License> licenses = new HashSet<License>();

	public License findLicense(String licenseIdentifier) {
		License license = null;
		for (License l : licenses) {
			if (l.getIdentifier().equals(licenseIdentifier)) {
				license = l;
			}
		}
		return license;
	}

	public ArrayList<License> getLicenses() {
		return new ArrayList<License>(licenses);
	}

	public boolean licenseExists(String lIdentifier) {
		License license = findLicense(lIdentifier);
		if (license != null) {
			return true;
		}
		return false;
	}

	public License registerLicense(String name, String identifier) throws LicenseAlreadyExistsException {
		if (licenseExists(identifier)) {
			throw new LicenseAlreadyExistsException(identifier + " already exists in the system.");
		}

		License license = new License(name, identifier);
		licenses.add(license);
		return license;
	}
}
