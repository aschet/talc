// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.license.obligation;

import java.util.ArrayList;
import java.util.List;

public class LGPLv3Obligations extends GPLv3Obligations {

	private static final long serialVersionUID = -8977480009337844915L;

	@Override
	public List<String> getLicenseIDs() {
		List<String> ids = new ArrayList<String>();
		ids.add("LGPL-3.0");
		ids.add("LGPL-3.0+");
		return ids;
	}
}
