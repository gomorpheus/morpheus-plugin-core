package com.morpheusdata.core;

import com.morpheusdata.model.OptionType;
import com.morpheusdata.model.TaskType;

import java.util.List;

/**
 * Provides a standard set of methods for interacting with cloud integrations or on-prem service providers.
 * This includes syncing assets related to things like VirtualMachines or Containers for various cloud types. For
 * integrating with actual provisioning a {@link ProvisioningProvider} is also available.
 *
 * @author Mike Truso
 */
public interface TaskProvider extends PluginProvider {

	/**
	 * A service class containing task execution logic
	 * @return a task service
	 */
	ExecutableTaskInterface getService();

	TaskType.TaskScope getScope();

	String getDescription();

	/**
	 * A flag indicating if this task can be configured to execute on a remote context
	 * @return boolean
	 */
	Boolean isAllowExecuteLocal();

	/**
	 * A flag indicating if this task can be configured to execute on a remote context
	 * @return boolean
	 */
	Boolean isAllowExecuteRemote();

	/**
	 * A flag indicating if this task can be configured to execute on a resource
	 * @return boolean
	 */
	Boolean isAllowExecuteResource();

	/**
	 * A flag indicating if this task can be configured to execute a script from a git repository
	 * @return boolean
	 */
	Boolean isAllowLocalRepo();

	/**
	 * A flag indicating if this task can be configured with ssh keys
	 * @return boolean
	 */
	Boolean isAllowRemoteKeyAuth();

	/**
	 * Additional task configuration
	 * {@link OptionType}
	 * @return a List of OptionType
	 */
	List<OptionType> getOptionTypes();
}
