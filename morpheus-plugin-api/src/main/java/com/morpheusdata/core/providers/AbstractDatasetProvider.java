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

package com.morpheusdata.core.providers;

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.Plugin;
import com.morpheusdata.core.data.DatasetInfo;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import java.util.List;
import java.util.Map;

/**
 * Base class 
 * @author bdwheeler
 * @since 0.15.1
 * @param <T> The Model class type for this dataset - or just basic types for fixed lists
 * @param <V> The "value" or identifier type on the map of option items - usually a long or a string
 */
public abstract class AbstractDatasetProvider<T, V> implements DatasetProvider<T, V> {

	public static DatasetInfo datasetInfo;
	public Plugin plugin;
	public MorpheusContext morpheusContext;

	public MorpheusContext getMorpheus() {
		return morpheusContext;
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public DatasetInfo getInfo() {
		return datasetInfo;
	}

	public String getKey() {
		String rtn = null;
		if(getInfo() != null)
			rtn = getInfo().key;
		return rtn;
	}

	public String getNamespace() {
		String rtn = null;
		if(getInfo() != null)
			rtn = getInfo().namespace;
		return rtn;
	}

	public String getCode() {
		String rtn = null;
		if(getInfo() != null)
			rtn = getInfo().getDatasetCode();
		return rtn;
	}

	public String getName() {
		String rtn = null;
		if(getInfo() != null)
			rtn = getInfo().name;
		return rtn;
	}
	
}
