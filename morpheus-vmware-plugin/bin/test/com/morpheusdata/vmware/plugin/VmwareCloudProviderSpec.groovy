package com.morpheusdata.vmware.plugin

import com.morpheusdata.core.cloud.MorpheusCloudService
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.cloud.MorpheusComputeZonePoolService
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.projection.ComputeZonePoolIdentityProjection
import com.morpheusdata.model.projection.ReferenceDataSyncProjection
import com.morpheusdata.response.ServiceResponse
import com.morpheusdata.vmware.plugin.utils.*
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.annotations.NonNull
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import io.reactivex.Single

class VmwareCloudProviderSpec extends Specification {

	@Subject
	VmwareCloudProvider service

	MorpheusContext context
	MorpheusCloudService cloudContext
	MorpheusComputeZonePoolService poolContext
	VmwarePlugin plugin
	@Shared VmwareComputeUtility vmwareComputeUtility

	void setup() {
		context = Mock(MorpheusContext)
		cloudContext = Mock(MorpheusCloudService)
		poolContext = Mock(MorpheusComputeZonePoolService)
		context.getCloud() >> cloudContext
		cloudContext.getPool() >> poolContext
		plugin = Mock(VmwarePlugin)
		vmwareComputeUtility = GroovySpy(VmwareComputeUtility, global: true)

		service = new VmwareCloudProvider(plugin, context)
	}

	void "DI works"() {
		expect:
		service.morpheus
	}

	void "getOptionTypes"() {
		when:
		def optionTypes = service.getOptionTypes()

		then:
		optionTypes.size() == 17
	}

	void "getComputeServerTypes"() {
		when:
		def serverTypes = service.getComputeServerTypes()

		then:
		serverTypes.size() == 5
	}
}
