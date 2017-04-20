// Copyright 2016 Demetris Paschalides
// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-2.0+

package ac.ucy.cs.spdx.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

import ac.ucy.cs.spdx.exception.LicenseAlreadyExistsException;
import ac.ucy.cs.spdx.exception.LicenseNodeAlreadyExistsException;
import ac.ucy.cs.spdx.license.License;
import ac.ucy.cs.spdx.license.LicenseRegistry;

public class LicenseNode {
	private LicenseGraph graph;
	private HashSet<License> licenses;
	private String nodeIdentifier;

	public LicenseNode(LicenseGraph graph, String nodeIdentifier, License... licencesArray)
			throws LicenseNodeAlreadyExistsException {
		this.graph = graph;

		if (nodeExists((String[]) getLicenseIdentifiers(licencesArray).toArray())) {
			throw new LicenseNodeAlreadyExistsException(nodeIdentifier + " already exists in the system.");
		}
		this.setNodeIdentifier(nodeIdentifier);
		licenses = new HashSet<License>();
		for (License l : licencesArray) {
			licenses.add(l);
		}
		graph.getLicenseNodes().add(this);
	}

	public LicenseNode(LicenseGraph graph, String nodeIdentifier, String... licencesArray) {
		this.graph = graph;

		if (nodeExists(licencesArray)) {
			throw new LicenseNodeAlreadyExistsException(nodeIdentifier + " already exists in the system.");
		}
		this.setNodeIdentifier(nodeIdentifier);
		licenses = new HashSet<License>();
		for (String l : licencesArray) {
			License license = LicenseRegistry.getInstance().findLicense(l);
			if (license == null) {
				try {
					licenses.add(LicenseRegistry.getInstance().registerLicense(l, l));
				} catch (LicenseAlreadyExistsException e) {
					e.printStackTrace();
				}
			} else {
				licenses.add(license);
			}
		}
		graph.getLicenseNodes().add(this);
	}

	public boolean containsLicense(String licenseIdentifier) {
		License license = LicenseRegistry.getInstance().findLicense(licenseIdentifier);

		if (license == null) {
			return false;
		} else if (licenses.contains(license)) {
			return true;
		}

		return false;
	}

	public boolean equals(LicenseNode ln) {
		Comparator<LicenseNode> cmp = new Comparator<LicenseNode>() {
			@Override
			public int compare(LicenseNode o1, LicenseNode o2) {
				for (License l1 : o1.getLicenses()) {
					if (!o2.containsLicense(l1.getIdentifier())) {
						return -1;
					}
				}
				return 0;
			}
		};
		if (cmp.compare(this, ln) == 0) {
			return true;
		}
		return false;
	}

	public LicenseNode findLicenseNode(String nodeIdentifier) {
		return graph.findLicenseNode(nodeIdentifier);
	}

	public ArrayList<String> getLicenseIdentifiers(License... licencesArray) {
		ArrayList<String> identifiers = new ArrayList<String>();
		for (License license : licencesArray) {
			identifiers.add(license.getIdentifier());
		}
		return identifiers;
	}

	public HashSet<LicenseNode> getLicenseNodes() {
		return graph.getLicenseNodes();
	}

	public HashSet<License> getLicenses() {
		return licenses;
	}

	public String getNodeIdentifier() {
		return nodeIdentifier;
	}

	public boolean nodeExists(String... lIdentifiers) {
		return graph.nodeExists(lIdentifiers);
	}

	public void setNodeIdentifier(String nodeIdentifier) {
		this.nodeIdentifier = nodeIdentifier;
	}

	@Override
	public String toString() {
		return new ArrayList<License>(this.licenses).toString();
	}
}
