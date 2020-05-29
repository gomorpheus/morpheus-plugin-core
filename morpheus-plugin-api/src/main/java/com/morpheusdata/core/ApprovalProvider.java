package com.morpheusdata.core;

import com.morpheusdata.model.Instance;
import com.morpheusdata.model.Request;
import com.morpheusdata.response.RequestResponse;

import java.util.List;
import java.util.Map;

public interface ApprovalProvider extends PluginProvider {
	RequestResponse createApprovalRequest(List<Instance> instances, Request request, Map opts);
	List<Request> monitorApproval();
}
