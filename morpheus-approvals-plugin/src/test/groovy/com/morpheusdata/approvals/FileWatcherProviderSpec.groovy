package com.morpheusdata.approvals

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Instance
import com.morpheusdata.model.Request
import spock.lang.Specification
import spock.lang.Subject

import java.nio.file.Files
import java.nio.file.Paths

class FileWatcherProviderSpec extends Specification {

	Plugin plugin
	MorpheusContext context
	@Subject
	FileWatcherProvider provider

	def setup() {
		plugin = Mock(Plugin)
		context = Mock(MorpheusContext)
		provider = new FileWatcherProvider(plugin, context)
	}

	void "writes file"() {
		when:
		provider.createApprovalRequest([new Instance(id: 123)], new Request(id: 456), [:])

		then:
		File file = new File("approvals/AO_REQ_456.txt")
		file.exists()
		List<String> lines = Files.readAllLines(Paths.get("approvals/AO_REQ_456.txt"))
		lines[0] == 'requested'
		lines[1] == 'AO_INST_123'
	}

	void "watchApprovals"() {
		when:
		def resp = provider.monitorApproval()

		then:
		resp == [
				[
						externalId: 'AO_REQ_456',
						itemStatus: [
								[
										externalId: 'AO_INST_123',
										status    : 'requested'
								]
						]
				]
		]
	}
}
