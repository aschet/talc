// Copyright 2017 Thomas Ascher <thomas.ascher@gmx.at>
// SPDX-License-Identifier: GPL-3.0+

package talc.analysis.result;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

import talc.license.obligation.ObligationSet;

public interface ArchitectureComplianceInformation {
	void addComponent(ComponentComplianceInformation component);

	List<ComponentComplianceInformation> getComponents();

	Map<String, ObligationSet> getObligations();

	BufferedImage getVisualisation();

	boolean isCompliant();

	void setComponents(List<ComponentComplianceInformation> components);

	void setVisualisation(BufferedImage image);
}
