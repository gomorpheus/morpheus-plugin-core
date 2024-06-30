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

package com.morpheusdata.core;

import com.morpheusdata.core.providers.PluginProvider;

import java.util.List;

/**
 * Provides support for defining custom providers for options supplied to optionTypes
 * These may include datasets for feeding into type-aheads, dropdown components, or even multiselect components.
 * A single method exists on this interface that provides a list of method names within the class for use as an OptionSource.
 *
 * This class has been Deprecated in favor of using the {@link com.morpheusdata.core.providers.DatasetProvider}
 *
 * @author Bob Whiton
 * @deprecated
 */
@Deprecated
public interface OptionSourceProvider extends PluginProvider {

	/**
	 * Method names implemented by this provider
	 * @return list of method names
	 */
	List<String> getMethodNames();

}
