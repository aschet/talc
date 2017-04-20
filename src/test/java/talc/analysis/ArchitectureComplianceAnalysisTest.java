// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.analysis;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.spdx.rdfparser.InvalidSPDXAnalysisException;

import talc.TestUtils;
import talc.analysis.result.ArchitectureComplianceInformation;
import talc.reporting.SimpleHTMLReportWriter;

public class ArchitectureComplianceAnalysisTest {

	@Test
	public void testCorrectAnalysisFromFile() throws IOException, InvalidSPDXAnalysisException, URISyntaxException {
		ArchitectureComplianceAnalysis analysis = new ArchitectureComplianceAnalysis();
		ArchitectureComplianceInformation infos = analysis.analyse(TestUtils.getTestFileName("correct.rdf"));
		assertEquals(true, infos.isCompliant());
		assertEquals(7, infos.getComponents().size());
		assertEquals(4, infos.getObligations().size());

		SimpleHTMLReportWriter reportWriter = new SimpleHTMLReportWriter("correct.rdf.html");
		reportWriter.write(infos);
	}

	@Test
	public void testIncorrectAnalysisFromFile() throws IOException, InvalidSPDXAnalysisException, URISyntaxException {
		ArchitectureComplianceAnalysis analysis = new ArchitectureComplianceAnalysis();
		ArchitectureComplianceInformation infos = analysis.analyse(TestUtils.getTestFileName("incorrect.rdf"));
		assertEquals(false, infos.isCompliant());
		assertEquals(7, infos.getComponents().size());
		assertEquals(6, infos.getObligations().size());

		SimpleHTMLReportWriter reportWriter = new SimpleHTMLReportWriter("incorrect.rdf.html");
		reportWriter.write(infos);
	}
}
