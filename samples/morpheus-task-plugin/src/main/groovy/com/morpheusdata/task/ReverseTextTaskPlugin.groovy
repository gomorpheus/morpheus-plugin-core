package com.morpheusdata.task

import com.morpheusdata.core.Plugin
import com.morpheusdata.views.HandlebarsRenderer
import com.morpheusdata.views.ViewModel

/**
 * An Example Task plugin
 */
class ReverseTextTaskPlugin extends Plugin {

	/**
	 * <ul>
	 * <li>Initializes the plugin name, description, and author.</li>
	 * <li>Registers the task provider</li>
	 * <li>Registers a Handlebars template renderer</li>
	 * <li>Registers an example Controller</li>
	 * <li>Demonstrates rendering a template</li>
	 * </ul>
	 */
	@Override
	void initialize() {
		ReverseTextTaskProvider reverseTextTaskProvider = new ReverseTextTaskProvider(this, morpheusContext)
		this.setName("Reverse Text Task Plugin")
		this.setDescription("Provides a task that can reverse the value of any string input")
		this.setAuthor("Mike Truso")
		this.pluginProviders.put(reverseTextTaskProvider.providerCode, reverseTextTaskProvider)
		this.setRenderer(new HandlebarsRenderer(this.classLoader))
		this.controllers.add(new ReverseTextTaskController())
		def model = new ViewModel<String>()
		model.object = "Eric"

		println this.getRenderer().renderTemplate('instanceTab', model).html
	}

	/**
	 * Called when a plugin is being removed from the plugin manager (aka Uninstalled)
	 */
	@Override
	void onDestroy() {
		morpheusContext.task.disableTask('reverseTextTask')
	}
}
