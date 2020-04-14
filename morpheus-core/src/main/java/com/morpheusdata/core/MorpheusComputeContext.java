package com.morpheusdata.core;

import com.morpheusdata.model.*;
import io.reactivex.Single;

import java.util.Date;

public interface MorpheusComputeContext {

	void updateZoneStatus(Zone zone, String status, String message, Date syncDate);

	Single<Container> getContainerById(Long id);

	Single<InstanceTypeLayout> getInstanceTypeLayoutById(Long id);

	Single<ServicePlan> getServicePlanById(Long id);

	Single<ComputeZonePool> getComputeZonePoolById(Long id);

	Single<Instance> getInstanceById(Long id);

	Single<ComputeServer> getComputeServerById(Long id);

	Single<ComputeZone> getComputeZoneById(Long id);

	Single<ComputeZoneType> getComputeZoneTypeById(Long id);

	Single<OsType> getOsTypeById(Long id);

	Single<VirtualImage> findVirtualImageByOwnerAndCode(Account account, String code);
}
