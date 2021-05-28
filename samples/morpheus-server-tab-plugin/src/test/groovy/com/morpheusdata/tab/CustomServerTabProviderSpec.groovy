package com.morpheusdata.tab

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.OsType
import com.morpheusdata.model.TaskConfig
import com.morpheusdata.views.HTMLResponse
import io.reactivex.Single
import spock.lang.Specification
import spock.lang.Subject
import sun.awt.OSInfo

class CustomServerTabProviderSpec extends Specification {

	Plugin plugin = Mock(Plugin)
	MorpheusContext context = Mock(MorpheusContext)
	@Subject
	CustomServerTabProvider provider = new CustomServerTabProvider(plugin, context)

	void "renderTemplate with server"() {
		when:
		ComputeServer server = new ComputeServer(displayName: 'My Server', serverType: 'node', hostname: 'hostName')
		HTMLResponse res = provider.renderTemplate(server)

		then:
		1 * plugin.name >> 'My Plugin'
		1 * plugin.classLoader >> this.class.classLoader
		1 * context.buildComputeServerConfig(_, _, _, _, _) >> Single.just(new TaskConfig())
		res.html.contains(server.displayName)
	}
}
