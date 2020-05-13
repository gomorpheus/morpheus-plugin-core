package com.morpheusdata.task

import com.morpheusdata.views.JsonResponse
import com.morpheusdata.views.TemplateResponse
import com.morpheusdata.views.ViewModel
import com.morpheusdata.web.PluginController

class MikeTaskController implements PluginController {
	Map<String, String> getRoutes() {
		['/mikeTask/example': "example",
		 '/mikeTask/json': "json"]
	}

	def example(ViewModel<String> model) {
		println model
		return TemplateResponse.success("foo")
	}

	def json(ViewModel<Map> model) {
		println model
		model.object.foo = "fizz"
		return JsonResponse.of(model.object)
	}
}
