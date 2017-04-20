// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.reporting;

import talc.analysis.result.ArchitectureComplianceInformation;

public interface ReportWriter {
	void write(ArchitectureComplianceInformation reportData);
}
