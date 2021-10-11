package com.morpheusdata.core;

import com.morpheusdata.model.CustomLocale;
import java.util.List;

public interface LocalizationProvider extends PluginProvider {
	/**
	 * Get list of custom Locale Codes to be added to Global and User Morpheus Locale settings.
	 * This should typically be used for non standard Locale codes in conjunction with custom provided custom_locale.properties files.
	 * @return List of CustomLocales to be made available in Global and User locale settings
	 */
	List<CustomLocale> getCustomLocales();

}