package com.morpheusdata.core.backup;

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.Plugin;
import com.morpheusdata.model.Backup;
import com.morpheusdata.model.BackupResult;
import com.morpheusdata.model.Instance;
import com.morpheusdata.response.ServiceResponse;

import java.util.Map;

public abstract class AbstractMorpheusBackupTypeProvider implements BackupTypeProvider {
	Plugin plugin;
	MorpheusContext morpheusContext;

	public AbstractMorpheusBackupTypeProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin;
		this.morpheusContext = context;
	}

	@Override
	public MorpheusContext getMorpheus() {
		return this.morpheusContext;
	}

	@Override
	public Plugin getPlugin() {
		return this.plugin;
	}

	@Override
	public ServiceResponse configureBackup(Backup backup, Map config, Map opts) {
		return ServiceResponse.success(backup);
	}

	@Override
	public ServiceResponse validateBackup(Backup backup, Map config, Map opts) {
		return ServiceResponse.success(backup);
	}

	@Override
	public ServiceResponse createBackup(Backup backup, Map opts) {
		return ServiceResponse.success(backup);
	}

	@Override
	public ServiceResponse deleteBackup(Backup backup, Map opts) {
		return ServiceResponse.success(backup);
	}

	@Override
	public ServiceResponse cancelBackup(BackupResult backupResult, Map opts) {
		return ServiceResponse.error("Unable to cancel backup");
	}

	@Override
	public ServiceResponse extractBackup(BackupResult backupResult, Map opts) {
		return ServiceResponse.error("Unable to extract backup");
	}
}
