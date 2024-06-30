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
