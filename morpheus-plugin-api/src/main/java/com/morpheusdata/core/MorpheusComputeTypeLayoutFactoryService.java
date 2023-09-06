package com.morpheusdata.core;

import com.morpheusdata.model.ComputeTypeLayout;
import io.reactivex.Single;

public interface MorpheusComputeTypeLayoutFactoryService {

	/**
	 * Construct the scaffolding of a ComputeTypeLayout for defining the layout structure for a Docker server. The
	 * returned ComputeTypeLayout will have the associated ComputeTypeSet and ContainerType defined. The typical use of
	 * this method is to obtain the structure (modify if needed) and then pass back to Morpheus via a call to
	 * ProvisionProvider.getComputeTypeLayouts()
	 * @param codePrefix a prefix to use on the ComputeTypeLayout, ComputeTypeSet, and ContainerType
	 * @param osVersion the operating system version (i.e. '18.04' for Ubuntu)
	 * @param provisionTypeCode the code of the ProvisionProvider to use when building the structure
	 * @param computeServerTypeCode the code of the ComputeServerType to use on the ComputeTypeLayout and ComputeTypeSet
	 * @param virtualImageCode the VirtualImage code to use on the ContainerType
	 * @return Observable the constructed ComputeTypeLayout
	 */
	Single<ComputeTypeLayout> buildDockerLayout(String codePrefix, String osVersion, String provisionTypeCode, String computeServerTypeCode, String virtualImageCode);
}
