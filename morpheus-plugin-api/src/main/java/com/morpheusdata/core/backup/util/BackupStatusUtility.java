package com.morpheusdata.core.backup.util;

/**
 * @deprecated replace by enums in {@link com.morpheusdata.model.BackupResult} and {@link com.morpheusdata.model.BackupRestore}
 */
@Deprecated(since = "0.14.6")
public class BackupStatusUtility {
	public static String START_REQUESTED = "START_REQUESTED";
	public static String INITIALIZING = "INITIALIZING";
	public static String IN_PROGRESS = "IN_PROGRESS";
	public static String CANCEL_REQUESTED = "CANCEL_REQUESTED";
	public static String CANCELLED = "CANCELLED";
	public static String SUCCEEDED = "SUCCEEDED";
	public static String SUCCEEDED_WARNING = "SUCCEEDED_WARNING";
	public static String FAILED = "FAILED";
}
