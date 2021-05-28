package com.morpheusdata.core.util

import com.morpheusdata.model.PlatformType
import spock.lang.Specification

class ComputeUtilitySpec extends Specification {
	void "formatHostname"() {
		expect:
		expected == ComputeUtility.formatHostname(host, platform)

		where:
		host             | platform             || expected
		'MAAS'           | PlatformType.linux   || 'maas'
		'api.maas'       | PlatformType.linux   || 'api-maas'
		'api.maas,foo'   | PlatformType.linux   || 'api-maas-foo'
		"api.maas'foo"   | PlatformType.linux   || 'api-maas-foo'
		"api.maas'foo"   | PlatformType.linux   || 'api-maas-foo'
		"api.maas'foo--" | PlatformType.windows || 'api-maas-foo'
	}

	void "formatProvisionHostname"() {
		expect:
		expected == ComputeUtility.formatProvisionHostname(host)

		where:
		host                       | expected
		'MAAS'                     | 'maas'
		'api.maas.com'             | 'api.maas.com'
		'api.maas.com '            | 'api.maas.com-'
		'https://api.maas.com'     | 'https://api.maas.com'
		'https://api.maas.com,foo' | 'https://api.maas.comfoo'
		"https://api.maas.com'foo" | 'https://api.maas.comfoo'
		'$api.maasfoo--'           | '$api.maasfoo--'
	}

	void "parseGigabytesToBytes"() {
		expect:
		bytes == ComputeUtility.parseGigabytesToBytes(gb)

		where:
		gb   | bytes
		null | 0
		0    | 0
		1    | 1073741824
		2    | 2147483648
		1000 | 1073741824000
	}

	void "bytesToHuman"() {
		expect:
		human == ComputeUtility.bytesToHuman(bytes)

		where:
		bytes                            | human
		null                             | '0MB'
		0f                               | '0MB'
		10f                              | '10B'
		12.5f                            | '12.5B'
		ComputeUtility.ONE_BYTE          | '1B'
		ComputeUtility.ONE_KILOBYTE_REAL | '1000B'
		ComputeUtility.ONE_KILOBYTE      | '1KB'
		ComputeUtility.ONE_MEGABYTE      | '1MB'
		ComputeUtility.ONE_MEGABYTE_REAL | '976.5KB'
		ComputeUtility.ONE_GIGABYTE      | '1GB'
		ComputeUtility.ONE_GIGABYTE_REAL | '953.6MB'
		ComputeUtility.ONE_TERABYTE      | '1TB'
		2 * 1024 * 1024                  | '2MB'
		1073741824f                      | '1GB'
	}

	void "bytesAsGB - int"() {
		when:
		def gb = ComputeUtility.bytesAsGB(ComputeUtility.ONE_TERABYTE)

		then:
		gb instanceof Long
		gb == 1024
	}

	void "bytesAsGB - double"() {
		when:
		def gb = ComputeUtility.bytesAsGB(ComputeUtility.ONE_GIGABYTE_REAL)

		then:
		gb instanceof Double
		gb == 0.9313225746154785
	}
}
