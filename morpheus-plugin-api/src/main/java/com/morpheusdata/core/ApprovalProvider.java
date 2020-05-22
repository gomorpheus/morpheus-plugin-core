package com.morpheusdata.core;

public interface ApprovalProvider extends PluginProvider {
	void createApprovalRequest();
	void monitorApproval();
}
