package com.morpheusdata.core;

import com.morpheusdata.model.*;

public abstract class AbstractProvisionProvider implements ProvisioningProvider {

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
