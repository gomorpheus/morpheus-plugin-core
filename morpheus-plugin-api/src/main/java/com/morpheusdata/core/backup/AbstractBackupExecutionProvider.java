package com.morpheusdata.core.backup;

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.Plugin;

public abstract class AbstractBackupExecutionProvider implements BackupExecutionProvider {

	Plugin plugin;
	MorpheusContext morpheusContext;

	public AbstractBackupExecutionProvider(Plugin plugin, MorpheusContext context) {
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
