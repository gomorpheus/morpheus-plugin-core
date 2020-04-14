package com.morpheusdata.core;

import com.morpheusdata.model.*;
import io.reactivex.Single;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public interface MorpheusBackupContext {
	void setBackupProcessId(Object backupResult, String s1, String s2);

	void updateBackupStatus(Long id, String status, Map opts);

	Single<Backup> getBackupById(Long id);

	Object saveBackupResults(Account ac, String Path, Long id);

	void updateBackupRestore(Restore restore, Map opts);

	void cleanBackupResult(BackupResult backupResult);

	Object getBackupStorageProviderConfig(Account account, Long id);

	void deleteBackupResult(Account account, Long id, Long storageProviderId, String backupId, String archiveName);

	String getWorkingBackupPath(Long backupId, Long backupResultId);

	Object exportSnapshot(String provider, Container container, Map<String, String> opts);

	void updateBackupResult(Long id, Map opts);

	Date extractBackupDate(Date date);

	Map transferBackupToVirtualImage(Account account, BackupResult backupResult, LinkedHashMap<String, Object> transferOpts);
}
