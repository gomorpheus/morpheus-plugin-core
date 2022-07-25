package com.morpheusdata.core.backup;

import com.morpheusdata.core.PluginProvider;
import com.morpheusdata.model.*;

import java.util.Collection;

public interface BackupProvider extends PluginProvider {

	/**
	 * Returns the integration logo for display when a user needs to view or add this integration
	 * @return Icon representation of assets stored in the src/assets of the project.
	 */
	Icon getIcon();

	BackupProviderType getBackupProviderType();

	Collection<BackupType> getBackupTypes();
	Collection<ReplicationType> getReplicationTypes();


	Collection<BackupIntegration> getBackupIntegrations();

	Collection<BackupJobProvider> getAvailableBackupJobProviders();
	BackupJobProvider getAvailableBackupJobProvider(String providerCode);
	Collection<BackupExecutionProvider> getAvailableBackupExecutionProviders();
	BackupExecutionProvider getAvailableBackupExecutionProvider(String providerCode);
	Collection<BackupRestoreProvider> getAvailableBackupRestoreProviders();
	BackupRestoreProvider getAvailableBackupRestoreProvider(String providerCode);
}
