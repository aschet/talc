// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.dependencygraph;

public abstract class ConnectorImpl implements Connector {
	@Override
	public String toString() {
		switch (getType()) {
		case DYNAMIC_LINK:
			return "dynamic";
		case STATIC_LINK:
			return "static";
		default:
			return new String();
		}
	}
}
