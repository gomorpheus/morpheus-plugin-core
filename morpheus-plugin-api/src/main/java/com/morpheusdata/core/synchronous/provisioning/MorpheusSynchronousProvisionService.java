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

package com.morpheusdata.core.synchronous.provisioning;


import com.morpheusdata.model.*;
import com.morpheusdata.model.provisioning.UsersConfiguration;
import io.reactivex.rxjava3.core.Single;

import java.util.Map;

public interface MorpheusSynchronousProvisionService {

	/**
	 * Sets the Provisioning state of a Workload and Server to Failed
	 * This can be called in an error scenario during a provision operation. The instance status will also be set to Failed
	 * @param server The Server object we want to set to failed.
	 * @param workload the Workload object we want to set to failed.
	 */
	void setProvisionFailed(ComputeServer server, Workload workload);
	/**
	 * Sets the Provisioning state of a Workload and Server to Failed
	 * This can be called in an error scenario during a provision operation. The instance status will also be set to Failed.
	 * Setting the errorMessage is helpful in telling the user why their provision request did fail
	 * @param server The Server object we want to set to failed.
	 * @param workload the Workload object we want to set to failed.
	 * @param errorMessage The descriptive error message as to why provisioning failed.
	 */
	void setProvisionFailed(ComputeServer server, Workload workload, String errorMessage);
	/**
	 * Sets the Provisioning state of a Workload and Server to Failed
	 * This can be called in an error scenario during a provision operation. The instance status will also be set to Failed.
	 * Setting the errorMessage is helpful in telling the user why their provision request did fail
	 * @param server The Server object we want to set to failed.
	 * @param workload the Workload object we want to set to failed.
	 * @param errorMessage The descriptive error message as to why provisioning failed.
	 * @param opts Any additional options to be sent back to the callback container service for error handling
	 */
	void setProvisionFailed(ComputeServer server, Workload workload, String errorMessage, Map opts);

	/**
	 * Generates a UsersConfiguration given workload and virtual image
	 * @param workload the Workload object we are working with
	 * @param virtualImage the virtual image from which user data will be generated from
	 * @param opts additional options like fqdn, or hostname data
	 * @return UsersConfiguration generated
	 */
	UsersConfiguration getUserConfig(Workload workload, VirtualImage virtualImage, Map<String,Object> opts);

	/**
	 * Generates Cloud Configuration Options for Passing into User-Data Generation or Unattend Generation Depending on Platform
	 * @param cloud the current Cloud the server is being provisioned into
	 * @param server the server instance being provisioned
	 * @param installAgent flag to determine if agent is being installed over cloud-init/unattend or not
	 * @param opts additional options like fqdn, domainName, virtualImage, hostname data. These will be returned in the Map
	 * @return Map of cloud configuration options
	 */
	Map<String,Object> buildCloudConfigOptions(Cloud cloud, ComputeServer server, Boolean installAgent, Map<String,Object> opts);

	/**
	 * Builds the userdata typically passed to cloud-init
	 * @param platform the platform being provisioned
	 * @param usersConfiguration typically the UsersConfigurations from buildWorkloadUsersConfig
	 * @param cloudConfigOptions typically the return of buildCloudConfigOptions or buildCloudNetworkConfig
	 * @return String userdata
	 */
	String buildCloudUserData(PlatformType platform, UsersConfiguration usersConfiguration, Map<String,Object> cloudConfigOptions);

	/**
	 * Builds the cloud metadata
	 * @param platform the platform being provisioned
	 * @param instanceId the ID of the Instance
	 * @param hostname the hostname
	 * @param cloudConfigOptions typically the return of buildCloudConfigOptions or buildCloudNetworkConfig
	 * @return String cloud metadata
	 */
	String buildCloudMetaData(PlatformType platform, Long instanceId, String hostname, Map<String,Object> cloudConfigOptions);

	/**
	 * Builds the cloud network data
	 * @param platform the platform being provisioned
	 * @param cloudConfigOptions typically the return of buildCloudConfigOptions or buildCloudNetworkConfig
	 * @return String cloud networkdata
	 */
	String buildCloudNetworkData(PlatformType platform, Map<String,Object> cloudConfigOptions);


	/**
	 * Builds up the data for an iso image based on the given parameters
	 * @param isSysPrep indicates if the iso is for a sysprep image
	 * @param platform the platform (should be windows or linux)
	 * @param metaData the metadata to embed in the iso
	 * @param userData the userdata to embed in the iso
	 * @param networkData the networkdata to embed in the iso (optional)
	 * @return a byte array for the iso
	 */
	byte[] buildIsoOutputStream(Boolean isSysPrep, PlatformType platform, String metaData, String userData, String networkData);
}
