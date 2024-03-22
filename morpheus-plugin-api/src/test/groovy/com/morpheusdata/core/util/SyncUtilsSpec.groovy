package com.morpheusdata.core.util

import com.morpheusdata.model.Account
import com.morpheusdata.model.ProvisionType
import com.morpheusdata.model.ResourcePermission
import com.morpheusdata.model.ServicePlan
import spock.lang.Shared
import spock.lang.Specification

class SyncUtilsSpec extends Specification {

	static Long ONE_MEGABYTE = 1024l * 1024l
	static Long ONE_GIGABYTE = 1024l * 1024l * 1024l

	@Shared plan512 = new ServicePlan(
		id: 1,
		account: new Account(id: 1),
		name:'512MB Memory',
		maxCores:1,
		maxStorage: 10l * ONE_GIGABYTE,
		maxMemory: 512l * ONE_MEGABYTE,
		maxCpu:1,
		provisionType: new ProvisionType(code: 'generic'),
		visibility: 'private'
	)
	@Shared plan1024 = new ServicePlan(
		id: 2,
		account: new Account(id: 1),
		name:'1GB Memory',
		maxCores:1,
		maxStorage: 10l * ONE_GIGABYTE,
		maxMemory: 1l * ONE_GIGABYTE,
		maxCpu:1,
		provisionType: new ProvisionType(code: 'generic'),
		visibility: 'public'
	)
	@Shared plan2048 = new ServicePlan(
		id: 3,
		account: new Account(id: 2),
		name:'2GB Memory',
		maxCores:1,
		maxStorage:10l * ONE_GIGABYTE,
		maxMemory: 2l * ONE_GIGABYTE,
		maxCpu:1,
		provisionType: new ProvisionType(code: 'generic'),
		visibility: 'private'
	)
	@Shared plan3072 = new ServicePlan(
		id: 6,
		account: new Account(id: 1),
		name:'3GB Memory',
		maxCores:1,
		maxStorage:10l * ONE_GIGABYTE,
		maxMemory: 3l * ONE_GIGABYTE,
		maxCpu:1,
		provisionType: new ProvisionType(code: 'generic')
	)
	@Shared plan4096 = new ServicePlan(
		id:  4,
		account: new Account(id: 1),
		name:'4GB Memory',
		maxCores:1,
		maxStorage:10l * ONE_GIGABYTE,
		maxMemory: 4l * ONE_GIGABYTE,
		maxCpu:1,
		provisionType: new ProvisionType(code: 'generic')
	)

	@Shared planCustom = new ServicePlan(
		id: 5,
		account: new Account(id: 1),
		name:'Custom',
		maxCores:1,
		maxStorage:0l,
		maxMemory:0l,
		maxCpu:1,
		provisionType: new ProvisionType(code: 'generic')
	)

	@Shared Collection<ServicePlan> allServicePlans = [plan512, plan1024, plan2048]
	@Shared Account account = new Account(id: 1)

	def setupSpec() {
		// setup your test data here
	}

	def "FindServicePlanBySizing with all parameters"() {
		given: "maxMemory, maxCores, coresPerSocket, fallbackPlan, existingPlan, account, and no resourcePermissions"
		Long maxMemory = 1L * ONE_GIGABYTE
		Long maxCores = 1L
		Long coresPerSocket = 1L
		ServicePlan fallbackPlan = planCustom
		ServicePlan existingPlan = null
		Collection<ResourcePermission> resourcePermissions = []

		when:
		ServicePlan planMatch = SyncUtils.findServicePlanBySizing(allServicePlans, maxMemory, maxCores, coresPerSocket, fallbackPlan, existingPlan, account, resourcePermissions)

		then:
		planMatch == plan1024
	}

	def "FindServicePlanBySizing with only memory and cores"() {
		given: "maxMemory and maxCores"
		Long maxMemory = 512L * ONE_MEGABYTE
		Long maxCores = 1L

		when:
		ServicePlan planMatch = SyncUtils.findServicePlanBySizing(allServicePlans, maxMemory, maxCores)

		then:
		planMatch == plan512
	}

	def "FindServicePlanBySizing with only memory, cores, and cores per socket"() {
		given: "maxMemory, maxCores, and coresPerSocket"
		Long maxMemory = 512L * ONE_MEGABYTE
		Long maxCores = 1L
		Long coresPerSocket = 1L

		when:
		ServicePlan planMatch = SyncUtils.findServicePlanBySizing(allServicePlans, maxMemory, maxCores, coresPerSocket)

		then:
		planMatch == plan512
	}

	def "FindServicePlanBySizing with no match and a fallback plan"() {
		given: "maxMemory, maxCores, coresPerSocket, and a fallbackPlan"
		Long maxMemory = 4L * ONE_GIGABYTE
		Long maxCores = 1L
		Long coresPerSocket = 1L
		ServicePlan fallbackPlan = planCustom

		when:
		ServicePlan planMatch = SyncUtils.findServicePlanBySizing(allServicePlans, maxMemory, maxCores, coresPerSocket, fallbackPlan)

		then: "it should return the fallback plan"
		planMatch == planCustom
	}

	def "FindServicePlanBySizing with no match and no fallback plan"() {
		given: "maxMemory, maxCores, coresPerSocket, and no fallbackPlan"
		Long maxMemory = 4L * ONE_GIGABYTE
		Long maxCores = 1L
		Long coresPerSocket = 1L
		ServicePlan fallbackPlan = null

		when:
		ServicePlan planMatch = SyncUtils.findServicePlanBySizing(allServicePlans, maxMemory, maxCores, coresPerSocket, fallbackPlan)

		then: "it should not match a plan"
		planMatch == null
	}

	def "FindServicePlanBySizing with no match and an existing plan that matches the sizing"() {
		given: "maxMemory, maxCores, coresPerSocket, no fallbackPlan, and an existingPlan"
		Long maxMemory = 4L * ONE_GIGABYTE
		Long maxCores = 1L
		Long coresPerSocket = 1L
		ServicePlan fallbackPlan = null
		ServicePlan existingPlan = plan4096

		when:
		ServicePlan planMatch = SyncUtils.findServicePlanBySizing(allServicePlans, maxMemory, maxCores, coresPerSocket, fallbackPlan, existingPlan)

		then: "it should return the existing plan"
		planMatch == existingPlan
	}

	def "FindServicePlanBySizing with no match and an existing plan that doesn't match the sizing"() {
		given: "maxMemory, maxCores, coresPerSocket, no fallbackPlan, and an existingPlan"
		Long maxMemory = 4L * ONE_GIGABYTE
		Long maxCores = 1L
		Long coresPerSocket = 1L
		ServicePlan fallbackPlan = null
		ServicePlan existingPlan = plan1024

		when:
		ServicePlan planMatch = SyncUtils.findServicePlanBySizing(allServicePlans, maxMemory, maxCores, coresPerSocket, fallbackPlan, existingPlan)

		then: "it should not match a planl"
		planMatch == null
	}

	def "FindServicePlanBySizing with no match and an existing plan that doesn't match the sizing and a fallback plan"() {
		given: "maxMemory, maxCores, coresPerSocket, a fallbackPlan, and an existingPlan"
		Long maxMemory = 4L * ONE_GIGABYTE
		Long maxCores = 1L
		Long coresPerSocket = 1L
		ServicePlan fallbackPlan = planCustom
		ServicePlan existingPlan = plan1024

		when:
		ServicePlan planMatch = SyncUtils.findServicePlanBySizing(allServicePlans, maxMemory, maxCores, coresPerSocket, fallbackPlan, existingPlan)

		then: "it should return the fallback plan"
		planMatch == fallbackPlan
	}

	def "FindServicePlanBySizing with a match on a private plan belonging to another account with no resource permissions"() {
		given: "maxMemory, maxCores, coresPerSocket, a fallbackPlan, and an existingPlan"
		Long maxMemory = 2L * ONE_GIGABYTE
		Long maxCores = 1L
		Long coresPerSocket = 1L
		ServicePlan fallbackPlan = null
		ServicePlan existingPlan = null
		Collection<ResourcePermission> resourcePermissions = []

		when:
		ServicePlan planMatch = SyncUtils.findServicePlanBySizing(allServicePlans, maxMemory, maxCores, coresPerSocket, fallbackPlan, existingPlan, account, resourcePermissions)

		then: "it should not match a plan"
		planMatch == null
	}

	def "FindServicePlanBySizing with a match on a private plan belonging to another account with resource permissions"() {
		given: "maxMemory, maxCores, coresPerSocket, a fallbackPlan, and an existingPlan"
		Long maxMemory = 2L * ONE_GIGABYTE
		Long maxCores = 1L
		Long coresPerSocket = 1L
		ServicePlan fallbackPlan = null
		ServicePlan existingPlan = null
		Collection<ResourcePermission> resourcePermissions = [
		    new ResourcePermission(
				morpheusResourceType: ResourcePermission.ResourceType.ServicePlan,
		        morpheusResourceId: plan2048.id,
		        account: account
		    )
		]

		when:
		ServicePlan planMatch = SyncUtils.findServicePlanBySizing(allServicePlans, maxMemory, maxCores, coresPerSocket, fallbackPlan, existingPlan, account, resourcePermissions)

		then: "it should match a plan"
		planMatch == plan2048
	}

	def "FindServicePlanBySizing with a match on a private plan belonging to another account with resource permissions that do not belong to the current account"() {
		given: "maxMemory, maxCores, coresPerSocket, a fallbackPlan, and an existingPlan"
		Long maxMemory = 2L * ONE_GIGABYTE
		Long maxCores = 1L
		Long coresPerSocket = 1L
		ServicePlan fallbackPlan = null
		ServicePlan existingPlan = null
		Collection<ResourcePermission> resourcePermissions = [
			new ResourcePermission(
				morpheusResourceType: ResourcePermission.ResourceType.ServicePlan,
				morpheusResourceId: plan2048.id,
				account: new Account(id: 3)
			)
		]

		when:
		ServicePlan planMatch = SyncUtils.findServicePlanBySizing(allServicePlans, maxMemory, maxCores, coresPerSocket, fallbackPlan, existingPlan, account, resourcePermissions)

		then: "it should not match a plan"
		planMatch == null
	}


}
