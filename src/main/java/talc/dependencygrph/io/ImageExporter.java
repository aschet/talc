// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.dependencygrph.io;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jgrapht.ext.JGraphXAdapter;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.util.mxCellRenderer;

import talc.dependencygraph.Component;
import talc.dependencygraph.Connector;
import talc.dependencygraph.DependencyGraph;

public class ImageExporter {
	private JGraphXAdapter<Component, Connector> graph;

	public ImageExporter(DependencyGraph graph) {
		this.graph = new JGraphXAdapter<>(graph);
		mxHierarchicalLayout layout = new mxHierarchicalLayout(this.graph);
		layout.execute(this.graph.getDefaultParent());
	}

	public BufferedImage export() {
		return mxCellRenderer.createBufferedImage(graph, null, 1.0, Color.WHITE, true, null);
	}

	public void export(String fileName, String format) throws IOException {
		BufferedImage image = export();
		ImageIO.write(image, format, new File(fileName));
	}
}
