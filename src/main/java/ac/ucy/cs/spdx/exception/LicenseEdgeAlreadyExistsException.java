// Copyright 2016 Demetris Paschalides
// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-2.0+

package ac.ucy.cs.spdx.exception;

public class LicenseEdgeAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -5295906599641916972L;

	public LicenseEdgeAlreadyExistsException() {
		super();
	}

	public LicenseEdgeAlreadyExistsException(String message) {
		super(message);
	}

	public LicenseEdgeAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public LicenseEdgeAlreadyExistsException(Throwable cause) {
		super(cause);
	}

}
