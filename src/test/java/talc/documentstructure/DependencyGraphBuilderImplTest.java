// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.documentstructure;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.traverse.BreadthFirstIterator;
import org.junit.Test;
import org.spdx.rdfparser.InvalidSPDXAnalysisException;

import talc.TestWithSpdxDocument;
import talc.dependencygraph.Component;
import talc.dependencygraph.Connector;
import talc.dependencygraph.DependencyGraph;

public class DependencyGraphBuilderImplTest extends TestWithSpdxDocument {
	@Test
	public void testBuild() throws IOException, InvalidSPDXAnalysisException, URISyntaxException {
		List<String> expectedIDs = new ArrayList<String>();
		expectedIDs.add("SPDXRef-APP");
		expectedIDs.add("SPDXRef-LIB_1");
		expectedIDs.add("SPDXRef-LIB_1_1");
		expectedIDs.add("SPDXRef-LIB_2");
		expectedIDs.add("SPDXRef-LIB_2_1");
		expectedIDs.add("SPDXRef-LIB_2_2");
		expectedIDs.add("SPDXRef-LIB_3");

		Collections.sort(expectedIDs);

		DependencyGraphBuilder graphBuilder = new DependencyGraphBuilderImpl(document);
		DependencyGraph graph = graphBuilder.build();
		BreadthFirstIterator<Component, Connector> graphIterator = new BreadthFirstIterator<>(graph);

		List<String> actualIDs = new ArrayList<String>();
		while (graphIterator.hasNext()) {
			Component current = graphIterator.next();
			actualIDs.add(current.getID());
		}
		Collections.sort(actualIDs);

		assertEquals(expectedIDs, actualIDs);
	}
}
