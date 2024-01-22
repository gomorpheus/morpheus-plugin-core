package com.morpheusdata.core.providers;

import com.morpheusdata.model.AccountResource;
import com.morpheusdata.model.Instance;
import com.morpheusdata.model.Workload;
import com.morpheusdata.response.ServiceResponse;
import com.morpheusdata.response.WorkloadResourceMappingResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Provides methods for interacting Infrastructure as Code provisioning via Morpheus, e.g. the Terraform provision type
 * @author Alex Clement
 * @since 0.15.10
 */
public interface IacResourceMappingProvider extends PluginProvider {

	/**
	 * Provides a list of string codes for provision types that this plug supports mapping and provisioning for, e.g "terraform"
	 * @author Alex Clement
	 * @since 0.15.3
	 * @return A list of ProvisionType codes
	 */
	default List<String> getIacProvisionTypeCodes() {
		return new ArrayList<String>();
	}

	/**
	 * Handles mapping an IaC resource to a Morpheus Instance. Should set externalId on all instance servers as well as any other fields desired
	 * @author Alex Clement
	 * @since 0.15.10
	 * @param instance The Morpheus {@link Instance} to update
	 * @param resource The Morpheus {@link AccountResource} for reference
	 * @param resourceResult The response from the IaC service
	 * @param iacProviderType The IaC provider type, e.g. aws
	 * @param iacType The IaC type, e.g. aws_instance
	 * @return A ServiceResponse with a {@link WorkloadResourceMappingResponse} as the data
	 */
	ServiceResponse<WorkloadResourceMappingResponse> resolveInstance(Instance instance, AccountResource resource, Map resourceResult, String iacProviderType, String iacType);

	/**
	 * Handles mapping an IaC resource to a Morpheus Workload. Should set externalId the workload server as well as any other fields desired
	 * @author Alex Clement
	 * @since 0.15.10
	 * @param workload The Morpheus {@link Workload} to update
	 * @param resource The Morpheus {@link AccountResource} for reference
	 * @param resourceResult The response from the IaC service
	 * @param iacProviderType The IaC provider type, e.g. aws
	 * @param iacType The IaC type, e.g. aws_instance
	 * @return A ServiceResponse with a {@link WorkloadResourceMappingResponse} as the data
	 */
	ServiceResponse<WorkloadResourceMappingResponse> resolveContainer(Workload workload, AccountResource resource, Map resourceResult, String iacProviderType, String iacType);

}
