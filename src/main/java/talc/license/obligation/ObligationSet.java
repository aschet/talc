// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.license.obligation;

import java.util.HashSet;
import java.util.List;

public abstract class ObligationSet extends HashSet<Obligation> {

	private static final long serialVersionUID = 5080367547359423896L;

	public abstract List<String> getLicenseIDs();
}
