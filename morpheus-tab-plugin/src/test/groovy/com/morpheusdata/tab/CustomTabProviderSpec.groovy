package com.morpheusdata.tab

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Instance
import com.morpheusdata.views.TemplateResponse
import spock.lang.Specification
import spock.lang.Subject

class CustomTabProviderSpec extends Specification {

	Plugin plugin = Mock(Plugin)
	MorpheusContext context = Mock(MorpheusContext)
	@Subject
	CustomTabProvider provider = new CustomTabProvider(plugin, context)

	void "renderTemplate with instance"() {
		when:
		Instance instance = new Instance(name: 'My Instance', maxCores: 2, status: 'Running', description: 'abc123')
		TemplateResponse res = provider.renderTemplate(instance)

		then:
		1 * plugin.classLoader >> this.class.classLoader
		res.text == """<h1>${instance.name}</h1>
<dl>
	<dt>Description</dt>
	<dd>${instance.description}</dd>
	<dt>Status</dt>
	<dd>${instance.status}</dd>
	<dt>Max Cores</dt>
	<dd>${instance.maxCores}</dd>
</dl>
<img src="/assets/custom-tab-1/foo/bar" />
"""
	}
}
