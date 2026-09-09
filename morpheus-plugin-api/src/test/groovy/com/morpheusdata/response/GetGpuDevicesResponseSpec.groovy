package com.morpheusdata.response

import com.morpheusdata.model.GpuDeviceInfo
import com.morpheusdata.model.GpuDeviceKind
import spock.lang.Specification

class GetGpuDevicesResponseSpec extends Specification {

	void "devices default to an empty non-null list"() {
		when:
		def response = new GetGpuDevicesResponse()

		then:
		response.devices != null
		response.devices.empty
	}

	void "devices can be populated"() {
		given:
		def device = new GpuDeviceInfo(uniqueId: 'pci-1', kind: GpuDeviceKind.PCI)
		def devices = [device]

		when:
		def response = new GetGpuDevicesResponse()
		response.devices = devices

		then:
		response.devices.is(devices)
		response.devices == [device]
	}

	void "null devices are normalized to an empty list"() {
		given:
		def response = new GetGpuDevicesResponse()

		when:
		response.devices = null

		then:
		response.devices != null
		response.devices.empty
	}

	void "contains only the device inventory without status metadata"() {
		expect:
		GetGpuDevicesResponse.declaredFields.findAll { !it.synthetic }*.name == ['devices']
	}
}
