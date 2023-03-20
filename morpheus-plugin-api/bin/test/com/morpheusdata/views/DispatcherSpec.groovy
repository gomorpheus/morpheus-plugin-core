package com.morpheusdata.views

import com.morpheusdata.core.MorpheusContext
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
		def testController = new TestController(plugin, null)
		plugin.getControllers() >> [testController]
		pm.getPlugins() >> [plugin]
		def d = new Dispatcher(pm)
		expect:
		d.doDispatch(
				testController,
				'example',
				new ViewModel<Map>()
		).html == "foo"
	}

	void "Test dispatch json"() {
		given:
		def pm = Mock(PluginManager)
		def plugin = Mock(Plugin)
		def testController = new TestController(plugin, null)
		plugin.getControllers() >> [testController]
		pm.getPlugins() >> [plugin]

		def d = new Dispatcher(pm)
		def map = [foo: "bar"]
		expect:
		d.doDispatch(
				testController,
				'json',
				ViewModel.of(map)
		).data.foo == "fizz"
	}

	void "handle route"() {
		given:
		def usersPermissions = [admin:"full"]
		def pm = Mock(PluginManager)
		def plugin = Mock(Plugin)
		pm.getPlugins() >> [plugin]
		pm.getRoutes() >> [(TestController.class): [foo: 'example']]
		def testController = new TestController(plugin, null)
		plugin.getControllers() >> [testController]

		def d = new Dispatcher(pm)

		when:
		String url = "/foo/example"

		then:
		d.handleRoute(url, usersPermissions).class == HTMLResponse.class
		d.handleRoute("/bad/url", usersPermissions)?.class == null
	}

	void "duplicate routes"() {
		given:
		def usersPermissions = [admin:"full"]
		def pm = Mock(PluginManager)
		def plugin = Mock(Plugin)
		def testController = new TestController(plugin, null)
		def anotherController = new AnotherController(plugin, null)
		plugin.getControllers() >> [testController, anotherController]
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
		d.handleRoute(url, usersPermissions).class == HTMLResponse.class
		d.handleRoute("/bad/url", usersPermissions)?.class == null
	}

	void "handleRoute permission check"() {
		given:
		def usersPermissions = [admin:"full"]
		def badPermissions = [bad:"read"]
		def pm = Mock(PluginManager)
		def plugin = Mock(Plugin)
		def permissionController = new PermissionController(plugin, null)
		plugin.getControllers() >> [permissionController]
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
		result.class == HTMLResponse.class

		when:
		result = d.handleRoute(url, badPermissions)

		then:
		result?.class == null


	}
}

class TestController implements PluginController {
	MorpheusContext morpheusContext
	Plugin plugin

	public TestController(Plugin plugin, MorpheusContext morpheusContext) {
		this.plugin = plugin
		this.morpheusContext = morpheusContext
	}

	List<Route> getRoutes() {
		[
			Route.build("/foo/example", "example", Permission.build("admin", "full")),
			Route.build("/foo/json", "json", Permission.build("admin", "full"))
		]
	}

	@Override
	public String getCode() {
		return 'testController'
	}

	@Override
	String getName() {
		return 'Test Controller'
	}

	@Override
	MorpheusContext getMorpheus() {
		return morpheusContext
	}

	def example(ViewModel<String> model) {
		return HTMLResponse.success("foo")
	}
	def json(ViewModel<Map> model) {
		model.object.foo = "fizz"
		return JsonResponse.of(model.object)
	}
}

class AnotherController implements PluginController {
	MorpheusContext morpheusContext
	Plugin plugin

	public AnotherController(Plugin plugin, MorpheusContext morpheusContext) {
		this.plugin = plugin
		this.morpheusContext = morpheusContext
	}

	List<Route> getRoutes() {
		[
			Route.build("/foo/example", "example", Permission.build("admin", "full"))
		]
	}

	@Override
	public String getCode() {
		return 'anotherController'
	}

	@Override
	String getName() {
		return 'Another Controller'
	}

	@Override
	MorpheusContext getMorpheus() {
		return morpheusContext
	}
}

class PermissionController implements PluginController {
	MorpheusContext morpheusContext
	Plugin plugin

	public PermissionController(Plugin plugin, MorpheusContext morpheusContext) {
		this.plugin = plugin
		this.morpheusContext = morpheusContext
	}

	List<Route> getRoutes() {
		[
				Route.build("/foo/example", "example", Permission.build("admin", "full"))
		]
	}

	@Override
	public String getCode() {
		return 'permissionController'
	}

	@Override
	String getName() {
		return 'Permission Controller'
	}

	@Override
	MorpheusContext getMorpheus() {
		return morpheusContext
	}

	def example(ViewModel<String> model) {
		return HTMLResponse.success("foo")
	}

}
