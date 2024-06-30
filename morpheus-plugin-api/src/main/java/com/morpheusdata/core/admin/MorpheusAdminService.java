/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
