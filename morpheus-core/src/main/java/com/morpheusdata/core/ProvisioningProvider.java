package com.morpheusdata.core;

import com.morpheusdata.model.OptionType;

import java.util.Collection;

/**
 * Provides methods for interacting with the provisioning engine of Morpheus. This is akin to dealing with requests made
 * from "Add Instance" or from Application Blueprints
 *
 * @author David Estes
 */
public interface ProvisioningProvider extends PluginProvider {

	/**
	 * Provides a Collection of OptionType inputs that need to be made available to various provisioning Wizards
	 * @return
	 */
	public Collection<OptionType> getOptionTypes();
}
