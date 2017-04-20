// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.reporting;

import java.awt.image.BufferedImage;

public interface ReportWriterBackend {
	void beginDocument();

	void beginList();

	void beginParagraph();

	void breakLine();

	void endDocument();

	void endList();

	void endParagraph();

	void writeHeader(int level, String text);

	void writeImage(BufferedImage image);

	void writeListEntry(String text);

	void writeText(String text);
}
