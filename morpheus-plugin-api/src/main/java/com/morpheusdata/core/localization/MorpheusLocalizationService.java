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

package com.morpheusdata.core.localization;

import java.util.List;

public interface MorpheusLocalizationService {

	/**
	 * Convert a localization code into a localized string
	 * @param code i18n code of the localized string
	 * @return the localized string
	 */
	String get(String code);

	/**
	 * Convert a localization code into a localized string
	 * @param code i18n code of the localized string
	 * @param args argumnets interpolated by the localized string
	 * @return the localized string
	 */
	String get(String code, List<String> args);
}
