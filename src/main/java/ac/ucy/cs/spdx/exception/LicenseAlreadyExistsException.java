// Copyright 2016 Demetris Paschalides
// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-2.0+

package ac.ucy.cs.spdx.exception;

public class LicenseAlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = 7125765731723568083L;

	public LicenseAlreadyExistsException() {
		super();
	}

	public LicenseAlreadyExistsException(String message) {
		super(message);
	}

	public LicenseAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public LicenseAlreadyExistsException(Throwable cause) {
		super(cause);
	}

}
