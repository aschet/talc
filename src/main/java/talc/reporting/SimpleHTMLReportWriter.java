// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.reporting;

public class SimpleHTMLReportWriter extends ReportWriterImpl {

	public SimpleHTMLReportWriter(String fileName) {
		super(new SimpleHTMLReportWriterBackend(fileName));
	}
}
