// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.analysis.result;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import talc.license.obligation.ObligationSet;

public class ArchitectureComplianceInformationImpl implements ArchitectureComplianceInformation {

	List<ComponentComplianceInformation> components = new ArrayList<>();

	BufferedImage visualisation;

	@Override
	public void addComponent(ComponentComplianceInformation component) {
		this.components.add(component);
	}

	@Override
	public List<ComponentComplianceInformation> getComponents() {
		return components;
	}

	@Override
	public Map<String, ObligationSet> getObligations() {
		Map<String, ObligationSet> obligations = new HashMap<String, ObligationSet>();

		for (ComponentComplianceInformation component : getComponents()) {
			for (LicenseComplianceInformation license : component.getContainedLicenses()) {
				obligations.put(license.getLicenseName(), license.getObligations());
			}
		}

		return obligations;
	}

	@Override
	public BufferedImage getVisualisation() {
		return visualisation;
	}

	@Override
	public boolean isCompliant() {
		boolean compliant = true;
		for (ComponentComplianceInformation info : components) {
			if (!info.isCompliant()) {
				compliant = false;
			}
		}
		return compliant;
	}

	@Override
	public void setComponents(List<ComponentComplianceInformation> components) {
		this.components = components;

	}

	@Override
	public void setVisualisation(BufferedImage image) {
		visualisation = image;

	}

}
