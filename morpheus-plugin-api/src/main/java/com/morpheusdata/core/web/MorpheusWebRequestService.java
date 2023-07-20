package com.morpheusdata.core.web;

import com.morpheusdata.core.providers.UIExtensionProvider;

import java.util.Locale;

/**
 * Provides accessor methods for some common servlet Request attributes.
 * NOTE: This should ONLY be used in {@link UIExtensionProvider} based providers
 * It will fail in other scenarios. A common use case for this accessor is to grab the nonce token for injecting
 * stylesheets and javascript into custom views
 *
 * @author David Estes
 */
public interface MorpheusWebRequestService {
	/**
	 * Gets the current request Nonce Token Attribute for use in injecting javascript/stylesheets
	 * @return the nonce token
	 */
	public String getNonceToken();

	/**
	 * Returns the locale of the current request. Often used in helpers for the renderer when localizing strings
	 * @return the current request locale (if available in the thread context)
	 */
	public Locale getLocale();

	/**
	 * Returns the i18n result message based on the passed in code
	 * @param code
	 * @param args
	 * @param locale
	 * @return
	 */
	public String getMessage(String code, Object[] args, Locale locale);

	/**
	 * Returns the i18n generated message based on the passed in code.
	 * If the i18n property is not found, a default message can be passed
	 * @param code
	 * @param args
	 * @param defaultMessage
	 * @param locale
	 * @return
	 */
	public String getMessage(String code, Object[] args, String defaultMessage, Locale locale);
	//TODO: Add more
}
