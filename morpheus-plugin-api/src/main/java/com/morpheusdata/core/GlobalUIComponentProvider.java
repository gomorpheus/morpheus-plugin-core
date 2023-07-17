package com.morpheusdata.core;

import com.morpheusdata.core.providers.UIExtensionProvider;

/**
 * This provider creates a means to render global components into the main layout of Morpheus. This could be a global chat component
 * or other type of overlay code that one might want to run throughout the entire application render lifecycle.
 * It extends the common {@link UIExtensionProvider} which allows for extending available content security policies as well
 * as defining the type of renderer being used.
 *
 * NOTE: This provider has been moved to {@link com.morpheusdata.core.providers.GlobalUIComponentProvider}.
 *
 * @see UIExtensionProvider
 * @see com.morpheusdata.core.providers.GlobalUIComponentProvider
 * @author David Estes
 * @since 0.8.0
 * @deprecated
 */
@Deprecated
public interface GlobalUIComponentProvider extends com.morpheusdata.core.providers.GlobalUIComponentProvider {
}
