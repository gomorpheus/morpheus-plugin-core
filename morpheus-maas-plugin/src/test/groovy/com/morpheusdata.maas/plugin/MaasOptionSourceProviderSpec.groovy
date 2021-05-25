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
		def releaseModes = service.maasReleaseModes(cloud)

		then: "contains the correct values"
		releaseModes.collect {it.value} == ['release', 'quick-delete', 'delete']

		and: "is used as an option source"
		1 == maasCloudProvider.optionTypes.findAll {it.optionSource == 'maasReleaseModes'}.size()
	}

	void "maasResourcePools"() {
		given:
		Cloud cloud = new Cloud(serviceUrl: 'localhost', serviceToken: 'token')
		Observable listSyncProjections = Observable.create(new ObservableOnSubscribe<ComputeZonePoolIdentityProjection>() {
			@Override
			void subscribe(@NonNull ObservableEmitter<ComputeZonePoolIdentityProjection> emitter) throws Exception {
				try {
					List<ComputeZonePoolIdentityProjection> projections = [new ComputeZonePoolIdentityProjection(id: 1, name: 'Pool1', externalId: 'pool-1'),]
					for (projection in projections) {
						emitter.onNext(projection)
					}
					emitter.onComplete()
				} catch (Exception e) {
					emitter.onError(e)
				}
			}
		})

		when:
		def pools = service.maasResourcePools(cloud)

		then: "contains the correct values"
		1 * poolContext.listSyncProjections(_, _) >> listSyncProjections
		pools == [[name: 'Pool1', value: 'pool-1']]

		and: "is used as an option source"
		2 == maasCloudProvider.optionTypes.findAll {it.optionSource == 'maasResourcePools'}.size()
	}
}
