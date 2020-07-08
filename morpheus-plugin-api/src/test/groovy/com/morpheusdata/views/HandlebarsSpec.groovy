package com.morpheusdata.views

import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.io.ClassPathTemplateLoader
import spock.lang.Specification

class HandlebarsSpec extends Specification {
	void "default template location"() {
		given:
		def model = new ViewModel<String>()
		model.object = "John"
		def hbt = new HandlebarsRenderer()

		when: "the template file is located in /resources/hbs/"
		def result = hbt.renderTemplate("defaultLocation", model)

		then:
		result.html == 'My name is John!\n'
		result.status == 200
	}

	void "template location"() {
		given:
		def model = new ViewModel<String>()
		model.object = "World"
		def hbt = new HandlebarsRenderer("renderer")

		when:
		def result = hbt.renderTemplate("hbs/test", model)

		then:
		result.html == 'Hello World!\n'
		result.status == 200
	}

	void "template location with scoped lookup"() {
		given:
		def model = new ViewModel<String>()
		model.object = "World"
		def hbt = new HandlebarsRenderer("renderer/hbs/")

		when:
		def result = hbt.renderTemplate("test", model)

		then:
		result.html == 'Hello World!\n'
		result.status == 200
	}

	void "template location - no file exists"() {
		given:
		def model = new ViewModel<String>()
		model.object = "World"
		def hbt = new HandlebarsRenderer('renderer')

		when:
		def result = hbt.renderTemplate("bad/location", model)

		then:
		result.html == 'Template file not found: bad/location'
		result.status == 400
	}

	void "string template"() {
		given:
		def model = new ViewModel<String>()
		model.object = "World"
		String template = "Hello {{this}}!"
		def hbt = new HandlebarsRenderer()

		when:
		def result = hbt.render(template, model)

		then:
		result.html == 'Hello World!'
		result.status == 200
	}

	void "Template string null"() {
		given:
		def model = new ViewModel<String>()
		model.object = "World"
		String template = null
		def hbt = new HandlebarsRenderer()

		when:
		def result = hbt.render(template, model)

		then:
		result.html == "No template defined."
		result.status == 400
	}

	void "Template with Map Model"() {
		given:
		def model = new ViewModel<Map>()
		model.object = [name: "Eric", color: "green"]
		String template = "{{name}}'s favorite color is {{color}}."
		def hbt = new HandlebarsRenderer()

		when:
		def result = hbt.render(template, model)

		then:
		result.html == "Eric's favorite color is green."
		result.status == 200
	}

	void "Template with Object"() {
		given:
		def model = new ViewModel<Server>()
		model.object = new Server(name: "tomcat", ip: "127.0.0.1", running: true)
		String template = "{{name}} server ip is {{ip}} and is running == {{running}}."
		def hbt = new HandlebarsRenderer()

		when:
		def result = hbt.render(template, model)

		then:
		result.html == "tomcat server ip is 127.0.0.1 and is running == true."
		result.status == 200
	}


	void "Template with null Model"() {
		given:
		def model = new ViewModel<String>()
		model.object = null
		String template = "ok null model"
		def hbt = new HandlebarsRenderer()

		when:
		def result = hbt.render(template, model)

		then:
		result.html == "ok null model"
		result.status == 200
	}

	void "custom hbs engine with constructor"() {
		given:
		def customLoader = new ClassPathTemplateLoader('/myplugin/')
		def engine = new Handlebars(customLoader)
		when:
		def hbt = new HandlebarsRenderer(engine)
		then:
		hbt.engine == engine
		hbt.engine.loader == customLoader
	}

	void "hbs engine with prefix"() {
		when:
		def hbt = new HandlebarsPluginTemplateLoader("myplugin/", this.class.classLoader)
		then:
		hbt.prefix == "myplugin/"
		when:
		hbt = new HandlebarsPluginTemplateLoader(this.class.classLoader)
		then:
		hbt.prefix == "hbs/"
	}

	void "template response static helper methods"() {
		expect:
		HTMLResponse.success().html == ""
		HTMLResponse.success().status == 200
		HTMLResponse.success("message").html == "message"
		HTMLResponse.success("message").status == 200
		HTMLResponse.error("error").html == "error"
		HTMLResponse.error("error").status == 400
		HTMLResponse.error("error", 422).html == "error"
		HTMLResponse.error("error", 422).status == 422
	}

	void "template with asset helper"() {
		given:
		def model = new ViewModel<String>()
		model.object = "World"
		def hbt = new HandlebarsRenderer('renderer')
		hbt.registerAssetHelper('Handlebars Plugin')

		when:
		def result = hbt.renderTemplate("hbs/assetHelperTest", model)

		then:
		result.html == 'Hello World!\n<img src="/assets/plugin/handlebars-plugin/foo/bar" />\n'
		result.status == 200
	}

	class Server {
		String ip
		String name
		Boolean running
	}
}

