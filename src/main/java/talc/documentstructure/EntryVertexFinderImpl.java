// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.documentstructure;

import java.util.ArrayList;
import java.util.List;

import org.spdx.rdfparser.model.Relationship;
import org.spdx.rdfparser.model.SpdxDocument;
import org.spdx.rdfparser.model.SpdxElement;
import org.spdx.rdfparser.model.SpdxItem;

public class EntryVertexFinderImpl extends DocumentStructureVisitor implements EntryVertexFinder {

	private SpdxItem[] documentItems;

	private List<String> entryVertexIDs = new ArrayList<String>();

	@Override
	public List<SpdxItem> find(SpdxDocument document) {
		entryVertexIDs.clear();

		visit(document);

		List<SpdxItem> entryVertices = new ArrayList<SpdxItem>();
		for (SpdxItem item : documentItems) {
			if (entryVertexIDs.contains(item.getId())) {
				entryVertices.add(item);
			}
		}

		return entryVertices;
	}

	@Override
	public void visit(SpdxElement item, Relationship relationship) {
		if (isLinkingRelationship(relationship) || isContainsRelationship(relationship)) {
			entryVertexIDs.remove(relationship.getRelatedSpdxElement().getId());
			super.visit(item, relationship);
		}
	}

	@Override
	public void visit(SpdxItem[] items) {
		documentItems = items;

		for (SpdxItem item : items) {
			entryVertexIDs.add(item.getId());
		}

		super.visit(items);
	}
}
