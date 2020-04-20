package com.morpheusdata.util

import com.morpheusdata.model.Network
import com.morpheusdata.model.NetworkPool
import groovy.util.logging.Slf4j
import org.apache.commons.net.util.SubnetUtils

// FIXME: should live in Core, need to convert to java
@Slf4j
class MorpheusUtils {
    static formatDate(Object date, String outputFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") {
        def rtn
        try {
            if(date) {
                if(date instanceof Date)
                    rtn = date.format(outputFormat, TimeZone.getTimeZone('GMT'))
                else if(date instanceof CharSequence)
                    rtn = date
            }
        } catch(ignored) { }
        return rtn
    }

	static String getNetworkSubnetMask(NetworkPool networkPool, Network network) {
		String rtn
		try {
			rtn = network?.netmask
			if(!rtn && network?.cidr) {
				def subnetInfo = new SubnetUtils(network.cidr).getInfo()
				rtn = subnetInfo.getNetmask()
			}
			if(!rtn) {
				rtn = networkPool?.netmask
			}
		} catch(ignore) {}
		return rtn
	}

	static Map getNetworkPoolConfig(cidr) {
		def rtn = [config:[:], ranges:[]]
		try {
			def subnetInfo = new SubnetUtils(cidr).getInfo()
			rtn.config.netmask = subnetInfo.getNetmask()
			rtn.config.ipCount = subnetInfo.getAddressCountLong() ?: 0
			rtn.config.ipFreeCount = rtn.config.ipCount
			rtn.ranges << [startAddress:subnetInfo.getLowAddress(), endAddress:subnetInfo.getHighAddress()]
		} catch(e) {
			log.warn("error parsing network pool cidr: ${e}", e)
		}
		return rtn
	}

	static buildSyncLists(existingItems, masterItems, matchExistingToMasterFunc, secondaryMatchExistingToMasterFunc=null) {
		log.info "buildSyncLists: ${existingItems}, ${masterItems}"
		def rtn = [addList:[], updateList: [], removeList: []]
		try {
			existingItems?.each { existing ->
				def matches = masterItems?.findAll { matchExistingToMasterFunc(existing, it) }
				if(!matches && secondaryMatchExistingToMasterFunc != null) {
					matches = masterItems?.findAll { secondaryMatchExistingToMasterFunc(existing, it) }
				}
				if(matches?.size() > 0) {
					matches?.each { match ->
						rtn.updateList << [existingItem:existing, masterItem:match]
					}
				} else {
					rtn.removeList << existing
				}
			}
			masterItems?.each { masterItem ->
				def match = rtn?.updateList?.find {
					it.masterItem == masterItem
				}
				if(!match) {
					rtn.addList << masterItem
				}
			}
		} catch(e) {
			log.error "buildSyncLists error: ${e}", e
		}
		return rtn
	}

	static Map<String, String> extractCookie(String rawCookie) {
		if(!rawCookie) return null
		def name = rawCookie.split('=')?.getAt(0)
		String data = rawCookie?.split("$name=")?.getAt(1)?.split(";")?.getAt(0)
		def value = data?.substring(1, data?.length() - 1)
		[(name.toString()): value]
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
}
