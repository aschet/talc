// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.license.obligation;

import java.util.ArrayList;
import java.util.List;

public class DefaultObligationSet extends ObligationSet {

	private static final long serialVersionUID = -7449250012950349364L;

	public DefaultObligationSet() {
		ObligationImpl obligation = new ObligationImpl();
		obligation.setText("Please refer to the license text for more information.");
		add(obligation);
	}

	@Override
	public List<String> getLicenseIDs() {
		List<String> ids = new ArrayList<String>();
		ids.add("NONE");
		return ids;
	}
}
