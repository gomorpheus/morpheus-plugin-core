package com.morpheusdata.core.backup;

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.Plugin;

public abstract class AbstractBackupRestoreProvider implements BackupRestoreProvider {

	Plugin plugin;
	MorpheusContext morpheusContext;

	public AbstractBackupRestoreProvider(Plugin plugin, MorpheusContext context) {
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

}
