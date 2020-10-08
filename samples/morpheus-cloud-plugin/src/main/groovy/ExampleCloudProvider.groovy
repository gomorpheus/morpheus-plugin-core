import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.PluginProvider

class ExampleCloudProvider implements PluginProvider {
	Plugin plugin
	MorpheusContext morpheusContext

	ExampleCloudProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
	}

	@Override
	MorpheusContext getMorpheusContext() {
		return null
	}

	@Override
	Plugin getPlugin() {
		return null
	}

	@Override
	String getProviderCode() {
		return 'example-cloud'
	}

	@Override
	String getProviderName() {
		return 'ExampleCloud'
	}
}
