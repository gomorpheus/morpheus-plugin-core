package com.morpheusdata.core;

import com.morpheusdata.model.Cloud;

import java.util.Date;

public interface MorpheusComputeContext {

	void updateZoneStatus(Cloud cloud, String status, String message, Date syncDate);
}
