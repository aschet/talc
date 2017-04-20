// Copyright 2016 Demetris Paschalides
// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-2.0+

package ac.ucy.cs.spdx.graph;

import org.jgrapht.graph.DefaultEdge;

import ac.ucy.cs.spdx.exception.LicenseEdgeAlreadyExistsException;

/**
 * This is License Edge class. It is custom graph edge implementation that
 * extends standard jgraph's DefaultEdge. It will be used in the labeled license
 * graph to separate transitive edges with non-transitive ones by the use of a
 * boolean field nonTransitive.
 *
 * @author dpasch01
 * @version 1.2
 */
@SuppressWarnings("serial")
public class LicenseEdge extends DefaultEdge {
	private LicenseGraph graph;
	private boolean nonTransitive;
	private LicenseNode v1;
	private LicenseNode v2;

	public LicenseEdge(LicenseGraph graph, LicenseNode v1, LicenseNode v2, boolean nonTransitive) {
		this.graph = graph;

		if (edgeExists(v1.getNodeIdentifier(), v2.getNodeIdentifier())) {
			throw new LicenseEdgeAlreadyExistsException(v1 + " -> " + v2 + " already exists in the system.");
		}
		this.setV1(v1);
		this.setV2(v2);
		this.setTransitive(nonTransitive);
		graph.getLicenseEdges().add(this);
	}

	public LicenseEdge(LicenseGraph graph, String v1, String v2, boolean nonTransitive)
			throws LicenseEdgeAlreadyExistsException {
		this.graph = graph;

		if (edgeExists(v1, v2)) {
			throw new LicenseEdgeAlreadyExistsException(v1 + " -> " + v2 + " already exists in the system.");
		}
		this.setV1(graph.findLicenseNode(v1));
		this.setV2(graph.findLicenseNode(v2));
		this.setTransitive(nonTransitive);
		graph.getLicenseEdges().add(this);
	}

	private boolean edgeExists(String from, String to) {
		return graph.edgeExists(from, to);
	}

	public LicenseNode getV1() {
		return v1;
	}

	public LicenseNode getV2() {
		return v2;
	}

	public boolean isTransitive() {
		return nonTransitive;
	}

	public void setTransitive(boolean transitive) {
		this.nonTransitive = transitive;
	}

	public void setV1(LicenseNode v1) {
		this.v1 = v1;
	}

	public void setV2(LicenseNode v2) {
		this.v2 = v2;
	}

	@Override
	public String toString() {
		return this.v1 + " -> " + this.v2;
	}
}
