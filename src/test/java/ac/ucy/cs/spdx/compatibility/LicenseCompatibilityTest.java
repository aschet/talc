// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package ac.ucy.cs.spdx.compatibility;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.jgrapht.alg.util.Pair;
import org.junit.Before;
import org.junit.Test;

public class LicenseCompatibilityTest {

	LicenseCompatibility compat;

	@Before
	public void setUp() {
		compat = new LicenseCompatibility();
	}

	@Test
	public void testCompatibleLicenseCombination() {
		assertEquals(true, compat.areCompatible("LGPL-3.0", "LGPL-2.1+"));
	}

	@Test
	public void testIncompatibleLicenseCombination() {
		assertEquals(false, compat.areCompatible("GPL-3.0", "Libpng", "CDDL-1.0"));
	}

	@Test
	public void testIncompatiblePairs() {
		List<Pair<String, String>> incompatiblePairs = compat.getIncompatiblePairs("Proprietary", "LGPL-3.0",
				"GPL-3.0");
		assertEquals(1, incompatiblePairs.size());
	}

	@Test
	public void testPropositionWithCompatibleCombination() {
		assertEquals("[AGPL-3.0, BSD-2-Clause-FreeBSD, BSD-3-Clause, GPL-3.0, GPL-3.0+, MIT, MPL-2.0, X11]",
				compat.proposeLicense("LGPL-2.1", "Apache-2.0").toString());
	}

	@Test
	public void testPropositionWithIncompatibleCombination() {
		assertEquals("[]", compat.proposeLicense("LGPL-2.1", "OSL-3.0", "Artistic-2.0").toString());
	}

	@Test
	public void testProprietaryLicenseMapping() {
		assertEquals(true, compat.areCompatible("Test", "MIT"));
		assertEquals(true, compat.areCompatible("Test", "LGPL-2.1"));
		assertEquals(false, compat.areCompatible("Test", "GPL-2.0"));
	}
}
