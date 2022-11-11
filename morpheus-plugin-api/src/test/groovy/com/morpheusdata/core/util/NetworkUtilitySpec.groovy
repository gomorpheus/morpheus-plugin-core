package com.morpheusdata.core.util

import com.morpheusdata.model.Network
import com.morpheusdata.model.NetworkPool
import com.morpheusdata.model.NetworkSubnet
import spock.lang.Specification

class NetworkUtilitySpec extends Specification {
	void "validateIpAddr"() {
		expect:
		isValid == NetworkUtility.validateIpAddr(ip)

		where:
		ip                                        | isValid
		'0.0.0.0'                                 | true
		'O.0.0.0'                                 | false
		'10.0.0.1'                                | true
		'::'                                      | true
		'...'                                     | false
		'999.999.999.999'                         | true
		'255.255.255.255'                         | true
		'2555.255.255.255'                        | false
		'255.2555.255.255'                        | false
		'255.255.2555.255'                        | false
		'255.255.255.2555'                        | false
		'2001:0db8:85a3:0000:0000:8a2e:0370:7334' | true
	}

	void "getIpAddressType"() {
		expect:
		type == NetworkUtility.getIpAddressType(ip)

		where:
		ip                                        | type
		'1.1.1.1'                                 | 'ipv4'
		'2001:0db8:85a3:0000:0000:8a2e:0370:7334' | 'ipv6'
		'2001:0db8:85a3:0000:0000:8a2e::'         | 'ipv6'
	}

	void "checkIpv4Ip"() {
		expect:
		isV4 == NetworkUtility.checkIpv4Ip(ip)

		where:
		ip                                        | isV4
		'192.168.0.1'                             | true
		'192.169.0.1'                             | true
		'169.168.0.1'                             | false
		'2001:0db8:85a3:0000:0000:8a2e:0370:7334' | false
		'169:0db8:85a3:0000:0000:8a2e:0370:7334'  | false
	}

	void "getFriendlyDomainName"() {
		expect:
		friendlyDomainName == NetworkUtility.getFriendlyDomainName(name)

		where:
		name              | friendlyDomainName
		null              | null
		'WWW.GOOGLE.COM'  | 'www.google.com'
		'WWW.GOOGLE.COM.' | 'www.google.com'

	}

	void "getFqdnDomainName"() {
		expect:
		fqdn == NetworkUtility.getFqdnDomainName(name)

		where:
		name               | fqdn
		null               | null
		'en.wikipedia.org' | 'en.wikipedia.org.'
		'EN.WIKIPEDIA.ORG' | 'en.wikipedia.org.'
	}

	void "getNetworkSubnetMask"() {
		expect:
		subnetMask == NetworkUtility.getNetworkSubnetMask(pool, network, subnet)

		where:
		pool                                      | network                              | subnet                                      || subnetMask
		new NetworkPool()                         | new Network()                        | null                                        || null
		new NetworkPool(netmask: '255.255.240.0') | new Network(cidr: '20.10.50.100/20') | new NetworkSubnet(cidr: '20.10.50.100/28')  || '255.255.255.240'
		new NetworkPool(netmask: '255.255.240.0') | new Network(cidr: '20.10.50.100/20') | new NetworkSubnet(netmask: '255.255.255.0') || '255.255.255.0'
		new NetworkPool(netmask: '255.255.240.0') | new Network(cidr: '20.10.50.100/16') | null                                        || '255.255.0.0'
		new NetworkPool(netmask: '255.255.240.0') | new Network(cidr: null)              | null                                        || '255.255.240.0'
	}

	void "ipToLong"() {
		expect:
		longVal == NetworkUtility.ipToLong(ip)

		where:
		ip                                        | longVal
		'0.0.0.0'                                 | 0
		'10.0.0.1'                                | 167772161
		'255.255.255.255'                         | 4294967295
	}

	void "longToIp"() {
		expect:
		ip == NetworkUtility.longToIp(longVal)

		where:
		ip                                        | longVal
		'0.0.0.0'                                 | 0
		'10.0.0.1'                                | 167772161
		'255.255.255.255'                         | 4294967295
	}

	void "getNextIpAddress"() {
		expect:
		res == NetworkUtility.getNextIpAddress(ip, cnt)

		where:
		ip                                        | cnt           | res
		'0.0.0.0'                                 | 0             | '0.0.0.0'
		'10.0.0.1'                                | 1             | '10.0.0.2'
		'10.0.0.1'                                | 300           | '10.0.1.45'
	}


	void "getNextIpv6Address"() {
		expect:
		res == NetworkUtility.getNextIpv6Address(ip, cnt,end)

		where:
		ip               | end                                    | cnt           | res
		'ff80:0:0:0:0:0:0:0001' | 'ff80:ffff:ffff:ffff:ffff:ffff:ffff'   | 0             | 'ff80:0000:0000:0000:0000:0000:0000:0001'
		'ff80:0:0:0:0:0:0:0001' | 'ff80:ffff:ffff:ffff:ffff:ffff:ffff'   | 1             | 'ff80:0000:0000:0000:0000:0000:0000:0002'
		'ff80:0:0:0:0:0:0:0001' | 'ff80:ffff:ffff:ffff:ffff:ffff:ffff'   | 300           | 'ff80:0000:0000:0000:0000:0000:0000:012d'
		'ff80:0:0:0:0:0:0:0001' | 'ff80:ffff:ffff:ffff:ffff:ffff:ffff'   | 65535           | 'ff80:0000:0000:0000:0000:0000:0001:0001'
		'ff80:0:0:0:0:0:0:0001' | 'ff80:0000:0000:0000:0000:0000:0000:ffff'   | 65535           | null
	}
}

