// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.license;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.spdx.rdfparser.InvalidSPDXAnalysisException;
import org.spdx.rdfparser.model.SpdxDocument;

import talc.TestUtils;
import talc.dependencygraph.Connector;
import talc.dependencygraph.DependencyGraph;
import talc.dependencygraph.DynamicLinkConnector;
import talc.dependencygraph.StaticLinkConnector;
import talc.documentstructure.DependencyGraphBuilder;
import talc.documentstructure.DependencyGraphBuilderImpl;

public class ComponentLicenseInfoImplTest {

	private static SpdxDocument document;

	@BeforeClass
	public static void setUpClass() throws IOException, InvalidSPDXAnalysisException, URISyntaxException {
		document = TestUtils.readTestSpdxDocument("correct.rdf");
	}

	@AfterClass
	public static void tearDownClass() {
		document = null;
	}

	private DependencyGraph graph;

	@Before
	public void setUp() {
		DependencyGraphBuilder graphBuilder = new DependencyGraphBuilderImpl(document);
		graph = graphBuilder.build();
	}

	@Test
	public void testMergeWithDifferenctConnectors() {
		ComponentLicenseInfo appLicense = graph.getVertexByID("SPDXRef-APP").extractLicenseInfo();
		ComponentLicenseInfo lib1License = graph.getVertexByID("SPDXRef-LIB_1").extractLicenseInfo();

		assertEquals(1, appLicense.getContainedLicenses().size());

		Connector connectorDynamic = new DynamicLinkConnector();
		appLicense.mergeWith(lib1License, connectorDynamic);

		assertEquals(1, appLicense.getContainedLicenses().size());

		Connector connectorStatic = new StaticLinkConnector();
		appLicense.mergeWith(lib1License, connectorStatic);

		assertEquals(2, appLicense.getContainedLicenses().size());
	}

}
