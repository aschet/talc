// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.documentstructure;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Test;
import org.spdx.rdfparser.InvalidSPDXAnalysisException;
import org.spdx.rdfparser.model.SpdxItem;

import talc.TestWithSpdxDocument;

public class EntryItemFinderImplTest extends TestWithSpdxDocument {
	@Test
	public void testFind() throws IOException, InvalidSPDXAnalysisException, URISyntaxException {
		EntryVertexFinder finder = new EntryVertexFinderImpl();
		List<SpdxItem> items = finder.find(document);

		assertEquals(1, items.size());
		assertEquals("SPDXRef-APP", items.get(0).getId());
	}
}
