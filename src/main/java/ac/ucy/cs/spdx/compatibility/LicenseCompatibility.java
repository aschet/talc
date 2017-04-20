// Copyright 2016 Demetris Paschalides
// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-2.0+

package ac.ucy.cs.spdx.compatibility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.SimpleDirectedGraph;

import ac.ucy.cs.spdx.dot.DotFile;
import ac.ucy.cs.spdx.graph.LicenseEdge;
import ac.ucy.cs.spdx.graph.LicenseGraph;
import ac.ucy.cs.spdx.graph.LicenseNode;
import ac.ucy.cs.spdx.license.License;
import ac.ucy.cs.spdx.license.LicenseRegistry;

public class LicenseCompatibility {

	public static final String PROPRIETARY_LICENSE = "Proprietary";

	LicenseGraph licenseGraph;

	public LicenseCompatibility() {
		this.licenseGraph = new LicenseGraph();
		try {
			this.licenseGraph.initialize(new DotFile());
		} catch (IOException e) {
		}
	}

	public LicenseCompatibility(LicenseGraph licenseGraph) {
		this.licenseGraph = licenseGraph;
	}

	public boolean areCompatible(String... licenses) {
		for (String license1 : licenses) {
			for (String license2 : licenses) {
				if (!areCompatible(license1, license2)) {
					return false;
				}

			}
		}
		return true;
	}

	boolean areCompatible(String licenseIdentifier1, String licenseIdentifier2) {
		LicenseNode v1 = null;
		LicenseNode v2 = null;

		for (LicenseNode ln : licenseGraph.getLicenseNodes()) {
			if (ln.containsLicense(licenseIdentifier2)) {
				v2 = ln;
			}
			if (ln.containsLicense(licenseIdentifier1)) {
				v1 = ln;
			}
		}

		if (v1 == null) {
			v1 = licenseGraph.findLicenseNode(PROPRIETARY_LICENSE);
		}

		if (v2 == null) {
			v2 = licenseGraph.findLicenseNode(PROPRIETARY_LICENSE);
		}

		if (v1.equals(v2)) {
			return true;
		}

		Set<String> v1Visited = transitiveBfs(licenseGraph.licenseGraph, v1.getNodeIdentifier());
		Set<String> v2Visited = transitiveBfs(licenseGraph.licenseGraph, v2.getNodeIdentifier());
		if (v1Visited.contains(v2.getNodeIdentifier())) {
			return true;
		} else if (v2Visited.contains(v1.getNodeIdentifier())) {
			return true;
		}

		return false;
	}

	public Set<String> bfs(SimpleDirectedGraph<LicenseNode, LicenseEdge> graph, String startVertex) {
		Set<String> visitedNodes = new LinkedHashSet<String>();
		Queue<String> queue = new LinkedList<String>();
		queue.add(startVertex);
		visitedNodes.add(startVertex);
		while (!queue.isEmpty()) {
			LicenseNode node = licenseGraph.findLicenseNode(queue.remove());
			Iterator<LicenseEdge> children = graph.outgoingEdgesOf(node).iterator();
			while (children.hasNext()) {
				LicenseEdge edge = children.next();
				String nextNode = edge.getV2().getNodeIdentifier();
				if (!visitedNodes.contains(nextNode)) {
					visitedNodes.add(nextNode);
					queue.add(nextNode);
				}

			}
		}
		return visitedNodes;
	}

	public boolean checkEdgesForTransitive(List<LicenseEdge> edgeList) {
		for (int i = 0; i < edgeList.size() - 1; i++) {
			if (edgeList.get(i).isTransitive()) {
				return false;
			}
		}
		return true;
	}

	public List<Pair<String, String>> getIncompatiblePairs(String... licenses) {
		List<Pair<String, String>> pairs = new ArrayList<>();

		for (int i = 0; i < licenses.length; ++i) {
			for (int j = i + 1; j < licenses.length; ++j) {
				String first = licenses[i];
				String second = licenses[j];

				if (!areCompatible(first, second)) {
					pairs.add(new Pair<String, String>(first, second));
				}
			}
		}

		return pairs;
	}

	public List<License> proposeLicense(String... declared) {
		Set<String> proposals = new HashSet<String>();
		List<License> licensearray = new ArrayList<License>();
		LicenseNode v1 = null;

		int i = 0;
		for (String licenseIdentifier1 : declared) {
			for (LicenseNode ln : licenseGraph.getLicenseNodes()) {
				if (ln.containsLicense(licenseIdentifier1)) {
					v1 = ln;
				}
			}

			if (v1 == null) {
				v1 = licenseGraph.findLicenseNode(PROPRIETARY_LICENSE);
			}

			Set<String> v1Visited = transitiveBfs(licenseGraph.licenseGraph, v1.getNodeIdentifier());

			if (i == 0) {
				proposals.addAll(v1Visited);
			} else {

				proposals.retainAll(v1Visited);
			}

			if (proposals.isEmpty()) {
				return new ArrayList<License>();
			}
			i++;
		}

		proposals.removeAll(Arrays.asList(declared));
		proposals.remove(PROPRIETARY_LICENSE);
		for (String liString : proposals) {
			License license = LicenseRegistry.getInstance().findLicense(liString);
			if (license != null) {
				licensearray.add(license);
			}
		}

		return licensearray;
	}

	List<License> proposeLicense(String licenseIdentifier1, String licenseIdentifier2) {
		HashSet<License> proposals = new HashSet<License>();
		for (License l : LicenseRegistry.getInstance().getLicenses()) {
			if (areCompatible(licenseIdentifier1, l.getIdentifier())) {
				if (areCompatible(licenseIdentifier2, l.getIdentifier())) {
					if (!l.getIdentifier().equals(PROPRIETARY_LICENSE)) {
						proposals.add(l);
					}
				}
			}

		}

		List<License> proposedLicenses = new ArrayList<License>(proposals);
		Collections.sort(proposedLicenses);
		return proposedLicenses;
	}

	public Set<String> transitiveBfs(SimpleDirectedGraph<LicenseNode, LicenseEdge> graph, String startVertex) {
		Set<String> visitedNodes = new LinkedHashSet<String>();
		Set<String> visitedNonTransitiveNodes = new LinkedHashSet<String>();

		Queue<String> queue = new LinkedList<String>();
		queue.add(startVertex);
		visitedNodes.add(startVertex);
		while (!queue.isEmpty()) {
			LicenseNode node = licenseGraph.findLicenseNode(queue.remove());
			Iterator<LicenseEdge> children = graph.outgoingEdgesOf(node).iterator();
			while (children.hasNext()) {
				LicenseEdge edge = children.next();
				String nextNode = edge.getV2().getNodeIdentifier();
				if (!edge.isTransitive()) {
					visitedNonTransitiveNodes.add(nextNode);
				} else if (!visitedNodes.contains(nextNode)) {
					visitedNodes.add(nextNode);
					queue.add(nextNode);
				}

			}
		}
		visitedNodes.addAll(visitedNonTransitiveNodes);
		return visitedNodes;
	}
}
