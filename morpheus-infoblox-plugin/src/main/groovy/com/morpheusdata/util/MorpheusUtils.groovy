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

	static buildSyncLists(existingItems, masterItems, matchExistingToMasterFunc, secondaryMatchExistingToMasterFunc=null) {
		log.debug "buildSyncLists: ${existingItems}, ${masterItems}"
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
				def match = rtn.updateList?.find { it.masterItem == masterItem }
				if(!match) {
					rtn.addList << masterItem
				}
			}
		} catch(e) {
			log.error "buildSyncLists error: ${e}", e
		}
		return rtn
	}
}
