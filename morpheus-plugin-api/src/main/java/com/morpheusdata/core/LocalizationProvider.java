package com.morpheusdata.core;

import com.morpheusdata.model.CustomLocale;
import com.morpheusdata.model.User;
import java.util.List;

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
