// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.reporting;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;

public class SimpleHTMLReportWriterBackend implements ReportWriterBackend {

	String fileName;
	int imageCounter;
	PrintWriter writer;

	public SimpleHTMLReportWriterBackend(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void beginDocument() {
		try {
			writer = new PrintWriter(fileName, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			throw new ReportGenerationException(e);
		}

		writer.println(createStartTag("!DOCTYPE html"));
		writer.println(createStartTag("html lang=en"));
		writer.println(createStartTag("style"));
		writer.println("body {");
		writer.println("font-family: Arial, Helvetica, sans-serif;");
		writer.println("font-size: 0.75em;");
		writer.println("}");
		writer.println(createEndTag("style"));
		writer.println(createStartTag("body"));
	}

	@Override
	public void beginList() {
		writer.println(createStartTag("ul"));
	}

	@Override
	public void beginParagraph() {
		writer.println(createStartTag("p"));
	}

	@Override
	public void breakLine() {
		writer.println(createStartTag("br"));
	}

	String createEndTag(String tagText) {
		return "</" + tagText + ">";
	}

	String createStartTag(String tagText) {
		return "<" + tagText + ">";
	}

	String createTagEnclosed(String tagText, String text) {
		return createStartTag(tagText) + text + createEndTag(tagText);
	}

	@Override
	public void endDocument() {
		writer.println(createEndTag("body"));
		writer.println(createEndTag("html"));
		writer.close();
	}

	@Override
	public void endList() {
		writer.println(createEndTag("ul"));
	}

	@Override
	public void endParagraph() {
		writer.println(createEndTag("p"));
	}

	@Override
	public void writeHeader(int level, String text) {
		String tag = "h" + level;
		writer.println(createTagEnclosed(tag, text));

	}

	@Override
	public void writeImage(BufferedImage image) {
		String imageFileName = fileName + "." + imageCounter + ".png";

		File outputfile = new File(imageFileName);
		try {
			ImageIO.write(image, "png", outputfile);
		} catch (IOException e) {
			throw new ReportGenerationException(e);
		}

		String baseName = FilenameUtils.getName(imageFileName);
		writer.println("<img src=\"" + baseName + "\">");
		breakLine();
		++imageCounter;
	}

	@Override
	public void writeListEntry(String text) {
		writer.print(createStartTag("li") + text);
	}

	@Override
	public void writeText(String text) {
		writer.print(text);
	}
}
