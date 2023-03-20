package com.morpheusdata.cloud.sync

import com.morpheusdata.cloud.DigitalOceanApiService
import com.morpheusdata.cloud.DigitalOceanCloudProvider
import com.morpheusdata.cloud.DigitalOceanPlugin
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.MorpheusServicePlanService
import com.morpheusdata.core.MorpheusVirtualImageService
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ServicePlan
import com.morpheusdata.model.projection.ServicePlanIdentityProjection
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single
import io.reactivex.annotations.NonNull
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class SizesSyncSpec extends Specification {

	@Subject
	SizesSync service
	@Shared
	DigitalOceanApiService apiService
	@Shared
	MorpheusServicePlanService servicePlanContext

	def setup() {
		Plugin plugin = new DigitalOceanPlugin()
		MorpheusContext context = Mock(MorpheusContext)
		servicePlanContext = Mock(MorpheusServicePlanService)
		context.getServicePlan() >> servicePlanContext
		apiService = Mock(DigitalOceanApiService)
		Cloud cloud = new Cloud(configMap: [doApiKey: 'abc123', doUsername: 'user'])
		service = new SizesSync(plugin, cloud, apiService)
	}

	//TODO: this test doesn't work, needs some love
	@Ignore
	void "cacheSizes"() {
		given:
		ServicePlan updatePlan = new ServicePlan(id: 1, externalId: 'abc123')
		ServicePlan createPlan = new ServicePlan(externalId: 'def567')
		ServicePlan removePlan = new ServicePlan(id: 2, externalId: 'ghi890')
		Observable listFullObjectsObservable = Observable.create(new ObservableOnSubscribe<ServicePlan>() {
			@Override
			void subscribe(@NonNull ObservableEmitter<ServicePlan> emitter) throws Exception {
				try {
					List<ServicePlan> plans = [updatePlan]
					for (plan in plans) {
						emitter.onNext(plan)
					}
					emitter.onComplete()
				} catch (Exception e) {
					emitter.onError(e)
				}
			}
		})

		Observable listSyncProjections = Observable.create(new ObservableOnSubscribe<ServicePlanIdentityProjection>() {
			@Override
			void subscribe(@NonNull ObservableEmitter<ServicePlanIdentityProjection> emitter) throws Exception {
				try {
					List<ServicePlanIdentityProjection> projections = [new ServicePlanIdentityProjection(id: updatePlan.id, externalId: updatePlan.externalId), new ServicePlanIdentityProjection(id: removePlan.id, externalId: removePlan.externalId)]
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
		service.execute()

		then:
		1 * apiService.makeApiCall(_, _) >> [
			json: [
				sizes: [
					[slug: createPlan.externalId, memory: 1024, disk: 25],
					[slug: updatePlan.externalId, memory: 1024, disk: 25]
				]
			]
		]
		1 * servicePlanContext.listById(_) >> listFullObjectsObservable
		1 * servicePlanContext.listSyncProjections(_) >> listSyncProjections
		1 * servicePlanContext.create({ list -> list.size() == 1 && list.first().externalId == createPlan.externalId }) >> Single.just(true)
		1 * servicePlanContext.save([updatePlan]) >> Single.just([updatePlan])
		1 * servicePlanContext.remove({ list -> list.size() == 1 && list.first().externalId == removePlan.externalId })
	}

	void "getNameForSize"() {
		expect:
		expected == service.getNameForSize(sizeData)

		where:
		sizeData                                 | expected
		[vcpus: 1, memory: 25, disk: 25]         | 'Plugin Droplet 1 CPU, 25 MB Memory, 25 GB Storage'
		[vcpus: 3, memory: 25 * 1024, disk: 500] | 'Plugin Droplet 3 CPU, 25 GB Memory, 500 GB Storage'
	}
}
