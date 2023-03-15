package com.morpheusdata.cloud

import com.morpheusdata.core.backup.BackupExecutionProvider
import com.morpheusdata.core.backup.BackupJobProvider
import com.morpheusdata.core.backup.BackupProvider;
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.backup.BackupRestoreProvider
import com.morpheusdata.core.backup.BackupTypeProvider
import com.morpheusdata.core.backup.MorpheusBackupProvider
import com.morpheusdata.model.BackupIntegration
import com.morpheusdata.model.BackupJob
import com.morpheusdata.model.BackupProviderType
import com.morpheusdata.model.BackupType
import com.morpheusdata.model.Icon
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.ReplicationType
import com.morpheusdata.response.ServiceResponse

class DigitalOceanBackupProvider extends MorpheusBackupProvider {

	DigitalOceanBackupProvider(Plugin plugin, MorpheusContext morpheusContext) {
		super(plugin, morpheusContext)

		DigitalOceanSnapshotProvider digitalOceanSnapshotProvider = new DigitalOceanSnapshotProvider(plugin, morpheusContext)
		plugin.pluginProviders.put(digitalOceanSnapshotProvider.code, digitalOceanSnapshotProvider)
		addScopedProvider(digitalOceanSnapshotProvider, "do-provider", null)
	}


}
