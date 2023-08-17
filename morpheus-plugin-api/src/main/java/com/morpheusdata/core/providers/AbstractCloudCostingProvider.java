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
