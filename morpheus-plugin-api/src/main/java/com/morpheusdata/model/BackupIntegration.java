package com.morpheusdata.model;

import java.util.List;

public class BackupIntegration extends MorpheusModel {

	protected String backupTypeCode;
	protected String containerTypeCode;
	protected String provisionTypeCode;

	public BackupIntegration(String backupTypeCode, String containerTypeCode, String provisionTypeCode) {
		this.backupTypeCode = backupTypeCode;
		this.containerTypeCode = containerTypeCode;
		this.provisionTypeCode = provisionTypeCode;
	}

	public String getBackupTypeCode() {
		return backupTypeCode;
	}

	public void setBackupTypeCode(String backupTypeCode) {
		this.backupTypeCode = backupTypeCode;
		markDirty("backupTypeCode", backupTypeCode, this.backupTypeCode);
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
