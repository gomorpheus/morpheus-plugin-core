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

import com.morpheusdata.model.Setting;
import com.morpheusdata.model.SettingType;
import com.morpheusdata.model.NetworkProxy;
import io.reactivex.rxjava3.core.Maybe;

public interface MorpheusSettingService extends MorpheusDataService<Setting, Setting>, MorpheusIdentityService<Setting> {

	/**
	 * Returns the SettingType context used for performing updates or queries on {@link SettingType} related settings within Morpheus.
	 * @return An instance of the SettingType Context
	 */
	MorpheusSettingTypeService getType();

	/**
	 * Returns the Global NetworkProxy if exists else null
	 * @return An instance of the NetworkProxy
	 */
	Maybe<NetworkProxy> getGlobalNetworkProxy();
}
