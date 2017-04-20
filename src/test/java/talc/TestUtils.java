// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc;

import java.io.IOException;
import java.net.URISyntaxException;

import org.spdx.rdfparser.InvalidSPDXAnalysisException;
import org.spdx.rdfparser.SPDXDocumentFactory;
import org.spdx.rdfparser.model.SpdxDocument;

import talc.dependencygraph.DependencyGraph;
import talc.documentstructure.DependencyGraphBuilder;
import talc.documentstructure.DependencyGraphBuilderImpl;

public class TestUtils {
	public static DependencyGraph buildDefaultDependencyGraph()
			throws IOException, InvalidSPDXAnalysisException, URISyntaxException {
		return buildDependencyGraph("correct.rdf");
	}

	public static DependencyGraph buildDependencyGraph(String baseName)
			throws IOException, InvalidSPDXAnalysisException, URISyntaxException {
		SpdxDocument document = readTestSpdxDocument(getTestFileName(baseName));
		DependencyGraphBuilder graphBuilder = new DependencyGraphBuilderImpl(document);
		return graphBuilder.build();
	}

	public static String getTestFileName(String baseName) throws URISyntaxException {
		return ClassLoader.getSystemResource(baseName).toURI().toString();
	}

	public static SpdxDocument readTestSpdxDocument(String baseName)
			throws IOException, InvalidSPDXAnalysisException, URISyntaxException {
		return SPDXDocumentFactory.createSpdxDocument(getTestFileName(baseName));
	}
}
