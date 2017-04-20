// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.license.obligation;

import java.util.ArrayList;
import java.util.List;

public class BSD2ClauseObligations extends ObligationSet {

	private static final long serialVersionUID = -4134582277069408497L;

	BSD2ClauseObligations() {
		add(new IncludeCopyrightObligation());
		add(new IncludeLicenseObligation());
	}

	@Override
	public List<String> getLicenseIDs() {
		List<String> ids = new ArrayList<String>();
		ids.add("BSD-2-Clause-FreeBSD");
		return ids;
	}
}
