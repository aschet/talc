// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.spdx.rdfparser.InvalidSPDXAnalysisException;
import org.spdx.rdfparser.model.SpdxDocument;

public class TestWithSpdxDocument {

	protected static SpdxDocument document;

	@BeforeClass
	public static void setUpClass() throws IOException, InvalidSPDXAnalysisException, URISyntaxException {
		document = TestUtils.readTestSpdxDocument("correct.rdf");
	}

	@AfterClass
	public static void tearDownClass() {
		document = null;
	}
}
