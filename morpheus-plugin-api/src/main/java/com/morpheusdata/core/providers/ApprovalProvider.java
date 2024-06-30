/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.core.providers;

import com.morpheusdata.model.*;
import com.morpheusdata.response.RequestResponse;

import java.util.List;
import java.util.Map;

/**
 * Provides capability to create custom approval policies
 *
 * @author Mike Truso
 * @since 0.15.1
 */
public interface ApprovalProvider extends PluginProvider {
	/**
	 *
	 * @param instances List of {@link Instance} or {@link App} or {@link ComputeServer}
	 * @param request the Morpheus provision Request
	 * @param accountIntegration the integration details. OptionType values are keyed under configMap.cm.plugin
	 * @param policy the approval policy containing a Map config with values from provided optionTypes
	 * @param opts provision options
	 * @return a response object with a success status and references to external approval system
	 */
	RequestResponse createApprovalRequest(List instances, Request request, AccountIntegration accountIntegration, Policy policy, Map opts);

	/**
	 * Periodically called to check on approval status
	 * @param accountIntegration account integration details
	 * @return Request objects with their corresponding {@link RequestReference} containing approval status
	 */
	List<Request> monitorApproval(AccountIntegration accountIntegration);

	/**
	 * Optionally provide custom configuration options when creating a new {@link AccountIntegration}
	 * @return a List of OptionType
	 */
	List<OptionType> integrationOptionTypes();

	/**
	 * Optionally provide custom configuration options when creating a new {@link Policy}
	 * @return a List of OptionType
	 */
	List<OptionType> policyOptionTypes();
}
