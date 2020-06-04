package com.morpheusdata.views


import com.morpheusdata.core.Plugin
import com.morpheusdata.core.PluginManager
import com.morpheusdata.model.Permission
import com.morpheusdata.web.Dispatcher
import com.morpheusdata.web.PluginController
import com.morpheusdata.web.Route
import spock.lang.Specification

class DispatcherSpec extends Specification {
	void "Test dispatch template"() {
		given:
		def pm = Mock(PluginManager)
		def plugin = Mock(Plugin)
		plugin.getControllers() >> []
		pm.getPlugins() >> [plugin]
		def d = new Dispatcher(pm)
		expect:
		d.doDispatch(
				'com.morpheusdata.views.TestController',
				'example',
				new ViewModel<Map>()
		).text == "foo"
	}

	void "Test dispatch json"() {
		given:
		def pm = Mock(PluginManager)
		def plugin = Mock(Plugin)
		plugin.getControllers() >> []
		pm.getPlugins() >> [plugin]

		def d = new Dispatcher(pm)
		def map = [foo: "bar"]
		expect:
		d.doDispatch(
				'com.morpheusdata.views.TestController',
				'json',
				ViewModel.of(map)
		).data.foo == "fizz"
	}

	void "handle route"() {
		given:
		def usersPermissions = [[admin:"full"]]
		def pm = Mock(PluginManager)
		def plugin = Mock(Plugin)
		pm.getPlugins() >> [plugin]
		pm.getRoutes() >> [(TestController.class): [foo: 'example']]

		plugin.getControllers() >> [new TestController()]

		def d = new Dispatcher(pm)

		when:
		String url = "/foo/example"

		then:
		d.handleRoute(url, usersPermissions).class == TemplateResponse.class
		d.handleRoute("/bad/url", usersPermissions)?.class == null
	}

	void "duplicate routes"() {
		given:
		def usersPermissions = [[admin:"full"]]
		def pm = Mock(PluginManager)
		def plugin = Mock(Plugin)
		plugin.getControllers() >> [new TestController(), new AnotherController()]
		pm.getRoutes() >> [
				(TestController.class):
						Route.build("/foo/example", "example",
								Permission.build("admin", "full"))
		]
		pm.getPlugins() >> [plugin]
		def d = new Dispatcher(pm)

		when:
		String url = "/foo/example"

		then:
		d.handleRoute(url, usersPermissions).class == TemplateResponse.class
		d.handleRoute("/bad/url", usersPermissions)?.class == null
	}

	void "handleRoute permission check"() {
		given:
		def usersPermissions = [[admin:"full"]]
		def badPermissions = [[bad:"read"]]
		def pm = Mock(PluginManager)
		def plugin = Mock(Plugin)
		plugin.getControllers() >> [new PermissionController()]
		pm.getRoutes() >> [
				(PermissionController.class):
					Route.build("/foo/example", "example",
					Permission.build("admin", "full"))
			]
		pm.getPlugins() >> [plugin]
		def d = new Dispatcher(pm)
		String url = "/foo/example"

		when:
		def result = d.handleRoute(url, usersPermissions)

		then:
		result.class == TemplateResponse.class

		when:
		result = d.handleRoute(url, badPermissions)

		then:
		result?.class == null


	}
}

class TestController implements PluginController {
	List<Route> getRoutes() {
		[
			Route.build("/foo/example", "example", Permission.build("admin", "full")),
			Route.build("/foo/json", "json", Permission.build("admin", "full"))
		]
	}

	def example(ViewModel<String> model) {
		return TemplateResponse.success("foo")
	}
	def json(ViewModel<Map> model) {
		model.object.foo = "fizz"
		return JsonResponse.of(model.object)
	}
}

class AnotherController implements PluginController {
	List<Route> getRoutes() {
		[
			Route.build("/foo/example", "example", Permission.build("admin", "full"))
		]
	}
}

class PermissionController implements PluginController {
	List<Route> getRoutes() {
		[
				Route.build("/foo/example", "example", Permission.build("admin", "full"))
		]
	}

	def example(ViewModel<String> model) {
		return TemplateResponse.success("foo")
	}

}
