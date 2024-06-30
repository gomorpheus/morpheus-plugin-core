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

package com.morpheusdata.core.providers;

import com.morpheusdata.model.*;
import com.morpheusdata.response.ServiceResponse;

import java.util.Collection;

/**
 * Provides a standard set of methods for interacting with auto scale integrations
 */
public interface ScaleProvider extends PluginProvider {
	/**
	 * Grabs the description for the ScaleProvider
	 * @return String
	 */
	String getDescription();

	/**
	 * Returns the logo for display. SVGs are preferred.
	 * @since 0.13.0
	 * @return Icon representation of assets stored in the src/assets of the project.
	 */
	Icon getIcon();

	/**
	 * Provides a Collection of OptionType inputs that define the required input fields for defining a scale type (if applicable)
	 * @return Collection of OptionType
	 */
	Collection<OptionType> getOptionTypes();

	/**
	 * Provides a collection of scale types that need to be configured in the morpheus environment along
	 * with the various child entities required by the scale type
	 * @return Collection of InstanceScaleType
	 */
	Collection<InstanceScaleType> getScaleTypes();

	/**
	 * Implement this method to perform any operations that occur PRIOR to the instance being provisioned.
	 * @param instance an {@link Instance} to be provisioned
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse provisionInstance(Instance instance) { return ServiceResponse.success(); }

	/**
	 * Implement this method to perform some tasks after an instance has been provisioned via morpheus.  For example,
	 * update a scale group details etc.
	 * @param instance a provisioned {@link Instance}
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse postProvisionInstance(Instance instance) {
		return ServiceResponse.success();
	}

	/**
	 * Implement this method to perform some operations when an instance has been removed from morpheus
	 * @param instance the {@link Instance} to be removed
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse removeInstance(Instance instance) {
		return ServiceResponse.success();
	}

	/**
	 * Implement this method to perform some operations when an instance has been updated
	 * @param instance the {@link Instance} being updated
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse updateInstance(Instance instance) {
		return ServiceResponse.success();
	}
}
