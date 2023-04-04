package com.morpheusdata.core.backup;

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.Plugin;
import com.morpheusdata.model.BackupIntegration;

import java.util.ArrayList;
import java.util.Collection;

public abstract class BackupProvider implements BackupProviderInterface {
	Plugin plugin;
	MorpheusContext morpheusContext;

	// Associations between cloud type and backup providers
	protected Collection<BackupIntegration> scopedProviders = new ArrayList<BackupIntegration>();

	BackupJobProvider backupJobProvider;

	public BackupProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin;
		this.morpheusContext = context;
	}
}
