// Copyright 2016 Demetris Paschalides
// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-2.0+

package ac.ucy.cs.spdx.license;

/**
 * This is License object class. It consists of several constructors and methods
 * for editing and accessing data. The object consists of the license name,
 * identifier and text.
 *
 * @author dpasch01
 * @version 1.2
 */
public class License implements Comparable<License> {
	private String identifier;
	private String licenseName;

	public License(String name, String identifier) {
		this.setLicenseName(name);
		this.setIdentifier(identifier);
	}

	@Override
	public int compareTo(License o) {
		return identifier.compareTo(o.identifier);
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getLicenseName() {
		return licenseName;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public void setLicenseName(String licenseName) {
		this.licenseName = licenseName;
	}

	@Override
	public String toString() {
		return this.getIdentifier();
	}
}
