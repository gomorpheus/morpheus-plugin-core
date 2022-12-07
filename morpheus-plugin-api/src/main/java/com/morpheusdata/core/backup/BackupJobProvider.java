package com.morpheusdata.core.backup;

import com.morpheusdata.core.PluginProvider;
import com.morpheusdata.model.BackupJob;
import com.morpheusdata.response.ServiceResponse;

import java.util.Map;

/**
 * Provides a standard set of methods for Backup Job Providers. A {@link DefaultBackupJobProvider} is available
 * for integrations that do not have a concept of backup jobs, a grouping of backups, but would like to have the
 * feature function for the integration in Morpheus. The {@link DefaultBackupJobProvider} pushes the job operations
 * back to Morpheus.
 * @since 0.12.2
 * @author Dustin DeYoung
 */
public interface BackupJobProvider {

	/**
	 * Apply provider specific configurations to a {@link BackupJob}. The standard configurations are handled by Morpheus.
	 * @param backupJobModel the backup job to apply the configuration changes to
	 * @param config the configuration supplied by external inputs.
	 * @param opts optional parameters used for configuration.
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a false success will indicate a failed
	 * configuration and will halt the backup creation process.
	 */
	ServiceResponse configureBackupJob(BackupJob backupJobModel, Map config, Map opts);

	/**
	 * Validate the configuration of the {@link BackupJob}. Morpheus will validate the backup based on the supplied option type
	 * configurations such as required fields. Use this to either override the validation results supplied by the
	 * default validation or to create additional validations beyond the capabilities of option type validation.
	 * @param backupJobModel the backup job to validate
	 * @param config the original configuration supplied by external inputs.
	 * @param opts optional parameters used for
	 * @return a {@link ServiceResponse} object. The errors field of the ServiceResponse is used to send validation
	 * results back to the interface in the format of {@code errors['fieldName'] = 'validation message' }. The msg
	 * property can be used to send generic validation text that is not related to a specific field on the model.
	 * A ServiceResponse with any items in the errors list or a success value of 'false' will halt the backup job
	 * creation process.
	 */
	ServiceResponse validateBackupJob(BackupJob backupJobModel, Map config, Map opts);

	/**
	 * Create the {@link BackupJob} on the external provider system.
	 * @param backupJobModel the fully configured and validated backup job
	 * @param opts additional options used during backup job creation
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a success value of 'false' will indicate the
	 * creation on the external system failed and will halt any further processing in Morpheus.
	 */
	ServiceResponse createBackupJob(BackupJob backupJobModel, Map opts);

	/**
	 * Clone the {@link BackupJob} on the external system.
	 * @param sourceBackupJobModel the source backup job for cloning
	 * @param backupJobModel the backup job that will be associated to the cloned backup job. The externalId of the
	 *                       backup job should be set with the ID of the cloned job result.
	 * @param opts additional options used during backup job clone
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a success value of 'false' will indicate the
	 * clone on the external system failed and will halt any further processing in Morpheus.
	 */
	ServiceResponse cloneBackupJob(BackupJob sourceBackupJobModel, BackupJob backupJobModel, Map opts);

	/**
	 * Add a backup to an existing backup job in the external system.
	 * @param backupJobModel the backup job receiving the additional backup
	 * @param opts additional options
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a success value of 'false' will indicate the
	 * operation on the external system failed and will halt any further processing in Morpheus.
	 */
	ServiceResponse addToBackupJob(BackupJob backupJobModel, Map opts);

	/**
	 * Delete the backup job in the external system.
	 * @param backupJobModel the backup job to be removed
	 * @param opts additional options
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a success value of 'false' will indicate the
	 * operation on the external system failed and will halt any further processing in Morpheus.
	 */
	ServiceResponse deleteBackupJob(BackupJob backupJobModel, Map opts);

	/**
	 * Execute the backup job on the external system.
	 * @param backupJob the backup job to be executed
	 * @param opts additional options
	 * @return a {@link ServiceResponse} object. A ServiceResponse with a success value of 'false' will indicate the
	 * operation on the external system failed and will halt any further processing in Morpheus.
	 */
	ServiceResponse executeBackupJob(BackupJob backupJob, Map opts);


}
