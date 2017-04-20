// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.reporting;

public class ReportGenerationException extends RuntimeException {

	private static final long serialVersionUID = -1721548953825093261L;

	public ReportGenerationException(Exception e) {
		super(e);
	}
}
