package com.morpheusdata.core.backup;

import com.morpheusdata.core.*;
import com.morpheusdata.model.BackupType;
import com.morpheusdata.model.OptionType;
import com.morpheusdata.model.ReplicationType;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Provides a standard set of methods for interacting with backup providers.
 * @since 0.12.2
 * @author Dustin DeYoung
 */
public abstract class AbstractBackupProvider implements BackupProvider {

	Plugin plugin;
	MorpheusContext morpheusContext;

	public AbstractBackupProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin;
		this.morpheusContext = context;
	}

	@Override
	public MorpheusContext getMorpheus() {
		return morpheusContext;
	}

	@Override
	public Plugin getPlugin() {
		return plugin;
	}

	@Override
	public String providerService() {
		return this.getCode();
	}

	@Override
	public Collection<BackupTypeProvider> getAvailableBackupTypeProviders() {
		Collection<PluginProvider> pluginProviders = plugin.getProvidersByType(BackupTypeProvider.class);
		Collection<BackupTypeProvider> backupTypeProviders = new ArrayList<>();
		for(PluginProvider pluginProvider:pluginProviders) {
			backupTypeProviders.add((BackupTypeProvider)pluginProvider);
		}
		return backupTypeProviders;
	}

	@Override
	public BackupTypeProvider getAvailableBackupTypeProvider(String providerCode) {
		Collection<BackupTypeProvider> typeProviders = getAvailableBackupTypeProviders();
		BackupTypeProvider matchedProvider = null;
		for(BackupTypeProvider typeProvider:typeProviders) {
			if(typeProvider.getCode() == providerCode) {
				matchedProvider = typeProvider;
			}
		}

		return matchedProvider;
	}

	@Override
	public Collection<BackupJobProvider> getAvailableBackupJobProviders() {
		Collection<PluginProvider> pluginProviders = plugin.getProvidersByType(BackupJobProvider.class);
		Collection<BackupJobProvider> backupJobProviders = new ArrayList<>();
		for(PluginProvider pluginProvider:pluginProviders) {
			backupJobProviders.add((BackupJobProvider)pluginProvider);
		}
		return backupJobProviders;
	}

	@Override
	public BackupJobProvider getAvailableBackupJobProvider(String providerCode) {
		Collection<BackupJobProvider> jobProviders = getAvailableBackupJobProviders();
		BackupJobProvider matchedProvider = null;
		for(BackupJobProvider jobProvider:jobProviders) {
			if(jobProvider.getCode() == providerCode) {
				matchedProvider = jobProvider;
			}
		}

		return matchedProvider;
	}

	@Override
	public Collection<BackupExecutionProvider> getAvailableBackupExecutionProviders() {
		Collection<PluginProvider> pluginProviders = plugin.getProvidersByType(BackupJobProvider.class);
		Collection<BackupExecutionProvider> backupExecutionProviders = new ArrayList<>();
		for(PluginProvider pluginProvider:pluginProviders) {
			backupExecutionProviders.add((BackupExecutionProvider)pluginProvider);
		}
		return backupExecutionProviders;
	}

	@Override
	public BackupExecutionProvider getAvailableBackupExecutionProvider(String providerCode) {
		Collection<BackupExecutionProvider> jobProviders = getAvailableBackupExecutionProviders();
		BackupExecutionProvider matchedProvider = null;
		for(BackupExecutionProvider jobProvider:jobProviders) {
			if(jobProvider.getCode() == providerCode) {
				matchedProvider = jobProvider;
			}
		}

		return matchedProvider;
	}

	@Override
	public Collection<BackupRestoreProvider> getAvailableBackupRestoreProviders() {
		Collection<PluginProvider> pluginProviders = plugin.getProvidersByType(BackupJobProvider.class);
		Collection<BackupRestoreProvider> backupRestoreProviders = new ArrayList<>();
		for(PluginProvider pluginProvider:pluginProviders) {
			backupRestoreProviders.add((BackupRestoreProvider)pluginProvider);
		}
		return backupRestoreProviders;
	}

	@Override
	public BackupRestoreProvider getAvailableBackupRestoreProvider(String providerCode) {
		Collection<BackupRestoreProvider> restoreProviders = getAvailableBackupRestoreProviders();
		BackupRestoreProvider matchedProvider = null;
		for(BackupRestoreProvider restoreProvider:restoreProviders) {
			if(restoreProvider.getCode() == providerCode) {
				matchedProvider = restoreProvider;
			}
		}

		return matchedProvider;
	}

	@Override
	public Boolean hasCopyToStore() { return false; }

	@Override
	public Boolean hasStreamToStore() { return false; }

	@Override
	public Boolean downloadEnabled() { return false; }

	@Override
	public Boolean restoreExistingEnabled() { return false; }

	@Override
	public Boolean restoreNewEnabled() { return false; }

	@Override
	public Boolean hasBackups() { return false; }

	@Override
	public Boolean hasReplication() { return false; }

	@Override
	public Boolean hasServers() { return false; }

	@Override
	public Boolean hasRepositories() { return false; }

	@Override
	public Boolean hasJobs() { return false; }

	@Override
	public Boolean hasSites() { return false; }

	@Override
	public Boolean hasReplicationGroups() { return false; }

	@Override
	public Boolean hasCreateJob() { return false; }

	@Override
	public Boolean hasCloneJob() { return false; }

	@Override
	public Boolean hasAddToJob() { return false; }

	@Override
	public Boolean hasOptionalJob() { return false; }

	@Override
	public Boolean hasSchedule() { return false; }

	@Override
	public Boolean hasStorageProvider() { return false; }

	@Override
	public Boolean hasRetentionCount() { return false; }

	@Override
	public Boolean hasCancelBackup() { return false; }


	@Override
	public Collection<BackupType> backupTypes() { return new ArrayList<BackupType>(); }

	@Override
	public Collection<ReplicationType> replicationTypes() { return new ArrayList<ReplicationType>(); };


	@Override
	public Collection<OptionType> OptionTypes() { return new ArrayList<OptionType>(); };

	@Override
	public Collection<OptionType> backupJobOptions() { return new ArrayList<OptionType>(); };

	@Override
	public Collection<OptionType> backupOptions() { return new ArrayList<OptionType>(); };

	@Override
	public Collection<OptionType> replicationGroupOptions() { return new ArrayList<OptionType>(); };

	@Override
	public Collection<OptionType> replicationOptions() { return new ArrayList<OptionType>(); };

	@Override
	public Collection<OptionType> instanceReplicationGroupOptions() { return new ArrayList<OptionType>(); };

}
