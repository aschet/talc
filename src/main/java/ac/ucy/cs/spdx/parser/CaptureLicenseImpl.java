// Copyright 2016 Demetris Paschalides
// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-2.0+

package ac.ucy.cs.spdx.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.spdx.rdfparser.InvalidSPDXAnalysisException;
import org.spdx.rdfparser.SPDXDocument;
import org.spdx.rdfparser.SPDXDocument.SPDXPackage;
import org.spdx.rdfparser.SPDXDocumentFactory;
import org.spdx.rdfparser.license.AnyLicenseInfo;
import org.spdx.rdfparser.license.ExtractedLicenseInfo;
import org.spdx.rdfparser.license.LicenseInfoFactory;
import org.spdx.rdfparser.license.LicenseSet;
import org.spdx.rdfparser.model.SpdxDocument;
import org.spdx.rdfparser.model.SpdxPackage;
import org.spdx.spdxspreadsheet.InvalidLicenseStringException;

/**
 * CaptureLicenses class provides us with an object containing the declared
 * license or licenses of an SPDXPackage, and whether or not those licenses are
 * conjunctive or disjunctive. Also contains the {@link SPDXDocument} for each
 * {@link LicenseSet}
 *
 * @version 1.2
 * @author dpasch01
 */
@SuppressWarnings("deprecation")
public class CaptureLicenseImpl implements CaptureLicense {

	private SpdxDocument captureDocument;
	private LicenseExpression captureLicenses;
	private SpdxPackage capturePackage;
	private String fileName;
	private HashMap<String, String> referencedLicenses;

	public CaptureLicenseImpl(String path)
			throws IOException, InvalidSPDXAnalysisException, InvalidLicenseStringException {

		SpdxDocument spdxDoc = SPDXDocumentFactory.createSpdxDocument(path);
		SpdxPackage spdxPackage = spdxDoc.getSpdxPackage();
		AnyLicenseInfo declared = LicenseInfoFactory
				.parseSPDXLicenseString(spdxPackage.getDeclaredLicense().toString());

		this.setFileName(FilenameUtils.getBaseName(path));
		this.setReferencedLicenses(new HashMap<String, String>());
		for (ExtractedLicenseInfo exLicense : spdxDoc.getExtractedLicenseInfos()) {
			referencedLicenses.put(exLicense.getLicenseId(), exLicense.getName());
		}

		this.captureDocument = spdxDoc;
		this.capturePackage = spdxPackage;
		this.captureLicenses = LicenseExpression.parseExpression(declared.toString(), this.referencedLicenses);
	}

	@Override
	public AnyLicenseInfo getConcludedLicense() {
		return this.capturePackage.getConcludedLicenses();
	}

	public AnyLicenseInfo getDataLicense() throws InvalidSPDXAnalysisException {
		return this.captureDocument.getDataLicense();
	}

	public AnyLicenseInfo getDeclaredLicense() throws InvalidLicenseStringException, InvalidSPDXAnalysisException {
		AnyLicenseInfo declared = LicenseInfoFactory
				.parseSPDXLicenseString(this.capturePackage.getDeclaredLicense().toString());
		return declared;
	}

	public ExtractedLicenseInfo[] getExtractedLicenses() throws InvalidSPDXAnalysisException {
		return this.captureDocument.getExtractedLicenseInfos();
	}

	@Override
	public List<String> getExtractedLicensesList() throws InvalidSPDXAnalysisException {
		ArrayList<String> extractedLicenses = new ArrayList<String>();
		for (ExtractedLicenseInfo extractedLicense : this.getExtractedLicenses()) {
			if (!Arrays.asList(LicenseExpression.ignoredLicenseList).contains(extractedLicense.getName())) {
				extractedLicenses.add(extractedLicense.getName());
			}
		}
		return extractedLicenses;
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public LicenseExpression getLicenseExpression() {
		return this.captureLicenses;
	}

	@Override
	public HashMap<String, String> getReferencedLicenses() {
		return referencedLicenses;
	}

	public SpdxDocument getSpdxDocument() {
		return this.captureDocument;
	}

	public SpdxPackage getSpdxPackage() {
		return this.capturePackage;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setReferencedLicenses(HashMap<String, String> referencedLicenses) {
		this.referencedLicenses = referencedLicenses;
	}
}
