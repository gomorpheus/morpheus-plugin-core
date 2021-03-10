package com.morpheusdata.cloud

import com.morpheusdata.core.MorpheusComputeContext
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.VirtualImage
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
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
	MorpheusComputeContext computeContext


	def setup() {
		Plugin plugin = Mock(Plugin)
		MorpheusContext context = Mock(MorpheusContext)
		computeContext = Mock(MorpheusComputeContext)
		context.getCompute() >> computeContext
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
		Cloud cloud = new Cloud(id: 1,configMap: [doApiKey: 'api_key'])
		VirtualImage updateImage = new VirtualImage(externalId: 'abc123')
		VirtualImage newImage = new VirtualImage(externalId: 'def567')
		VirtualImage removeImage = new VirtualImage(externalId: 'ghi890')
		Observable listImagesObservable = Observable.create(new ObservableOnSubscribe<VirtualImage>() {
			@Override
			void subscribe(@NonNull ObservableEmitter<VirtualImage> emitter) throws Exception {
				try {
					List<VirtualImage> images = [updateImage, removeImage]
					for (image in images) {
						emitter.onNext(image)
					}
					emitter.onComplete()
				} catch(Exception e) {
					emitter.onError(e)
				}
			}
		})

		when:
		provider.cacheImages(cloud)

		then:
		1 * apiService.makePaginatedApiCall(_,_,_,{map -> map.private == 'true'}) >> [[id: 'abc123']]
		1 * apiService.makePaginatedApiCall(_,_,_,{map -> !map.private}) >> [[id: 'def567']]
		1 * computeContext.listVirtualImages(cloud) >>  listImagesObservable
//		1 * computeContext.saveVirtualImage([newImage])
//		1 * computeContext.updateVirtualImage([updateImage])
		1 * computeContext.removeVirtualImage([removeImage])
	}
}
