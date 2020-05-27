package com.morpheusdata.core;

import com.morpheusdata.model.AccountIntegration;
import com.morpheusdata.model.Instance;
import com.morpheusdata.model.Zone;

import java.util.Date;

public interface MorpheusComputeContext {

	void updateZoneStatus(Zone zone, String status, String message, Date syncDate);
	void requestApproval(Instance instance, String providerCode);
}
