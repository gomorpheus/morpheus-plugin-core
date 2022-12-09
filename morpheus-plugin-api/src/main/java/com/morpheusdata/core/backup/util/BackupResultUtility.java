package com.morpheusdata.core.backup.util;

import com.morpheusdata.core.util.KeyUtility;

public class BackupResultUtility {

	/**
	 * Generate a random UUID to be used as the result set id on a {@link com.morpheusdata.model.BackupResult}
	 * @return String a 10 character UUID
	 */
	public static String generateBackupResultSetId() {
		return KeyUtility.generateDateKey(10);
	}
}
