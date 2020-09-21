package com.morpheusdata.approvals

import com.morpheusdata.core.ApprovalProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.AccountIntegration
import com.morpheusdata.model.Instance
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.Policy
import com.morpheusdata.model.Request
import com.morpheusdata.model.RequestReference
import com.morpheusdata.response.RequestResponse

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
	RequestResponse createApprovalRequest(List instances, Request request, AccountIntegration accountIntegration, Policy policy, Map opts) {
		String externalRequestId = "AO_REQ_${request.id}"
		def resp
		try {
			String approvalsDirName = getApprovalsDir(accountIntegration)
			println(policy.configMap?."file-watch-policy-1")
			File approvalsDir = new File(approvalsDirName)
			if(!approvalsDir.exists()) {
				approvalsDir.mkdir()
			}
			File file = new File("$approvalsDirName/${externalRequestId}.txt")

			if(file.createNewFile()) {
				println "created file $file.absolutePath"
				resp = new RequestResponse(
						success: true,
						externalRequestId: externalRequestId,
						externalRequestName: 'AO Request 123',
						references: []
				)
				instances.each {
					resp.references << new RequestReference(refId: it.id, externalId: "AO_INST_$it.id", externalName: "AO Instance $it.name")
				}
				String fileContents = """requested
${resp.references*.externalId.join(',')}
"""
				file.write(fileContents)
			} else {
				println "failed to create file $file.absolutePath"
				resp = new RequestResponse(success: false)
			}
		} catch(Exception e) {
			println e.message
			resp = new RequestResponse(success: false)
		}
		resp
	}

	@Override
	List<Request> monitorApproval(AccountIntegration accountIntegration) {
		List approvalsResp = []
		File approvalsDir = new File(getApprovalsDir(accountIntegration))
		approvalsDir.listFiles().each { File file ->
			try {
				if(file.isFile() && file.exists() && file.name.endsWith('.txt')) {
					println "reading file $file.absolutePath"
					List<String> lines = file.readLines()
					approvalsResp << new Request(externalId: file.name - '.txt', refs: [new RequestReference(status: RequestReference.ApprovalStatus.valueOf(lines[0]), externalId: lines[1])])	
				}
			} catch(ex) {
				ex.printStackTrace()	
			}
			
		}
		approvalsResp
	}

	@Override
	List<OptionType> integrationOptionTypes() {
		[new OptionType(code: 'plugin-integration-1', name: 'File Location', fieldName: 'file-location', fieldLabel: 'File Location', displayOrder: 0)]
	}

	@Override
	List<OptionType> policyOptionTypes() {
		[new OptionType(code: 'plugin-policy-1', name: 'Policy Option 1', fieldName: 'file-watch-policy-1', fieldLabel: 'Custom Policy Option 1', displayOrder: 0)]
	}

	private getApprovalsDir(AccountIntegration accountIntegration) {
		accountIntegration.configMap?.cm?.plugin?."file-location"
	}
}
