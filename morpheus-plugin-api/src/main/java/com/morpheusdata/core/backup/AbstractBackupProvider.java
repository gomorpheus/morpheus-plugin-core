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
	public Collection<BackupType> getBackupTypes() {
		return this.getBackupProviderType().getBackupTypes();
	}

	@Override
	public Collection<ReplicationType> getReplicationTypes() {
		return this.getBackupProviderType().getReplicationTypes();
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

}
