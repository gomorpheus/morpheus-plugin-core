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
	 * Generates Cloud Configuration Options for Passing into User-Data Generation or Unattend Generation Depending on Platform
	 * @param cloud the current Cloud the server is being provisioned into
	 * @param server the server instance being provisioned
	 * @param installAgent flag to determine if agent is being installed over cloud-init/unattend or not
	 * @param opts additional options like fqdn, or hostname data
	 * @return
	 */
	Single<Map<String,Object>> buildCloudConfigOptions(Cloud cloud, ComputeServer server, Boolean installAgent, Map<String,Object> opts);

	/**
	 * Typically this is called immediately following {@link #buildCloudConfigOptions(Cloud, ComputeServer, Boolean, Map)}
	 * @param platform
	 * @param virtualImage
	 * @param cloudConfigOptions
	 * @param networkConfig
	 * @return
	 */
	Single<Map<String,Object>> buildCloudNetworkConfig(PlatformType platform, VirtualImage virtualImage, Map<String,Object> cloudConfigOptions, Map<String,Object> networkConfig);
	Single<String> buildCloudUserData(PlatformType platform, Map<String,Object> userConfig, Map<String,Object> cloudConfigOptions);
	Single<String> buildCloudMetaData(PlatformType platform, String instanceId, String hostname,Map<String,Object> cloudConfigOptions);
	Single<String> buildCloudNetworkData(PlatformType platform, Map<String,Object> cloudConfigOptions);
	
}
