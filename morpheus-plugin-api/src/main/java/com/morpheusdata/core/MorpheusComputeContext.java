package com.morpheusdata.core;

import com.morpheusdata.model.Zone;

import java.util.Date;

public interface MorpheusComputeContext {

	void updateZoneStatus(Zone zone, String status, String message, Date syncDate);
}
