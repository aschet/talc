// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.documentstructure;

import java.util.Stack;

import org.spdx.rdfparser.model.Relationship;
import org.spdx.rdfparser.model.SpdxDocument;
import org.spdx.rdfparser.model.SpdxElement;
import org.spdx.rdfparser.model.SpdxItem;

import talc.dependencygraph.Component;
import talc.dependencygraph.ComponentImpl;
import talc.dependencygraph.DependencyGraph;

public class DependencyGraphBuilderImpl extends DocumentStructureVisitor implements DependencyGraphBuilder {
	private SpdxDocument document;

	private EntryVertexFinder entryFinder = new EntryVertexFinderImpl();

	private DependencyGraph graph;

	boolean isNewComponent = false;

	private Stack<Component> scopes = new Stack<>();

	public DependencyGraphBuilderImpl(SpdxDocument document) {
		this.document = document;
	}

	@Override
	public DependencyGraph build() {
		graph = new DependencyGraph();

		for (SpdxItem entryItem : entryFinder.find(document)) {
			isNewComponent = true;
			visit(entryItem);
			scopes.pop();
		}

		return graph;
	}

	public Component createComponentFromItem(SpdxElement item) {
		return new ComponentImpl(item);
	}

	@Override
	public void visit(SpdxElement item) {
		if (isNewComponent) {
			Component component = graph.getVertexByID(item.getId());
			if (component == null) {
				component = createComponentFromItem(item);
				graph.addVertex(component);
			}
			scopes.push(component);
		} else {
			scopes.get(scopes.size() - 1).addContent(item);
		}
		super.visit(item);
	}

	@Override
	public void visit(SpdxElement item, Relationship relationship) {
		if (isLinkingRelationship(relationship)) {
			isNewComponent = true;
			visit(relationship.getRelatedSpdxElement());

			if (scopes.size() > 1) {
				Component source = scopes.get(scopes.size() - 2);
				Component target = scopes.get(scopes.size() - 1);
				graph.addEdge(source, target);
			}
			scopes.pop();
		} else if (isContainsRelationship(relationship)) {
			isNewComponent = false;
			visit(relationship.getRelatedSpdxElement());
		}
	}
}
