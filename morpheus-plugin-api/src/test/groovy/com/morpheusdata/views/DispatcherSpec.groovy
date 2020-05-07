package com.morpheusdata.views


import com.morpheusdata.core.Plugin
import com.morpheusdata.core.PluginManager
import com.morpheusdata.web.Dispatcher
import com.morpheusdata.web.PluginController
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
		def pm = Mock(PluginManager)
		def plugin = Mock(Plugin)
		pm.getPlugins() >> [plugin]
		plugin.getControllers() >> [new TestController()]
		def d = new Dispatcher(pm)

		when:
		String url = "/foo/example"

		then:
		d.handleRoute(url, 'permission').class == TestController.class
		d.handleRoute("/bad/url", 'permission')?.class == null
	}

	void "duplicate routes"() {
		given:
		def pm = Mock(PluginManager)
		def plugin = Mock(Plugin)
		plugin.getControllers() >> [new TestController(), new AnotherController()]
		pm.getPlugins() >> [plugin]
		def d = new Dispatcher(pm)

		when:
		String url = "/foo/example"

		then:
		d.handleRoute(url, 'permission').class == TestController.class
		d.handleRoute("/bad/url", 'permission')?.class == null
	}
}

class TestController implements PluginController {
	Map<String, String> getRoutes() {
		['/foo/example': "example",
		 '/foo/json': "json"]
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
	Map<String, String> getRoutes() {
		['/foo/example': "example"]
	}
}
