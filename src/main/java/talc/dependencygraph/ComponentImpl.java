// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.dependencygraph;

import java.util.ArrayList;
import java.util.List;

import org.spdx.rdfparser.model.Relationship;
import org.spdx.rdfparser.model.Relationship.RelationshipType;
import org.spdx.rdfparser.model.SpdxElement;
import org.spdx.rdfparser.model.SpdxFile;
import org.spdx.rdfparser.model.SpdxPackage;

import talc.license.ComponentLicenseInfo;
import talc.license.ComponentLicenseInfoImpl;

public class ComponentImpl implements Component {

	private List<SpdxElement> contents = new ArrayList<>();

	private SpdxElement documentElement;

	public ComponentImpl(SpdxElement documentElement) {
		setDocumentElement(documentElement);
	}

	@Override
	public void addContent(SpdxElement content) {
		contents.add(content);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ComponentImpl other = (ComponentImpl) obj;
		if (documentElement == null) {
			if (other.documentElement != null) {
				return false;
			}
		} else if (!documentElement.equals(other.documentElement)) {
			return false;
		}
		return true;
	}

	@Override
	public ComponentLicenseInfo extractLicenseInfo() {
		return ComponentLicenseInfoImpl.create(this);
	}

	@Override
	public ConnectorType getConnectorTypeTo(Component module) {
		String targetModuleID = module.getID();

		List<SpdxElement> possibleSourceModules = new ArrayList<>();
		possibleSourceModules.add(getDocumentElement());
		possibleSourceModules.addAll(contents);

		for (SpdxElement sourceModule : possibleSourceModules) {

			for (Relationship rel : sourceModule.getRelationships()) {

				RelationshipType relType = rel.getRelationshipType();
				String relationshipTargetID = rel.getRelatedSpdxElement().getId();

				if (targetModuleID.equals(relationshipTargetID)) {
					if (relType == RelationshipType.DYNAMIC_LINK) {
						return ConnectorType.DYNAMIC_LINK;
					} else if (relType == RelationshipType.STATIC_LINK) {
						return ConnectorType.STATIC_LINK;
					}
				}
			}
		}

		return ConnectorType.UNDEFINED;
	}

	@Override
	public SpdxElement getDocumentElement() {
		return documentElement;
	}

	@Override
	public String getID() {
		return getDocumentElement().getId();
	}

	@Override
	public String getName() {
		return getDocumentElement().getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((documentElement == null) ? 0 : documentElement.hashCode());
		return result;
	}

	@Override
	public boolean isFile() {
		return getDocumentElement() instanceof SpdxFile;
	}

	@Override
	public boolean isPackage() {
		return getDocumentElement() instanceof SpdxPackage;
	}

	@Override
	public void setDocumentElement(SpdxElement documentElement) {
		this.documentElement = documentElement;
	}

	@Override
	public String toString() {
		return getName();
	}
}
