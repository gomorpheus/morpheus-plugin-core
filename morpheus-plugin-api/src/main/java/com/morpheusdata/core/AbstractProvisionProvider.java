package com.morpheusdata.core;

import com.morpheusdata.core.providers.ProvisionProvider;
import com.morpheusdata.model.*;
import com.morpheusdata.response.*;

import java.util.Collection;

public abstract class AbstractProvisionProvider implements ProvisionProvider {

	public ComputeServerType findVmNodeServerTypeForCloud(Long cloudId, String platform, String provisionTypeCode) {
		ComputeServerType rtn = null;
		Collection<ComputeServerType> serverTypes = getMorpheus().getCloud().getComputeServerTypes(cloudId).blockingGet();
		String nodeType = null;
		if(provisionTypeCode != null) {
			nodeType = (platform == "windows") ? "morpheus-windows-vm-node" : "morpheus-vm-node";
		}

		for(ComputeServerType serverType:serverTypes) {
			if(rtn == null) {
				if(serverType.getNodeType() == nodeType && serverType.getManaged() == true && (provisionTypeCode == null || serverType.getProvisionTypeCode() == provisionTypeCode)) {
					rtn = serverType;
				}
			}
		}

		// If we still don't have one... leave off the provisionTypeCode when searching
		if(rtn == null) {
			for(ComputeServerType serverType:serverTypes) {
				if(rtn == null) {
					if(serverType.getNodeType() == nodeType && serverType.getManaged() == true) {
						rtn = serverType;
					}
				}
			}
		}

		return rtn;
	}

    public Long getRootSize(Workload workload) {
        StorageVolume rootDisk = getRootDisk(workload);
		Long rootSize = null;
        if(rootDisk != null) {
			rootSize = rootDisk.getMaxStorage();
        } else {
            if(workload.getMaxStorage() != null && workload.getMaxStorage() != 0l) {
				rootSize = workload.getMaxStorage();
            } else if (workload.getInstance() != null && workload.getInstance().plan != null) {
				rootSize = workload.getInstance().plan.maxStorage;
            }
        }
		return rootSize;
    }

    public StorageVolume getRootDisk(Workload workload) {
        StorageVolume rtn = null;
        if(workload.getServer() != null && workload.getServer().getVolumes() != null && workload.getServer().getVolumes().size() > 0) {
            for(StorageVolume sv: workload.getServer().getVolumes()) {
                if(rtn == null && sv.getRootVolume() == true) {
                    rtn = sv;
                }
            }
        }
        return rtn;
    }

	public HostResponse workloadResponseToHostResponse(WorkloadResponse workloadResponse) {
		HostResponse hostResponse = new HostResponse();
		hostResponse.unattendCustomized = workloadResponse.unattendCustomized;
		hostResponse.externalId = workloadResponse.externalId;
		hostResponse.publicIp = workloadResponse.publicIp;
		hostResponse.privateIp = workloadResponse.privateIp;
		hostResponse.installAgent = workloadResponse.installAgent;
		hostResponse.noAgent = workloadResponse.noAgent;
		hostResponse.createUsers = workloadResponse.createUsers;
		hostResponse.success = workloadResponse.success;
		hostResponse.customized = workloadResponse.customized;
		hostResponse.licenseApplied = workloadResponse.licenseApplied;
		hostResponse.poolId = workloadResponse.poolId;
		hostResponse.hostname = workloadResponse.hostname;
		hostResponse.message = workloadResponse.message;
		hostResponse.skipNetworkWait = workloadResponse.skipNetworkWait;
		return hostResponse;
	}



}
