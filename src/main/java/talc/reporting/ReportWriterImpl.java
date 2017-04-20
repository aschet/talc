// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.reporting;

import java.util.List;
import java.util.Map;

import talc.analysis.result.ArchitectureComplianceInformation;
import talc.analysis.result.ComponentComplianceInformation;
import talc.analysis.result.LicenseComplianceConflict;
import talc.analysis.result.LicenseComplianceInformation;
import talc.license.obligation.Obligation;
import talc.license.obligation.ObligationSet;

public class ReportWriterImpl implements ReportWriter {

	private ReportWriterBackend backend;

	int headerLevel;

	ArchitectureComplianceInformation reportData;

	public ReportWriterImpl(ReportWriterBackend backend) {
		this.backend = backend;
	}

	private String getComplianceStatusText(boolean status) {
		if (status) {
			return "compliant";
		} else {
			return "non compliant";
		}
	}

	@Override
	public void write(ArchitectureComplianceInformation reportData) {
		this.reportData = reportData;

		headerLevel = 1;

		backend.beginDocument();
		backend.writeHeader(headerLevel, "Free Software Compliance Report");
		++headerLevel;

		writeSummary();
		writeComponents();
		writeObligations();

		backend.endDocument();
	}

	private void writeComponent(ComponentComplianceInformation component) {
		backend.writeHeader(headerLevel, component.getName());
		backend.writeText("Primary Licenses:");
		writeLicenseList(component.getPrimaryLicenses());
		backend.writeText("Contained Licenses:");
		writeLicenseList(component.getContainedLicenses());
		if (!component.isCompliant()) {
			backend.writeText("License Conflicts:");
			backend.beginList();
			for (LicenseComplianceConflict conflict : component.getLicenseConflicts()) {
				backend.writeListEntry(conflict.toString());
			}
			backend.endList();
		}
	}

	private void writeComponents() {
		for (ComponentComplianceInformation component : reportData.getComponents()) {
			writeComponent(component);
		}
	}

	private void writeLicenseList(List<LicenseComplianceInformation> licenses) {
		backend.beginList();
		for (LicenseComplianceInformation license : licenses) {
			backend.writeListEntry(license.toString());
		}
		backend.endList();
	}

	private void writeObligations() {
		backend.writeHeader(headerLevel, "License Obligations");

		for (Map.Entry<String, ObligationSet> entry : reportData.getObligations().entrySet()) {
			backend.writeText(entry.getKey() + ":");
			backend.beginList();
			for (Obligation obligation : entry.getValue()) {
				backend.writeListEntry(obligation.getText());
			}
			backend.endList();
		}

	}

	private void writeSummary() {
		backend.beginParagraph();
		backend.writeText("Overall compliance of architecture: " + getComplianceStatusText(reportData.isCompliant()));
		backend.endParagraph();
		backend.writeImage(reportData.getVisualisation());

		backend.beginList();
		for (ComponentComplianceInformation item : reportData.getComponents()) {
			backend.writeListEntry(item.getName() + ": " + getComplianceStatusText(item.isCompliant()));
		}
		backend.endList();
	}

}
