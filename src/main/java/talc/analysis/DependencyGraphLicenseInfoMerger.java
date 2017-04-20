// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.analysis;

import java.util.ArrayList;
import java.util.HashMap;

import talc.dependencygraph.Component;
import talc.dependencygraph.Connector;
import talc.dependencygraph.DependencyGraph;
import talc.license.ComponentLicenseInfo;

public class DependencyGraphLicenseInfoMerger {
	DependencyGraph graph;

	HashMap<String, ComponentLicenseInfo> mergedLicenseInfos = new HashMap<>();

	public DependencyGraphLicenseInfoMerger(DependencyGraph graph) {
		this.graph = graph;
	}

	ComponentLicenseInfo extractLicenseInfo(Component component) {
		String componentID = component.getID();
		if (!mergedLicenseInfos.containsKey(componentID)) {
			mergedLicenseInfos.put(componentID, component.extractLicenseInfo());
		}

		return mergedLicenseInfos.get(componentID);
	}

	public ArrayList<ComponentLicenseInfo> merge() {
		mergedLicenseInfos.clear();

		for (Component currentComponent : graph.getVerticesOrderedByInversedDependency()) {
			ComponentLicenseInfo currentLicenseInfo = extractLicenseInfo(currentComponent);

			for (Connector currentConnector : graph.outgoingEdgesOf(currentComponent)) {
				Component componentDependency = graph.getEdgeTarget(currentConnector);
				mergeLicenseInfo(currentLicenseInfo, extractLicenseInfo(componentDependency), currentConnector);
			}
		}

		return new ArrayList<ComponentLicenseInfo>(mergedLicenseInfos.values());
	}

	void mergeLicenseInfo(ComponentLicenseInfo licenseInfo, ComponentLicenseInfo dependencyLicenseInfo,
			Connector connector) {
		licenseInfo.mergeWith(dependencyLicenseInfo, connector);
	}
}
