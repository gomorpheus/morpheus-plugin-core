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
