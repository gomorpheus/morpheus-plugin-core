package com.morpheusdata.core;

import com.morpheusdata.model.OptionType;
import com.morpheusdata.model.Workload;
import com.morpheusdata.response.ServiceResponse;

import javax.xml.ws.Service;
import java.util.Collection;
import java.util.Map;

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

	/**
	 * Provides a unique code for the implemented provisioning provider  (i.e. vmware, azure,ecs,etc.)
	 * @return unique String code
	 */
	public String getProvisionTypeCode();

	/**
	 * Returns the Name of the Provisioning Provider
	 * @return
	 */
	public String getName();


	/**
	 * Determines if this provision type has datastores that can be selected or not.
	 * @return Boolean representation of whether or not this provision type has datastores
	 */
	public Boolean hasDatastores();


	/**
	 * Determines if this provision type has networks that can be selected or not.
	 * @return Boolean representation of whether or not this provision type has datastores
	 */
	public Boolean hasNetworks();

	/**
	 * Returns the maximum number of network interfaces that can be chosen when provisioning with this type
	 * @return maximum number of networks or 0,null if unlimited.
	 */
	public Integer getMaxNetworks();


	/**
	 * Validates the provided provisioning options of a workload
	 * @param opts
	 * @return
	 */
	ServiceResponse validateWorkload(Map opts);


	/**
	 * This method is a key entry point in provisioning a workload. This could be a vm, a container, or something else.
	 * Information associated with the passed Workload object is used to kick off the workload provision request
	 * @param workload the Workload object we intend to provision along with some of the associated data needed to determine
	 *                 how best to provision the workload
	 * @param opts additional configuration options that may have been passed during provisioning
	 * @return
	 */
	ServiceResponse runWorkload(Workload workload, Map opts);

	/**
	 * Issues the remote calls necessary top stop a workload element from running.
	 * @param workload the Workload we want to shut down
	 * @return
	 */
	ServiceResponse stopWorkload(Workload workload);

	/**
	 * Issues the remote calls necessary to start a workload element for running.
	 * @param workload the Workload we want to start up.
	 * @return
	 */
	ServiceResponse startWorkload(Workload workload);

	/**
	 * Issues the remote calls to restart a workload element. In some cases this is just a simple alias call to do a stop/start,
	 * however, in some cases cloud providers provide a direct restart call which may be preferred for speed.
	 * @param workload the Workload we want to restart.
	 * @return
	 */
	ServiceResponse restartWorkload(Workload workload);

	/**
	 * This is the key method called to destroy / remove a workload. This should make the remote calls necessary to remove any assets
	 * associated with the workload.
	 * @param workload
	 * @param opts
	 * @return
	 */
	ServiceResponse removeWorkload(Workload workload,Map opts);
}
