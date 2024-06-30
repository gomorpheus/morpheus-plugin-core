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

import com.morpheusdata.core.providers.NetworkTabProvider;
import com.morpheusdata.views.HandlebarsRenderer;
import com.morpheusdata.views.Renderer;

/**
 * Renders tabs for networks. This could be useful if , for example a custom integration for network providers was made for
 * NSX or ACI. It may be beneficial to render an additional tab on network details that would provide useful information
 *
 * @author David Estes
 * @see NetworkTabProvider
 */
public abstract class AbstractNetworkTabProvider implements NetworkTabProvider {
	private HandlebarsRenderer renderer;

	@Override
	public Renderer<?> getRenderer() {
		if(renderer == null) {
			renderer = new HandlebarsRenderer("renderer", getPlugin().getClassLoader());
			renderer.registerAssetHelper(getPlugin().getName());
			renderer.registerNonceHelper(getMorpheus().getWebRequest());
			renderer.registerI18nHelper(getPlugin(),getMorpheus());
		}
		return renderer;
	}
}
