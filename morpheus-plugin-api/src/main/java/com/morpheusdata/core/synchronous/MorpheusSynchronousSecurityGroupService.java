package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.SecurityGroup;
import com.morpheusdata.model.projection.SecurityGroupIdentityProjection;

public interface MorpheusSynchronousSecurityGroupService extends MorpheusSynchronousDataService<SecurityGroup, SecurityGroupIdentityProjection>, MorpheusSynchronousIdentityService<SecurityGroupIdentityProjection> {

	MorpheusSynchronousSecurityGroupLocationService getLocation();

	MorpheusSynchronousSecurityGroupRuleService getRule();
	
}
