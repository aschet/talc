// Copyright 2016 Demetris Paschalides
// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-2.0+

package ac.ucy.cs.spdx.dot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;

public class DotFile {
	public String content;
	private ArrayList<DotEdge> edgeIdentifiers;
	private HashMap<Integer, String> nodeIdentifiers;
	private HashMap<String, String[]> nodeLicenseContent;
	public String path;

	public DotFile() throws IOException {
		InputStream stream = ClassLoader.class.getResourceAsStream("/graph.dot");
		initFromContent(IOUtils.toString(stream, "UTF-8"));
	}

	public DotFile(String path) throws IOException {
		this.path = path;
		initFromContent(new String(Files.readAllBytes(new File(path).toPath())));
	}

	public String getContent() {
		return content;
	}

	public ArrayList<DotEdge> getEdgeIdentifiers() {
		return edgeIdentifiers;
	}

	public HashMap<Integer, String> getNodeIdentifiers() {
		return nodeIdentifiers;
	}

	public HashMap<String, String[]> getNodeLicenseContent() {
		return nodeLicenseContent;
	}

	public String[] getNodeLicenses(String nodeIdentifier) {
		return this.nodeLicenseContent.get(nodeIdentifier);
	}

	public String getPath() {
		return path;
	}

	public void initFromContent(String content) {
		this.content = content;
		this.nodeIdentifiers = new HashMap<Integer, String>();
		this.nodeLicenseContent = new HashMap<String, String[]>();
		this.edgeIdentifiers = new ArrayList<DotEdge>();

		String[] dotLines = this.content.split("\\n");

		for (String line : dotLines) {

			int nodeStart = line.indexOf("\"[");

			if (nodeStart >= 0) {
				nodeStart += 2;

				int nodeEnd = line.indexOf("]\"");
				String nodeLicenses = (String) line.subSequence(nodeStart, nodeEnd);

				String[] lIdentifiers = nodeLicenses.split(", ");

				StringBuilder nodeIdentifier = new StringBuilder();
				for (String li : lIdentifiers) {
					nodeIdentifier.append(li + "_");
				}

				nodeIdentifier.deleteCharAt(nodeIdentifier.length() - 1);

				String nodeId = line.substring(0, line.indexOf('[')).trim();

				this.nodeIdentifiers.put(Integer.parseInt(nodeId), nodeIdentifier.toString());
				this.nodeLicenseContent.put(nodeIdentifier.toString(), lIdentifiers);

			} else if (line.contains("->")) {

				int from, to;
				boolean isTransitive = false;
				String fromIdentifier, toIdentifier;

				line = line.trim();

				if (line.charAt(line.indexOf('"') + 1) == 'b') {
					isTransitive = true;
				}

				line = line.substring(0, line.indexOf('['));
				String[] edgeNodes = line.split("->");

				from = Integer.parseInt(edgeNodes[0].trim());
				to = Integer.parseInt(edgeNodes[1].trim());
				fromIdentifier = nodeIdentifiers.get(from);
				toIdentifier = nodeIdentifiers.get(to);

				this.edgeIdentifiers.add(new DotEdge(fromIdentifier, toIdentifier, isTransitive));

			}
		}
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setEdgeIdentifiers(ArrayList<DotEdge> edgeIdentifiers) {
		this.edgeIdentifiers = edgeIdentifiers;
	}

	public void setNodeIdentifiers(HashMap<Integer, String> nodeIdentifiers) {
		this.nodeIdentifiers = nodeIdentifiers;
	}

	public void setNodeLicenseContent(HashMap<String, String[]> nodeLicenseContent) {
		this.nodeLicenseContent = nodeLicenseContent;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
