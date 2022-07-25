package com.morpheusdata.rubrik

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.backup.AbstractBackupProvider
import com.morpheusdata.model.BackupIntegration
import com.morpheusdata.model.BackupProviderType as BackupProviderTypeModel
import com.morpheusdata.model.BackupType
import com.morpheusdata.model.Icon
import com.morpheusdata.model.OptionType
import com.morpheusdata.response.ServiceResponse
import com.morpheusdata.core.util.RestApiUtil
import groovy.util.logging.Slf4j
import groovy.json.JsonOutput

@Slf4j
class RubrikBackupProvider extends AbstractBackupProvider {

	static String LOCK_NAME = 'backups.rubrik'

	RubrikBackupProvider(Plugin plugin, MorpheusContext morpheusContext) {
		super(plugin, morpheusContext);
	}

	@Override
	String getCode() {
		return 'rubrik2'
	}

	@Override
	String getName() {
		return 'Rubrik 2.0'
	}

	@Override
	Icon getIcon() {
		return new Icon(path:"rubrik.svg", darkPath: "rubrik-dark.svg")
	}

	@Override
	BackupProviderTypeModel getBackupProviderType() {
		BackupProviderTypeModel providerType = new BackupProviderTypeModel(
			code: this.getCode(),
			name: this.getName(),
			enabled: true,
			creatable: true,
			providerService: this.getCode(),
			jobService: "rubrik2BackupJobProvider",
			restoreNewEnabled: true,
			hasBackups: true,
			hasCreateJob: true,
			hasCloneJob: true,
			hasAddToJob: true,
			hasOptionalJob: true,
			hasSchedule: true,
			hasRetentionCount: true,
		)
		providerType.optionTypes = []
		providerType.optionTypes << new OptionType(
			code:"backupProviderType.${this.getCode()}.host", inputType:OptionType.InputType.TEXT, name:'host', category:"backupProviderType.${this.getCode()}",
			fieldName:'host', fieldCode: 'gomorpheus.optiontype.Host', fieldLabel:'Host', fieldContext:'domain', fieldGroup:'default',
			required:true, enabled:true, editable:true, global:false, placeHolder:null, helpBlock:'', defaultValue:null, custom:false,
			displayOrder:10, fieldClass:null
		)
		providerType.optionTypes << new OptionType(code:"backupProviderType.${this.getCode()}.credential", inputType:OptionType.InputType.CREDENTIAL, name:'credentials', optionSource:'credentials', category:"backupProviderType.${this.getCode()}",
			fieldName:'type', fieldCode:'gomorpheus.label.credentials', fieldLabel:'Credentials', fieldContext:'credential',
			required:true, enabled:true, editable:true, global:false, placeHolder:null, helpBlock:'', defaultValue:'local', custom:false,
			displayOrder:25, fieldClass:null, wrapperClass:null, config: JsonOutput.toJson(credentialTypes:['username-password']).toString()
		)
		providerType.optionTypes << new OptionType(code:"backupProviderType.${this.getCode()}.username", inputType:OptionType.InputType.TEXT, name:'username', category:"backupProviderType.${this.getCode()}",
			fieldName:'username', fieldCode: 'gomorpheus.optiontype.Username', fieldLabel:'Username', fieldContext:'domain', fieldGroup:'default',
			required:false, enabled:true, editable:true, global:false, placeHolder:null, helpBlock:'', defaultValue:null, custom:false,
			displayOrder:30, fieldClass:null, localCredential:true
		)
		providerType.optionTypes << new OptionType(code:"backupProviderType.${this.getCode()}.password", inputType:OptionType.InputType.PASSWORD, name:'password', category:"backupProviderType.${this.getCode()}",
			fieldName:'password', fieldCode: 'gomorpheus.optiontype.Password', fieldLabel:'Password', fieldContext:'domain', fieldGroup:'default',
			required:false, enabled:true, editable:true, global:false, placeHolder:null, helpBlock:'', defaultValue:null, custom:false,
			displayOrder:35, fieldClass:null, localCredential:true
		)

		providerType.backupTypes = []
		providerType.backupTypes << new BackupType(
			code: 'rubrikVmwareBackupProvider2',
			name: 'Rubrik VMware',
			providerCode: this.getCode(),
			jobService: null,
			execService: "rubrikVmwareBackupExecutionProvider2",
			restoreService: "rubrikVmwareBackupRestoreProvider2",
			containerType: "single",
			copyToStore: false,
			downloadEnabled: false,
			restoreExistingEnabled: true,
			restoreNewEnabled: true,
			restoreType: 'online',
			restoreNewMode: 'VM_RESTORE',
			hasCopyToStore: false
		)

		return providerType
	}

	@Override
	Collection<BackupIntegration> getBackupIntegrations() {
		def rtn = []
		rtn << new BackupIntegration("rubrikVmwareBackupProvider2", null, 'vmware')
	}

}
