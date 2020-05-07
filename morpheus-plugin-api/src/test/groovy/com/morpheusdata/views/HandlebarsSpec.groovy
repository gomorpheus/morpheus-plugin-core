package com.morpheusdata.views

import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.io.ClassPathTemplateLoader
import spock.lang.Specification

class HandlebarsSpec extends Specification {
	void "template location"() {
		given:
		def model = new ViewModel<String>()
		model.object = "World"
		def hbt = new HandlebarsRenderer()

		when:
		def result = hbt.renderTemplate("/renderer/hbs/test", model)

		then:
		result.text == 'Hello World!\n'
		result.status == 200
	}

	void "template location with scoped lookup"() {
		given:
		def model = new ViewModel<String>()
		model.object = "World"
		def hbt = new HandlebarsRenderer("/renderer/hbs/")

		when:
		def result = hbt.renderTemplate("test", model)

		then:
		result.text == 'Hello World!\n'
		result.status == 200
	}

	void "template location - no file exists"() {
		given:
		def model = new ViewModel<String>()
		model.object = "World"
		def hbt = new HandlebarsRenderer()

		when:
		def result = hbt.renderTemplate("/bad/location", model)

		then:
		result.text == 'Template file not found: /bad/location.hbs'
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
		result.text == 'Hello World!'
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
		result.text == "No template defined."
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
		result.text == "Eric's favorite color is green."
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
		result.text == "tomcat server ip is 127.0.0.1 and is running == true."
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
		result.text == "ok null model"
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
		def hbt = new HandlebarsRenderer("/myplugin/")
		then:
		hbt.engine.loader.prefix == "/myplugin/"
	}

	void "template response static helper methods"() {
		expect:
		TemplateResponse.success().text == ""
		TemplateResponse.success().status == 200
		TemplateResponse.success("message").text == "message"
		TemplateResponse.success("message").status == 200
		TemplateResponse.error("error").text == "error"
		TemplateResponse.error("error").status == 400
		TemplateResponse.error("error", 422).text == "error"
		TemplateResponse.error("error", 422).status == 422
	}

	class Server {
		String ip
		String name
		Boolean running
	}
}

