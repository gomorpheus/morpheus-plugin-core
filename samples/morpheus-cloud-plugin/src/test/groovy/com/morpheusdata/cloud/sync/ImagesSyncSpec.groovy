package com.morpheusdata.cloud.sync

import com.morpheusdata.cloud.DigitalOceanApiService
import com.morpheusdata.cloud.DigitalOceanCloudProvider
import com.morpheusdata.cloud.DigitalOceanPlugin
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.MorpheusServicePlanService
import com.morpheusdata.core.MorpheusVirtualImageService
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.VirtualImage
import com.morpheusdata.model.projection.VirtualImageIdentityProjection
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single
import io.reactivex.annotations.NonNull
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class ImagesSyncSpec extends Specification {

	@Subject
	SizesSync service
	@Shared
	DigitalOceanApiService apiService
	@Shared
	MorpheusVirtualImageService virtualImageContext

	def setup() {
		Plugin plugin = new DigitalOceanPlugin()
		MorpheusContext context = Mock(MorpheusContext)
		virtualImageContext = Mock(MorpheusVirtualImageService)
		context.getVirtualImage() >> virtualImageContext
		apiService = Mock(DigitalOceanApiService)
		Cloud cloud = new Cloud(configMap: [doApiKey: 'abc123', doUsername: 'user'])
		service = new SizesSync(plugin, cloud, apiService)
	}

	//TODO: this test doesn't work, needs some love
	@Ignore
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
		service.execute()

		then:
		1 * apiService.makePaginatedApiCall(_, _, _, { map -> map.private == 'true' }) >> [[id: 'abc123']]
		1 * apiService.makePaginatedApiCall(_, _, _, { map -> !map.private }) >> [[id: 'def567']]
		1 * virtualImageContext.listById(_) >> listFullObjectsObservable
		1 * virtualImageContext.listSyncProjections(_) >> listSyncProjections
		1 * virtualImageContext.create({ list -> list.size() == 1 && list.first().externalId == newImage.externalId }, cloud) >> Single.just(true)
		1 * virtualImageContext.save([updateImage], cloud) >> Single.just([updateImage])
		1 * virtualImageContext.remove({ list -> list.size() == 1 && list.first().externalId == removeImage.externalId }) >> Single.just(true)
	}
}
