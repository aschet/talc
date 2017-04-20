// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.license.obligation;

import java.util.ArrayList;
import java.util.List;

public class GPLv2Obligations extends ObligationSet {

	private static final long serialVersionUID = -1387125225915626857L;

	public GPLv2Obligations() {
		add(new DiscloseSourceObligation());
		add(new IncludeCopyrightObligation());
		add(new IncludeLicenseObligation());
		add(new NoSublicingObligation());
	}

	@Override
	public List<String> getLicenseIDs() {
		List<String> ids = new ArrayList<String>();
		ids.add("GPL-2.0");
		ids.add("GPL-2.0+");
		return ids;
	}
}
