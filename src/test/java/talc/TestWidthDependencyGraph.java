// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc;

import org.junit.Before;

import talc.dependencygraph.DependencyGraph;
import talc.documentstructure.DependencyGraphBuilder;
import talc.documentstructure.DependencyGraphBuilderImpl;

public class TestWidthDependencyGraph extends TestWithSpdxDocument {
	protected DependencyGraph graph;

	@Before
	public void setUp() {
		DependencyGraphBuilder graphBuilder = new DependencyGraphBuilderImpl(document);
		graph = graphBuilder.build();
	}
}
