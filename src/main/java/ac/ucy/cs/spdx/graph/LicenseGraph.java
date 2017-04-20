// Copyright 2016 Demetris Paschalides
// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-2.0+

package ac.ucy.cs.spdx.graph;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import org.jgrapht.ext.DOTExporter;
import org.jgrapht.graph.SimpleDirectedGraph;

import ac.ucy.cs.spdx.dot.DotEdge;
import ac.ucy.cs.spdx.dot.DotFile;
import ac.ucy.cs.spdx.exception.LicenseAlreadyExistsException;
import ac.ucy.cs.spdx.exception.LicenseEdgeAlreadyExistsException;
import ac.ucy.cs.spdx.exception.LicenseNodeAlreadyExistsException;
import ac.ucy.cs.spdx.license.LicenseRegistry;

public class LicenseGraph {

	private DotFile dotFile;
	private HashSet<LicenseEdge> licenseEdges = new HashSet<LicenseEdge>();
	public SimpleDirectedGraph<LicenseNode, LicenseEdge> licenseGraph = new SimpleDirectedGraph<LicenseNode, LicenseEdge>(
			LicenseEdge.class);

	private HashSet<LicenseNode> licenseNodes = new HashSet<LicenseNode>();

	private HashMap<String, LicenseNode> nodeIdentifiers = new HashMap<String, LicenseNode>();

	public void addLicenseNode(String nodeIdentifier, String... licenseIdentifiers)
			throws LicenseNodeAlreadyExistsException {

		if (nodeExists(licenseIdentifiers)) {
			throw new LicenseNodeAlreadyExistsException(nodeIdentifier + " already exists in the system.");
		}
		for (String lIdentifier : licenseIdentifiers) {
			if (!LicenseRegistry.getInstance().licenseExists(lIdentifier)) {
				try {
					LicenseRegistry.getInstance().registerLicense(lIdentifier, lIdentifier);
				} catch (LicenseAlreadyExistsException e) {
					e.printStackTrace();
				}
			}
		}

		LicenseNode ln = null;
		try {
			ln = new LicenseNode(this, nodeIdentifier, licenseIdentifiers);
		} catch (LicenseNodeAlreadyExistsException e) {
			e.printStackTrace();
		}
		nodeIdentifiers.put(nodeIdentifier, ln);
		licenseGraph.addVertex(ln);
	}

	public void connectNode(boolean isTransitive, String nodeIdentifier, String... nodeIdentifiers)
			throws LicenseEdgeAlreadyExistsException {

		LicenseNode ln = findLicenseNode(nodeIdentifier);
		for (int i = 0; i < nodeIdentifiers.length; i++) {
			if (edgeExists(nodeIdentifier, nodeIdentifiers[i])) {
				throw new LicenseEdgeAlreadyExistsException(
						nodeIdentifier + " -> " + nodeIdentifiers[i] + " already exists in the system.");
			}
			LicenseEdge licenseEdge = null;
			try {
				licenseEdge = new LicenseEdge(this, ln, findLicenseNode(nodeIdentifiers[i]), isTransitive);
			} catch (LicenseEdgeAlreadyExistsException e) {
				e.printStackTrace();
			}

			licenseEdges.add(licenseEdge);
			licenseGraph.addEdge(ln, findLicenseNode(nodeIdentifiers[i]), licenseEdge);
		}
	}

	public void echoGraph() {
		for (LicenseEdge edge : licenseEdges) {
			System.out.println(edge);
		}
	}

	public boolean edgeExists(String from, String to) {
		boolean exists = false;
		for (LicenseEdge le : licenseEdges) {
			if (from.equals(le.getV1().getNodeIdentifier()) && to.equals(le.getV2().getNodeIdentifier())) {
				return true;
			}
		}
		return exists;
	}

	public LicenseNode findLicenseNode(String nodeIdentifier) {
		LicenseNode licenseNode = null;
		for (LicenseNode ln : getLicenseNodes()) {
			if (ln.getNodeIdentifier().equals(nodeIdentifier)) {
				licenseNode = ln;
			}
		}
		return licenseNode;
	}

	public HashSet<LicenseEdge> getLicenseEdges() {
		return licenseEdges;
	}

	public HashSet<LicenseNode> getLicenseNodes() {
		return licenseNodes;
	}

	public void initialize(DotFile dotFile) {
		this.dotFile = dotFile;

		for (String nodeIdentifier : dotFile.getNodeIdentifiers().values()) {
			LicenseNode licenseNode = null;
			try {
				licenseNode = new LicenseNode(this, nodeIdentifier, dotFile.getNodeLicenses(nodeIdentifier));
			} catch (LicenseNodeAlreadyExistsException e) {
				e.printStackTrace();
			}
			nodeIdentifiers.put(nodeIdentifier, licenseNode);
			licenseGraph.addVertex(licenseNode);
		}

		for (DotEdge dotEdge : dotFile.getEdgeIdentifiers()) {
			LicenseNode lnFrom = findLicenseNode(dotEdge.getFrom());
			LicenseNode lnTo = findLicenseNode(dotEdge.getTo());
			LicenseEdge ledge = null;
			try {
				ledge = new LicenseEdge(this, lnFrom, lnTo, dotEdge.isTransitive());
			} catch (LicenseEdgeAlreadyExistsException e) {
				e.printStackTrace();
			}
			licenseEdges.add(ledge);

			licenseGraph.addEdge(lnFrom, lnTo, ledge);
		}
	}

	public void initialize(String path) {

		try {
			dotFile = new DotFile(path);
		} catch (IOException e) {
			e.printStackTrace();
		}

		initialize(dotFile);
	}

	public boolean nodeExists(String... lIdentifiers) {
		boolean found = false;
		for (LicenseNode ln : getLicenseNodes()) {
			found = true;

			for (String li : lIdentifiers) {
				boolean e = ln.containsLicense(li);
				found = found && e;
			}
			if (found) {
				return found;
			}
		}

		return found;
	}

	public void setLicenseEdges(HashSet<LicenseEdge> licenseEdges) {
		this.licenseEdges = licenseEdges;
	}

	public void setLicenseNodes(HashSet<LicenseNode> licenseNodes) {
		this.licenseNodes = licenseNodes;
	}
}
