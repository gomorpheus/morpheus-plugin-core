package com.morpheusdata.core.backup;

import com.morpheusdata.core.PluginProvider;
import com.morpheusdata.response.ServiceResponse;

public interface BackupRestoreProvider extends PluginProvider {

	ServiceResponse prepareRestoreBackup();
	ServiceResponse restoreBackup();
	ServiceResponse finalizeRestore();

}
