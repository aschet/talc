// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.license;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.spdx.rdfparser.license.AnyLicenseInfo;
import org.spdx.rdfparser.license.LicenseInfoFactory;
import org.spdx.spdxspreadsheet.InvalidLicenseStringException;

import talc.TestWithSpdxDocument;

public class AnyLicenseInfoOperationsImplTest extends TestWithSpdxDocument {

	AnyLicenseInfo info;

	@Before
	public void setUp() throws InvalidLicenseStringException {
		info = LicenseInfoFactory.parseSPDXLicenseString(
				"(((LGPL-3.0+ OR MIT) AND GPL-2.0) OR BSD-3-Clause WITH DigiRule-FOSS-exception)");
	}

	@Test
	public void testFiltering() {
		String expectedExpression = "(MIT AND GPL-2.0)";

		AnyLicenseInfoOperations ops = new AnyLicenseInfoOperationsImpl();
		List<String> licenseIDs = new ArrayList<>();
		licenseIDs.add("LGPL-3.0");
		licenseIDs.add("BSD-3-Clause");

		info = ops.filtered(info, licenseIDs, true);

		String actualExpression = info.toString();
		assertEquals(expectedExpression, actualExpression);
	}

	@Test
	public void testGetNameFromID() {
		AnyLicenseInfoOperations ops = new AnyLicenseInfoOperationsImpl();

		assertEquals("GNU General Public License v3.0 or later", ops.getLicenseName("GPL-3.0+", document));
		assertEquals("Ashsoft Proprietary Software License", ops.getLicenseName("LicenseRef-Proprietary", document));
	}

	@Test
	public void testIDExtraction() {
		List<String> expectedLicenseIDs = new ArrayList<>();
		expectedLicenseIDs.add("BSD-3-Clause");
		expectedLicenseIDs.add("LGPL-3.0");
		expectedLicenseIDs.add("MIT");
		expectedLicenseIDs.add("GPL-2.0");

		AnyLicenseInfoOperations ops = new AnyLicenseInfoOperationsImpl();
		List<String> actialLicenseIDs = ops.getContainedLicenses(info, true);

		assertEquals(expectedLicenseIDs, actialLicenseIDs);
	}
}
