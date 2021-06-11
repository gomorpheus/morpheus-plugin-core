package com.morpheusdata.core;

import com.morpheusdata.model.*;
import com.morpheusdata.response.ServiceResponse;
import java.util.List;
import java.util.Map;

/**
 * Provides support for monitoring integrations
 *
 * @since 0.8.0
 * @author bdwheeler
 */
public interface MonitoringProvider extends PluginProvider {

	/**
	 * Validation Method used to validate all inputs applied to the integration of a Monitoring provider upon save.
	 * If an input fails validation or authentication information cannot be verified, Error messages should be returned
	 * via a {@link ServiceResponse} object where the key on the error is the field name and the value is the error message.
	 * If the error is a generic authentication error or unknown error, a standard message can also be sent back in the response.
	 *
	 * @param monitorServer The Integration Object contains all the saved information regarding configuration of the Monitoring Provider.
	 * @param opts any custom payload submission options may exist here
	 * @return A response is returned depending on if the inputs are valid or not.
	 */
	public ServiceResponse verifyMonitorServer(MonitorServer monitorServer, Map opts);

	/**
	 * Called during creation of a {@link MonitorServer} operation. This allows for any custom operations that need
	 * to be performed outside of the standard operations.
	 * @param monitorServer The Integration Object contains all the saved information regarding configuration of the Monitoring Provider.
	 * @param opts any custom payload submission options may exist here
	 * @return A response is returned depending on if the operation was a success or not.
	 */
	public ServiceResponse createMonitorServer(MonitorServer monitorServer, Map opts);

	/**
	 * Called during update of an existing {@link MonitorServer}. This allows for any custom operations that need
	 * to be performed outside of the standard operations.
	 * @param monitorServer The Integration Object contains all the saved information regarding configuration of the Monitoring Provider.
	 * @param opts any custom payload submission options may exist here
	 * @return A response is returned depending on if the operation was a success or not.
	 */
	public ServiceResponse updateMonitorServer(MonitorServer monitorServer, Map opts);

	/**
	 * Periodically called to refresh and sync data coming from the relevant integration. Most integration providers
	 * provide a method like this that is called periodically (typically 5 - 10 minutes).
	 * @param monitorServer The Integration Object contains all the saved information regarding configuration of the Monitoring Provider.
	 */
	void refresh(MonitorServer monitorServer);


	/**
	 * Called on the first save / update of a monitoring server integration. Used to do any initialization of a new integration
	 * Often times this calls the periodic refresh method directly.
	 * @param monitorServer The Integration Object contains all the saved information regarding configuration of the Monitoring Provider.
	 * @param opts an optional map of parameters that could be sent. This may not currently be used and can be assumed blank
	 * @return a ServiceResponse containing the success state of the initialization phase
	 */
	ServiceResponse initializeMonitorServer(MonitorServer monitorServer, Map opts);

	/**
	 * A Monitoring Provider can register monitoring server types
	 * @return a List of {@link MonitorServerType} to be loaded into the Morpheus database.
	 */
	Collection<MonitorServerType> getMonitorServerTypes();

	/**
	 * A Monitoring Provider can register check types for display and capability information when syncing Monitoring checks
	 * @return a List of {@link MonitorCheckType} to be loaded into the Morpheus database.
	 */
	Collection<MonitorCheckType> getMonitorCheckTypes();

	/**
	 * Provide custom configuration options when creating a new {@link AccountIntegration}
	 * @return a List of OptionType
	 */
	List<OptionType> getIntegrationOptionTypes();

}
