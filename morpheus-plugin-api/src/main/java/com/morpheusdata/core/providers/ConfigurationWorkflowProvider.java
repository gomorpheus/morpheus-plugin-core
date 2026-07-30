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

import com.morpheusdata.response.ServiceResponse;
import com.morpheusdata.model.ConfigurationWorkflow;
import com.morpheusdata.model.ConfigurationWorkflowProgress;
import com.morpheusdata.model.ConfigurationWorkflowStep;
import com.morpheusdata.model.User;
import java.util.List;
import java.util.Map;

/**
 * Provides support for defining complex multi-step configuration workflow
 * workflows.
 * Each configuration workflow consists of multiple steps, where each step
 * contains a
 * wizard for configuration.
 * The orchestrator maintains state across all steps and coordinates with a
 * parent object to persist
 * the configuration workflow state. Final submission triggers a long-running
 * process
 * that executes the
 * orchestrated workflow.
 *
 * @author Andy Warner
 * @since 1.2.6
 */
public interface ConfigurationWorkflowProvider extends PluginProvider {

	/**
	 * Returns the display name for this configuration workflow
	 *
	 * @return configuration workflow display name
	 */
	String getWorkflowName();

	/**
	 * Returns a description of this configuration workflow's purpose
	 *
	 * @return configuration workflow description
	 */
	default String getWorkflowDescription() {
		return null;
	}

	/**
	 * Returns a complete ConfigurationWorkflow object representing this
	 * configuration workflow provider.
	 * This method constructs a ConfigurationWorkflow model from the provider's
	 * configuration,
	 * including the code, name, description, and steps.
	 *
	 * @return ConfigurationWorkflow object representing this configuration workflow
	 */
	default ConfigurationWorkflow getConfigurationWorkflow() {
		ConfigurationWorkflow workflow = new ConfigurationWorkflow();
		workflow.setCode(getCode());
		workflow.setName(getWorkflowName());
		workflow.setDescription(getWorkflowDescription());
		workflow.setSteps(getWorkflowSteps());
		return workflow;
	}

	/**
	 * Determines whether the specified user has permission to access this
	 * configuration workflow.
	 * This method allows providers to implement custom authorization logic based on
	 * user roles, permissions, or other criteria.
	 *
	 * @param user the user to check access for
	 * @return true if the user can access this configuration workflow, false
	 *         otherwise
	 */
	default boolean hasAccess(User user) {
		return true;
	}

	/**
	 * Returns the ordered list of configuration workflow steps that make up this
	 * workflow.
	 * The steps will be presented to the user in the order returned by this list.
	 *
	 * @return List of ConfigurationWorkflowStep objects defining the configuration
	 *         workflow
	 */
	List<ConfigurationWorkflowStep> getWorkflowSteps();

	/**
	 * Saves the configuration from a completed step to the configuration workflow
	 * state.
	 * This method is called after each step's wizard is successfully completed,
	 * allowing the configuration workflow to accumulate configuration across all
	 * steps.
	 *
	 * <p>The state map must use a <strong>flat</strong> structure: each step's data
	 * is stored at the top level keyed by step code.
	 *
	 * <p>Example state after two steps complete:
	 * <pre>
	 * {
	 *   "my-network-step":      { "managementIp": "10.0.0.1", ... },
	 *   "my-credentials-step":  { "username": "admin", ... },
	 *   "status":               "completed",
	 *   "completed":            true,
	 *   "lastCompletedStep":    "my-credentials-step"
	 * }
	 * </pre>
	 *
	 * <p>The ServiceResponse data map must contain a {@code workflowState} key
	 * holding the updated state map:
	 * {@code ServiceResponse.success([workflowState: newState])}.
	 *
	 * @param stepCode     the code of the step that was completed
	 * @param stepData     the configuration data collected from the step's wizard
	 * @param currentState the current configuration workflow state before this
	 *                     update
	 * @param opts         additional options or context
	 * @return ServiceResponse whose data contains {@code workflowState} — the
	 *         updated flat state map
	 */
	ServiceResponse<?> saveStepConfiguration(String stepCode, Map<String, Object> stepData,
			Map<String, Object> currentState, Map<String, Object> opts);

	/**
	 * Updates the parent object with the current configuration workflow state.
	 * This method is called after each step configuration is saved, allowing
	 * the parent object to persist and track the configuration workflow progress.
	 *
	 * @param parentObject               the parent object that holds the
	 *                                   configuration workflow
	 *                                   state
	 * @param configurationWorkflowState the current complete state of the
	 *                                   configuration workflow
	 * @param opts                       additional options or context
	 * @return ServiceResponse indicating success or failure of the update
	 */
	ServiceResponse<?> updateParentState(Object parentObject, Map<String, Object> configurationWorkflowState,
			Map<String, Object> opts);

	/**
	 * Validates the complete configuration workflow state before final submission.
	 * This method performs cross-step validation and business rule checks that
	 * span multiple configuration workflow steps.
	 *
	 * <p>The {@code configurationWorkflowState} map uses a <strong>flat</strong>
	 * structure: step data is stored at the top level keyed by step code (e.g.
	 * {@code state["my-network-step"]}). Metadata keys {@code status},
	 * {@code completed}, and {@code lastCompletedStep} are also present at the
	 * top level.
	 *
	 * @param configurationWorkflowState flat map of all step data, keyed by step
	 *                                   code, plus metadata keys
	 * @param parentObject               the parent object that holds the
	 *                                   configuration workflow state
	 * @param opts                       additional options or context
	 * @return ServiceResponse containing validation results. If validation fails,
	 *         the response should contain error messages explaining what went
	 *         wrong.
	 */
	ServiceResponse<?> validateConfigurationWorkflow(Map<String, Object> configurationWorkflowState,
			Object parentObject, Map<String, Object> opts);

	/**
	 * Submits and executes the configuration workflow.
	 * This method is called after all steps are completed and validated.
	 * It should initiate the actual execution of the configuration workflow.
	 *
	 * <p>The {@code configurationWorkflowState} map uses a <strong>flat</strong>
	 * structure: step data is stored at the top level keyed by step code.
	 *
	 * <p><strong>Setting system status:</strong> Set {@code parentObject.status = "initialized"}
	 * to signal that setup is complete.
	 *
	 * <p>Since this method typically triggers another long-running process or method,
	 * it returns a synchronous ServiceResponse. The long-running execution should
	 * be handled by the method this calls.
	 *
	 * @param configurationWorkflowState flat map of all step data, keyed by step
	 *                                   code, plus metadata keys
	 * @param parentObject               the parent object that holds the
	 * 	                                    configuration workflow
	 * 	                                    state
	 * @param opts                       additional options or context
	 * @return ServiceResponse containing the result of the configuration workflow
	 *         submission
	 */
	ServiceResponse<?> submitConfigurationWorkflow(Map<String, Object> configurationWorkflowState, Object parentObject,
			Map<String, Object> opts);

	/**
	 * Optional method to retrieve the current configuration workflow state from the
	 * parent
	 * object.
	 * This is useful for resuming an configuration workflow or reviewing its
	 * current
	 * progress.
	 *
	 * @param parentObject the parent object that holds the configuration workflow
	 *                     state
	 * @param opts         additional options or context
	 * @return Map containing the current configuration workflow state
	 */
	default Map<String, Object> getConfigurationWorkflowState(Object parentObject, Map<String, Object> opts) {
		return null;
	}

	/**
	 * Optional method to determine if a specific step should be shown based on
	 * the current configuration workflow state. This allows for conditional
	 * configuration workflow
	 * flows.
	 *
	 * @param step                       the configuration workflow step to evaluate
	 * @param configurationWorkflowState map containing the current state from all
	 *                                   previously
	 *                                   completed steps
	 * @param opts                       additional options or context
	 * @return true if the step should be shown, false to skip it
	 */
	default boolean shouldShowStep(ConfigurationWorkflowStep step, Map<String, Object> configurationWorkflowState,
			Map<String, Object> opts) {
		return true;
	}

	/**
	 * Optional method called after successful configuration workflow submission.
	 * Useful for cleanup, notifications, or triggering dependent actions after the
	 * configuration workflow process completes.
	 *
	 * @param configurationWorkflowState map containing all configuration data from
	 *                                   all
	 *                                   steps
	 * @param submitResponse             the response from
	 *                                   submitConfigurationWorkflow()
	 * @param parentObject               the parent object that holds the
	 *                                   configuration workflow
	 *                                   state
	 * @param opts                       additional options or context
	 */
	default void afterSubmit(Map<String, Object> configurationWorkflowState, ServiceResponse<?> submitResponse,
			Object parentObject,
			Map<String, Object> opts) {
		// Default implementation does nothing
	}

	/**
	 * Optional method to retrieve the progress of a running configuration workflow.
	 * This is used to track the execution status after the workflow has been submitted.
	 * Returns a standardized {@link ConfigurationWorkflowProgress} structure containing
	 * activities and their individual steps with completion percentages.
	 *
	 * <p>All system types that support progress tracking should override this method
	 * and return a {@link ConfigurationWorkflowProgress} populated with the current
	 * activity and step states.
	 *
	 * @param parentObject the parent object (e.g., System) that the workflow operates on
	 * @param opts         additional options or context
	 * @return ServiceResponse containing a {@link ConfigurationWorkflowProgress} with
	 *         activity and step-level progress details
	 */
	default ServiceResponse<ConfigurationWorkflowProgress> getConfigurationWorkflowProgress(Object parentObject,
			Map<String, Object> opts) {
		return ServiceResponse.error("Progress tracking not supported");
	}

	/**
	 * Optional method to cancel or abort a running configuration workflow process.
	 *
	 * @param configurationWorkflowState map containing the current configuration
	 *                                   workflow state
	 * @param parentObject               the parent object that holds the
	 *                                   configuration workflow
	 *                                   state
	 * @param opts                       additional options or context
	 * @return ServiceResponse indicating success or failure of the cancellation
	 */
	default ServiceResponse<?> cancelConfigurationWorkflow(Map<String, Object> configurationWorkflowState,
			Object parentObject, Map<String, Object> opts) {
		return ServiceResponse.error("Cancellation not supported");
	}
}
