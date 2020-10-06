package com.morpheusdata.task

import com.morpheusdata.model.Permission
import com.morpheusdata.views.JsonResponse
import com.morpheusdata.views.HTMLResponse
import com.morpheusdata.views.ViewModel
import com.morpheusdata.web.PluginController
import com.morpheusdata.web.Route

/**
 * Example PluginController
 */
class ReverseTextTaskController implements PluginController {

	/**
	 * Defines two Routes with the builder method
	 * @return
	 */
	List<Route> getRoutes() {
		[
			Route.build("/reverseTask/example", "example", Permission.build("admin", "full")),
			Route.build("/reverseTask/json", "json", Permission.build("admin", "full"))
		]
	}

	/**
	 * As defined in {@link #getRoutes}, Method will be invoked when /reverseTask/example is requested
	 * @param model
	 * @return a simple html response
	 */
	def example(ViewModel<String> model) {
		println model
		return HTMLResponse.success("foo")
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
