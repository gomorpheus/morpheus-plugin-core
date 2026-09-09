package com.morpheusdata.model

import spock.lang.Specification

/**
 * Covers the storagePolicy association added so a plugin can read which named QoS tier the
 * user selected for a volume. See docs/storage-policy-volume-migration-plan.md.
 */
class StorageVolumeSpec extends Specification {

	void "storagePolicy defaults to null when no tier was selected"() {
		when:
		def volume = new StorageVolume()

		then:
		volume.getStoragePolicy() == null
	}

	void "setStoragePolicy stores the value and marks the property dirty"() {
		given:
		def volume = new StorageVolume()
		def policy = new StoragePolicy(code: 'gold', name: 'Gold')

		when:
		volume.setStoragePolicy(policy)

		then:
		volume.getStoragePolicy().is(policy)
		volume.isDirty('storagePolicy')
		volume.getDirtyPropertyValues().get('storagePolicy').is(policy)
	}

	void "storagePolicy retains its owning stamp so the right same-coded record is identifiable"() {
		given: "two owners publish the same code, which per-owner uniqueness allows"
		def volume = new StorageVolume()
		def ownedByServerSeven = new StoragePolicy(
			code: 'gold', name: 'Gold', refType: 'StorageServer', refId: '7'
		)

		when:
		volume.setStoragePolicy(ownedByServerSeven)

		then: "the volume points at one specific record, not merely at the code 'gold'"
		volume.getStoragePolicy().getCode() == 'gold'
		volume.getStoragePolicy().getRefType() == 'StorageServer'
		volume.getStoragePolicy().getRefId() == '7'
	}

	void "clearing storagePolicy back to null is tracked, matching an ON DELETE SET NULL"() {
		given:
		def volume = new StorageVolume()
		volume.setStoragePolicy(new StoragePolicy(code: 'gold', name: 'Gold'))

		when:
		volume.setStoragePolicy(null)

		then:
		volume.getStoragePolicy() == null
		volume.isDirty('storagePolicy')
	}
}
