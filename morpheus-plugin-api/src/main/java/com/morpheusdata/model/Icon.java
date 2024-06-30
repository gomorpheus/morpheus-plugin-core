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

package com.morpheusdata.model;

import com.morpheusdata.core.providers.CloudProvider;
import com.morpheusdata.core.providers.DNSProvider;
import com.morpheusdata.core.providers.IPAMProvider;
import com.morpheusdata.core.providers.TaskProvider;

/**
 * Stores path information related to logos/icons in use when using different Providers as Integrations of certain types.
 * @author David Estes
 * @since 0.12.3
 * @see IPAMProvider
 * @see TaskProvider
 * @see DNSProvider
 * @see CloudProvider
 */
public class Icon {
	protected String path;
	protected String hidpiPath;
	protected String darkPath;
	protected String darkHidpiPath;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getHidpiPath() {
		return hidpiPath;
	}

	public void setHidpiPath(String hidpiPath) {
		this.hidpiPath = hidpiPath;
	}

	public String getDarkPath() {
		return darkPath;
	}

	public void setDarkPath(String darkPath) {
		this.darkPath = darkPath;
	}

	public String getDarkHidpiPath() {
		return darkHidpiPath;
	}

	public void setDarkHidpiPath(String darkHidpiPath) {
		this.darkHidpiPath = darkHidpiPath;
	}
}
