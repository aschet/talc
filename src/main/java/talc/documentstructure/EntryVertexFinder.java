// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.documentstructure;

import java.util.List;

import org.spdx.rdfparser.model.SpdxDocument;
import org.spdx.rdfparser.model.SpdxItem;

public interface EntryVertexFinder {
	List<SpdxItem> find(SpdxDocument document);
}
