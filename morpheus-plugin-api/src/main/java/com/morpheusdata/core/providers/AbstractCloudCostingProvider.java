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

package com.morpheusdata.core.providers;

import com.morpheusdata.model.Cloud;
import com.morpheusdata.views.HandlebarsRenderer;
import com.morpheusdata.views.Renderer;

/**
 * Morpheus provides a way to generate monthly invoices based on resources in a {@link Cloud}. For most On-Prem clouds
 * this is handled automatically as the Morpheus Usage/Metering engine automatically tracks usage and applies Standard
 * Costing invoices based on the price tables imported. However, when dealing with external public clouds, the source
 * of usage truth is the cloud and not Morpheus. Therefore, to provide accurate costing data for the end user, that data
 * needs imported from the third party cloud. Amazon, for example creates a CUR report file in S3. Azure provides thirteen
 * million different APIs depending on your account type. The following abstract adds the default render of Handlebars
 * Java for server side rendering of a summary snippet if used.
 *
 * @since 0.15.3
 * @author David Estes
 */
public abstract class AbstractCloudCostingProvider implements CloudCostingProvider {
	private HandlebarsRenderer renderer;

	@Override
	public Renderer<?> getRenderer() {
		if(renderer == null) {
			renderer = new HandlebarsRenderer("renderer", getPlugin().getClassLoader());
			renderer.registerAssetHelper(getPlugin().getName());
			renderer.registerNonceHelper(getMorpheus().getServices().getWebRequest());
			renderer.registerI18nHelper(getPlugin(),getMorpheus());
		}
		return renderer;
	}
}
