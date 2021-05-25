package com.morpheusdata.maas.plugin

import com.morpheusdata.core.cloud.MorpheusCloudService
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.cloud.MorpheusComputeZonePoolService
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.projection.ComputeZonePoolIdentityProjection
import com.morpheusdata.model.projection.ReferenceDataSyncProjection
import com.morpheusdata.response.ServiceResponse
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.annotations.NonNull
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import io.reactivex.Single

class MaasCloudProviderSpec extends Specification {

	@Subject
	MaasCloudProvider service

	MorpheusContext context
	MorpheusCloudService cloudContext
	MorpheusComputeZonePoolService poolContext
	MaasPlugin plugin
	@Shared MaasComputeUtility maasComputeUtility

	void setup() {
		context = Mock(MorpheusContext)
		cloudContext = Mock(MorpheusCloudService)
		poolContext = Mock(MorpheusComputeZonePoolService)
		context.getCloud() >> cloudContext
		cloudContext.getPool() >> poolContext
		plugin = Mock(MaasPlugin)
		maasComputeUtility = GroovySpy(MaasComputeUtility, global: true)

		service = new MaasCloudProvider(plugin, context)
	}

	void "DI works"() {
		expect:
		service.morpheus
	}

	void "getOptionTypes"() {
		when:
		def optionTypes = service.getOptionTypes()

		then:
		optionTypes.size() == 5
	}

	void "cacheRackControllers"() {
		given:
		Cloud cloud = new Cloud(id: 1, serviceUrl: 'http://localhost', serviceToken: 'api_key')

		when:
		service.cacheRackControllers(cloud, [:])

		then:
		1 * MaasComputeUtility.listRackControllers(_, _) >> new ServiceResponse(success: true, data: [[system_id: 'abc', fqdn: 'def', hostname: 'ghi']])
		1 * context.cloud.listReferenceDataByCategory(_, _) >> Single.just(new ReferenceDataSyncProjection())
	}

	void "cacheRackControllers - api failure"() {
		given:
		Cloud cloud = new Cloud(id: 1, serviceUrl: 'http://localhost', serviceToken: 'api_key')

		when:
		service.cacheRackControllers(cloud, [:])

		then:
		1 * MaasComputeUtility.listRackControllers(_, _) >> new ServiceResponse(success: false)
		0 * context.cloud.listReferenceDataByCategory(_, _) >> Single.just(new ReferenceDataSyncProjection())
	}

	void "cacheRegionControllers"() {
		given:
		Cloud cloud = new Cloud(id: 1, serviceUrl: 'http://localhost', serviceToken: 'api_key')

		when:
		service.cacheRegionControllers(cloud, [:])

		then:
		1 * MaasComputeUtility.listRegionControllers(_, _) >> new ServiceResponse(success: true, data: [[system_id: 'abc', fqdn: 'def', hostname: 'ghi']])
		1 * context.cloud.listReferenceDataByCategory(_, _) >> Single.just(new ReferenceDataSyncProjection())
	}

	void "cacheRegionControllers - api failure"() {
		given:
		Cloud cloud = new Cloud(id: 1, serviceUrl: 'http://localhost', serviceToken: 'api_key')

		when:
		service.cacheRegionControllers(cloud, [:])

		then:
		1 * MaasComputeUtility.listRegionControllers(_, _) >> new ServiceResponse(success: false)
		0 * context.cloud.listReferenceDataByCategory(_, _) >> Single.just(new ReferenceDataSyncProjection())
	}
}
