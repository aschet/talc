// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.license.obligation;

public interface ObligationSetRegistry {
	ObligationSet lookup(String licenseID);

	void register(ObligationSet obligationSet);
}
