package com.morpheusdata.vmware

import com.morpheusdata.core.Plugin

class VmwareBackupPlugin extends Plugin {

	@Override
	void initialize() {
		VmwareBackupProvider vmwareBackupProvider = new VmwareBackupProvider(this, morpheusContext)
		this.pluginProviders.put("vmware.backup", vmwareBackupProvider)
		this.setName("VMWare Backup")
	}

	@Override
	void onDestroy() { }
}
