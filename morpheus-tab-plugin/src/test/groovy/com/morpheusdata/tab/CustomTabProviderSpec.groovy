package com.morpheusdata.tab

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.views.TemplateResponse
import spock.lang.Specification
import spock.lang.Subject

class CustomTabProviderSpec extends Specification {

	Plugin plugin = Mock(Plugin)
	MorpheusContext context = Mock(MorpheusContext)
	@Subject
	CustomTabProvider provider = new CustomTabProvider(plugin, context)

	void "renderTemplate"() {
		when:
		TemplateResponse res = provider.renderTemplate()

		then:
		res.text == "My Plugin Provider's name is Custom Tab 1!\n"
	}
}
