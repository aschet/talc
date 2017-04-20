// Copyright 2016 Demetris Paschalides
// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-2.0+

package ac.ucy.cs.spdx.dot;

public class DotEdge {
	private String from;
	private boolean isTransitive;
	private String to;

	public DotEdge(String from, String to, boolean isTransitive) {
		this.setFrom(from);
		this.setTo(to);
		this.setTransitive(isTransitive);
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public boolean isTransitive() {
		return isTransitive;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public void setTransitive(boolean isTransitive) {
		this.isTransitive = isTransitive;
	}

	@Override
	public String toString() {
		return this.from + " -> " + this.to;
	}
}
