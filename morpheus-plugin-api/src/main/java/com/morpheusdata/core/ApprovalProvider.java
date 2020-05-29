package com.morpheusdata.core;

import com.morpheusdata.model.Instance;
import com.morpheusdata.model.Request;

import java.util.List;
import java.util.Map;

public interface ApprovalProvider extends PluginProvider {
	Map createApprovalRequest(List<Instance> instances, Request request, Map opts);
	List<Map> monitorApproval();
}
