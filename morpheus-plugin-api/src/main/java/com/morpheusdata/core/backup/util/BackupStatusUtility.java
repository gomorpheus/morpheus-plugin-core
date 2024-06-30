/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
