package com.morpheusdata.core.backup;

import com.morpheusdata.core.PluginProvider;
import com.morpheusdata.model.BackupIntegration;

import java.util.ArrayList;
import java.util.Collection;

public interface BackupTypeProvider extends PluginProvider {

	String providerService();
	String jobService();
	String execService();
	String restoreService();

	String backupFormat();
	String containerFormat();

	Collection<BackupIntegration> backupIntegrations();

	String containerType();
	String containerCategory();
	String restoreType();
	Boolean isActive();
	Boolean hasCopyToStore();
	Boolean hasStreamToStore();
	Boolean copyToStore();
	String providerCode();
	Boolean downloadEnabled();
	Boolean downloadFromStoreOnly();
	Boolean restoreExistingEnabled();
	Boolean restoreNewEnabled();
	String restoreNewMode();
	Boolean pruneResultsOnRestoreExisting();
	Boolean restrictTargets();

}
