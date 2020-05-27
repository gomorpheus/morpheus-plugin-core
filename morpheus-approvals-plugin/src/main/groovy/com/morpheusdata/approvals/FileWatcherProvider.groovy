package com.morpheusdata.approvals

import com.morpheusdata.core.ApprovalProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Instance
import com.morpheusdata.model.Request

class FileWatcherProvider implements ApprovalProvider {
	Plugin plugin
	MorpheusContext morpheusContext

	FileWatcherProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
	}

	@Override
	MorpheusContext getMorpheusContext() {
		return morpheusContext
	}

	@Override
	Plugin getPlugin() {
		return plugin
	}

	@Override
	String getProviderCode() {
		return 'file-watcher-approval'
	}

	@Override
	String getProviderName() {
		return 'File Watcher Approval'
	}

	@Override
	Map createApprovalRequest(List<Instance> instance, Request request, Map opts) {
//		Request request = new Request(
//				externalId: 'AO123',
//				externalName: 'AO Request 123',
//				requestType: Request.ApprovalRequestType.INSTANCE_APPROVAL_TYPE
//		)
//		request
//		morpheusContext.compute.requestApproval(instance, getProviderCode())
		[success: true, externalId: 'AO123', externalName: 'AO Request 123',]
	}

	@Override
	void monitorApproval() {
		println('approval1')
		println('approval2')
		println('approval3')
	}
}
