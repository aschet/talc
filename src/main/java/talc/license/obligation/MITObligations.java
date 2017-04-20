// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.license.obligation;

import java.util.ArrayList;
import java.util.List;

public class MITObligations extends ObligationSet {

	private static final long serialVersionUID = 2630750021729877418L;

	public MITObligations() {
		add(new IncludeCopyrightObligation());
		add(new IncludeLicenseObligation());
	}

	@Override
	public List<String> getLicenseIDs() {
		List<String> ids = new ArrayList<String>();
		ids.add("MIT");
		return ids;
	}
}
