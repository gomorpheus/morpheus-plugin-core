package com.morpheusdata.approvals

import com.morpheusdata.core.ApprovalProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin

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
	void createApprovalRequest() {

	}

	@Override
	void monitorApproval() {

	}
}
