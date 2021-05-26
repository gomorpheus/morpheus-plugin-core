package com.morpheusdata.cataloglayout

import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Permission

/**
 * Example Catalog Item Detail Plugin
 *
 * @since 0.9.0 , 5.3.2
 * @author David Estes
 */
class CatalogItemStandardLayoutPlugin extends Plugin {

	@Override
	void initialize() {
		StandardCatalogLayoutProvider standardCatalogLayoutProvider = new StandardCatalogLayoutProvider(this,morpheus)
		
		this.pluginProviders.put(standardCatalogLayoutProvider.code, standardCatalogLayoutProvider)
		this.setName("Standard Catalog Layout")
	}

	@Override
	void onDestroy() {
	}
}
