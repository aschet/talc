// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.dependencygraph;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;

import talc.TestWidthDependencyGraph;

public class DependencyGraphTest extends TestWidthDependencyGraph {

	@Test
	public void testDependencyEvaluation() {
		List<String> actualIDs = DependencyGraph.getIDsFromVertices(graph.getVerticesOrderedByInversedDependency());

		assertEquals(7, actualIDs.size());
		assertEquals("SPDXRef-APP", actualIDs.get(actualIDs.size() - 1));
	}

	@Test
	public void testFindByID() {
		graph = new DependencyGraph();

		Component mockedComponent = mock(Component.class);
		when(mockedComponent.getID()).thenReturn("23");
		graph.addVertex(mockedComponent);

		assertEquals(null, graph.getVertexByID("24"));
		assertEquals(mockedComponent, graph.getVertexByID("23"));
	}
}
