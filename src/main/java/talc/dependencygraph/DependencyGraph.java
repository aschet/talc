// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.dependencygraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.traverse.TopologicalOrderIterator;

public class DependencyGraph extends DefaultDirectedGraph<Component, Connector> {

	private static final long serialVersionUID = -5990367891331342556L;

	static List<String> getIDsFromVertices(List<Component> vertices) {
		List<String> ids = new ArrayList<>();
		for (Component component : vertices) {
			ids.add(component.getID());
		}
		return ids;
	}

	public DependencyGraph() {
		super(new DependencyGraphEdgeFactory());
	}

	public DependencyGraph(EdgeFactory<Component, Connector> factory) {
		super(factory);
	}

	public Component getVertexByID(String componentID) {
		for (Component existingComponent : vertexSet()) {
			if (existingComponent.getID().equals(componentID)) {
				return existingComponent;
			}
		}

		return null;
	}

	public List<Component> getVerticesOrderedByDependency() {
		// NOTE: topological sorting only works for non circular graphs

		List<Component> components = new ArrayList<>();

		TopologicalOrderIterator<Component, Connector> iterator = new TopologicalOrderIterator<>(this);
		while (iterator.hasNext()) {
			components.add(iterator.next());
		}

		return components;
	}

	public List<Component> getVerticesOrderedByInversedDependency() {
		List<Component> components = getVerticesOrderedByDependency();
		Collections.reverse(components);
		return components;
	}
}
