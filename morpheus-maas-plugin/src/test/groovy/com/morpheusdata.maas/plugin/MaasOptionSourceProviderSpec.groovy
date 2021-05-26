package com.morpheusdata.maas.plugin

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

class MaasOptionSourceProviderSpec extends Specification {

	@Subject
	MaasOptionSourceProvider service

	MorpheusContext context
	MorpheusCloudService cloudContext
	MorpheusComputeZonePoolService poolContext
	MaasPlugin plugin
	@Shared MaasCloudProvider maasCloudProvider
	@Shared MaasComputeUtility maasComputeUtility

	void setup() {
		context = Mock(MorpheusContext)
		cloudContext = Mock(MorpheusCloudService)
		poolContext = Mock(MorpheusComputeZonePoolService)
		context.getCloud() >> cloudContext
		cloudContext.getPool() >> poolContext
		plugin = Mock(MaasPlugin)
		maasComputeUtility = GroovySpy(MaasComputeUtility, global: true)
		maasCloudProvider = new MaasCloudProvider(plugin, context)
		service = new MaasOptionSourceProvider(plugin, context)
	}

	void "maasReleaseModes"() {
		given:
		Cloud cloud = new Cloud()

		when:
		def releaseModes = service.maasPluginReleaseModes(cloud)

		then: "contains the correct values"
		releaseModes.collect {it.value} == ['release', 'quick-delete', 'delete']

		and: "is used as an option source"
		1 == maasCloudProvider.optionTypes.findAll {it.optionSource == 'maasReleaseModes'}.size()
	}

	void "maasPluginResourcePools"() {
		given:
		def args = [[zoneId: 10]]

		when:
		def pools = service.maasPluginResourcePools(args)

		then: "contains the correct values"
		1 * cloudContext.getCloudById(10) >> Single.just(new Cloud(id: 10, serviceUrl: 'http://someurel', serviceToken: 'sometoken'))
		1 * MaasComputeUtility.listResourcePools(*_) >> new ServiceResponse(success: true, data: [[name: 'Pool1', id: 'pool-1']])
		pools == [[name: 'Pool1', value: 'pool-1']]

		and: "is used as an option source"
		2 == maasCloudProvider.optionTypes.findAll {it.optionSource == 'maasPluginResourcePools'}.size()
	}
}
