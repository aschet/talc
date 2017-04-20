// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.reporting;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MultiplexingReportWriterBackend implements ReportWriterBackend {

	private List<ReportWriterBackend> backends = new ArrayList<>();

	public void attachBackend(ReportWriterBackend backend) {
		backends.add(backend);
	}

	@Override
	public void beginDocument() {
		for (ReportWriterBackend backend : backends) {
			backend.beginDocument();
		}
	}

	@Override
	public void beginList() {
		for (ReportWriterBackend backend : backends) {
			backend.beginList();
		}
	}

	@Override
	public void beginParagraph() {
		for (ReportWriterBackend backend : backends) {
			backend.beginParagraph();
		}
	}

	@Override
	public void breakLine() {
		for (ReportWriterBackend backend : backends) {
			backend.breakLine();
		}
	}

	@Override
	public void endDocument() {
		for (ReportWriterBackend backend : backends) {
			backend.endDocument();
		}
	}

	@Override
	public void endList() {
		for (ReportWriterBackend backend : backends) {
			backend.endList();
		}
	}

	@Override
	public void endParagraph() {
		for (ReportWriterBackend backend : backends) {
			backend.endParagraph();
		}
	}

	@Override
	public void writeHeader(int level, String text) {
		for (ReportWriterBackend backend : backends) {
			backend.writeHeader(level, text);
		}
	}

	@Override
	public void writeImage(BufferedImage image) {
		for (ReportWriterBackend backend : backends) {
			backend.writeImage(image);
		}
	}

	@Override
	public void writeListEntry(String text) {
		for (ReportWriterBackend backend : backends) {
			backend.writeListEntry(text);
		}
	}

	@Override
	public void writeText(String text) {
		for (ReportWriterBackend backend : backends) {
			backend.writeText(text);
		}
	}

}
