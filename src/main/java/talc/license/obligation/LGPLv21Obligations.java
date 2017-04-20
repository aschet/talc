// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.license.obligation;

import java.util.ArrayList;
import java.util.List;

public class LGPLv21Obligations extends GPLv2Obligations {

	private static final long serialVersionUID = 8310301527063996384L;

	@Override
	public List<String> getLicenseIDs() {
		List<String> ids = new ArrayList<String>();
		ids.add("LGPL-2.1");
		ids.add("LGPL-2.1+");
		return ids;
	}
}
