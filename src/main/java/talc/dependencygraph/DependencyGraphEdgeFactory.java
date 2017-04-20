// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.dependencygraph;

import org.jgrapht.EdgeFactory;

public class DependencyGraphEdgeFactory implements EdgeFactory<Component, Connector> {

	@Override
	public Connector createEdge(Component sourceVertex, Component targetVertex) {
		ConnectorType connectorType = sourceVertex.getConnectorTypeTo(targetVertex);

		if (connectorType == ConnectorType.DYNAMIC_LINK) {
			return new DynamicLinkConnector();
		} else if (connectorType == ConnectorType.STATIC_LINK) {
			return new StaticLinkConnector();
		}

		throw new UnknownConnectorTypeException();
	}
}
