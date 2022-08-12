package com.morpheusdata.core.backup;

import com.morpheusdata.core.PluginProvider;
import com.morpheusdata.model.*;
import com.morpheusdata.response.ServiceResponse;

import java.util.*;

public interface BackupProvider extends PluginProvider {

	// Associations between cloud type and backup providers
	Collection<BackupIntegration> scopedProviders = new ArrayList<BackupIntegration>();

	/**
	 * Returns the integration logo for display when a user needs to view or add this integration
	 * @return Icon representation of assets stored in the src/assets of the project.
	 */
	Icon getIcon();

	String getViewSet();

	Boolean getEnabled();

	Boolean getCreatable();

	Boolean getDownloadEnabled();

	Boolean getHasAddToJob();

	Boolean getHasBackups();

	Boolean hasCancelBackup();

	Boolean getHasCloneJob();

	Boolean getHasCopyToStore();

	Boolean getHasCreateJob();

	Boolean getHasJobs();

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

	void addScopedProvider(BackupTypeProvider backupTypeProvider, String provisionTypeCode, String containerTypeCode);

	Collection<BackupIntegration> getScopedProviders();

	// Provider
	ServiceResponse configureBackupProvider(com.morpheusdata.model.BackupProvider backupProviderModel, Map config, Map opts);

	ServiceResponse validateBackupProvider(com.morpheusdata.model.BackupProvider backupProviderModel, Map opts);

	ServiceResponse initializeBackupProvider(com.morpheusdata.model.BackupProvider backupProviderModel, Map opts);

	ServiceResponse updateBackupProvider(com.morpheusdata.model.BackupProvider backupProviderModel, Map opts);

	ServiceResponse deleteBackupProvider(com.morpheusdata.model.BackupProvider backupProviderModel, Map opts);

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
