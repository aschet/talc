// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.license.obligation;

public class ObligationImpl implements Obligation {

	private String text;

	public ObligationImpl() {
		text = "";
	}

	public ObligationImpl(String text) {
		setText(text);
	}

	@Override
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
