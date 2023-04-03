package com.morpheusdata.core.backup;

import com.morpheusdata.core.*;
import com.morpheusdata.model.BackupIntegration;
import com.morpheusdata.response.ServiceResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Provides a standard set of methods for a {@link BackupProvider}. A backup provider is the primary connection to the
 * external provider services. The backup provider supplies providers for provision types and/or container types via
 * the {@link BackupTypeProvider BackupTypeProviders} implemented within the provider.
 * @since 0.13.4
 * @author Dustin DeYoung
 */
public abstract class AbstractBackupProvider implements BackupProvider {

	Plugin plugin;
	MorpheusContext morpheusContext;

	// Associations between cloud type and backup providers
	protected Collection<BackupIntegration> scopedProviders = new ArrayList<BackupIntegration>();

	BackupJobProvider backupJobProvider;

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
	public Boolean getEnabled() { return true; }

	@Override
	public Boolean getCreatable() { return false; }

	@Override
	public String getViewSet() { return null; }

	@Override
	public Boolean getDownloadEnabled() { return false; };

	@Override
	public Boolean getHasAddToJob() { return true; }

	@Override
	public Boolean getHasBackups() { return true; }

	@Override
	public Boolean hasCancelBackup() { return true; };

	@Override
	public Boolean getHasCloneJob() { return true; }

	@Override
	public Boolean getHasCopyToStore() { return false; };

	@Override
	public Boolean getHasCreateJob() { return true; }

	@Override
	public Boolean getHasJobs() { return false; };

	@Override
	public String getDefaultJobType() { return "new"; };

	@Override
	public Boolean getHasOptionalJob() { return false; }

	@Override
	public Boolean getHasReplication() { return false; };

	@Override
	public Boolean getHasReplicationGroups() { return false; };

	@Override
	public Boolean getHasRepositories() { return false; };

	@Override
	public Boolean getHasRetentionCount() { return true; }

	@Override
	public Boolean getHasSchedule() { return true; }

	@Override
	public Boolean getHasServers() { return false; };

	@Override
	public Boolean getHasSites() { return false; };

	@Override
	public Boolean hasStorageProvider() { return true; };

	@Override
	public Boolean getHasStreamToStore() { return false; };

	@Override
	public Boolean getRestoreExistingEnabled() { return false; };

	@Override
	public Boolean getRestoreNewEnabled() { return false; };

	/**
	 * Add a scoped backup provider. A {@link com.morpheusdata.model.BackupIntegration } links the provider
	 * to the provision or container type. Morpheus will use this associated to determine
	 * 	 * the available providers when executing an operation in the backup services.
	 * @param backupIntegration a backup integration referencing the provider type and the provision
	 *                             and/or container types
	 */
	public void addScopedProvider(com.morpheusdata.model.BackupIntegration backupIntegration) {
		scopedProviders.add(backupIntegration);
	}

	/**
	 * Add a scoped backup provider. Registers a {@link BackupTypeProvider} and the associated
	 * provision and/or container types with morpheus. Morpheus will use this associated to determine
	 * the available providers when executing an operation in the backup services.
	 * @param backupTypeProvider the backup type provider to register
	 * @param provisionTypeCode the provision type code to be associated with the backup type provider
	 * @param containerTypeCode the container type code to be associated with the backup the provider
	 */
	public void addScopedProvider(BackupTypeProvider backupTypeProvider, String provisionTypeCode, String containerTypeCode) {
		com.morpheusdata.model.BackupIntegration scopedProvider = new com.morpheusdata.model.BackupIntegration(backupTypeProvider,  provisionTypeCode, containerTypeCode);
		scopedProviders.add(scopedProvider);
	}

	/**
	 * Get the list of {@link com.morpheusdata.model.BackupIntegration BackupIntegrations} associated with this backup provider
	 * @return a list of {@link com.morpheusdata.model.BackupIntegration BackupIntegrations}
	 */
	public Collection<com.morpheusdata.model.BackupIntegration> getScopedProviders() {
		return scopedProviders;
	}

	/**
	 * Get the list of {@link BackupTypeProvider BackupTypeProviders} associated with this backup provider
	 * @return a list of {@link BackupTypeProvider BackupTypeProviders}
	 */
	public Collection<BackupTypeProvider> getBackupProviders() {
		ArrayList<BackupTypeProvider> backupTypeProviders = new ArrayList<>();
		for(com.morpheusdata.model.BackupIntegration scopedProvider:scopedProviders) {
			if(backupTypeProviders.contains(scopedProvider.getBackupTypeProvider()) == false) {
				backupTypeProviders.add(scopedProvider.getBackupTypeProvider());
			}
		}

		return backupTypeProviders;
	}

	// provider
	@Override
	public ServiceResponse configureBackupProvider(com.morpheusdata.model.BackupProvider backupProviderModel, Map config, Map opts) {
		return ServiceResponse.success();
	}

	@Override
	public ServiceResponse validateBackupProvider(com.morpheusdata.model.BackupProvider backupProviderModel, Map opts) {
		return ServiceResponse.success();
	}

	@Override
	public ServiceResponse initializeBackupProvider(com.morpheusdata.model.BackupProvider backupProviderModel, Map opts) {
		return ServiceResponse.success();
	}

	@Override
	public ServiceResponse updateBackupProvider(com.morpheusdata.model.BackupProvider backupProviderModel, Map opts) {
		return ServiceResponse.success();
	}

	// backup jobs
	/**
	 * Get the {@link BackupJobProvider} responsible for all backup job operations in this backup provider
	 * The {@link DefaultBackupJobProvider} can be used if the provider would like morpheus to handle all job operations.
	 * @return the {@link BackupJobProvider} for this backup provider
	 */
	public abstract BackupJobProvider getBackupJobProvider();
	
	@Override
	public ServiceResponse configureBackupJob(com.morpheusdata.model.BackupJob backupJobModel, Map config, Map opts) {
		return getBackupJobProvider().configureBackupJob(backupJobModel, config, opts);
	}

	@Override
	public ServiceResponse validateBackupJob(com.morpheusdata.model.BackupJob backupJobModel, Map config, Map opts) {
		return getBackupJobProvider().validateBackupJob(backupJobModel, config, opts);
	}

	@Override
	public ServiceResponse createBackupJob(com.morpheusdata.model.BackupJob backupJobModel, Map opts) {
		return getBackupJobProvider().createBackupJob(backupJobModel, opts);
	}

	@Override
	public ServiceResponse cloneBackupJob(com.morpheusdata.model.BackupJob sourceBackupJobModel, com.morpheusdata.model.BackupJob backupJobModel, Map opts) {
		return getBackupJobProvider().cloneBackupJob(sourceBackupJobModel, backupJobModel, opts);
	}

	@Override
	public ServiceResponse addToBackupJob(com.morpheusdata.model.BackupJob backupJobModel, Map opts) {
		return getBackupJobProvider().addToBackupJob(backupJobModel, opts);
	}

	@Override
	public ServiceResponse deleteBackupJob(com.morpheusdata.model.BackupJob backupJobModel, Map opts) {
		return getBackupJobProvider().deleteBackupJob(backupJobModel, opts);
	}

	@Override
	public ServiceResponse executeBackupJob(com.morpheusdata.model.BackupJob backupJobModel, Map opts) {
		return getBackupJobProvider().executeBackupJob(backupJobModel, opts);
	}

}
