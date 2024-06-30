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
