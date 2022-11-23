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
	 * This is the old way and is no longer being used
	 * @deprecated
	 * @return
	 */
	String getViewSet();

	/**
	 * Sets the enabled state of the provider for consumer use.
	 * NOTE: This may go away as this should be implied by the installation state of the plugin
	 * @return if this provider is usable.
	 */
	Boolean getEnabled();

	/**
	 * Is this backup provider creatable by the end user. This could be false for providers that may be
	 * forced by specific CloudProvider plugins, for example.
	 * @return the creatable state
	 */
	Boolean getCreatable();

	/**
	 * Does this provider allow the end user to download a backup. Some backup providers can allow the
	 * user to download the backup archive file.
	 * @return whether the backup can be downloaded.
	 */
	Boolean getDownloadEnabled();

	Boolean getHasAddToJob();

	Boolean getHasBackups();

	Boolean hasCancelBackup();

	Boolean getHasCloneJob();

	Boolean getHasCopyToStore();

	Boolean getHasCreateJob();

	Boolean getHasJobs();

	String getDefaultJobType();

	Boolean getHasOptionalJob();

	Boolean getHasReplication();

	Boolean getHasReplicationGroups();

	Boolean getHasRepositories();

	Boolean getHasRetentionCount();

	Boolean getHasSchedule();

	Boolean getHasServers();

	Boolean getHasSites();

	Boolean hasStorageProvider();

	Boolean getHasStreamToStore();

	Boolean getRestoreExistingEnabled();

	Boolean getRestoreNewEnabled();

	Collection<OptionType> getOptionTypes();

	Collection<OptionType> getReplicationGroupOptionTypes();

	Collection<OptionType> getReplicationOptionTypes();

	Collection<OptionType> getBackupJobOptionTypes();

	Collection<OptionType> getBackupOptionTypes();

	Collection<OptionType> getInstanceReplicationGroupOptionTypes();

	void addScopedProvider(BackupIntegration backupIntegration);

	/**
	 * Used to register {@link BackupTypeProvider} implementations by backup type
	 * @param backupTypeProvider the current backup type provider instance
	 * @param provisionTypeCode the provision type code unique to the specific types of workloads this is scoped to
	 * @param containerTypeCode optional workload type code in the event the backup type is very specific to a workload type.
	 */
	void addScopedProvider(BackupTypeProvider backupTypeProvider, String provisionTypeCode, String containerTypeCode);

	Collection<BackupIntegration> getScopedProviders();

	// Provider
	ServiceResponse configureBackupProvider(com.morpheusdata.model.BackupProvider backupProviderModel, Map config, Map opts);

	ServiceResponse validateBackupProvider(com.morpheusdata.model.BackupProvider backupProviderModel, Map opts);

	ServiceResponse initializeBackupProvider(com.morpheusdata.model.BackupProvider backupProviderModel, Map opts);

	ServiceResponse updateBackupProvider(com.morpheusdata.model.BackupProvider backupProviderModel, Map opts);

	ServiceResponse deleteBackupProvider(com.morpheusdata.model.BackupProvider backupProviderModel, Map opts);

	/**
	 * The main refresh method called periodically by Morpheus to sync any necessary objects from the integration.
	 * This can call sub services for better organization, and it is recommended that {@link com.morpheusdata.core.util.SyncTask} is used.
	 * @param backupProvider the current instance of the backupProvider being refreshed
	 * @return the success state of the refresh
	 */
	ServiceResponse refresh(com.morpheusdata.model.BackupProvider backupProvider);

	// jobs
	ServiceResponse configureBackupJob(BackupJob backupJobModel, Map config, Map opts);

	ServiceResponse validateBackupJob(BackupJob backupJobModel, Map config, Map opts);

	ServiceResponse createBackupJob(BackupJob backupJobModel, Map opts);

	ServiceResponse cloneBackupJob(BackupJob sourceBackupJobModel, BackupJob backupJobModel, Map opts);

	ServiceResponse addToBackupJob(BackupJob backupJobModel, Map opts);

	ServiceResponse deleteBackupJob(BackupJob backupJobModel, Map opts);

	ServiceResponse executeBackupJob(BackupJob backupJobModel, Map opts);

}
