package com.morpheusdata.model

import com.morpheusdata.model.projection.StoragePolicyIdentityProjection
import spock.lang.Specification

class StoragePolicyIdentityProjectionSpec extends Specification {
	void "setCode stores value and marks property dirty"() {
		given:
		def projection = new StoragePolicyIdentityProjection()

		when:
		projection.setCode('gold')

		then:
		projection.getCode() == 'gold'
		projection.isDirty('code')
		projection.getDirtyPropertyValues().get('code') == 'gold'
	}

	void "setName stores value and marks property dirty"() {
		given:
		def projection = new StoragePolicyIdentityProjection()

		when:
		projection.setName('Gold Tier')

		then:
		projection.getName() == 'Gold Tier'
		projection.isDirty('name')
		projection.getDirtyPropertyValues().get('name') == 'Gold Tier'
	}

	void "convenience constructor populates id, code, and name"() {
		when:
		def projection = new StoragePolicyIdentityProjection(1L, 'gold', 'Gold Tier')

		then:
		projection.getId() == 1L
		projection.getCode() == 'gold'
		projection.getName() == 'Gold Tier'
	}
}
