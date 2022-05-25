package com.morpheusdata.core;

import com.morpheusdata.model.*;
import com.morpheusdata.model.provisioning.WorkloadRequest;
import com.morpheusdata.response.ServiceResponse;

import java.util.Collection;
import java.util.Map;

public abstract class AbstractProvisionProvider implements ProvisioningProvider {

	@Override
	public Boolean hasComputeZonePools() {
		return false;
	}

	@Override
	public Boolean computeZonePoolRequired() {
		return false;
	}

	@Override
	public ServiceResponse prepareWorkload(Workload workload, WorkloadRequest workloadRequest, Map opts) {
		return ServiceResponse.success();
	}

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

}
