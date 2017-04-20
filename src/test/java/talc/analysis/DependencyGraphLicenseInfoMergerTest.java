// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.analysis;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import talc.TestWidthDependencyGraph;

public class DependencyGraphLicenseInfoMergerTest extends TestWidthDependencyGraph {

	@Test
	public void testLicenseMerging() {
		List<String> expectedMerges = new ArrayList<>();
		expectedMerges.add("SPDXRef-LIB_2_2->SPDXRef-LIB_2");
		expectedMerges.add("SPDXRef-LIB_2_1->SPDXRef-LIB_2");
		expectedMerges.add("SPDXRef-LIB_2_2->SPDXRef-LIB_3");
		expectedMerges.add("SPDXRef-LIB_1_1->SPDXRef-LIB_1");
		expectedMerges.add("SPDXRef-LIB_1->SPDXRef-APP");
		expectedMerges.add("SPDXRef-LIB_3->SPDXRef-APP");
		expectedMerges.add("SPDXRef-LIB_2->SPDXRef-APP");
		Collections.sort(expectedMerges);

		DependencyGraphLicenseInfoMergerSpy merger = new DependencyGraphLicenseInfoMergerSpy(graph);
		merger.merge();

		List<String> actualMerges = merger.getMergeOperations();
		Collections.sort(actualMerges);

		assertEquals(expectedMerges, actualMerges);
	}

}
