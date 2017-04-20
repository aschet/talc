// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.documentstructure;

import org.spdx.rdfparser.InvalidSPDXAnalysisException;
import org.spdx.rdfparser.model.Relationship;
import org.spdx.rdfparser.model.SpdxDocument;
import org.spdx.rdfparser.model.SpdxElement;
import org.spdx.rdfparser.model.SpdxItem;

public class DocumentStructureVisitor {

	public boolean isContainsRelationship(Relationship relationship) {
		return relationship.getRelationshipType() == Relationship.RelationshipType.CONTAINS;
	}

	public boolean isLinkingRelationship(Relationship relationship) {
		return relationship.getRelationshipType() == Relationship.RelationshipType.DYNAMIC_LINK
				|| relationship.getRelationshipType() == Relationship.RelationshipType.STATIC_LINK;
	}

	public void visit(SpdxDocument document) {
		SpdxItem[] documentItems = null;
		try {
			documentItems = document.getDocumentDescribes();
		} catch (InvalidSPDXAnalysisException e) {
			return;
		}

		visit(documentItems);
	}

	public void visit(SpdxElement item) {
		for (Relationship relationship : item.getRelationships()) {
			if (isContainsRelationship(relationship)) {
				visit(item, relationship);
			}
		}

		for (Relationship relationship : item.getRelationships()) {
			if (!isContainsRelationship(relationship)) {
				visit(item, relationship);
			}
		}
	}

	public void visit(SpdxElement item, Relationship relationship) {
		visit(relationship.getRelatedSpdxElement());
	}

	public void visit(SpdxItem[] items) {
		for (SpdxItem item : items) {
			visit(item);
		}
	}
}
