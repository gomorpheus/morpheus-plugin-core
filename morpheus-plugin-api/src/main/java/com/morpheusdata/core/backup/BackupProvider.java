package com.morpheusdata.core.backup;

import com.morpheusdata.core.PluginProvider;
import com.morpheusdata.model.BackupType;
import com.morpheusdata.model.Icon;
import com.morpheusdata.model.OptionType;
import com.morpheusdata.model.ReplicationType;

import java.util.Collection;

public interface BackupProvider extends PluginProvider {

	Collection<BackupTypeProvider> getAvailableBackupTypeProviders();
	BackupTypeProvider getAvailableBackupTypeProvider(String providerCode);
	Collection<BackupJobProvider> getAvailableBackupJobProviders();
	BackupJobProvider getAvailableBackupJobProvider(String providerCode);
	Collection<BackupExecutionProvider> getAvailableBackupExecutionProviders();
	BackupExecutionProvider getAvailableBackupExecutionProvider(String providerCode);
	Collection<BackupRestoreProvider> getAvailableBackupRestoreProviders();
	BackupRestoreProvider getAvailableBackupRestoreProvider(String providerCode);

	String providerService();

	String jobService();
	Boolean hasCopyToStore();
	Boolean hasStreamToStore();
	Boolean downloadEnabled();
	Boolean restoreExistingEnabled();
	Boolean restoreNewEnabled();
	Boolean hasBackups();
	Boolean hasReplication();
	Boolean hasServers();
	Boolean hasRepositories();
	Boolean hasJobs();
	Boolean hasSites();
	Boolean hasReplicationGroups();
	Boolean hasCreateJob();
	Boolean hasCloneJob();
	Boolean hasAddToJob();
	Boolean hasOptionalJob();
	Boolean hasSchedule();
	Boolean hasStorageProvider();
	Boolean hasRetentionCount();
	Boolean hasCancelBackup();


	Collection<BackupType> backupTypes();
	Collection<ReplicationType> replicationTypes();

	Collection<OptionType> OptionTypes();
	Collection<OptionType> replicationGroupOptions();
	Collection<OptionType> replicationOptions();
	Collection<OptionType> backupJobOptions();
	Collection<OptionType> backupOptions();
	Collection<OptionType> instanceReplicationGroupOptions();

	/**
	 * Returns the integration logo for display when a user needs to view or add this integration
	 * @return Icon representation of assets stored in the src/assets of the project.
	 */
	Icon getIcon();

}
