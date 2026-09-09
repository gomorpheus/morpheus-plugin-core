package com.morpheusdata.model

import spock.lang.Specification

class StoragePolicySpec extends Specification {
	void "setCode stores value and marks property dirty"() {
		given:
		def policy = new StoragePolicy()

		when:
		policy.setCode('gold')

		then:
		policy.getCode() == 'gold'
		policy.isDirty('code')
		policy.getDirtyPropertyValues().get('code') == 'gold'
	}

	void "setName stores value and marks property dirty"() {
		given:
		def policy = new StoragePolicy()

		when:
		policy.setName('Gold Tier')

		then:
		policy.getName() == 'Gold Tier'
		policy.isDirty('name')
		policy.getDirtyPropertyValues().get('name') == 'Gold Tier'
	}

	void "setDisplayOrder stores value and marks property dirty"() {
		given:
		def policy = new StoragePolicy()

		when:
		policy.setDisplayOrder(1)

		then:
		policy.getDisplayOrder() == 1
		policy.isDirty('displayOrder')
		policy.getDirtyPropertyValues().get('displayOrder') == 1
	}

	void "setEnabled stores value and marks property dirty"() {
		given:
		def policy = new StoragePolicy()

		when:
		policy.setEnabled(true)

		then:
		policy.getEnabled() == true
		policy.isDirty('enabled')
		policy.getDirtyPropertyValues().get('enabled') == true
	}

	void "setProvisionTypeCode stores value and marks property dirty"() {
		given:
		def policy = new StoragePolicy()

		when:
		policy.setProvisionTypeCode('vmware')

		then:
		policy.getProvisionTypeCode() == 'vmware'
		policy.isDirty('provisionTypeCode')
		policy.getDirtyPropertyValues().get('provisionTypeCode') == 'vmware'
	}

	void "setRefType stores value and marks property dirty"() {
		given:
		def policy = new StoragePolicy()

		when:
		policy.setRefType('StorageServer')

		then:
		policy.getRefType() == 'StorageServer'
		policy.isDirty('refType')
		policy.getDirtyPropertyValues().get('refType') == 'StorageServer'
	}

	void "setRefId stores value and marks property dirty"() {
		given:
		def policy = new StoragePolicy()

		when:
		policy.setRefId('42')

		then:
		policy.getRefId() == '42'
		policy.isDirty('refId')
		policy.getDirtyPropertyValues().get('refId') == '42'
	}

	void "refType and refId default to null so a Morpheus-seeded record is unowned"() {
		when:
		def policy = new StoragePolicy()

		then:
		policy.getRefType() == null
		policy.getRefId() == null
	}
}
