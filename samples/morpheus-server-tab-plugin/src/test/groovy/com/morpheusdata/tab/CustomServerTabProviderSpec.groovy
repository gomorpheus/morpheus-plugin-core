package com.morpheusdata.tab

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.TaskConfig
import com.morpheusdata.views.HTMLResponse
import io.reactivex.Single
import spock.lang.Specification
import spock.lang.Subject

class CustomServerTabProviderSpec extends Specification {

	Plugin plugin = Mock(Plugin)
	MorpheusContext context = Mock(MorpheusContext)
	@Subject
	CustomServerTabProvider provider = new CustomServerTabProvider(plugin, context)

	void "renderTemplate with server"() {
		when:
		ComputeServer server = new ComputeServer(displayName: 'My Server', serverOs: 'Operating System', hostname: 'hostName')
		HTMLResponse res = provider.renderTemplate(server)

		then:
		1 * plugin.name >> 'My Plugin'
		1 * plugin.classLoader >> this.class.classLoader
		1 * context.buildComputeServerConfig(_, _, _, _, _) >> Single.just(new TaskConfig())
		res.html == """<h1>${server.displayName}</h1>
<dl>
	<dt>Os Type</dt>
	<dd>${server.serverOs}</dd>
	<dt>Host Name</dt>
	<dd>${server.hostname}</dd>
</dl>
<img src="/assets/plugin/my-plugin/foo/bar" />
"""
	}
}
