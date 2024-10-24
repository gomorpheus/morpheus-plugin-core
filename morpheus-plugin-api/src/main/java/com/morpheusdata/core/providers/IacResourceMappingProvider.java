/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.core.providers;

import com.morpheusdata.model.AccountResource;
import com.morpheusdata.model.Instance;
import com.morpheusdata.model.Workload;
import com.morpheusdata.response.InstanceResourceMappingResponse;
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
	 * @param iacProvider The IaC provider, e.g. terraform
	 * @param iacProviderType The IaC provider type, e.g. aws
	 * @param iacType The IaC type, e.g. aws_instance
	 * @return A ServiceResponse with a {@link WorkloadResourceMappingResponse} as the data
	 */
	ServiceResponse<InstanceResourceMappingResponse> resolveInstance(Instance instance, AccountResource resource, Map resourceResult, String iacProvider, String iacProviderType, String iacType);

	/**
	 * Handles mapping an IaC resource to a Morpheus Workload. Should set externalId the workload server as well as any other fields desired
	 * @author Alex Clement
	 * @since 0.15.11
	 * @param workload The Morpheus {@link Workload} to update
	 * @param resource The Morpheus {@link AccountResource} for reference
	 * @param resourceResult The response from the IaC service
	 * @param iacProvider The IaC provider, e.g. terraform
	 * @param iacProviderType The IaC provider type, e.g. aws
	 * @param iacType The IaC type, e.g. aws_instance
	 * @return A ServiceResponse with a {@link WorkloadResourceMappingResponse} as the data
	 */
	ServiceResponse<WorkloadResourceMappingResponse> resolveWorkload(Workload workload, AccountResource resource, Map resourceResult, String iacProvider, String iacProviderType, String iacType);

	/**
	 * Handles mapping an IaC resource to a Morpheus Workload. Should set externalId the workload server as well as any other fields desired
	 * @author Alex Clement
	 * @deprecated use {@link #resolveWorkload(Workload workload, AccountResource resource, Map resourceResult, String iacProvider, String iacProviderType, String iacType) } instead
	 * @since 0.15.10
	 * @param workload The Morpheus {@link Workload} to update
	 * @param resource The Morpheus {@link AccountResource} for reference
	 * @param resourceResult The response from the IaC service
	 * @param iacProvider The IaC provider, e.g. terraform
	 * @param iacProviderType The IaC provider type, e.g. aws
	 * @param iacType The IaC type, e.g. aws_instance
	 * @return A ServiceResponse with a {@link WorkloadResourceMappingResponse} as the data
	 */
	@Deprecated
	default ServiceResponse<WorkloadResourceMappingResponse> resolveContainer(Workload workload, AccountResource resource, Map resourceResult, String iacProvider, String iacProviderType, String iacType) {
		return resolveWorkload(workload, resource, resourceResult, iacProvider, iacProviderType, iacType);
	}

}
