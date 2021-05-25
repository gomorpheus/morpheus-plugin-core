package com.morpheusdata.cloud

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.MorpheusServicePlanService
import com.morpheusdata.core.MorpheusVirtualImageService
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ServicePlan
import com.morpheusdata.model.VirtualImage
import com.morpheusdata.model.projection.ServicePlanIdentityProjection
import com.morpheusdata.model.projection.VirtualImageIdentityProjection
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single
import io.reactivex.annotations.NonNull
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class DigitalOceanCloudProviderSpec extends Specification {

	@Subject
	DigitalOceanCloudProvider provider
	@Shared
	DigitalOceanApiService apiService
	@Shared
	MorpheusVirtualImageService virtualImageContext
	@Shared
	MorpheusServicePlanService servicePlanContext


	def setup() {
		Plugin plugin = Mock(Plugin)
		MorpheusContext context = Mock(MorpheusContext)
		virtualImageContext = Mock(MorpheusVirtualImageService)
		context.getVirtualImage() >> virtualImageContext
		servicePlanContext = Mock(MorpheusServicePlanService)
		context.getServicePlan() >> servicePlanContext
		provider = new DigitalOceanCloudProvider(plugin, context)
		apiService = Mock(DigitalOceanApiService)
		provider.apiService = apiService
	}

	void "validate - fail"() {
		given:
		Cloud cloud = new Cloud(configMap: [doApiKey: 'abc123', doUsername: 'user'])

		when:
		def res = provider.validate(cloud)

		then:
		!res.success
		res.msg == 'Choose a datacenter'
	}

	void "validate"() {
		given:
		Cloud cloud = new Cloud(configMap: [doApiKey: 'abc123', doUsername: 'user', datacenter: 'nyc1'])

		when:
		def res = provider.validate(cloud)

		then:
		1 * apiService.makeApiCall(*_) >> [resp: [statusLine: [statusCode: 200]], json: [:]]
		res.success
	}

	void "getNameForSize"() {
		expect:
		expected == provider.getNameForSize(sizeData)

		where:
		sizeData                                 | expected
		[vcpus: 1, memory: 25, disk: 25]         | 'Plugin Droplet 1 CPU, 25 MB Memory, 25 GB Storage'
		[vcpus: 3, memory: 25 * 1024, disk: 500] | 'Plugin Droplet 3 CPU, 25 GB Memory, 500 GB Storage'
	}

	void "initializeCloud - fail"() {
		given:
		Cloud cloud = new Cloud(code: 'doCloud', configMap: [doApiKey: 'abc123'])

		when:
		def resp = provider.initializeCloud(cloud)

		then:
		1 * apiService.makeApiCall(_, _) >> [resp: [success: false, statusLine: [statusCode: 400]]]
		!resp.success
		resp.msg == '400'
	}

	void "cacheImages"() {
		given:
		Cloud cloud = new Cloud(id: 1, configMap: [doApiKey: 'api_key'])
		VirtualImage updateImage = new VirtualImage(id: 1, externalId: 'abc123')
		VirtualImage newImage = new VirtualImage(externalId: 'def567')
		VirtualImage removeImage = new VirtualImage(id: 2, externalId: 'ghi890')
		Observable listFullObjectsObservable = Observable.create(new ObservableOnSubscribe<VirtualImage>() {
			@Override
			void subscribe(@NonNull ObservableEmitter<VirtualImage> emitter) throws Exception {
				try {
					List<VirtualImage> images = [updateImage]
					for (image in images) {
						emitter.onNext(image)
					}
					emitter.onComplete()
				} catch (Exception e) {
					emitter.onError(e)
				}
			}
		})

		Observable listSyncProjections = Observable.create(new ObservableOnSubscribe<VirtualImageIdentityProjection>() {
			@Override
			void subscribe(@NonNull ObservableEmitter<VirtualImageIdentityProjection> emitter) throws Exception {
				try {
					List<VirtualImageIdentityProjection> images = [new VirtualImageIdentityProjection(id: updateImage.id, externalId: updateImage.externalId), new VirtualImageIdentityProjection(id: removeImage.id, externalId: removeImage.externalId)]
					for (image in images) {
						emitter.onNext(image)
					}
					emitter.onComplete()
				} catch (Exception e) {
					emitter.onError(e)
				}
			}
		})

		when:
		provider.cacheImages(cloud)

		then:
		1 * apiService.makePaginatedApiCall(_, _, _, { map -> map.private == 'true' }) >> [[id: 'abc123']]
		1 * apiService.makePaginatedApiCall(_, _, _, { map -> !map.private }) >> [[id: 'def567']]
		1 * virtualImageContext.listById(_) >> listFullObjectsObservable
		1 * virtualImageContext.listSyncProjections(_) >> listSyncProjections
		1 * virtualImageContext.create({ list -> list.size() == 1 && list.first().externalId == newImage.externalId }, cloud) >> Single.just(true)
		1 * virtualImageContext.save([updateImage], cloud) >> Single.just([updateImage])
		1 * virtualImageContext.remove({ list -> list.size() == 1 && list.first().externalId == removeImage.externalId }) >> Single.just(true)
	}

	void "cacheSizes"() {
		given:
		Cloud cloud = new Cloud(id: 1, configMap: [doApiKey: 'api_key'])
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
		provider.cacheSizes(cloud, 'api_key')

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
}
