package com.morpheusdata.core.backup;

import com.morpheusdata.core.PluginProvider;
import com.morpheusdata.model.*;
import com.morpheusdata.response.ServiceResponse;

import java.util.*;

/**
 * This is the main entrypoint for registering a backup provider. Morpheus models backup integrations as a tree.
 * A BackupProvider can have many backup types and therefore needs backup type providers scoped to specific types.
 * Typically, it is best to extend the {@link AbstractBackupProvider} rather than directly implementing some of these methods.
 *
 * @see AbstractBackupProvider
 * @since 0.12.2
 * @author Dustin DeYoung
 */
public interface BackupProvider extends PluginProvider {

	// Associations between cloud type and backup providers
	Collection<BackupIntegration> scopedProviders = new ArrayList<BackupIntegration>();

	/**
	 * Returns the integration logo for display when a user needs to view or add this integration
	 * @return Icon representation of assets stored in the src/assets of the project.
	 */
	Icon getIcon();

	/**
	 * Returns a viewset for reference custom inputs when setting up a backup provider during add instance.
	 * This is the old way and is no longer being used.
	 * @deprecated
	 */
	@Deprecated
	String getViewSet();

	/**
	 * Sets the enabled state of the provider for consumer use.
	 * NOTE: This may go away as it should be implied by the installation state of the plugin
	 */
	Boolean getEnabled();

	/**
	 * The backup provider is creatable by the end user. This could be false for providers that may be
	 * forced by specific CloudProvider plugins, for example.
	 */
	Boolean getCreatable();

	/**
	 * The backup provider allows the end user to download a backup. Some backup providers can allow the
	 * user to download the backup archive file.
	 */
	Boolean getDownloadEnabled();

	/**
	 * The backup provider can add a workload backup to an existing job.
	 */
	Boolean getHasAddToJob();

	/**
	 * The backup provider supports backups. For example, a backup provider may be intended for disaster recovery failover
	 * only and may not directly support backups.
	 */
	Boolean getHasBackups();

	/**
	 * The backup provider has the ability to cancel an in-progress backup.
	 */
	Boolean hasCancelBackup();

	/**
	 * The backup provider supports cloning a job from an existing job.
	 */
	Boolean getHasCloneJob();

	/**
	 * The backup provider supports copying the backup to a separate datastore (export).
	 */
	Boolean getHasCopyToStore();

	/**
	 * The backup provider supports creating new jobs.
	 */
	Boolean getHasCreateJob();

	/**
	 * The backup provider supports running multiple workload backups within an encapsulating job.
	 */
	Boolean getHasJobs();

	/**
	 * The default job type of the provider. The basic options are: new, clone, addTo, none
	 */
	String getDefaultJobType();

	/**
	 * The backup provider supports backups outside an encapsulating job.
	 */
	Boolean getHasOptionalJob();

	/**
	 * The backup provider supports replication.
	 */
	Boolean getHasReplication();

	/**
	 * The backup provider supports replication groups.
	 */
	Boolean getHasReplicationGroups();

	/**
	 * The backup provider supports multiple repositories for storage of backup assets. This is used for the display and
	 * selection of repositories during backup configuration steps.
	 */
	Boolean getHasRepositories();

	/**
	 * The backup provider supports retention counts for maintaining the desired number of backups.
	 */
	Boolean getHasRetentionCount();

	/**
	 * The backup provider supports scheduled backups. This is primarily used for display of hte schedules and providing
	 * options during the backup configuration steps.
	 */
	Boolean getHasSchedule();

	// TODO: verify doc
	/**
	 * The backup provider supports backup servers.
	 */
	Boolean getHasServers();

	// TODO: verify doc
	/**
	 * The backup provider supports backup sites.
	 */
	Boolean getHasSites();

	/**
	 * The backup provider supports multiple storage providers.
	 */
	Boolean hasStorageProvider();

	// TODO: verify doc
	/**
	 * The backup provider supports streaming directly to the datastore.
	 */
	Boolean getHasStreamToStore();

	/**
	 * The backup provider supports restoring to an existing workload.
	 */
	Boolean getRestoreExistingEnabled();

	/**
	 * The backup provider supports restoring to a new workload.
	 */
	Boolean getRestoreNewEnabled();

	/**
	 * Get the list of option types for the backup provider. The option types are used for creating and updating an
	 * instance of the backup provider.
	 */
	Collection<OptionType> getOptionTypes();

	/**
	 * Get the list of replication group option types for the backup provider. The option types are used for creating and updating
	 * replication groups.
	 */
	Collection<OptionType> getReplicationGroupOptionTypes();

	/**
	 * Get the list of replication option types for the backup provider. The option types are used for creating and updating
	 * replications.
	 */
	Collection<OptionType> getReplicationOptionTypes();

	/**
	 * Get the list of backup job option types for the backup provider. The option types are used for creating and updating
	 * backup jobs.
	 */
	Collection<OptionType> getBackupJobOptionTypes();

	/**
	 * Get the list of backup option types for the backup provider. The option types are used for creating and updating
	 * backups.
	 */
	Collection<OptionType> getBackupOptionTypes();

	// TODO: verify doc
	/**
	 * Get the list of replication group option types for the backup provider. The option types are used for creating
	 * replications on an instance during provisioning.
	 */
	Collection<OptionType> getInstanceReplicationGroupOptionTypes();

	/**
	 * Add a scoped backup provider by {@link BackupIntegration}
	 * @param backupIntegration the scoped provider to add
	 */
	void addScopedProvider(BackupIntegration backupIntegration);

	/**
	 * Register a scoped provider which associates a {@link BackupTypeProvider} to one or both of a {@link ProvisionType}
	 * and a {@link ContainerType}.
	 * @param backupTypeProvider the current backup type provider instance
	 * @param provisionTypeCode the provision type code unique to the specific types of workloads this is scoped to
	 * @param containerTypeCode optional workload type code in the event the backup type is very specific to a workload type.
	 */
	void addScopedProvider(BackupTypeProvider backupTypeProvider, String provisionTypeCode, String containerTypeCode);

	/**
	 * Get a list of all registered scoped providers
	 * @return list of scoped providers
	 */
	Collection<BackupIntegration> getScopedProviders();

	// Provider

	/**
	 * Apply provider specific configurations to a {@link BackupProvider}. The standard configurations are handled by the core system.
	 * @param backupProviderModel backup provider to configure
	 * @param config the configuration supplied by external inputs.
	 * @param opts optional parameters used for configuration.
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a false success will indicate a failed
	 * configuration and will halt the backup creation process.
	 */
	ServiceResponse configureBackupProvider(com.morpheusdata.model.BackupProvider backupProviderModel, Map config, Map opts);

	/**
	 * Validate the configuration of the {@link com.morpheusdata.model.BackupProvider}. Morpheus will validate the backup based on the supplied option type
	 * configurations such as required fields. Use this to either override the validation results supplied by the
	 * default validation or to create additional validations beyond the capabilities of option type validation.
	 * @param backupProviderModel backup provider to validate
	 * @param opts additional options
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a false success will indicate a failed
	 * validation and will halt the backup provider creation process.
	 */
	ServiceResponse validateBackupProvider(com.morpheusdata.model.BackupProvider backupProviderModel, Map opts);

	/**
	 * Execute operations upon initialization of a new backup provider. This method is executed after a successful save
	 * and is used to setup any additional require resources and execute the first full integration sync.
	 * @param backupProviderModel backup provider initializing
	 * @param opts additional options
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a false success will indicate a failed
	 * initialization and will halt the process.
	 */
	ServiceResponse initializeBackupProvider(com.morpheusdata.model.BackupProvider backupProviderModel, Map opts);

	/**
	 * Update the backup provider
	 * @param backupProviderModel the backup provider
	 * @param opts additional options
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a false success will indicate a failed
	 * update and will halt the process.
	 */
	ServiceResponse updateBackupProvider(com.morpheusdata.model.BackupProvider backupProviderModel, Map opts);

	/**
	 * Delete the backup provider. Typically used to clean up any provider specific data that will not be cleaned
	 * up by the default remove in the core system.
	 * @param backupProviderModel the backup provider being removed
	 * @param opts additional options
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a false success will indicate a failed
	 * delete and will halt the process.
	 */
	ServiceResponse deleteBackupProvider(com.morpheusdata.model.BackupProvider backupProviderModel, Map opts);

	/**
	 * The main refresh method called periodically by Morpheus to sync any necessary objects from the integration.
	 * This can call sub services for better organization. It is recommended that {@link com.morpheusdata.core.util.SyncTask} is used.
	 * @param backupProvider the current instance of the backupProvider being refreshed
	 * @return the success state of the refresh
	 */
	ServiceResponse refresh(com.morpheusdata.model.BackupProvider backupProvider);

	// jobs

	/**
	 * Apply provider specific configurations to a {@link BackupJob}. The standard configurations are handled by the core system.
	 * @param backupJobModel the backup job to apply the configuration changes to
	 * @param config the configuration supplied by external inputs.
	 * @param opts optional parameters used for configuration.
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a false success will indicate a failed
	 * configuration and will halt the backup creation process.
	 */
	ServiceResponse configureBackupJob(BackupJob backupJobModel, Map config, Map opts);

	/**
	 * Validate the configuration of the {@link BackupJob}. Morpheus will validate the backup based on the supplied option type
	 * configurations such as required fields. Use this to either override the validation results supplied by the
	 * default validation or to create additional validations beyond the capabilities of option type validation.
	 * @param backupJobModel the backup job to validate
	 * @param config the original configuration supplied by external inputs.
	 * @param opts optional parameters used for
	 * @return a {@link ServiceResponse} object. The errors field of the ServiceResponse is used to send validation
	 * results back to the interface in the format of {@code errors['fieldName'] = 'validation message' }. The msg
	 * property can be used to send generic validation text that is not related to a specific field on the model.
	 * A ServiceResponse with any items in the errors list or a success value of 'false' will halt the backup job
	 * creation process.
	 */
	ServiceResponse validateBackupJob(BackupJob backupJobModel, Map config, Map opts);

	/**
	 * Create the {@link BackupJob} on the external provider system.
	 * @param backupJobModel the fully configured and validated backup job
	 * @param opts additional options used during backup job creation
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a success value of 'false' will indicate the
	 * creation on the external system failed and will halt any further processing in Morpheus.
	 */
	ServiceResponse createBackupJob(BackupJob backupJobModel, Map opts);

	/**
	 * Clone the {@link BackupJob} on the external system.
	 * @param sourceBackupJobModel the source backup job for cloning
	 * @param backupJobModel the backup job that will be associated to the cloned backup job. The externalId of the
	 *                       backup job should be set with the ID of the cloned job result.
	 * @param opts additional options used during backup job clone
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a success value of 'false' will indicate the
	 * clone on the external system failed and will halt any further processing in Morpheus.
	 */
	ServiceResponse cloneBackupJob(BackupJob sourceBackupJobModel, BackupJob backupJobModel, Map opts);

	/**
	 * Add a backup to an existing backup job in the external system.
	 * @param backupJobModel the backup job receiving the additional backup
	 * @param opts additional options
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a success value of 'false' will indicate the
	 * operation on the external system failed and will halt any further processing in Morpheus.
	 */
	ServiceResponse addToBackupJob(BackupJob backupJobModel, Map opts);

	/**
	 * Delete the backup job in the external system.
	 * @param backupJobModel the backup job to be removed
	 * @param opts additional options
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a success value of 'false' will indicate the
	 * operation on the external system failed and will halt any further processing in Morpheus.
	 */
	ServiceResponse deleteBackupJob(BackupJob backupJobModel, Map opts);

	/**
	 * Execute the backup job on the external system.
	 * @param backupJobModel the backup job to be executed
	 * @param opts additional options
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a success value of 'false' will indicate the
	 * operation on the external system failed and will halt any further processing in Morpheus.
	 */
	ServiceResponse executeBackupJob(BackupJob backupJobModel, Map opts);

}
