// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.analysis;

import java.util.ArrayList;
import java.util.List;

import talc.dependencygraph.Connector;
import talc.dependencygraph.DependencyGraph;
import talc.license.ComponentLicenseInfo;

public class DependencyGraphLicenseInfoMergerSpy extends DependencyGraphLicenseInfoMerger {

	List<String> mergeOperations = new ArrayList<>();

	public DependencyGraphLicenseInfoMergerSpy(DependencyGraph graph) {
		super(graph);
	}

	public List<String> getMergeOperations() {
		return mergeOperations;
	}

	@Override
	void mergeLicenseInfo(ComponentLicenseInfo licenseInfo, ComponentLicenseInfo dependencyLicenseInfo,
			Connector connector) {
		String sourceID = graph.getEdgeSource(connector).getID();
		String targetID = graph.getEdgeTarget(connector).getID();
		mergeOperations.add(targetID + "->" + sourceID);

		super.mergeLicenseInfo(licenseInfo, dependencyLicenseInfo, connector);
	}
}
