package com.morpheusdata.core.backup.util;

import com.morpheusdata.core.util.KeyUtility;

public class BackupResultUtility {

	public static String generateBackupResultSetId() {
		return KeyUtility.generateDateKey(10);
	}
}
