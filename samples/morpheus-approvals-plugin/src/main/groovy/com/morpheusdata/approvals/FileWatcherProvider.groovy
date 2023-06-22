package com.morpheusdata.approvals

import com.morpheusdata.core.providers.ApprovalProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.AccountIntegration
import com.morpheusdata.model.Instance
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.Policy
import com.morpheusdata.model.Request
import com.morpheusdata.model.RequestReference
import com.morpheusdata.response.RequestResponse

/**
 * Example ApprovalProvider
 */
class FileWatcherProvider implements ApprovalProvider {
	Plugin plugin
	MorpheusContext morpheusContext

	FileWatcherProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
	}

	@Override
	MorpheusContext getMorpheus() {
		return morpheusContext
	}

	@Override
	Plugin getPlugin() {
		return plugin
	}

	@Override
	String getCode() {
		return 'file-watcher-approval'
	}

	@Override
	String getName() {
		return 'File Watcher Approval'
	}

	/**
	 * Writes approval requests as text files in the configured approval directory
	 * @param instances List of {@link Instance} or {@link App}
	 * @param request the Morpheus provision Request
	 * @param accountIntegration the integration details. OptionType values are keyed under configMap.cm.plugin
	 * @param policy the approval policy containing a Map config with values from provided optionTypes
	 * @param opts provision options
	 * @return RequestResponse with the created externalRequestId
	 */
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
			if(!file.exists()) {
				if(!file.createNewFile()) {
					println "failed to create file $file.absolutePath"
					resp = new RequestResponse(success: false)
					return resp
				}
			}
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
			def details = ""
			request.refs?.each { ref ->
				def detail = "Reference: refId: ${ref.refId}, refType: ${ref.refType}, name: ${ref.name}, price per month: ${ref.pricePerMonth}, currency: ${ref.currency}"
				if(ref.details) {
					detail += ", details ["
					ref.details.each { d ->
						detail += "(${d.category} ${d.type} ${d.name} ${d.fromValue} ${d.toValue})"
					}
					detail += "]"
				}
				details += detail

			}
			String fileContents = """requested
${resp.references*.externalId.join(',')}
${details}
"""
			file.write(fileContents)

		} catch(Exception e) {
			println e.message
			println e.printStackTrace()
			resp = new RequestResponse(success: false)
		}
		resp
	}

	/**
	 * Reads the files created in {@link #createApprovalRequest} and marshals them to a list of Request
	 * @param accountIntegration account integration details
	 * @return
	 */
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

	/**
	 * Create an OptionType for the approvals directory
	 * @return list of integration OptionType
	 */
	@Override
	List<OptionType> integrationOptionTypes() {
		[new OptionType(code: 'plugin-integration-1', name: 'File Location', fieldName: 'file-location', fieldLabel: 'File Location', displayOrder: 0)]
	}

	/**
	 * Sample Policy OptionType
	 * @return list of policy OptionType
	 */
	@Override
	List<OptionType> policyOptionTypes() {
		[new OptionType(code: 'plugin-policy-1', name: 'Policy Option 1', fieldName: 'file-watch-policy-1', fieldLabel: 'Custom Policy Option 1', displayOrder: 0)]
	}

	/**
	 * Searches the Integration OptionType configMap for the approvals dir configured in {@link #integrationOptionTypes() integrationOptionTypes}
	 * @param accountIntegration the AccountIntegration created in the Morpheus UI
	 * @return directory path
	 */
	private getApprovalsDir(AccountIntegration accountIntegration) {
		accountIntegration.configMap?.cm?.plugin?."file-location"
	}
}
