package com.morpheusdata.vmware.plugin

import com.vmware.vim25.*
import com.vmware.vim25.mo.*

class VmwareServiceCache {

	public ServiceInstance serviceInstance
	public Date dateCreated
	public Date leasedAt = null
	public Boolean noReap = false

	VmwareServiceCache(ServiceInstance serviceInstance, Date dateCreated, Boolean noReap = false) {
		this.serviceInstance = serviceInstance
		this.dateCreated = dateCreated
		this.noReap = noReap
	}
	
}
