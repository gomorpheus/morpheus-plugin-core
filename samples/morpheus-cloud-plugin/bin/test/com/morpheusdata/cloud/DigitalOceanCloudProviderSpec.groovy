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
import com.morpheusdata.request.ValidateCloudRequest
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
		Plugin plugin = new DigitalOceanPlugin()
		MorpheusContext context = Mock(MorpheusContext)
		provider = new DigitalOceanCloudProvider(plugin, context)
		apiService = Mock(DigitalOceanApiService)
		provider.apiService = apiService
	}

	void "validate - fail"() {
		given:
		Cloud cloud = new Cloud(configMap: [doApiKey: 'abc123', doUsername: 'user'])
		ValidateCloudRequest validateCloudRequest = new ValidateCloudRequest("username", "password", "local", [:])

		when:
		def res = provider.validate(cloud, validateCloudRequest)

		then:
		!res.success
		res.msg == 'Choose a datacenter'
	}

	void "validate"() {
		given:
		Cloud cloud = new Cloud(configMap: [doApiKey: 'abc123', doUsername: 'user', datacenter: 'nyc1'])
		ValidateCloudRequest validateCloudRequest = new ValidateCloudRequest("user", "abc123", "local", [:])

		when:
		def res = provider.validate(cloud, validateCloudRequest)

		then:
		1 * apiService.makeApiCall(*_) >> [resp: [statusLine: [statusCode: 200]], json: [:]]
		res.success
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
}
