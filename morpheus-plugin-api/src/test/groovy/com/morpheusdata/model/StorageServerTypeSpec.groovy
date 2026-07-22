package com.morpheusdata.model

import spock.lang.Specification

class StorageServerTypeSpec extends Specification {

	void "setShareOptionTypes stores value and marks property dirty"() {
		given:
		def type = new StorageServerType()
		def optionTypes = [new OptionType(name: 'Storage Server'), new OptionType(name: 'File Share')]

		when:
		type.setShareOptionTypes(optionTypes)

		then: 'the getter round-trips the value (this is what the ui sync loop reads)'
		type.getShareOptionTypes() == optionTypes

		and: 'the property is flagged dirty so it is persisted (same contract as bucketOptionTypes)'
		type.isDirty('shareOptionTypes')
	}

	void "setShareOptionTypes follows the same dirty contract as bucketOptionTypes"() {
		given:
		def type = new StorageServerType()
		def shares = [new OptionType(name: 'File Share')]
		def buckets = [new OptionType(name: 'Bucket')]

		when:
		type.setBucketOptionTypes(buckets)
		type.setShareOptionTypes(shares)

		then: 'both accessors behave identically'
		type.getBucketOptionTypes() == buckets
		type.getShareOptionTypes() == shares
		type.isDirty('bucketOptionTypes')
		type.isDirty('shareOptionTypes')
	}

	void "getShareOptionTypes returns null before it is set"() {
		given:
		def type = new StorageServerType()

		expect:
		type.getShareOptionTypes() == null
	}
}
