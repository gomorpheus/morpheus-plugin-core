package com.morpheusdata.core.backup;

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.Plugin;
import com.morpheusdata.model.BackupIntegration;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractBackupTypeProvider implements BackupTypeProvider {

	Plugin plugin;
	MorpheusContext morpheusContext;

	public AbstractBackupTypeProvider(Plugin plugin, MorpheusContext context) {
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
		return this.providerCode();
	}

	@Override
	public String backupFormat() { return null; }

	@Override
	public String containerFormat() { return null; }

	@Override
	public Collection<BackupIntegration> backupIntegrations() { return new ArrayList<BackupIntegration>(); }

	@Override
	public String containerCategory() { return null; }

	@Override
	public Boolean isActive() { return true; }

	@Override
	public Boolean hasCopyToStore() { return false; }

	@Override
	public Boolean copyToStore() { return true; }

	@Override
	public Boolean hasStreamToStore() { return false; }

	@Override
	public Boolean downloadEnabled() { return true; }

	@Override
	public Boolean downloadFromStoreOnly() { return false; }

	@Override
	public Boolean restoreExistingEnabled() { return true; }

	@Override
	public Boolean restoreNewEnabled() { return true; }

	@Override
	public Boolean pruneResultsOnRestoreExisting() { return false; }

	@Override
	public Boolean restrictTargets() { return false; }

}
