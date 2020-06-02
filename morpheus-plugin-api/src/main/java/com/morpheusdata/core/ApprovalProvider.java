package com.morpheusdata.core;

import com.morpheusdata.model.*;
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
	RequestResponse createApprovalRequest(List instances, Request request, Policy policy, Map opts);

	/**
	 * Periodically called to check on approval status
	 * @return Request objects with their corresponding {@link RequestReference} containing approval status
	 */
	List<Request> monitorApproval();

	/**
	 * Optionally provide custom configuration options when creating a new policy
	 * @return a List of OptionType
	 */
	List<OptionType> policyIntegrationOptionTypes();
}
