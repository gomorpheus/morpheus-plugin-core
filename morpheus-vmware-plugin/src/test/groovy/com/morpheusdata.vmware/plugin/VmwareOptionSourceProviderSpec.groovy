package com.morpheusdata.vmware.plugin

import com.morpheusdata.core.cloud.MorpheusCloudService
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.cloud.MorpheusComputeZonePoolService
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.projection.ComputeZonePoolIdentityProjection
import com.morpheusdata.model.projection.ReferenceDataSyncProjection
import com.morpheusdata.response.ServiceResponse
import com.morpheusdata.core.util.RestApiUtil
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.annotations.NonNull
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import io.reactivex.Single

class VmwareOptionSourceProviderSpec extends Specification {

	@Subject
	VmwareOptionSourceProvider service

	MorpheusContext context
	MorpheusCloudService cloudContext
	MorpheusComputeZonePoolService poolContext
	VmwarePlugin plugin
	@Shared VmwareCloudProvider vmwareCloudProvider
	@Shared VmwareComputeUtility vmwareComputeUtility

	void setup() {
		context = Mock(MorpheusContext)
		cloudContext = Mock(MorpheusCloudService)
		poolContext = Mock(MorpheusComputeZonePoolService)
		context.getCloud() >> cloudContext
		cloudContext.getPool() >> poolContext
		plugin = Mock(VmwarePlugin)
		vmwareComputeUtility = GroovySpy(VmwareComputeUtility, global: true)
		vmwareCloudProvider = new VmwareCloudProvider(plugin, context)
		service = new VmwareOptionSourceProvider(plugin, context)
	}

	void "vmwarePluginVersions"() {
		given:
		def args = [:]

		when:
		def releaseModes = service.vmwarePluginVersions(args)

		then: "contains the correct values"
		releaseModes.size() == 4
	}

	void "vmwarePluginVDC"() {
		given:
		def args = [[zone: [serviceUrl: '1.1.1.1', serviceUsername: 'u1', servicePassword: 'password']]]

		when:
		def datacenters = service.vmwarePluginVDC(args)

		then:
		VmwareProvisionProvider.getVmwareApiUrl(*_) >> 'http://localhost'
		VmwareCloudProvider.listDatastores(*_) >> Single.just(new Cloud(id: 10, serviceUrl: 'http://someurel', serviceToken: 'sometoken'))
		VmwareComputeUtility.listDatastores(*_) >> [success: true, datastores: [[name: 'dc1', id: '1']]]
        datacenters == [[name: 'dc1', value: '1']]
	}
}
