package com.morpheusdata.model

import spock.lang.Specification

class GpuDeviceInfoSpec extends Specification {

	void "retains all public GPU inventory fields"() {
		when:
		def device = new GpuDeviceInfo(
			uniqueId: 'pci_0000_65_00_0',
			kind: GpuDeviceKind.MDEV,
			vendor: 'NVIDIA',
			name: 'NVIDIA A100',
			uuid: 'GPU-1234',
			pciBusId: '0000:65:00.0',
			parentUniqueId: 'pci_0000_65_00_0',
			vmId: 'vm-42',
			vgpuType: 'A100-4C',
			vgpuTypeId: 9_876_543_210L,
			vendorId: Integer.valueOf(0x10de),
			productId: Integer.valueOf(0x20b0)
		)

		then:
		device.uniqueId == 'pci_0000_65_00_0'
		device.kind == GpuDeviceKind.MDEV
		device.vendor == 'NVIDIA'
		device.name == 'NVIDIA A100'
		device.uuid == 'GPU-1234'
		device.pciBusId == '0000:65:00.0'
		device.parentUniqueId == 'pci_0000_65_00_0'
		device.vmId == 'vm-42'
		device.vgpuType == 'A100-4C'
		device.vgpuTypeId == 9_876_543_210L
		device.vgpuTypeId instanceof Long
		device.vendorId == 0x10de
		device.vendorId instanceof Integer
		device.productId == 0x20b0
		device.productId instanceof Integer
	}

	void "optional fields default to null"() {
		when:
		def device = new GpuDeviceInfo(uniqueId: 'pci-1', kind: GpuDeviceKind.PCI, name: '', uuid: '')

		then:
		device.vendor == null
		device.pciBusId == null
		device.parentUniqueId == null
		device.vmId == null
		device.vgpuType == null
		device.vgpuTypeId == null
		device.vendorId == null
		device.productId == null
		device.name == ''
		device.uuid == ''
	}

	void "opaque identifiers are retained unchanged"() {
		given:
		def opaqueUniqueId = '  pci/vendor:value%2Fdevice  '
		def opaquePciBusId = 'domain:bus:slot.function'
		def opaqueParentUniqueId = 'parent::{opaque}'
		def opaqueVmId = ' VM/Identifier:007 '

		when:
		def device = new GpuDeviceInfo(
			uniqueId: opaqueUniqueId,
			pciBusId: opaquePciBusId,
			parentUniqueId: opaqueParentUniqueId,
			vmId: opaqueVmId
		)

		then:
		device.uniqueId == opaqueUniqueId
		device.pciBusId == opaquePciBusId
		device.parentUniqueId == opaqueParentUniqueId
		device.vmId == opaqueVmId
	}

	void "defines exactly the serialized fields without index"() {
		expect:
		GpuDeviceInfo.declaredFields.findAll { !it.synthetic }*.name == [
			'uniqueId',
			'kind',
			'vendor',
			'name',
			'uuid',
			'pciBusId',
			'parentUniqueId',
			'vmId',
			'vgpuType',
			'vgpuTypeId',
			'vendorId',
			'productId'
		]
	}

	void "defines only supported GPU device kinds"() {
		expect:
		GpuDeviceKind.values()*.name() == ['PCI', 'MDEV']
	}
}
