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

package com.morpheusdata.core.data;

/**
 * information about a dataset
 * @author bdwheeler
 * @since 0.15.1
 */
public class DatasetInfo {

	public String namespace;
	public String key;
	public String name;
	public String description;

	public DatasetInfo() {}

	public DatasetInfo(String namespace, String key) {
		this.namespace = namespace;
		this.key = key;
	}

	public DatasetInfo(String namespace, String key, String name) {
		this.namespace = namespace;
		this.key = key;
		this.name = name;
	}

	public DatasetInfo(String namespace, String key, String name, String description) {
		this.namespace = namespace;
		this.key = key;
		this.name = name;
		this.description = description;
	}

	public String getDatasetCode() {
		String rtn;
		if(namespace != null)
			rtn = namespace;
		else
			rtn = "global";
		rtn = rtn + "." + key;
		return rtn;
	}

}
