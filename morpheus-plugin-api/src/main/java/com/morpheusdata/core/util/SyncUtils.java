/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
					(pl.account != null && Objects.equals(pl.account.getId(), account.getId())) ||
					(pl.owner != null && Objects.equals(pl.owner.getId(), account.getId())) ||
					(pl.getAccount() == null && "public".equals(pl.getVisibility())) ||
					finalResourcePermissions.stream().anyMatch(rp -> Objects.equals(rp.getMorpheusResourceId(), pl.getId()) && Objects.equals(rp.getAccount().getId(), account.getId())))
				.collect(Collectors.toList());
			if (existingPlan != null) {
				ServicePlan finalExistingPlan = existingPlan;
				if ("public".equals(existingPlan.getVisibility()) ||
					(existingPlan.getAccount() != null && Objects.equals(existingPlan.getAccount().getId(), account.getId())) ||
					(existingPlan.getOwner() != null && Objects.equals(existingPlan.getOwner().getId(), account.getId())) ||
					(existingPlan.getAccount() == null && "public".equals(existingPlan.getVisibility())) ||
					resourcePermissions.stream().anyMatch(rp -> Objects.equals(rp.getMorpheusResourceId(), finalExistingPlan.getId()) && Objects.equals(rp.getAccount().getId(), account.getId()))) {
					// silly assignment because the inverse of the above is difficult to express
					existingPlan = existingPlan;
				} else {
					existingPlan = null; //we have to correct a plan discrepancy due to permissions on the vm
				}
			}
		}

		// does the current plan the correct size and is not the fallback plan
		if (existingPlan != null && (fallbackPlan == null || !existingPlan.getId().equals(fallbackPlan.getId()))) {
			if ((Objects.equals(existingPlan.getMaxMemory(), maxMemory) || existingPlan.getCustomMaxMemory()) &&
				((Objects.equals(existingPlan.getMaxCores(), 0L) && maxCores == 1) || Objects.equals(existingPlan.getMaxCores(), maxCores) || existingPlan.getCustomCores()) &&
				(coresPerSocket == null || Objects.equals(existingPlan.getCoresPerSocket(), coresPerSocket) || existingPlan.getCustomCores())) {
				return existingPlan;
			}
		}

		Collection<ServicePlan> matchedPlans;
		if (coresPerSocket == null || coresPerSocket == 0 || coresPerSocket == 1) {
			matchedPlans = availablePlans.stream()
				.filter(it -> Objects.equals(it.getMaxMemory(), maxMemory) &&
					!it.getCustomMaxMemory() &&
					!it.getCustomCores() &&
					((Objects.equals(maxCores, 1L) && (it.getMaxCores() == null || it.getMaxCores() == 0)) || Objects.equals(it.getMaxCores(), maxCores)) &&
					(it.getCoresPerSocket() == null || it.getCoresPerSocket() == 1))
				.collect(Collectors.toList());
		} else {
			matchedPlans = availablePlans.stream()
				.filter(it -> it.getMaxMemory().equals(maxMemory) &&
					((Objects.equals(maxCores, 1L) && (it.getMaxCores() == null || it.getMaxCores() == 0)) || Objects.equals(it.getMaxCores(), maxCores)) &&
					!it.getCustomMaxMemory() &&
					!it.getCustomCores() &&
					Objects.equals(it.getCoresPerSocket(), coresPerSocket))
				.collect(Collectors.toList());
		}

		if (matchedPlans.isEmpty()) {
			matchedPlans = availablePlans.stream()
				.filter(it -> Objects.equals(it.getMaxMemory(), maxMemory) && it.getCustomCores())
				.collect(Collectors.toList());
		}

		//check globals
		if (matchedPlans.isEmpty()) {
			//we need to look by custom
			if (coresPerSocket == null || coresPerSocket == 0 || coresPerSocket == 1) {
				matchedPlans = availablePlans.stream()
					.filter(it -> ((Objects.equals(maxCores, 1L) && (it.getMaxCores() == null || it.getMaxCores() == 0)) || Objects.equals(it.getMaxCores(), maxCores)) &&
						(it.getCoresPerSocket() == null || it.getCoresPerSocket() == 1) &&
						it.getCustomMaxMemory())
					.collect(Collectors.toList());
			} else {
				matchedPlans = availablePlans.stream()
					.filter(it -> ((Objects.equals(maxCores, 1L) && (it.getMaxCores() == null || it.getMaxCores() == 0)) || Objects.equals(it.getMaxCores(), maxCores)) &&
						Objects.equals(it.getCoresPerSocket(), coresPerSocket) &&
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
