// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.license.obligation;

import java.util.HashMap;

public class ObligationSetRegistryImpl implements ObligationSetRegistry {

	private HashMap<String, ObligationSet> registry = new HashMap<>();

	public ObligationSetRegistryImpl() {
		// NOTE: this should not be hard coded values
		register(new GPLv2Obligations());
		register(new LGPLv21Obligations());
		register(new GPLv3Obligations());
		register(new LGPLv3Obligations());
		register(new BSD2ClauseObligations());
		register(new MITObligations());
	}

	@Override
	public ObligationSet lookup(String licenseID) {
		if (registry.containsKey(licenseID)) {
			return registry.get(licenseID);
		} else {
			return new DefaultObligationSet();
		}
	}

	@Override
	public void register(ObligationSet obligationSet) {
		for (String licenseID : obligationSet.getLicenseIDs()) {
			registry.put(licenseID, obligationSet);
		}
	}

}
