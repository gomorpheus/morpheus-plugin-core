package com.morpheusdata.task

import com.morpheusdata.model.Permission
import com.morpheusdata.views.JsonResponse
import com.morpheusdata.views.HTMLResponse
import com.morpheusdata.views.ViewModel
import com.morpheusdata.web.PluginController
import com.morpheusdata.web.Route
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.MorpheusContext

/**
 * Example PluginController
 */
class ReverseTextTaskController implements PluginController {

	MorpheusContext morpheusContext
	Plugin plugin

	public ReverseTextTaskController(Plugin plugin, MorpheusContext morpheusContext) {
		this.plugin = plugin
		this.morpheusContext = morpheusContext
	}

	@Override
	public String getCode() {
		return 'reverseTextController'
	}

	@Override
	String getName() {
		return 'Reverse Text Controller'
	}

	@Override
	MorpheusContext getMorpheus() {
		return morpheusContext
	}

	/**
	 * Defines two Routes with the builder method
	 * @return
	 */
	List<Route> getRoutes() {
		[
			Route.build("/reverseTask/example", "example", Permission.build("reverseTextPlugin", "full")),
			Route.build("/reverseTask/json", "json", Permission.build("reverseTextPlugin", "full"))
		]
	}

	/**
	 * As defined in {@link #getRoutes}, Method will be invoked when /reverseTask/example is requested
	 * @param model
	 * @return a simple html response
	 */
	def example(ViewModel<String> model) {
		println model
		println "user: ${model.user}"
		return HTMLResponse.success("foo: ${model.user.firstName} ${model.user.lastName}")
	}

	/**
	 * As defined in {@link #getRoutes}, Method will be invoked when /reverseTask/json is requested
	 * @param model
	 * @return a simple json response
	 */
	def json(ViewModel<Map> model) {
		println model
		model.object.foo = "fizz"
		return JsonResponse.of(model.object)
	}
}
