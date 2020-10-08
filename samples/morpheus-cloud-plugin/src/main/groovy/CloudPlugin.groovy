import com.morpheusdata.core.Plugin

class CloudPlugin extends Plugin {

	@Override
	void initialize() {
		ExampleCloudProvider cloudProvider = new ExampleCloudProvider(this, morpheusContext)
		pluginProviders.put(cloudProvider.providerCode, cloudProvider)
	}

	@Override
	void onDestroy() {

	}
}
