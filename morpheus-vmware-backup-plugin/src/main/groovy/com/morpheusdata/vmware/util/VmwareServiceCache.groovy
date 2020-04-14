package com.morpheusdata.vmware.util

import com.vmware.vim25.mo.ServiceInstance

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
