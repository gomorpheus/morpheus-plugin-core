package com.morpheusdata.core.providers;

import com.morpheusdata.model.SecurityGroup;
import com.morpheusdata.model.SecurityGroupLocation;
import com.morpheusdata.model.SecurityGroupRule;
import com.morpheusdata.model.SecurityGroupRuleLocation;
import com.morpheusdata.response.ServiceResponse;

import java.util.Map;

public interface  SecurityGroupProvider extends PluginProvider {

	/**
	 * Prepare the {@link SecurityGroup} information before validate, create, and update.
	 * If a {@link ServiceResponse} is not marked as successful the parent process will be terminated
	 * and the results may be presented to the user.
	 * @param securityGroup SecurityGroup information
	 * @param opts additional configuration options including any form data
	 * @return ServiceResponse
	 */
	ServiceResponse<SecurityGroup> prepareSecurityGroup(SecurityGroup securityGroup, Map opts);

	/**
	 * Validates the submitted {@link SecurityGroup} information.
	 * If a {@link ServiceResponse} is not marked as successful the validation results will be
	 * bubbled up to the user.
	 * @param securityGroup SecurityGroup information
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	ServiceResponse validateSecurityGroup(SecurityGroup securityGroup, Map opts);

	/**
	 * Creates a {@link SecurityGroupLocation } from the submitted {@link SecurityGroup }
	 * @param securityGroup SecurityGroup object
	 * @param opts additional configuration options
	 * @return ServiceResponse containing the resulting {@link SecurityGroupLocation } including the information (externalId, etc.)
	 *  which identifies the security group within the current context (usually a cloud).
	 */
	ServiceResponse<SecurityGroupLocation> createSecurityGroup(SecurityGroup securityGroup, Map opts);

	/**
	 * Update the security group
	 * @param securityGroup SecurityGroup object
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	ServiceResponse<SecurityGroup> updateSecurityGroup(SecurityGroup securityGroup, Map opts);

	/**
	 * Delete a {@link SecurityGroup}
	 * @param securityGroup SecurityGroup object
	 * @return ServiceResponse
	 */
	ServiceResponse deleteSecurityGroup(SecurityGroup securityGroup);

	/**
	 * Delete a {@link SecurityGroupLocation}
	 * @param securityGroupLocation SecurityGroupLocation information
	 * @return ServiceResponse
	 */
	ServiceResponse deleteSecurityGroupLocation(SecurityGroupLocation securityGroupLocation);

	/**
	 * Prepare the security group rule before validate, create, and update.
	 * If a {@link ServiceResponse} is not marked as successful the parent process will be terminated
	 * and the results may be presented to the user.
	 * @param securityGroupRule SecurityGroupRule object
	 * @param opts additional configuration options including all form data
	 * @return ServiceResponse
	 */
	ServiceResponse<SecurityGroupRule> prepareSecurityGroupRule(SecurityGroupRule securityGroupRule, Map opts);

	/**
	 * Validate the submitted security group rule object.
	 * If a {@link ServiceResponse} is not marked as successful the validation results in the <i>errors</i> and <i>msg</i> properties will be
	 * surfaced to the user interface.
	 * @param securityGroupRule SecurityGroupRule object
	 * @return ServiceResponse
	 */
	ServiceResponse<SecurityGroupRule> validateSecurityGroupRule(SecurityGroupRule securityGroupRule);

	/**
	 * Creates a {@link SecurityGroupRuleLocation } from the submitted {@link SecurityGroupRule }
	 * @param securityGroupRule SecurityGroupRule object
	 * @return ServiceResponse containing the resulting {@link SecurityGroupRuleLocation } including the information (externalId, etc.)
	 *  which identifies the security group rule within the current context (usually a cloud).
	 */
	ServiceResponse<SecurityGroupRuleLocation> createSecurityGroupRule(SecurityGroupLocation securityGroupLocation, SecurityGroupRule securityGroupRule);

	/**
	 * Update the security group rule
	 * @param securityGroupLocation the {@link SecurityGroupLocation }
	 * @param originalRule the rule before any updates were applied.
	 * @param updatedRule the rule with all updates applied
	 * @return {@link ServiceResponse }
	 */
	ServiceResponse<SecurityGroupRule> updateSecurityGroupRule(SecurityGroupLocation securityGroupLocation, SecurityGroupRule originalRule, SecurityGroupRule updatedRule);

	/**
	 * Delete a {@link SecurityGroupRule}
	 * @param securityGroupLocation SecurityGroupLocation object
	 * @param rule SecurityGroupRule to be deleted
	 * @return ServiceResponse
	 */
	ServiceResponse deleteSecurityGroupRule(SecurityGroupLocation securityGroupLocation, SecurityGroupRule rule);
}
