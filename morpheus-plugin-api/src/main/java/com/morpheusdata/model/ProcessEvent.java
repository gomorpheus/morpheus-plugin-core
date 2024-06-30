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

package com.morpheusdata.model;

import java.util.Map;

public class ProcessEvent {

	public ProcessType getType() {
		return type;
	}

	public void setType(ProcessType type) {
		this.type = type;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Map getJobConfig() {
		return jobConfig;
	}

	public void setJobConfig(Map jobConfig) {
		this.jobConfig = jobConfig;
	}

	public enum ProcessType {
		ansibleCommand,
		ansibleInstall,
		ansiblePlaybook,
		ansibleProvision,
		ansibleRepo,
		ansibleTowerInventory,
		ansibleTowerJobLaunch,
		ansibleTowerJobTemplate,
		ansibleTowerProvision,
		applyPackage,
		applyResourceSpec,
		appProvision,
		appTerraformCommand,
		azureArmProvision,
		azureOperation,
		chefBootstrap,
		chefInstall,
		chefProvision,
		chefRun,
		cloning,
		configureResources,
		containerScript,
		containerTask,
		containerTemplate,
		containerWorkflow,
		deletesnapshot,
		deploy,
		deployFiles,
		deployPackage,
		deployScanner,
		deployStartInstance,
		deployStopInstance,
		executeAction,
		executeCommand,
		executeScan,
		executeScript,
		executeTask,
		executeTemplate,
		executeWorkflow,
		extractResults,
		general,
		guestCustomizations,
		instanceAction,
		instanceTask,
		instanceTerraformCommand,
		instanceWorkflow,
		localTask,
		localWorkflow,
		planResources,
		postProvision,
		provision,
		provisionAgent,
		provisionAppDeploy,
		provisionCloudInit,
		provisionConfig,
		provisionDeploy,
		provisionFinalize,
		provisionImage,
		provisionInstances,
		provisionItem,
		provisionItems,
		provisionLaunch,
		provisionNetwork,
		provisionResize,
		provisionResolve,
		provisionResources,
		provisionState,
		provisionStateRefresh,
		provisionUpdates,
		provisionVolumes,
		reconfigure,
		resize,
		resizeMemory,
		resizeStart,
		resizeStopInstance,
		resizeVolumes,
		resourceConfig,
		revert,
		saltCommand,
		saltExecute,
		saltInstall,
		saltMinion,
		saltProvision,
		saltState,
		securityScan,
		serverGroupProvision,
		serverGroupWorkflow,
		serverProvision,
		serverScript,
		serverTask,
		serverWorkflow,
		shutdown,
		snapshot,
		startup,
		teardown,
		terraformCommand
	}

	public ProcessType type;
	public String jobName;
	public Map jobConfig;
}
