package com.morpheusdata.core.util;

import com.morpheusdata.model.Account;
import com.morpheusdata.model.ResourcePermission;
import com.morpheusdata.model.ServicePlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class SyncUtils {

	static Logger log = LoggerFactory.getLogger(SyncUtils.class);

	static ServicePlan findServicePlanBySizing(Collection<ServicePlan> allPlans, Long maxMemory, Long maxCores) {
		return findServicePlanBySizing(allPlans, maxMemory, maxCores, null, null, null, null, new ArrayList<ResourcePermission>());
	}

	static ServicePlan findServicePlanBySizing(Collection<ServicePlan> allPlans, Long maxMemory, Long maxCores, Long coresPerSocket) {
		return findServicePlanBySizing(allPlans, maxMemory, maxCores, coresPerSocket, null, null, null, new ArrayList<ResourcePermission>());
	}

	static ServicePlan findServicePlanBySizing(Collection<ServicePlan> allPlans, Long maxMemory, Long maxCores, Long coresPerSocket, ServicePlan fallbackPlan) {
		return findServicePlanBySizing(allPlans, maxMemory, maxCores, coresPerSocket, fallbackPlan, null, null, new ArrayList<ResourcePermission>());
	}

	static ServicePlan findServicePlanBySizing(Collection<ServicePlan> allPlans, Long maxMemory, Long maxCores, Long coresPerSocket, ServicePlan fallbackPlan, ServicePlan existingPlan) {
		return findServicePlanBySizing(allPlans, maxMemory, maxCores, coresPerSocket, fallbackPlan, existingPlan, null, new ArrayList<ResourcePermission>());
	}

	static ServicePlan findServicePlanBySizing(Collection<ServicePlan> allPlans, Long maxMemory, Long maxCores, Long coresPerSocket, ServicePlan fallbackPlan, ServicePlan existingPlan, Account account) {
		return findServicePlanBySizing(allPlans, maxMemory, maxCores, coresPerSocket, fallbackPlan, existingPlan, account, new ArrayList<ResourcePermission>());
	}

	/**
	 * Given a list of available ServicePlans and various parameters for a ComputeServer, determine a matching ServicePlan. If the
	 * current existingPlan is still a match (might not be the 'best' match), it will be returned. If no plan is found,
	 * then the fallbackPlan is returned
	 * @param allPlans All possible plans to pick from
	 * @param maxMemory The maxMemory of the ComputeServer being matched
	 * @param maxCores The maxCores of the ComputeServer being matched
	 * @param coresPerSocket The coresPerSocket of the ComputeServer being matched
	 * @param fallbackPlan The ServicePlan to use if no match is found
	 * @param existingPlan The current ServicePlan of the ComputeServer
	 * @param account The account for the ComputeServer
	 * @param resourcePermissions The resourcePermissions for the ServicePlan
	 * @return The matched ServicePlan
	 */
	static ServicePlan findServicePlanBySizing(Collection<ServicePlan> allPlans, Long maxMemory, Long maxCores, Long coresPerSocket, ServicePlan fallbackPlan, ServicePlan existingPlan, Account account, Collection<ResourcePermission> resourcePermissions) {
		Collection<ServicePlan> availablePlans = allPlans;
		if (existingPlan != null && existingPlan.getDeleted()) {
			existingPlan = null;
		}
		if (existingPlan != null && "container-unmanaged".equals(existingPlan.getCode())) {
			//this is a special discovery plan we use in tf to match, its temporary and should not stay
			existingPlan = null;
		}
		if(resourcePermissions == null) {
			resourcePermissions = new ArrayList<ResourcePermission>();
		}
		if (account != null) {
			Collection<ResourcePermission> finalResourcePermissions = resourcePermissions;
			availablePlans = allPlans.stream()
				.filter(pl -> "public".equals(pl.visibility) ||
					(pl.account != null && pl.account.getId().equals(account.getId())) ||
					(pl.owner != null && pl.owner.getId().equals(account.getId())) ||
					(pl.getAccount() == null && "public".equals(pl.getVisibility())) ||
					finalResourcePermissions.stream().anyMatch(rp -> rp.getMorpheusResourceId().equals(pl.getId()) && rp.getAccount().getId().equals(account.getId())))
				.collect(Collectors.toList());
			if (existingPlan != null) {
				ServicePlan finalExistingPlan = existingPlan;
				if ("public".equals(existingPlan.getVisibility()) ||
					(existingPlan.getAccount() != null && existingPlan.getAccount().getId().equals(account.getId())) ||
					(existingPlan.getOwner() != null && existingPlan.getOwner().getId().equals(account.getId())) ||
					(existingPlan.getAccount() == null && "public".equals(existingPlan.getVisibility())) ||
					resourcePermissions.stream().anyMatch(rp -> rp.getMorpheusResourceId().equals(finalExistingPlan.getId()) && rp.getAccount().getId().equals(account.getId()))) {
					// silly assignment because the inverse of the above is difficult to express
					existingPlan = existingPlan;
				} else {
					existingPlan = null; //we have to correct a plan discrepancy due to permissions on the vm
				}
			}
		}

		//first lets try to find a match by zone and an exact match at that
		if (existingPlan != null && !existingPlan.equals(fallbackPlan)) {
			if ((existingPlan.getMaxMemory().equals(maxMemory) || existingPlan.getCustomMaxMemory()) &&
				((existingPlan.getMaxCores() == 0 && maxCores == 1) || existingPlan.getMaxCores().equals(maxCores) || existingPlan.getCustomCores()) &&
				(coresPerSocket == null || existingPlan.getCoresPerSocket().equals(coresPerSocket) || existingPlan.getCustomCores())) {
				return existingPlan;
			}
		}

		Collection<ServicePlan> matchedPlans;
		if (coresPerSocket == null || coresPerSocket == 1) {
			matchedPlans = availablePlans.stream()
				.filter(it -> it.getMaxMemory().equals(maxMemory) &&
					!it.getCustomMaxMemory() &&
					!it.getCustomCores() &&
					((Objects.equals(maxCores, 1L) && (it.getMaxCores() == null || it.getMaxCores() == 0)) || it.getMaxCores().equals(maxCores)) &&
					(it.getCoresPerSocket() == null || it.getCoresPerSocket() == 1))
				.collect(Collectors.toList());
		} else {
			matchedPlans = availablePlans.stream()
				.filter(it -> it.getMaxMemory().equals(maxMemory) &&
					((Objects.equals(maxCores, 1L) && (it.getMaxCores() == null || it.getMaxCores() == 0)) || it.getMaxCores().equals(maxCores)) &&
					!it.getCustomMaxMemory() &&
					!it.getCustomCores() &&
					it.getCoresPerSocket().equals(coresPerSocket))
				.collect(Collectors.toList());
		}

		if (matchedPlans.isEmpty()) {
			matchedPlans = availablePlans.stream()
				.filter(it -> it.getMaxMemory().equals(maxMemory) && it.getCustomCores())
				.collect(Collectors.toList());
		}

		//check globals
		if (matchedPlans.isEmpty()) {
			//we need to look by custom
			if (coresPerSocket == null || coresPerSocket == 0 || coresPerSocket == 1) {
				matchedPlans = availablePlans.stream()
					.filter(it -> ((Objects.equals(maxCores, 1L) && (it.getMaxCores() == null || it.getMaxCores() == 0)) || it.getMaxCores().equals(maxCores)) &&
						(it.getCoresPerSocket() == null || it.getCoresPerSocket() == 1) &&
						it.getCustomMaxMemory())
					.collect(Collectors.toList());
			} else {
				matchedPlans = availablePlans.stream()
					.filter(it -> ((Objects.equals(maxCores, 1L) && (it.getMaxCores() == null || it.getMaxCores() == 0)) || it.getMaxCores().equals(maxCores)) &&
						it.getCoresPerSocket().equals(coresPerSocket) &&
						it.getCustomMaxMemory())
					.collect(Collectors.toList());
			}
		}

		if (matchedPlans.isEmpty()) {
			matchedPlans = availablePlans.stream()
				.filter(it -> it.getCustomMaxMemory() && it.getCustomCores())
				.collect(Collectors.toList());
		}

		if (!matchedPlans.isEmpty()) {
			return matchedPlans.iterator().next();
		} else {
			return fallbackPlan;
		}
	}
}
