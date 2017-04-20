// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package ac.ucy.cs.spdx.compatibility;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.spdx.rdfparser.InvalidSPDXAnalysisException;
import org.spdx.spdxspreadsheet.InvalidLicenseStringException;

import ac.ucy.cs.spdx.parser.CaptureLicenseImpl;
import talc.TestUtils;

public class ViolationAnalysisInfoTest {

	@Test
	public void test()
			throws InvalidSPDXAnalysisException, InvalidLicenseStringException, IOException, URISyntaxException {
		CaptureLicenseImpl anomos = new CaptureLicenseImpl(TestUtils.getTestFileName("anomos.rdf"));
		CaptureLicenseImpl mply = new CaptureLicenseImpl(TestUtils.getTestFileName("mply.rdf"));

		SpdxLicensePairConflictError conflictAnomos = new SpdxLicensePairConflictError(anomos);
		SpdxLicensePairConflictError conflictMply = new SpdxLicensePairConflictError(mply);

		ViolationAnalysisInfo analysisInfo = new ViolationAnalysisInfo(conflictAnomos, conflictMply);
		assertEquals(
				"anomos has violations considering the declared licenses compatibility.\nThe violations are adjustable and the proposed licenses are:\nAGPL-3.0\n",
				analysisInfo.toString());
	}
}
