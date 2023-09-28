package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.model.SecurityGroupRule;
import com.morpheusdata.model.projection.SecurityGroupRuleIdentityProjection;

public interface MorpheusSynchronousSecurityGroupRuleService extends MorpheusSynchronousDataService<SecurityGroupRule, SecurityGroupRuleIdentityProjection>, MorpheusSynchronousIdentityService<SecurityGroupRuleIdentityProjection> {

	MorpheusSynchronousSecurityGroupRuleLocationService getLocation();

	MorpheusSynchronousSecurityGroupRuleApplicationService getApplication();

	MorpheusSynchronousSecurityGroupRuleDestinationService getDestination();

	MorpheusSynchronousSecurityGroupRuleProfileService getProfile();

	MorpheusSynchronousSecurityGroupRuleScopeService getScope();

	MorpheusSynchronousSecurityGroupRuleSourceService getSource();

}