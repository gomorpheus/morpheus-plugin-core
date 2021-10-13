package com.morpheusdata.core.web;

/**
 * Provides accessor methods for some common servlet Request attributes.
 * NOTE: This should ONLY be used in {@link com.morpheusdata.core.UIExtensionProvider} based providers
 * It will fail in other scenarios. A common use case for this accessor is to grab the nonce token for injecting
 * stylesheets and javascript into custom views
 *
 * @author David Estes
 */
public interface MorpheusWebRequestService {
	/**
	 * Gets the current request Nonce Token Attribute for use in injecting javascript/stylesheets
	 * @return
	 */
	public String getNonceToken();
	//TODO: Add more
}
