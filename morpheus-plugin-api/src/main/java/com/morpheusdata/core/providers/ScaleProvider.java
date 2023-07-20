package com.morpheusdata.core.providers;

import com.morpheusdata.model.Icon;
import com.morpheusdata.model.InstanceScaleType;
import com.morpheusdata.model.NetworkLoadBalancerType;
import com.morpheusdata.model.OptionType;

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
}
