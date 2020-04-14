package com.morpheusdata.vmware.util

import groovy.util.logging.Slf4j

import java.security.MessageDigest
import java.util.concurrent.ConcurrentHashMap
import com.vmware.vim25.*
import com.vmware.vim25.mo.*

/**
 * Connection pool (poor mans style)
 * TODO: FIX TO BE CONCURRENT
 * @author David Estes
 */
@Slf4j
class VmwareConnectionPool extends Thread {

	public static VMWARE_MAX_SESSIONS = 30
	public static RELEASE_TIMEOUT = 20000l
	public static OVERLEASE_TIMEOUT = 20l * 60000l
	public static EXPIRE_TIMEOUT = 30000l
	public static Object MUTEX_LOCK_OBJ = new Object()
	protected ConcurrentHashMap<String,List<VmwareServiceCache>> serviceInstanceCache = new ConcurrentHashMap<>()

	VmwareConnectionPool() {
		super("vmware-connection-pool-reaper")
		this.start()
	}

	ServiceInstance getConnection(String apiUrl, String username, String password, Boolean freshConnection=false, noReap=false) {
		def cacheKey = getPoolKey(apiUrl,username,password)
		def now = new Date()
		List connections = getConnectionList(cacheKey)
		VmwareServiceCache cacheEntry
		while(!cacheEntry) {
			def connectionCount
			synchronized(MUTEX_LOCK_OBJ) {
				connectionCount = connections.size()
				// println("Connection count: ${connectionCount}")
				cacheEntry = connections?.find{!it.leasedAt}
				if(cacheEntry) {
					cacheEntry.leasedAt = new Date()
					if(now.time >= cacheEntry.dateCreated.time + RELEASE_TIMEOUT || freshConnection) {
						com.morpheus.util.SSLUtility.trustAllHostnames()
						com.morpheus.util.SSLUtility.trustAllHttpsCertificates()
						cacheEntry.dateCreated = new Date()
						try {
							log.debug("Releasing Vmware Session...")
							if(cacheEntry.serviceInstance.sessionManager.currentSession) {
								cacheEntry.serviceInstance.sessionManager.logout()
							}
							//cacheEntry.serviceInstance.serverConnection.logout()
						} catch(ex2) {
							log.debug("Error attempting session logout: ${ex2.message}", ex2)
							//we tried to kill the connection
						}
						cacheEntry.serviceInstance = null
					}
					// println "Reusing Connection!"
				}
			}
			if(!cacheEntry) {
				if(connectionCount >= VMWARE_MAX_SESSIONS) {
					sleep(100)
				} else {
					com.morpheus.util.SSLUtility.trustAllHostnames()
					com.morpheus.util.SSLUtility.trustAllHttpsCertificates()
					cacheEntry = new VmwareServiceCache(new ServiceInstance(new URL(apiUrl),username,password), now, noReap)
					cacheEntry.leasedAt = new Date()
					synchronized(MUTEX_LOCK_OBJ) {
						connections << cacheEntry
					}
				}
			}
		}
		if(!cacheEntry.serviceInstance) {
			cacheEntry.serviceInstance = new ServiceInstance(new URL(apiUrl), username, password)
			cacheEntry.noReap = noReap
		}
		return cacheEntry.serviceInstance
	}

	void releaseConnection(String apiUrl, String username, String password, ServiceInstance instance) {
		def cacheKey = getPoolKey(apiUrl,username,password)
		List connections = getConnectionList(cacheKey)
		def now = new Date()
		synchronized(MUTEX_LOCK_OBJ) {
			VmwareServiceCache cacheEntry = connections?.find{ it.serviceInstance == instance }

			if(cacheEntry) {
				// println "Releaseing Vmware Connection ${instance} from connections: ${connections}"
				if(now.time >= cacheEntry?.dateCreated?.time + RELEASE_TIMEOUT || cacheEntry.noReap == true) {
					try {
						if(cacheEntry.serviceInstance.sessionManager.currentSession) {
							cacheEntry.serviceInstance.sessionManager.logout()
						}
//						cacheEntry.serviceInstance.serverConnection.logout()
					} catch(ex) {
						//we tried to kill the connection
					}
					connections.remove(cacheEntry)
				} else {
					cacheEntry.leasedAt = null
				}
			}
		}
	}

	@Override
	void run() {
		while(true) {
			try {

				def keys = serviceInstanceCache.keySet()
				def now = new Date()
				// println "Checking Key List:${keys}"
				keys?.each { key ->
					def connections = serviceInstanceCache.get(key)
					synchronized(MUTEX_LOCK_OBJ) {
						if(connections.size() > 0) {
							// println "Checking for Expired Connections "
							def expiredConnections = connections.findAll {
								it.noReap != true && it.leasedAt == null && now.time >= it.dateCreated.time + EXPIRE_TIMEOUT
							}
							expiredConnections?.each { cacheEntry ->
								// println "Removing Connection!"
								try {
									log.debug("Releasing Idle Connection")
									if(cacheEntry.serviceInstance.sessionManager.currentSession) {
										cacheEntry.serviceInstance.sessionManager.logout()
									}
//										cacheEntry.serviceInstance.serverConnection.logout()
								} catch(ex) {
									log.error("Error attempting session logout: ${ex.message}")

									//we tried to kill the connection
								}
								connections.remove(cacheEntry)
							}
							def overLeasedConnections = connections.findAll {
								it.noReap != true && it.leasedAt && now.time >= it.leasedAt?.time + OVERLEASE_TIMEOUT
							}
							overLeasedConnections?.each { cacheEntry ->
								cacheEntry.leasedAt = null
								// println "Releasing Connection!"
							}
						}
					}
				}
			} catch(ex) {
				println("Error: ${ex}")
				ex.printStackTrace()
			}
			try {
				sleep(60000)
			} catch(ex2) {
				// interrupt catch and ignore
			}
		}
	}

	protected getConnectionList(String key) {
		List connectionList = serviceInstanceCache.get(key)
		if(!connectionList) {
			connectionList = []
			serviceInstanceCache.put(key, connectionList)
		}
		return connectionList
	}

	protected String getPoolKey(String apiUrl, String username, String password) {
		def cacheString = "${apiUrl}:${username}:${password}".toString()
		MessageDigest md = MessageDigest.getInstance("MD5")
		md.update(cacheString.bytes)
		byte[] checksum = md.digest()
		return checksum.encodeHex().toString()
	}


	public closeAllConnections() {
		serviceInstanceCache?.each { key,value ->
			value?.each { cacheEntry ->
				try {
					if(cacheEntry.serviceInstance) {
						cacheEntry.serviceInstance.sessionManager.logout()
						cacheEntry.serviceInstance = null
					}
				} catch(ex) {
					// println "Error: ${ex}"
				}
			}
		}
	}

}
