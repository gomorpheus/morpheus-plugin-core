package com.morpheusdata.core.admin;

/**
 * This service provides access to other sub data services for accessing administration related objects such as {@link com.morpheusdata.model.User}
 * or {@link com.morpheusdata.model.Account}.
 *
 * @since 0.15.2
 * @author David Estes
 */
public interface MorpheusAdminService {

	/**
	 * Returns the User Service for querying / modifying user related objects asynchronously (reactive).
	 * @return an instance of the implementation of the MorpheusUserService
	 */
	MorpheusUserService getUser();

	/**
	 * Returns the Appliance Service for querying appliance related objects asynchronously (reactive).
	 * @return an instance of the implementation of the {@link MorpheusApplianceService}
	 */
	MorpheusApplianceService getAppliance();
}
