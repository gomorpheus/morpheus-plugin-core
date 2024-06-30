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

/**
 * Used to represent custom locales that may want to be registered within Morpheus. This could be alternative language
 * codes for languages such as Klingon and Elvish! To see its use, check the crowdin plugin.
 *
 * NOTE: This provider has been moved to {@link LocalizationProvider}.
 *
 * @author Chris Taylor
 * @see com.morpheusdata.core.providers.LocalizationProvider
 * @deprecated
 */
@Deprecated
public interface LocalizationProvider extends com.morpheusdata.core.providers.LocalizationProvider {
}
