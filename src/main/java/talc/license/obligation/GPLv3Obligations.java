// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.license.obligation;

import java.util.ArrayList;
import java.util.List;

public class GPLv3Obligations extends GPLv2Obligations {

	private static final long serialVersionUID = 1271056716441690907L;

	public GPLv3Obligations() {
		add(new InstallInstructionsObligation());
		add(new PatentGrantObligation());
	}

	@Override
	public List<String> getLicenseIDs() {
		List<String> ids = new ArrayList<String>();
		ids.add("GPL-3.0");
		ids.add("GPL-3.0+");
		return ids;
	}
}
