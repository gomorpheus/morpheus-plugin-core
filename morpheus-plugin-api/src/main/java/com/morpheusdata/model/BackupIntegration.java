package com.morpheusdata.model;

import com.morpheusdata.core.backup.BackupTypeProvider;

public class BackupIntegration extends MorpheusModel {

	protected BackupTypeProvider backupTypeProvider;
	protected String containerTypeCode;
	protected String provisionTypeCode;

	public BackupIntegration(BackupTypeProvider backupTypeProvider, String provisionTypeCode, String containerTypeCode) {
		this.backupTypeProvider = backupTypeProvider;
		this.containerTypeCode = containerTypeCode;
		this.provisionTypeCode = provisionTypeCode;
	}

	public BackupTypeProvider getBackupTypeProvider() {
		return backupTypeProvider;
	}

	public void setBackupTypeProvider(BackupTypeProvider backupTypeProvider) {
		this.backupTypeProvider = backupTypeProvider;
		markDirty("backupTypeCode", backupTypeProvider, this.backupTypeProvider);
	}

	public String getContainerTypeCode() {
		return containerTypeCode;
	}

	public void setContainerTypeCode(String containerTypeCode) {
		this.containerTypeCode = containerTypeCode;
		markDirty("containerTypeCode", containerTypeCode, this.containerTypeCode);
	}

	public String getProvisionTypeCode() {
		return provisionTypeCode;
	}

	public void setProvisionTypeCode(String provisionTypeCode) {
		this.provisionTypeCode = provisionTypeCode;
		markDirty("provisionTypeCode", provisionTypeCode, this.provisionTypeCode);
	}
}
