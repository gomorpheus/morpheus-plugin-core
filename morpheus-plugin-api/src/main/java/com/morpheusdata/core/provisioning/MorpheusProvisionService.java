package com.morpheusdata.core.provisioning;

import com.morpheusdata.model.*;
import io.reactivex.Single;

import java.util.Map;

public interface MorpheusProvisionService {

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
	Single<UsersConfiguration> getUserConfig(Workload workload, VirtualImage virtualImage, Map<String,Object> opts);

	/**
	 * Generates a map representation of the network configuration for a server being provisioned with a virtual image
	 * @param workload the Workload object we are working with
	 * @param virtualImage the Virtual Image being provisioned
	 * @param opts additional options like hostname, fqdn, lease options
	 * @return NetworkConfiguration generated
	 */
	Single<NetworkConfiguration> getNetworkConfig(Workload workload, VirtualImage virtualImage, Map<String,Object> opts);

	/**
	 * Generates Cloud Configuration Options for Passing into User-Data Generation or Unattend Generation Depending on Platform
	 * @param cloud the current Cloud the server is being provisioned into
	 * @param server the server instance being provisioned
	 * @param installAgent flag to determine if agent is being installed over cloud-init/unattend or not
	 * @param opts additional options like fqdn, domainName, virtualImage, hostname data. These will be returned in the Map
	 * @return Map of cloud configuration options
	 */
	Single<Map<String,Object>> buildCloudConfigOptions(Cloud cloud, ComputeServer server, Boolean installAgent, Map<String,Object> opts);

	/**
	 * Typically this is called immediately following {@link #buildCloudConfigOptions(Cloud, ComputeServer, Boolean, Map)}
	 * @param platform the platform being provisioned
	 * @param virtualImage the Virtual Image being provisioned
	 * @param cloudConfigOptions typically the return of buildCloudConfigOptions
	 * @param networkConfiguration the network configuration to provision
	 * @return Map of cloudConfigOptions with modifications for networking as needed. May include cloudNetworkInterfaces, staticNetwork, dhcpNetwork, and/or networkDomain
	 */
	Single<Map<String,Object>> buildCloudNetworkConfig(PlatformType platform, VirtualImage virtualImage, Map<String,Object> cloudConfigOptions, NetworkConfiguration networkConfiguration);

	/**
	 * Builds the userdata typically passed to cloud-init
	 * @param platform the platform being provisioned
	 * @param usersConfiguration typically the UsersConfigurations from buildWorkloadUsersConfig
	 * @param cloudConfigOptions typically the return of buildCloudConfigOptions or buildCloudNetworkConfig
	 * @return String userdata
	 */
	Single<String> buildCloudUserData(PlatformType platform, UsersConfiguration usersConfiguration, Map<String,Object> cloudConfigOptions);

	/**
	 * Builds the cloud metadata
	 * @param platform the platform being provisioned
	 * @param instanceId
	 * @param hostname
	 * @param cloudConfigOptions typically the return of buildCloudConfigOptions or buildCloudNetworkConfig
	 * @return String cloud metadata
	 */
	Single<String> buildCloudMetaData(PlatformType platform, Long instanceId, String hostname, Map<String,Object> cloudConfigOptions);

	/**
	 * Builds the cloud network data
	 * @param platform the platform being provisioned
	 * @param cloudConfigOptions typically the return of buildCloudConfigOptions or buildCloudNetworkConfig
	 * @return String cloud networkdata
	 */
	Single<String> buildCloudNetworkData(PlatformType platform, Map<String,Object> cloudConfigOptions);


	
}
