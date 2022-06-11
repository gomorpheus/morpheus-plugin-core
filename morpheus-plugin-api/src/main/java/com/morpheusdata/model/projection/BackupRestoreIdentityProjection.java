package com.morpheusdata.model.projection;

import com.morpheusdata.model.MorpheusModel;

public class BackupRestoreIdentityProjection extends MorpheusModel {
		protected String externalId;

		public BackupRestoreIdentityProjection() {
		}

		public BackupRestoreIdentityProjection(Long id, String externalId, String backupName) {
			this.id = id;
			this.externalId = externalId;
		}

		/**
		 * returns the externalId also known as the API id of the equivalent object.
		 * @return the external id or API id of the current record
		 */
		public String getExternalId() {
			return externalId;
		}

		/**
		 * Sets the externalId of the backup restore. In this class this should not be called directly
		 * @param externalId the external id or API id of the current record
		 */
		public void setExternalId(String externalId) {
			this.externalId = externalId;
			markDirty("externalId", externalId);
		}
}
