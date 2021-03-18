package com.morpheusdata.maas.plugin

import com.morpheusdata.core.MorpheusCloudContext
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ComputeZonePool
import com.morpheusdata.model.projection.ReferenceDataSyncProjection
import com.morpheusdata.response.ServiceResponse
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import io.reactivex.Single

class MaasCloudProviderSpec extends Specification {

	@Subject
	MaasCloudProvider service

	MorpheusContext context
	MorpheusCloudContext cloudContext
	MaasPlugin plugin
	@Shared MaasComputeUtility maasComputeUtility

	void setup() {
		context = Mock(MorpheusContext)
		cloudContext = Mock(MorpheusCloudContext)
		context.getCloud() >> cloudContext
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

	void "maasReleaseModes"() {
		when:
		def releaseModes = service.maasReleaseModes()

		then: "contains the correct values"
		releaseModes.collect {it.value} == ['release', 'quick-delete', 'delete']

		and: "is used as an option source"
		1 == service.optionTypes.findAll {it.optionSource == 'maasReleaseModes'}.size()
	}

	void "maasResourcePools"() {
		given:
		Cloud cloud = new Cloud(serviceUrl: 'localhost', serviceToken: 'token')
		when:
		def pools = service.maasResourcePools(cloud)

		then: "contains the correct values"
		1 * cloudContext.readResourcePools(_, _) >> Single.just([new ComputeZonePool(id: 1, name: 'Pool1', externalId: 'pool-1')])
		pools == [[name: 'Pool1', value: 'pool-1']]

		and: "is used as an option source"
		2 == service.optionTypes.findAll {it.optionSource == 'maasResourcePools'}.size()
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
