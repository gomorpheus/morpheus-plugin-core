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

import com.morpheusdata.core.providers.PluginProvider;
import com.morpheusdata.model.CustomLocale;
import com.morpheusdata.model.User;
import java.util.List;

/**
 * Used to represent custom locales that may want to be registered within Morpheus. This could be alternative language
 * codes for languages such as Klingon and Elvish! To see its use, check the crowdin plugin.
 *
 * @author Chris Taylor
 * @since 0.15.1
 */
public interface LocalizationProvider extends PluginProvider {
  /**
  * Get list of custom Locale Codes to be added to Global and User Morpheus Locale settings.
  * This should typically be used for non standard Locale codes in conjunction with custom provided custom_locale.properties files.
  * @param user The current user the locales are being requested for.
  * @return List of CustomLocales to be made available in Global and User locale settings
  */
  List<CustomLocale> getCustomLocales(User user);
  
  /**
  * Get list of custom Locale Codes to be added to Global and User Morpheus Locale settings.
  * This should typically be used for non standard Locale codes in conjunction with custom provided custom_locale.properties files.
  * @return List of CustomLocales to be made available in Global and User locale settings
  */
  List<CustomLocale> getCustomLocales();
}
