package com.morpheusdata.core.providers;

import com.morpheusdata.model.SecurityGroup;
import com.morpheusdata.response.ServiceResponse;

import java.util.Map;

public interface  SecurityGroupProvider extends PluginProvider {

	/**
	 * Prepare the security group information before validate, create, and update.
	 * If a {@link ServiceResponse} is not marked as successful the parent process will be terminated
	 * and the results may be presented to the user.
	 * @param securityGroup SecurityGroup information
	 * @param opts additional configuration options including any form data
	 * @return ServiceResponse
	 */
	ServiceResponse<SecurityGroup> prepareSecurityGroup(SecurityGroup securityGroup, Map opts);

	/**
	 * Validates the submitted security group information.
	 * If a {@link ServiceResponse} is not marked as successful the validation results will be
	 * bubbled up to the user.
	 * @param securityGroup SecurityGroup information
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	ServiceResponse validateSecurityGroup(SecurityGroup securityGroup, Map opts);

	/**
	 * Creates the security group submitted
	 * @param securityGroup SecurityGroup information
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	ServiceResponse<SecurityGroup> createSecurityGroup(SecurityGroup securityGroup, Map opts);

	/**
	 * Updates the security group submitted
	 * @param securityGroup SecurityGroup information
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	ServiceResponse<SecurityGroup> updateSecurityGroup(SecurityGroup securityGroup, Map opts);

	/**
	 * Deletes the security group submitted
	 * @param securityGroup SecurityGroup information
	 * @return ServiceResponse
	 */
	ServiceResponse deleteSecurityGroup(SecurityGroup securityGroup, Map opts);
}
