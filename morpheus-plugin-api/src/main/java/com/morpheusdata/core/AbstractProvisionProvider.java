package com.morpheusdata.core;

import com.morpheusdata.model.*;
import com.morpheusdata.model.provisioning.HostRequest;
import com.morpheusdata.model.provisioning.WorkloadRequest;
import com.morpheusdata.response.HostResponse;
import com.morpheusdata.response.ServiceResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public abstract class AbstractProvisionProvider implements ProvisioningProvider {

	public Icon getCircularIcon() { return null; }

	@Override
	public Boolean hasComputeZonePools() {
		return false;
	}

	@Override
	public Boolean hasSnapshots() {
		return false;
	}

	@Override
	public Boolean canAddVolumes() {
		return false;
	}

	@Override
	public Boolean canCustomizeRootVolume() {
		return false;
	}

	@Override
	public Boolean canCustomizeDataVolumes() {
		return false;
	}

	@Override
	public Boolean canResizeRootVolume() { return false; }

	@Override
	public Boolean canReconfigureNetwork() { return false; }

	@Override
	public Boolean hasStorageControllers() {
		return false;
	}

	@Override
	public Boolean supportsAutoDatastore() {
		return true;
	}

	@Override
	public Boolean networksScopedToPools() {
		return false;
	}

	@Override
	public Boolean disableRootDatastore() { return false; }

	@Override
	public Boolean hasConfigurableSockets() { return false; }

	@Override
	public Boolean supportsCustomServicePlans() { return true; }

	@Override
	public  Boolean hasNodeTypes() { return true; }

	@Override
	public Boolean customSupported() { return true; }

	@Override
	public Boolean lvmSupported() { return false; }

	@Override
	public Boolean createServer() { return true; }

	@Override
	public Collection<StorageVolumeType> getRootVolumeStorageTypes() {
		return new ArrayList<StorageVolumeType>();
	}

	@Override
	public Collection<StorageVolumeType> getDataVolumeStorageTypes() {
		return new ArrayList<StorageVolumeType>();
	}

	@Override
	public Boolean computeZonePoolRequired() {
		return false;
	}

	@Override
	public ServiceResponse prepareWorkload(Workload workload, WorkloadRequest workloadRequest, Map opts) {
		return ServiceResponse.success();
	}

	@Override
	public ServiceResponse prepareHost(ComputeServer server, HostRequest hostRequest, Map opts) {
		return ServiceResponse.success();
	}

	@Override
	public ServiceResponse<HostResponse> runHost(ComputeServer server, HostRequest hostRequest, Map opts) {
		return ServiceResponse.error();
	}

	@Override
	public ServiceResponse<HostResponse> waitForHost(ComputeServer server) {
		return ServiceResponse.success();
	}

	@Override
	public ServiceResponse finalizeHost(ComputeServer server) {
		return ServiceResponse.success();
	}

	@Override
	public ServiceResponse finalizeWorkload(Workload workload) {
		return ServiceResponse.success();
	}

	@Override
	public ServiceResponse getXvpVNCConsoleUrl(ComputeServer server) { return null; }

	@Override
	public ServiceResponse getNoVNCConsoleUrl(ComputeServer server) { return null; }

	@Override
	public ServiceResponse updateServerHost(ComputeServer server) {return null; }

	@Override
	public ServiceResponse enableConsoleAccess(ComputeServer server) {return null; }

	@Override
	public ServiceResponse getWMKSConsoleUrl(ComputeServer server) { return null; }

	@Override
	public ServiceResponse createSnapshot(ComputeServer server, Map opts) { return null; }

	@Override
	public ServiceResponse deleteSnapshots(ComputeServer server, Map opts) { return null; }

	@Override
	public ServiceResponse deleteSnapshot(Snapshot snapshot, Map opts) { return null; }

	@Override
	public ServiceResponse revertSnapshot(ComputeServer server, Snapshot snapshot, Map opts) { return null; }

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
