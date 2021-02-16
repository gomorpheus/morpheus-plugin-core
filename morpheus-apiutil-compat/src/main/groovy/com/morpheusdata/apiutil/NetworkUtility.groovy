package com.morpheusdata.apiutil

import com.morpheusdata.model.Account
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.Container
import com.morpheusdata.model.Network
import com.morpheusdata.model.NetworkDomain
import com.morpheusdata.model.NetworkPool
import com.morpheusdata.model.NetworkSubnet
import groovy.util.logging.Slf4j
import org.apache.commons.net.util.SubnetUtils

import static groovyx.gpars.GParsPool.withPool

@Slf4j
class NetworkUtility {
	static defaultPingTimeout = 3000l

	static Boolean validateIpAddr(String addr) {
		return addr?.matches(/^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/)
	}

	static Boolean validateIpAddrOrCidr(String addr) {
		return validateCidr(addr) || validateIpAddr(addr)
	}

	static Boolean validateCidr(String addr) {
		return addr?.matches(/^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}\/\d{1,2}$/)
	}

	static Map getNetworkCidrConfig(String cidr) {
		def rtn = [config:[:], ranges:[]]
		try {
			if(cidr?.indexOf(':') > -1) {
				//ipv6
				//TODO
			} else {
				def subnetInfo = new SubnetUtils(cidr).getInfo()
				rtn.address = cidrToAddress(cidr)
				rtn.config.netmask = subnetInfo.getNetmask()
				rtn.config.ipCount = subnetInfo.getAddressCountLong() ?: 0
				rtn.config.ipFreeCount = rtn.config.ipCount
				rtn.ranges << [startAddress:subnetInfo.getLowAddress(), endAddress:subnetInfo.getHighAddress()]
			}
		} catch(e) {
			log.warn("error parsing network cidr: ${e}", e)
		}
		return rtn
	}

	static getIpAddressCountBetween(String startAddress, String endAddress) {
		def cnt
		try {
			if(validateIpAddr(startAddress) && validateIpAddr(endAddress)) {
				def parts = startAddress.tokenize('.')
				def startValue = 0
				parts.reverse().eachWithIndex { part, idx ->
					startValue += part.toLong() * Math.pow(256, idx)
				}
				parts = endAddress.tokenize('.')
				def endValue = 0
				parts.reverse().eachWithIndex { part, idx ->
					endValue += part.toLong() * Math.pow(256, idx)
				}
				cnt = Math.abs(startValue - endValue).toInteger()
			}
		} catch(e) {
			log.error "error in calculating address count for ${startAddress}, ${endAddress}"
		}
		cnt
	}

	//gets a cidr string for the actual address and mask
	static String networkToCidr(String address, String netmask) {
		def rtn
		try {
			def subnetInfo = new SubnetUtils(address, netmask).getInfo()
			rtn = subnetInfo.getCidrSignature()
		} catch(e) {
			log.warn("error parsing network to cidr: ${e}", e)
		}
		return rtn
	}

	static Map cidrToNetwork(String cidr) {
		def rtn = [ipAddress:null, netmask:null, ipCount:0, range:[start:null, end:null], maskBits:null]
		try {
			if(cidr?.indexOf(':') > -1) {
				//ipv6
				def slashIndex = cidr.indexOf('/')
				if(slashIndex > -1) {
					rtn.ipAddress = cidr.substring(0, slashIndex)
					rtn.maskBits = cidr.substring(slashIndex + 1)
				}
				//TODO
			} else {
				def subnetInfo = new SubnetUtils(cidr).getInfo()
				rtn.ipAddress = subnetInfo.getAddress()
				rtn.netmask = subnetInfo.getNetmask()
				rtn.ipCount = subnetInfo.getAddressCountLong() ?: 0
				rtn.range.start = subnetInfo.getLowAddress()
				rtn.range.end = subnetInfo.getHighAddress()
			}
		} catch(e) {
			log.warn("error parsing network cidr: ${e}", e)
		}
		return rtn
	}

	//gets a cidr string for the first address and mask
	static String networkToDisplayCidr(String address, String netmask) {
		def rtn
		try {
			def subnetInfo = new SubnetUtils(address, netmask).getInfo()
			def addressCidr = subnetInfo.getCidrSignature()
			def networkAddress = subnetInfo.getNetworkAddress()
			def cirdRange = addressCidr.substring(addressCidr.indexOf('/'))
			rtn = networkAddress + cirdRange
		} catch(e) {
			log.warn("error parsing network to cidr - ${address} ${netmask}: ${e}", e)
		}
		return rtn
	}

	//remove the cidr mask if there is one
	static String cidrToAddress(String address) {
		def rtn
		if(address?.indexOf('/') > -1)
			rtn = address.substring(0, address.indexOf('/'))
		else
			rtn = address
		return rtn
	}

	//return the cidr if the address is in cidr form
	static String addressToCidr(String address) {
		def rtn
		if(address && address.indexOf('/') > -1) {
			def subnetInfo = new SubnetUtils(address).getInfo()
			rtn = subnetInfo.getLowAddress() + address.substring(address.indexOf('/'))
		}
		return rtn
	}

	static String addressToSubnet(String address) {
		def rtn
		if(address && address.indexOf('/') > -1) {
			def subnetInfo = new SubnetUtils(address).getInfo()
			rtn = subnetInfo.getNetmask()
		}
		return rtn
	}

	static String extractCidr(String address, String cidr, String netmask) {
		def rtn
		if(cidr) {
			//if cidr is there - use it
			rtn = cidr
		} else if(address) {
			//if the address has a cidr component
			if(address.indexOf('/') > -1) {
				rtn = addressToCidr(address)
			} else if(netmask) {
				rtn = networkToDisplayCidr(address, netmask)
			} else {
				rtn = networkToDisplayCidr(address, '255.255.255.0')
			}
		}
		return rtn
	}

	static String extractSubnet(String address, String cidr, String netmask) {
		def rtn = '255.255.255.0'
		def newCidr = extractCidr(address, cidr, netmask)
		if(newCidr)
			rtn = addressToSubnet(newCidr)
		return rtn
	}

	static String hexToSubnetMask(String hexAddress) {
		def rtn
		if(hexAddress?.length() > 7) {
			def firstOct = hexAddress.substring(0, 2)
			rtn = "${Integer.parseInt(firstOct, 16)}"
			def secondOct = hexAddress.substring(2, 4)
			rtn = rtn + ".${Integer.parseInt(secondOct, 16)}"
			def thirdOct = hexAddress.substring(4, 6)
			rtn = rtn + ".${Integer.parseInt(thirdOct, 16)}"
			def fourthOct = hexAddress.substring(6)
			rtn = rtn + ".${Integer.parseInt(fourthOct, 16)}"
		}
		return rtn
	}

	static String networkToPrefixLength(String address, String netmask) {
		def rtn
		def cidr = networkToCidr(address, netmask)
		if(cidr?.length() > 0) {
			def lastSlash = cidr.indexOf('/')
			if(lastSlash > -1)
				rtn = cidr.substring(lastSlash + 1)
		}
		return rtn
	}

	static getNetworkSubnetMask(NetworkPool networkPool, Network network, NetworkSubnet subnet=null) {
		def rtn
		try {
			rtn = subnet?.netmask
			if(!rtn && subnet?.cidr) {
				def subnetInfo = new SubnetUtils(subnet.cidr).getInfo()
				rtn = subnetInfo.getNetmask()
			}
			if(!rtn) {
				rtn = network?.netmask
			}
			if(!rtn && network?.cidr) {
				SubnetUtils.SubnetInfo subnetInfo = new SubnetUtils(network.cidr).getInfo()
				rtn = subnetInfo.getNetmask()
			}
			if(!rtn) {
				rtn = networkPool?.netmask
			}
		} catch(e) {
			log.warn("error parsing network cidr: ${e}", e)
		}
		return rtn
	}

	static String getServiceUrlHost(String serviceUrl) {
		return new URL(serviceUrl).getHost()
	}

	static Integer getServiceUrlPort(serviceUrl) {
		def urlObj = new URL(serviceUrl)
		return urlObj.getPort() > 0 ? urlObj.getPort() : (urlObj?.getProtocol()?.toLowerCase() == 'https' ? 443 : 80)
	}

	static getServerProxy(ComputeServer server) {
		def rtn
		try {
			if(server.interfaces) {
				server.interfaces?.each {
					if(rtn == null && it.network?.networkProxy)
						rtn = it.network.networkProxy
				}
			}
			if(rtn == null) {
				if(server.zone?.provisioningProxy)
					rtn = server.zone.provisioningProxy
			}
		} catch(e) {
			log.error("getServerProxy error: ${e}", e)
		}
		return rtn
	}

	static getServerProxyExceptions(ComputeServer server) {
		def rtn
		def network
		try {
			if(server.interfaces) {
				server.interfaces?.each {
					if(network == null && it.network)
						network = it.network
				}
			}
			if(network)
				rtn = network.noProxy ?: ''
			if(server.zone.noProxy) {
				rtn = ((rtn?.tokenize(", ") ?: []) + server.zone.noProxy.tokenize(", ")).join(',')
			}
		} catch(e) {
			log.error("getServerProxyExceptions error: ${e}", e)
		}
		return rtn
	}

	static String getDomainRecordFqdn(String name, String domain) {
		def rtn
		if(name.indexOf(domain) > -1) {
			rtn = getFqdnDomainName(name)
		} else {
			if(domain.endsWith('.')) {
				def shortDomain = getFriendlyDomainName(domain)
				if(name.indexOf(shortDomain) > -1) {
					rtn = getFqdnDomainName(name)
				} else {
					//
				}
			}
			if(rtn == null) {
				rtn = getFqdnDomainName(name + '.' + domain)
			}
		}
		return rtn
	}

	static String getDomainRecordName(String name, String domain) {
		def rtn
		if(domain && name) {
			domain = getFqdnDomainName(domain)
			name = getFqdnDomainName(name)
			if(name.length() > domain.length()) {
				log.debug("name: ${name} domain: ${domain}")
				rtn = name.substring(0, (name.length() - domain.length() - 1))
			} else {
				rtn = name
			}
		}
		return rtn
	}

	static String getFriendlyDomainName(String name) {
		def rtn = name
		if(name) {
			name = name.toLowerCase()
			if(name?.endsWith('.'))
				name = name.substring(0, name.length() - 1)
			rtn = name
		}
		return rtn

	}

	static String getFqdnDomainName(String name) {
		def rtn = name
		if(name) {
			name = name.toLowerCase()
			if(!name.endsWith('.'))
				name = name + '.'
			rtn = name
		}
		return rtn
	}

	static String cleanHostname(String name) {
		name = name.replaceAll(' ', '-')
		return name
	}

	static String getNextIpAddress(String ipAddress, Integer increment = 1) {
		def rtn
		def tokens = ipAddress.tokenize('.')
		if(tokens.size() == 4) {
			def first = tokens[0].toInteger()
			def second = tokens[1].toInteger()
			def third = tokens[2].toInteger()
			def fourth = tokens[3].toInteger()
			def newFourth = fourth + increment
			if(newFourth < 256) {
				rtn = "${first}.${second}.${third}.${newFourth}".toString()
			} else {
				def incThird = (int) (newFourth / 255)
				newFourth = newFourth % 256
				def newThird = third + incThird
				//println("newThird: ${newThird} newFourth: ${newFourth}")
				if(newThird < 256) {
					//just one inc
					rtn = "${first}.${second}.${newThird}.${newFourth}".toString()
				} else {
					//keep going...
				}
			}
		}
		return rtn
	}

	static List<Map<Integer, Boolean>> scanHost(String ipAddress) {
		def portTimeout = 200
		def scanListMutex = new Object()
		def scanList = []
		withPool(100) {
			(1..65500).eachParallel { port ->
				def portResults = scanPort(ipAddress, port, portTimeout)
				synchronized(scanListMutex) {
					scanList << portResults
				}
			}
		}
		return scanList
	}

	static Map<Integer, Boolean> scanPort(String ipAddress, Integer port, Integer timeout) {
		def rtn = [port:port, open:false]
		try {
			def socket = new Socket()
			socket.connect(new InetSocketAddress(ipAddress, port), timeout)
			socket.close()
			rtn.open = true
		} catch(e) {
			//nothing
		}
		return rtn
	}

	static getPortComm(List<Map<Integer, Boolean>> activePorts) {
		def rtn = [enabled:false]
		def commMatch
		activePorts?.each { activePort ->
			def tmpMatch = commMatchList.find{ it.port == activePort.port }
			if(tmpMatch)
				commMatch = tmpMatch
		}
		if(commMatch) {
			rtn.enabled = true
			rtn.commEnabled = true
			rtn.commType = commMatch.name
			rtn.commPort = commMatch.port
		}
		return rtn
	}

	static getPortServices(List<Map<Integer, Boolean>> activePorts) {
		def rtn = [enabled:false, serviceList:[]]
		def serviceList = []
		activePorts?.each { activePort ->
			def serviceMatch = serviceMatchList.find{ it.port == activePort.port }
			if(serviceMatch)
				serviceList << serviceMatch
		}
		if(serviceList?.size() > 0)  {
			rtn.enabled = true
			rtn.serviceList = serviceList
			rtn.serviceType = rtn.serviceList.collect{ it.name }.join(', ')
		}
		return rtn
	}

	static Integer getMinPort(String portRange) {
		if(portRange.isNumber()) {
			return portRange.toInteger()
		}
		// Must be a range
		def ports = portRange.split('-')
		return ports[0].toInteger()
	}

	static Integer getMaxPort(String portRange) {
		if(portRange.toString().isNumber()) {
			return portRange.toInteger()
		}
		// Must be a range
		def ports = portRange.split('-')
		return ports[1].toInteger()
	}

	static List getNetworksFromRange(String ipStart, String ipEnd) {
		long start = ipToLong(ipStart)
		long end = ipToLong(ipEnd)

		def result = []
		while (end >= start) {
			byte maxSize = 32
			while (maxSize > 0) {
				long mask = 0
				try {
					mask = iMask(maxSize - 1)
					long maskBase = start & mask

					if (maskBase != start) {
						break
					}
					maxSize--
				}
				catch (Throwable t) {
					log.error(t.message, t)
					maxSize--
				}
			}
			Double x = Math.log( end - start + 1) / Math.log( 2 )
			Byte maxDiff = (Byte)( 32 - Math.floor( x ) )
			if ( maxSize < maxDiff) {
				maxSize = maxDiff
			}
			String ip = longToIp(start);
			result << "${ip}/${maxSize}".toString()
			start += Math.pow( 2, (32 - maxSize) )
		}
		return result
	}

	static long ipToLong(String ipstr) {
		def ipAddressInArray = ipstr.split("\\.")
		long num = 0l
		long ip = 0l
		for (int x = 3; x >= 0; x--) {
			ip = Long.parseLong(ipAddressInArray[3 - x])
			num |= ip << (x << 3)
		}
		return num
	}

	static String longToIp(Long longIP) {
		return "${String.valueOf(longIP >>> 24)}.${String.valueOf((longIP & 0x00FFFFFF) >>> 16)}.${String.valueOf((longIP & 0x0000FFFF) >>> 8)}.${String.valueOf(longIP & 0x000000FF)}".toString()
	}

	static Long iMask(Integer s) {
		return Math.round(Math.pow(2, 32) - Math.pow(2, (32 - s)))
	}

	static Boolean checkIpv4Ip(String ipAddress) {
		def rtn = false
		if(ipAddress) {
			if(ipAddress.indexOf('.') > 0 && !ipAddress.startsWith('169'))
				rtn = true
		}
		return rtn
	}

	static String getIpAddressType(String ipAddress) {
		def rtn
		if(ipAddress) {
			if(ipAddress.indexOf('.') > 0)
				rtn = 'ipv4'
			else if(ipAddress.indexOf(':') > 0)
				rtn = 'ipv6'
		}
		return rtn
	}

	static String getIpAddressIndex(String name) {
		def rtn
		if(name) {
			if(name.startsWith('eth'))
				rtn = name.substring(3)
			else if(name.startsWith('en'))
				rtn = name.substring(name.length() - 1)
			else
				rtn = name.substring(name.length() - 1)
		}
		return rtn
	}

	static commMatchList = [
			[port:22, name:'ssh', platform:'linux'],
			[port:5985, name:'winrm', platform:'windows'],
			[port:5986, name:'winrm', platform:'windows']
	]

	static serviceMatchList = [
			[port:80, name:'Http', type:'app'],
			[port:8000, name:'Web', type:'app'],
			[port:443, name:'Https', type:'app'],
			[port:8443, name:'Https', type:'app'],
			[port:8080, name:'Web', type:'app'],
			[port:3306, name:'MySql', type:'app'],
			[port:143, name:'IMAP', type:'service'],
			[port:25, name:'SMTP', type:'service'],
			[port:53, name:'DNS', type:'service'],
			[port:88, name:'Kerberose', type:'service'],
			[port:161, name:'SNMP', type:'service'],
			[port:162, name:'SNMP', type:'service'],
			[port:389, name:'LDAP', type:'service'],
			[port:1433, name:'SQL Server', type:'app'],
			[port:1434, name:'SQL Server', type:'app'],
			[port:1521, name:'Oracle Db', type:'app'],
			[port:1522, name:'Oracle Db', type:'app'],
			[port:1525, name:'Oracle Db', type:'app'],
			[port:1529, name:'Oracle Db', type:'app'],
			[port:5010, name:'Yahoo Messenger', type:'app'],
			[port:5190, name:'AOL Messenger', type:'app'],
			[port:6379, name:'Redis', type:'app'],
			[port:27017, name:'Mongo', type:'app'],
			[port:5432, name:'Postgres', type:'app'],
			[port:5672, name:'AMQP', type:'service']
	]

}
