package com.morpheusdata.model.provisioning;

import com.morpheusdata.model.NetworkConfiguration;
import com.morpheusdata.model.ProxyConfiguration;
import com.morpheusdata.model.StorageVolume;

public class RunWorkloadRequest {

	public NetworkConfiguration networkConfiguration;
	public Long rootSize; // Move this to AbstractProvisionProvider
	public StorageVolume rootDisk; // Move this to AbstractProvisionProvider
	public ProxyConfiguration proxyConfiguration;

}
