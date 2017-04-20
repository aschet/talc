// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.analysis;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.spdx.rdfparser.InvalidSPDXAnalysisException;

import ac.ucy.cs.spdx.compatibility.SpdxLicensePairConflictError;
import ac.ucy.cs.spdx.parser.CaptureLicense;
import talc.TestWidthDependencyGraph;
import talc.dependencygraph.StaticLinkConnector;
import talc.license.ComponentLicenseInfo;

public class CaptureLicenseAdapterTest extends TestWidthDependencyGraph {

	private CaptureLicense adapter;

	@Override
	@Before
	public void setUp() {
		super.setUp();

		ComponentLicenseInfo infoLib1 = graph.getVertexByID("SPDXRef-LIB_1").extractLicenseInfo();
		ComponentLicenseInfo infoLib1_1 = graph.getVertexByID("SPDXRef-LIB_1_1").extractLicenseInfo();
		infoLib1.mergeWith(infoLib1_1, new StaticLinkConnector());
		adapter = new CaptureLicenseAdapter(document, infoLib1);
	}

	@Test
	public void testAdapter() throws InvalidSPDXAnalysisException {
		assertEquals("LGPL-3.0+", adapter.getConcludedLicense().toString());
		assertEquals("[LGPL-3.0+, BSD-2-Clause-FreeBSD]", adapter.getExtractedLicensesList().toString());
		assertEquals("LIB_1", adapter.getFileName());

		HashMap<String, String> referencedLicenses = adapter.getReferencedLicenses();
		assertEquals(2, referencedLicenses.size());
	}

	@Test
	public void testAdapterIntegration() throws InvalidSPDXAnalysisException {
		SpdxLicensePairConflictError analysis = new SpdxLicensePairConflictError(adapter);
		assertEquals(true, analysis.areCompatible());
	}
}
