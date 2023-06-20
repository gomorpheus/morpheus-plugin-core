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
