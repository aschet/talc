// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.analysis;

import java.io.IOException;
import java.util.List;

import org.jgrapht.alg.util.Pair;
import org.spdx.rdfparser.InvalidSPDXAnalysisException;
import org.spdx.rdfparser.SPDXDocumentFactory;
import org.spdx.rdfparser.license.AnyLicenseInfo;
import org.spdx.rdfparser.model.SpdxDocument;

import ac.ucy.cs.spdx.compatibility.SpdxLicensePairConflictError;
import talc.analysis.result.ArchitectureComplianceInformation;
import talc.analysis.result.ArchitectureComplianceInformationImpl;
import talc.analysis.result.ComponentComplianceInformation;
import talc.analysis.result.ComponentComplianceInformationImpl;
import talc.analysis.result.LicenseComplianceConflict;
import talc.analysis.result.LicenseComplianceInformation;
import talc.analysis.result.LicenseComplianceInformationImpl;
import talc.dependencygraph.Component;
import talc.dependencygraph.DependencyGraph;
import talc.dependencygrph.io.ImageExporter;
import talc.documentstructure.DependencyGraphBuilder;
import talc.documentstructure.DependencyGraphBuilderImpl;
import talc.license.AnyLicenseInfoOperations;
import talc.license.AnyLicenseInfoOperationsImpl;
import talc.license.ComponentLicenseInfo;
import talc.license.obligation.ObligationSetRegistry;
import talc.license.obligation.ObligationSetRegistryImpl;

public class ArchitectureComplianceAnalysis {
	private SpdxDocument document;

	private DependencyGraph graph;

	private AnyLicenseInfoOperations licenseOps = new AnyLicenseInfoOperationsImpl();

	private ObligationSetRegistry obligationsRegistry = new ObligationSetRegistryImpl();

	private ArchitectureComplianceInformation result;

	public ArchitectureComplianceInformation analyse(SpdxDocument document, DependencyGraph graph) {

		this.document = document;
		this.graph = graph;
		result = new ArchitectureComplianceInformationImpl();

		renderGraphVisualisation();

		DependencyGraphLicenseInfoMerger merger = new DependencyGraphLicenseInfoMerger(graph);
		List<ComponentLicenseInfo> mergedLicenseInfos = merger.merge();

		for (ComponentLicenseInfo licenseInfo : mergedLicenseInfos) {

			try {
				SpdxLicensePairConflictError errorAnalysis = new SpdxLicensePairConflictError(
						new CaptureLicenseAdapter(document, licenseInfo));

				ComponentComplianceInformation complianceInfo = new ComponentComplianceInformationImpl();
				complianceInfo.setName(licenseInfo.getOrigin().getName());
				complianceInfo.setCompliant(errorAnalysis.areCompatible());

				extractConcludedLicenseTo(licenseInfo, complianceInfo);
				extractContainedLicensesTo(licenseInfo, complianceInfo);
				extractLicenseConflictsTo(errorAnalysis, complianceInfo);

				result.addComponent(complianceInfo);

			} catch (InvalidSPDXAnalysisException e) {
				// TODO: error handling
			}
		}

		return result;
	}

	public ArchitectureComplianceInformation analyse(String spdxFile) throws IOException, InvalidSPDXAnalysisException {
		SpdxDocument document = SPDXDocumentFactory.createSpdxDocument(spdxFile);
		DependencyGraphBuilder builder = new DependencyGraphBuilderImpl(document);
		return analyse(document, builder.build());
	}

	private LicenseComplianceConflict createConflict(String first, String second) {
		return new LicenseComplianceConflict(createLicenseInfo(first, ""), createLicenseInfo(second, ""));
	}

	private LicenseComplianceInformation createLicenseInfo(String licenseID, String originName) {
		LicenseComplianceInformation licenseInfo = new LicenseComplianceInformationImpl();
		licenseInfo.setOriginName(originName);
		licenseInfo.setObligations(obligationsRegistry.lookup(licenseID));
		licenseInfo.setLicenseName(licenseOps.getLicenseName(licenseID, document));
		return licenseInfo;
	}

	private void extractConcludedLicenseTo(ComponentLicenseInfo component,
			ComponentComplianceInformation complianceInfo) {
		AnyLicenseInfo license = component.getConcludedLicense();
		List<String> licenseIDs = licenseOps.getContainedLicenses(license, false);
		for (String licenseID : licenseIDs) {
			complianceInfo.addPrimaryLicense(createLicenseInfo(licenseID, ""));
		}
	}

	private void extractContainedLicensesTo(ComponentLicenseInfo licenseInfo,
			ComponentComplianceInformation complianceInfo) {

		for (AnyLicenseInfo item : licenseInfo.getContainedLicenses()) {
			for (String licenseID : licenseOps.getContainedLicenses(item, false)) {
				String originName = "";
				Component origin = licenseInfo.getOriginOfLicense(item);
				if (!origin.equals(licenseInfo.getOrigin())) {
					originName = origin.getName();
				}

				complianceInfo.addContainedLicense(createLicenseInfo(licenseID, originName));
			}
		}
	}

	private void extractLicenseConflictsTo(SpdxLicensePairConflictError errorAnalysis,
			ComponentComplianceInformation complianceInfo) {
		for (Pair<String, String> conflict : errorAnalysis.getIncompatiblePairs()) {
			complianceInfo.addLicenseConflict(createConflict(conflict.first, conflict.second));
		}
	}

	private void renderGraphVisualisation() {
		ImageExporter exporter = new ImageExporter(graph);
		result.setVisualisation(exporter.export());
	}
}
