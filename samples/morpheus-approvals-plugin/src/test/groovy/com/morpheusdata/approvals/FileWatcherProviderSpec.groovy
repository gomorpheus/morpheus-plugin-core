package com.morpheusdata.approvals

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.AccountIntegration
import com.morpheusdata.model.Instance
import com.morpheusdata.model.Policy
import com.morpheusdata.model.Request
import com.morpheusdata.model.RequestReference
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Subject

import java.nio.file.Files
import java.nio.file.Paths

@Stepwise
class FileWatcherProviderSpec extends Specification {

	Plugin plugin
	MorpheusContext context
	@Shared
	AccountIntegration accountIntegration = new AccountIntegration(configMap: [cm: [plugin: ["file-location": 'src/test/resources/approval-requests']]])
	@Subject
	FileWatcherProvider provider

	def setup() {
		plugin = Mock(Plugin)
		context = Mock(MorpheusContext)
		provider = new FileWatcherProvider(plugin, context)
	}

	void "writes file"() {
		given:
		Policy policy = new Policy(configMap: [someProp: 'someVal'])

		when:
		provider.createApprovalRequest([new Instance(id: 123)], new Request(id: 456), accountIntegration, policy, [:])

		then:
		String filePath = 'src/test/resources/approval-requests/AO_REQ_456.txt'
		File file = new File(filePath)
		file.exists()
		List<String> lines = Files.readAllLines(Paths.get(filePath))
		lines[0] == 'requested'
		lines[1] == 'AO_INST_123'
	}

	void "watchApprovals"() {
		given:
		Request request = new Request(
				externalId: 'AO_REQ_456',
				refs: [
						new RequestReference(
								externalId: 'AO_INST_123',
								status    : 'requested'
						)
				]
		)

		when:
		def resp = provider.monitorApproval(accountIntegration)

		then:
		resp.size() == 1
		resp[0].externalId == request.externalId
		resp[0].refs.size() == 1
		resp[0].refs[0].properties == request.refs[0].properties
	}
}
