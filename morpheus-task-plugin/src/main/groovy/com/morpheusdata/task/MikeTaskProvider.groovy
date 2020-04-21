package com.morpheusdata.task


import com.morpheusdata.core.ExecutableTaskInterface
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.TaskProvider

class MikeTaskProvider implements TaskProvider {
	MorpheusContext morpheusContext
	Plugin plugin
	ExecutableTaskInterface service

	MikeTaskProvider(Plugin plugin, MorpheusContext morpheusContext) {
		this.plugin = plugin
		this.morpheusContext = morpheusContext
	}

	@Override
	MorpheusContext getMorpheusContext() {
		return morpheusContext
	}

	@Override
	Plugin getPlugin() {
		return plugin
	}

	@Override
	ExecutableTaskInterface getService() {
		return new MikeTaskService()
	}

	@Override
	String getProviderCode() {
		return "mikeTaskService"
	}

	@Override
	String getProviderName() {
		return "Mike Task Service"
	}

}
