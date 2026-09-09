package com.morpheusdata.core.providers

import com.morpheusdata.model.ReferenceData
import com.morpheusdata.model.StorageBucket
import com.morpheusdata.response.ServiceResponse
import spock.lang.Specification

class StorageProviderFileSharesSpec extends Specification {

	void "validateFileShare defaults to a no-op success response when not overridden"() {
		given:
		def provider = new MinimalFileShareProvider()

		when:
		def result = provider.validateFileShare(new StorageBucket(), [:])

		then:
		result.success
	}

	void "syncFileShare defaults to a no-op success response when not overridden"() {
		given:
		def provider = new MinimalFileShareProvider()

		when:
		def result = provider.syncFileShare(new StorageBucket(), [:])

		then:
		result.success
	}

	void "createFileShareAccess defaults to a no-op success response when not overridden"() {
		given:
		def provider = new MinimalFileShareProvider()

		when:
		def result = provider.createFileShareAccess(new StorageBucket(), [:], [:])

		then:
		result.success
	}

	void "deleteFileShareAccess defaults to a no-op success response when not overridden"() {
		given:
		def provider = new MinimalFileShareProvider()

		when:
		def result = provider.deleteFileShareAccess(new StorageBucket(), new ReferenceData(), [:])

		then:
		result.success
	}

	private static class MinimalFileShareProvider implements StorageProviderFileShares {
		@Override
		ServiceResponse createFileShare(StorageBucket storageShare, Map opts) {
			return ServiceResponse.success()
		}

		@Override
		ServiceResponse updateFileShare(StorageBucket storageShare, Map opts) {
			return ServiceResponse.success()
		}

		@Override
		ServiceResponse deleteFileShare(StorageBucket storageShare, Map opts) {
			return ServiceResponse.success()
		}

		@Override
		Collection<String> getFileShareProviderTypes() {
			return []
		}
	}
}
