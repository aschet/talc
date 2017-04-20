// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc;

import talc.analysis.ArchitectureComplianceAnalysis;
import talc.analysis.result.ArchitectureComplianceInformation;
import talc.reporting.ConsoleReportWriterBackend;
import talc.reporting.MultiplexingReportWriterBackend;
import talc.reporting.ReportWriter;
import talc.reporting.ReportWriterImpl;
import talc.reporting.SimpleHTMLReportWriterBackend;

public class Application {

	public static void main(String[] args) {
		Application app = new Application();
		app.doMain(args);
	}

	public void doMain(String[] args) {
		if (args.length < 1) {
			System.out.println("Tool Assisted License Compliance (TALC)");
			System.out.println("Usage: <SPDX RDF Document>");
			return;
		}

		String spdxFile = args[0];

		try {
			ArchitectureComplianceAnalysis analysis = new ArchitectureComplianceAnalysis();
			ArchitectureComplianceInformation info = analysis.analyse(spdxFile);

			MultiplexingReportWriterBackend backends = new MultiplexingReportWriterBackend();
			backends.attachBackend(new ConsoleReportWriterBackend());
			backends.attachBackend(new SimpleHTMLReportWriterBackend(spdxFile + ".html"));
			ReportWriter writer = new ReportWriterImpl(backends);
			writer.write(info);

		} catch (Exception e) {
			System.out.println("The analysis process failed:");
			e.printStackTrace();
		}
	}
}
