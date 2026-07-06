package com.morpheusdata.core.providers;

import com.morpheusdata.model.Icon;
import com.morpheusdata.model.SystemCatalogItemUpdate;
import com.morpheusdata.model.system.*;
import com.morpheusdata.model.system.System;
import com.morpheusdata.response.ServiceResponse;
import com.morpheusdata.model.DriftState;
import com.morpheusdata.model.CheckLevel;


import java.util.Collection;

/**
 * Provides a set of interface methods for integration with a vme manager {@link System}
 * @since 1.2.10
 */
public interface SystemProvider extends PluginProvider {
	/**
	 * Grabs the description for the SystemProvider
	 * @return String
	 */
	String getDescription();

	/**
	 * Returns the logo for display. SVGs are preferred.
	 * @return Icon representation of assets stored in the src/assets of the project.
	 */
	Icon getIcon();

	/**
	 * Provides a Collection of {@link SystemComponentType} which are supported for this system provider
	 */
	Collection<SystemComponentType> getSystemComponentTypes();

	/**
	 * Providers a collection of system types that are supported by this provider
	 */
	Collection<SystemType> getSystemTypes();

	/**
	 * Provides a collection of {@link SystemTypeLayout} which are supported by this system provider.  These layouts
	 * map the resources which are maintained, configured, updated by the provider implementation
	 */
	Collection<SystemTypeLayout> getSystemTypeLayouts();

	/**
	 * This phase is run prior to executing the system initialization workflows.  In this phase you can perform
	 * any form up pre initialization checks, additional input validations, etc.
	 * @param system
	 * @return
	 */
	default ServiceResponse prepareInitializeSystem(System system, SystemRequest systemRequest) { return ServiceResponse.success(); }

	/**
	 * This method is executed post initialization workflows have been executed. Not required to implement.
	 * @param system
	 * @return
	 */
	default ServiceResponse initializeSystem(System system, SystemRequest systemRequest) { return ServiceResponse.success(); }

	/**
	 * This phase is run prior to executing the system update workflows.  Perform any pre checks, validations, or
	 * resource auditing here.
	 * @param system
	 * @return
	 */
	default ServiceResponse prepareUpdateSystem(System system, SystemRequest systemRequest) { return ServiceResponse.success(); }

	/**
	 * This method is executed post update workflows for the given system.
	 * @param system
	 * @return
	 */
	default ServiceResponse updateSystem(System system, SystemRequest systemRequest) { return ServiceResponse.success(); }

	/**
	 * Applies system-level configuration properties (e.g. NTP servers, DNS resolvers,
	 * hostname) to the managed system. This is intentionally distinct from
	 * {@link #updateSystem}, which is reserved for software and firmware update workflows.
	 *
	 * <p>Configuration properties to apply are carried in
	 * {@link SystemRequest#getConfigOptions()}. Plugins should read the keys they care
	 * about and ignore unknown keys — the service layer performs no key validation.</p>
	 *
	 * <p>The Morpheus service layer will:</p>
	 * <ol>
	 *   <li>Set {@code system.configurationWorkflowStatus = 'in-progress'} before calling.</li>
	 *   <li>Merge non-null {@code configOptions} keys into {@code system.config} on success.</li>
	 *   <li>Set {@code configurationWorkflowStatus} to {@code 'completed'} or {@code 'failed'}
	 *       based on the returned {@link ServiceResponse}.</li>
	 * </ol>
	 * Plugins do not need to persist configuration changes themselves.
	 *
	 * <p>The default implementation is a no-op returning {@code ServiceResponse.success()}.
	 * Providers with no configuration to push may leave this default in place.</p>
	 *
	 * @param system  the fully populated plugin model for the target system
	 * @param request carries the active process record and a {@code configOptions} map
	 *                containing the configuration properties to apply
	 * @return {@link ServiceResponse#success()} if configuration was applied;
	 *         {@link ServiceResponse#error(String)} with a human-readable message otherwise
	 */
	default ServiceResponse updateSystemConfiguration(System system, SystemRequest request) { return ServiceResponse.success(); }

	/**
	 * Perform any cleanup/state reset operations required on removal of a system
	 * @param system
	 * @return
	 */
	default ServiceResponse deleteSystem(System system) { return ServiceResponse.success(); }

	/**
	 * This method is bound to a periodic job that is executed by the VME Manager.
	 * @param system
	 * @return
	 */
	default ServiceResponse refreshSystem(System system) { return ServiceResponse.success(); }

	/**
	 * Executed once a day, perform any desirable action on a daily interval
	 * @param system
	 * @return
	 */
	default ServiceResponse refreshSystemDaily(System system) { return ServiceResponse.success(); }

	/**
	 * This method is called when adding an additional component to an existing system (adding a new host, storage array
	 * etc)
	 * @param system
	 * @param systemRequest
	 * @param componentType
	 * @return
	 */
	default ServiceResponse addSystemComponent(System system, SystemRequest systemRequest, SystemComponentType componentType) { return ServiceResponse.success(); }

	/**
	 * This method is called when updating an existing component on a system (e.g. reconfiguring a host, storage array,
	 * etc. that was previously added to the system)
	 * @param system
	 * @param systemRequest
	 * @param componentType
	 * @return
	 */
	default ServiceResponse updateSystemComponent(System system, SystemRequest systemRequest, SystemComponentType componentType) { return ServiceResponse.success(); }

	/**
	 * Applying this facet to a {@link SystemProvider} registers it as a consumer of Central Services
	 * catalog item updates. Morpheus polls the CS catalog for changes (via hash comparison) and calls
	 * {@link #onCatalogItemUpdate} for each changed item, allowing the system plugin to process the
	 * new catalog payload as needed.
	 *
	 * @since 1.4.x
	 */
	interface CatalogItemFacet {

		/**
		 * Called when a CS catalog item has changed (new hash detected).
		 * The system plugin is responsible for determining how to handle the updated payload
		 * (e.g. downloading packages, updating configuration, triggering workflows).
		 *
		 * @param item the catalog item update containing the name, code, hash, payload, and optional version
		 * @return ServiceResponse indicating success or failure of processing the update
		 */
		ServiceResponse onCatalogItemUpdate(SystemCatalogItemUpdate item);
	}
	
	/**
	 * This method is called for executing Drift Checker checks
	 * @param system
	 * @param systemRequest
	 * @return
	 */
	default ServiceResponse executeDriftChecker(System system, SystemRequest systemRequest) {return ServiceResponse.success();}
	
}
