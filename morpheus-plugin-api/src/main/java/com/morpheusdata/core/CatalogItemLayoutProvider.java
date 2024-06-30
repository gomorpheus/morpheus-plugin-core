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
 * This provider provides a means to define custom layouts for rendering catalog item detail pages. The default layout easily renders markdown, and the user input form
 * via the {@link com.morpheusdata.model.OptionType} model. This enables full customizability of the page.
 *
 * NOTE: This provider has moved to {@link CatalogItemLayoutProvider}
 *
 * @since 0.9.0
 * @author David Estes
 * @deprecated
 * @see CatalogItemLayoutProvider
 */
@Deprecated
public interface CatalogItemLayoutProvider extends com.morpheusdata.core.providers.CatalogItemLayoutProvider {
}
