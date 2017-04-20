// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.reporting;

import java.awt.image.BufferedImage;

public class ConsoleReportWriterBackend implements ReportWriterBackend {

	@Override
	public void beginDocument() {
		System.out.println();
	}

	@Override
	public void beginList() {
		System.out.println();
	}

	@Override
	public void beginParagraph() {
		System.out.println();
	}

	@Override
	public void breakLine() {
		System.out.println();
	}

	@Override
	public void endDocument() {
	}

	@Override
	public void endList() {
		System.out.println();
	}

	@Override
	public void endParagraph() {
		System.out.println();
	}

	@Override
	public void writeHeader(int level, String text) {
		String separator = (level < 2) ? "=" : "-";

		System.out.println(text);
		for (int i = 0; i < text.length(); ++i) {
			System.out.print(separator);
		}
		System.out.println();
		System.out.println();
	}

	@Override
	public void writeImage(BufferedImage image) {
	}

	@Override
	public void writeListEntry(String text) {
		System.out.println("* " + text);
	}

	@Override
	public void writeText(String text) {
		System.out.print(text);
	}

}
