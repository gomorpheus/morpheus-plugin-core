package com.morpheusdata.apiutil

import com.morpheusdata.model.PlatformType
import spock.lang.Specification

class ComputeUtilitySpec extends Specification {
	void "formatHostname"() {
		expect:
		expected == ComputeUtility.formatHostname(host, platform)

		where:
		host              | platform             || expected
		'LUMEN'           | PlatformType.linux   || 'lumen'
		'api.lumen'       | PlatformType.linux   || 'api-lumen'
		'api.lumen,foo'   | PlatformType.linux   || 'api-lumen-foo'
		"api.lumen'foo"   | PlatformType.linux   || 'api-lumen-foo'
		"api.lumen'foo"   | PlatformType.linux   || 'api-lumen-foo'
		"api.lumen'foo--" | PlatformType.windows || 'api-lumen-foo'
	}

	void "formatProvisionHostname"() {
		expect:
		expected == ComputeUtility.formatProvisionHostname(host)

		where:
		host                        | expected
		'LUMEN'                     | 'lumen'
		'api.lumen.com'             | 'api.lumen.com'
		'api.lumen.com '            | 'api.lumen.com-'
		'https://api.lumen.com'     | 'https://api.lumen.com'
		'https://api.lumen.com,foo' | 'https://api.lumen.comfoo'
		"https://api.lumen.com'foo" | 'https://api.lumen.comfoo'
		'$api.lumenfoo--'           | '$api.lumenfoo--'
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
		10f                              | '10.0B'
		12.5f                            | '12.5B'
		ComputeUtility.ONE_BYTE          | '1.0B'
		ComputeUtility.ONE_KILOBYTE_REAL | '1000.0B'
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
		gb instanceof Integer
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
