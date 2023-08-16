package com.morpheusdata.core.providers;

import com.morpheusdata.model.SecurityGroup;
import com.morpheusdata.model.SecurityGroupLocation;
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
	 * Creates the {@link SecurityGroup}. A security group location will also be created
	 * @param securityGroup SecurityGroup information
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	ServiceResponse<SecurityGroupLocation> createSecurityGroup(SecurityGroup securityGroup, Map opts);

	/**
	 * Updates the {@link SecurityGroup}
	 * @param securityGroup SecurityGroup information
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	ServiceResponse<SecurityGroup> updateSecurityGroup(SecurityGroup securityGroup, Map opts);

	/**
	 * Deletes the {@link SecurityGroupLocation}
	 * @param securityGroupLocation SecurityGroupLocation information
	 * @return ServiceResponse
	 */
	ServiceResponse deleteSecurityGroupLocation(SecurityGroupLocation securityGroupLocation);
}
