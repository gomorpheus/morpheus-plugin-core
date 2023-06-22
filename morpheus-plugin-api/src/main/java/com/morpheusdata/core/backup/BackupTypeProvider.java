package com.morpheusdata.core.backup;

import com.morpheusdata.core.providers.PluginProvider;
import com.morpheusdata.core.backup.util.BackupStatusUtility;
import com.morpheusdata.model.*;
import com.morpheusdata.response.ServiceResponse;

import java.util.Collection;
import java.util.Map;

/**
 * Provider associated with a specific {@link BackupType}. Backup Types handle the differences in cloud providers.
 * For example the backup process for VMware and AWS are very different. These differences may be abstracted out by
 * the external backup provider, but often the provider has separate endpoints for each cloud provider type. Backup Types
 * provide the flexibility to handle the differences in clouds within a single backup provider.
 * @since 0.13.4
 * @author Dustin DeYoung
 */
public interface BackupTypeProvider extends PluginProvider {

	/**
	 * get the type of container compatible with this backup type
	 * @return the container type code
	 */
	String getContainerType();

	/**
	 * Determines if this backup type supports copying the backup to a datastore (export).
	 * @return boolean indicating copy to store is supported
	 */
	Boolean getCopyToStore();

	/**
	 * Determines if this backup type supports downloading the backups for this backup type.
	 * @return boolean indicating downloading is enabled
	 */
	Boolean getDownloadEnabled();

	/**
	 * Determines if this backup type supports restoring a backup to the existing workload.
	 * @return boolean indicating restore to existing is enabled
	 */
	Boolean getRestoreExistingEnabled();

	/**
	 * Determines if this backup type supports restoring to a new workload rather than replacing the existing workload.
	 * @return boolean indicating restore to new is enabled
	 */
	Boolean getRestoreNewEnabled();

	/**
	 * Indicates the type of restore supported. Current options include: new, existing, online, offline, migration, failover
	 * @return the supported restore type
	 */
	String getRestoreType();

	/**
	 * Get the desired method of restoring a backup to a new instance.
	 * <p>
	 * Available options:
	 * 		<ul>
	 * 			<li>
	 * 			 	DEFAULT -- Uses the backup as an input to the instance provision process. Generally this
	 * 			 				involves using a snapshot or image ID as the image used for provisioning, but the precise
	 * 			 				details are left up to the backup provider.
	 * 			</li>
	 * 		 	<li>
	 * 		 	    VM_RESTORE -- The external backup provider restores to a VM and the core system will associate the resulting
	 * 		 	    				VM to the internal resources.
	 * 		 	</li>
	 * 		 	<li>
	 * 		 	  	TEMP_EXTRACT -- determines the visibility of the restore to new option
	 * 		 	</li>
	 * 		</ul>
	 *
	 * @return
	 */
	String getRestoreNewMode();

	/**
	 * Does this backup type provider support copy to store?
	 * @return boolean indicating copy to store support
	 */
	Boolean getHasCopyToStore();

	/**
	 * A list of {@link OptionType OptionTypes} for use in the backup create and edit forms.
	 * @return a list of option types
	 */
	Collection<OptionType> getOptionTypes();

	// TODO future
	// Collection<StorageServerType> getStorageServerTypes();

	/**
	 * Refresh the provider with the associated data in the external system.
	 * @param authConfig  necessary connection and credentials to connect to the external provider
	 * @param backupProvider an instance of the backup integration provider
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a success value of 'false' will indicate the
	 * refresh process has failed and will halt any further backup creation processes in the core system.
	 */
	public ServiceResponse refresh(Map authConfig, com.morpheusdata.model.BackupProvider backupProvider);

	/**
	 * Clean up all data created by the backup type provider.
	 * @param backupProvider the provider to be cleaned up
	 * @param opts additional options
	 * @return a {@link ServiceResponse} object
	 */
	ServiceResponse clean(com.morpheusdata.model.BackupProvider backupProvider, Map opts);

	// Backup Execution

	/**
	 * Add additional configurations to a backup. Morpheus will handle all basic configuration details, this is a
	 * convenient way to add additional configuration details specific to this backup provider.
	 * @param backup the current backup the configurations are applied to.
	 * @param config the configuration supplied by external inputs.
	 * @param opts optional parameters used for configuration.
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a false success will indicate a failed
	 * configuration and will halt the backup creation process.
	 */
	public ServiceResponse configureBackup(Backup backup, Map config, Map opts);

	/**
	 * Validate the configuration of the backup. Morpheus will validate the backup based on the supplied option type
	 * configurations such as required fields. Use this to either override the validation results supplied by the
	 * default validation or to create additional validations beyond the capabilities of option type validation.
	 * @param backup the backup to validate
	 * @param config the original configuration supplied by external inputs.
	 * @param opts optional parameters used for
	 * @return a {@link ServiceResponse} object. The errors field of the ServiceResponse is used to send validation
	 * results back to the interface in the format of {@code errors['fieldName'] = 'validation message' }. The msg
	 * property can be used to send generic validation text that is not related to a specific field on the model.
	 * A ServiceResponse with any items in the errors list or a success value of 'false' will halt the backup creation
	 * process.
	 */
	ServiceResponse validateBackup(Backup backup, Map config, Map opts);

	/**
	 * Create the backup resources on the external provider system.
	 * @param backup the fully configured and validated backup
	 * @param opts additional options used during backup creation
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a success value of 'false' will indicate the
	 * creation on the external system failed and will halt any further backup creation processes in morpheus.
	 */
	ServiceResponse createBackup(Backup backup, Map opts);

	/**
	 * Delete the backup resources on the external provider system.
	 * @param backup the backup details
	 * @param opts additional options used during the backup deletion process
	 * @return a {@link ServiceResponse} indicating the results of the deletion on the external provider system.
	 * A ServiceResponse object with a success value of 'false' will halt the deletion process and the local refernce
	 * will be retained.
	 */
	ServiceResponse deleteBackup(Backup backup, Map opts);

	/**
	 * Delete the results of a backup execution on the external provider system.
	 * @param backupResult the backup result details
	 * @param opts additional options used during the backup result deletion process
	 * @return a {@link ServiceResponse} indicating the results of the deletion on the external provider system.
	 * A ServiceResponse object with a success value of 'false' will halt the deletion process and the local refernce
	 * will be retained.
	 */
	ServiceResponse deleteBackupResult(BackupResult backupResult, Map opts);

	/**
	 * A hook into the execution process. This method is called before the backup execution occurs.
	 * @param backup the backup details associated with the backup execution
	 * @param opts additional options used during the backup execution process
	 * @return a {@link ServiceResponse} indicating the success or failure of the execution preperation. A success value
	 * of 'false' will halt the execution process.
	 */
	ServiceResponse prepareExecuteBackup(Backup backup, Map opts);

	/**
	 * Provide additional configuration on the backup result. The backup result is a representation of the output of
	 * the backup execution including the status and a reference to the output that can be used in any future operations.
	 * @param backupResult the backup result details
	 * @param opts additional options used during the backup execution process
	 * @return a {@link ServiceResponse} indicating the success or failure of the method. A success value
	 * of 'false' will halt the further execution process.
	 */
	ServiceResponse prepareBackupResult(BackupResult backupResult, Map opts);

	/**
	 * Initiate the backup process on the external provider system.
	 * @param backup the backup details associated with the backup execution.
	 * @param backupResult the details associated with the results of the backup execution.
	 * @param executionConfig original configuration supplied for the backup execution.
	 * @param cloud cloud context of the target of the backup execution
	 * @param computeServer the target of the backup execution
	 * @param opts additional options used during the backup execution process
	 * @return a {@link ServiceResponse} indicating the success or failure of the backup execution. A success value
	 * of 'false' will halt the execution process.
	 */
	ServiceResponse executeBackup(Backup backup, BackupResult backupResult, Map executionConfig, Cloud cloud, ComputeServer computeServer, Map opts);

	/**
	 * Periodically call until the backup execution has successfully completed. The default refresh interval is 60 seconds.
	 * @param backupResult the reference to the results of the backup execution including the last known status. Set the
	 *                     status to a canceled/succeeded/failed value from one of the {@link BackupStatusUtility} values
	 *                     to end the execution process.
	 * @return a {@link ServiceResponse} indicating the success or failure of the method. A success value
	 * of 'false' will halt the further execution process.n
	 */
	ServiceResponse refreshBackupResult(BackupResult backupResult);

	/**
	 * Cancel the backup execution process without waiting for a result.
	 * @param backupResult the details associated with the results of the backup execution.
	 * @param opts additional options.
	 * @return a {@link ServiceResponse} indicating the success or failure of the backup execution cancellation.
	 */
	ServiceResponse cancelBackup(BackupResult backupResult, Map opts);

	/**
	 * Extract the results of a backup. This is generally used for packaging up a full backup for the purposes of
	 * a download or full archive of the backup.
	 * @param backupResult the details associated with the results of the backup execution.
	 * @param opts additional options.
	 * @return a {@link ServiceResponse} indicating the success or failure of the backup extraction.
	 */
	ServiceResponse extractBackup(BackupResult backupResult, Map opts);


	// Backup Restore

	/**
	 * Add additional configurations to a backup restore. Morpheus will handle all basic configuration details, this is a
	 * convenient way to add additional configuration details specific to this backup restore provider.
	 * @param backupResult backup result to be restored
	 * @param config the configuration supplied by external inputs
	 * @param opts optional parameters used for configuration.
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a false success will indicate a failed
	 * configuration and will halt the backup restore process.
	 */
	ServiceResponse configureRestoreBackup(BackupResult backupResult, Map config, Map opts);

	/**
	 * Build the configuration for the restored instance.
	 * @param backupResult backup result to be restored
	 * @param instance the instance the backup was created from, if it still exists. Retained backups will not have a reference to the instance.
	 * @param restoreConfig the restore configuration generated by morpheus.
	 * @param opts optional parameters used for configuration.
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a false success will indicate a failed
	 * configuration and will halt the backup restore process.
	 */
	ServiceResponse getBackupRestoreInstanceConfig(BackupResult backupResult, Instance instance, Map restoreConfig, Map opts);

	/**
	 * Verify the backup restore is valid. Generally used to check if the backup and instance are both in a state
	 * compatible for executing the restore process.
	 * @param backupResult backup result to be restored
	 * @param opts optional parameters used for configuration.
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a false success will indicate a failed
	 * configuration and will halt the backup restore process.
	 */
	ServiceResponse validateRestoreBackup(BackupResult backupResult, Map opts);

	/**
	 * Get restore options to configure the restore wizard. Although the {@link com.morpheusdata.core.backup.BackupProvider } and
	 * {@link com.morpheusdata.core.backup.BackupTypeProvider} supply configuration, there may be situations where the instance
	 * configuration will determine which options need to be presented in the restore wizard.
	 * <p>
	 * 		Available Restore options:
	 * 		<ul>
	 * 		 	<li>
	 * 		 	    restoreExistingEnabled (Boolean) -- determines the visibility of the restore to existing option
	 * 		 	</li>
	 * 		 	<li>
	 * 		 	  	restoreNewEnabled (Boolean) -- determines the visibility of the restore to new option
	 * 		 	</li>
	 * 		 	<li>
	 * 		 	  	name (String) -- default name of the restored instance
	 * 		 	</li>
	 * 		 	<li>
	 * 		 		hostname (String) -- default hostname of the restored instance
	 * 		 	</li>
	 * 		</ul>
	 * @param backup the backup
	 * @param opts optional parameters
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a false success will indicate a failed
	 * configuration and will halt the backup restore process.
	 */
	ServiceResponse getRestoreOptions(Backup backup, Map opts);

	/**
	 * Execute the backup restore on the external system
	 * @param backupRestore restore to be executed
	 * @param backupResult refernce to the backup result
	 * @param backup reference to the backup associated with the backup result
	 * @param opts optional parameters
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a false success will indicate a failed
	 * configuration and will halt the backup restore process.
	 */
	ServiceResponse restoreBackup(BackupRestore backupRestore, BackupResult backupResult, Backup backup, Map opts);

	/**
	 * Periodically check for any updates to an in-progress restore. This method will be executed every 60 seconds for
	 * the restore while the restore has a status of `START_REQUESTED` or `IN_PROGRESS`. Any other status will indicate
	 * the restore has completed and does not need to be refreshed. The primary use case for this method is long-running
	 * restores to avoid consuming resources during the restore process.
	 * @param backupRestore the running restore
	 * @param backupResult backup result referencing the backup to be restored
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a false success will indicate a failed
	 * configuration and will halt the backup restore process.
	 */
	ServiceResponse refreshBackupRestoreResult(BackupRestore backupRestore, BackupResult backupResult);


	// Replication Groups
	// Replication Sites
	// Replication Execution

}
