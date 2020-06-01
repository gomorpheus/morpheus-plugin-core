package com.morpheusdata.core;

import com.morpheusdata.model.App;
import com.morpheusdata.model.Instance;
import com.morpheusdata.model.Request;
import com.morpheusdata.response.RequestResponse;

import java.util.List;
import java.util.Map;

public interface ApprovalProvider extends PluginProvider {
	/**
	 *
	 * @param instances List of {@link Instance} or {@link App}
	 * @param request the Morpheus provision Request
	 * @param opts provision options
	 * @return a response object with a success status and references to external approval system
	 */
	RequestResponse createApprovalRequest(List instances, Request request, Map opts);
	List<Request> monitorApproval();
}
