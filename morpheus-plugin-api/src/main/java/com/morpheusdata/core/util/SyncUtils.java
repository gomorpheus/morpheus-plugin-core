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
import com.morpheusdata.model.ServicePlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class SyncUtils {

	static Logger log = LoggerFactory.getLogger(SyncUtils.class);

	/**
	 * Given a list of available ServicePlans and various parameters for a ComputeServer, determine a matching ServicePlan. If the
	 * current existingPlan is still a match (although might not be the 'best' match), it will be returned. If no plan is found,
	 * then the fallbackPlan is returned
	 * @param allPlans All possible plans to pick from
	 * @param maxMemory The maxMemory of the ComputeServer being matched
	 * @param maxCores The maxCores of the ComputeServer being matched
	 * @param coresPerSocket The coresPerSocket of the ComputeServer being matched
	 * @param fallbackPlan The ServicePlan to use if no match is found
	 * @param existingPlan The current ServicePlan of the ComputeServer
	 * @param account The account for the ComputeServer
	 * @return The matched ServicePlan
	 */
	static ServicePlan findServicePlanBySizing(Collection<ServicePlan> allPlans, Long maxMemory, Long maxCores, Long coresPerSocket, ServicePlan fallbackPlan, ServicePlan existingPlan, Account account) {
		log.debug("findServicePlanBySizing");
		Collection<ServicePlan> availablePlans = allPlans;
		if(account != null) {
			availablePlans = new ArrayList<ServicePlan>();
			for(ServicePlan pl : allPlans) {
				if(pl.visibility.equals("public") ||
						(pl.account != null && Objects.equals(pl.account.getId(), account.getId())) ||
						(pl.owner != null && Objects.equals(pl.owner.getId(), account.getId())) ||
						(pl.account == null && pl.visibility.equals("public"))) {
					availablePlans.add(pl);
				}
			}
			if(existingPlan != null) {
				if(existingPlan.visibility.equals("public") ||
						(existingPlan.account != null && Objects.equals(existingPlan.account.getId(), account.getId())) ||
						(existingPlan.owner != null && Objects.equals(existingPlan.owner.getId(), account.getId())) ||
						(existingPlan.account == null && existingPlan.visibility.equals("public")) ) {
					existingPlan = existingPlan;
				} else {
					existingPlan = null; //we have to correct a plan discrepency due to permissions on the vm
				}
			}
		}

		//first lets try to find a match by zone and an exact match at that
		if(existingPlan != null && !existingPlan.getId().equals(fallbackPlan.getId())) {
			if((Objects.equals(existingPlan.maxMemory, maxMemory) || existingPlan.customMaxMemory) &&
					((Objects.equals(existingPlan.maxCores, 0L) && maxCores == 1) || Objects.equals(existingPlan.maxCores, maxCores) || existingPlan.customCores) &&
					((coresPerSocket == null || coresPerSocket == 0) || (Objects.equals(existingPlan.coresPerSocket, coresPerSocket) || existingPlan.customCores))) {
				return existingPlan; //existingPlan is still sufficient
			}
		}
		Collection<ServicePlan> matchedPlans = new ArrayList<ServicePlan>();
		if((coresPerSocket == null || coresPerSocket == 0) || coresPerSocket == 1) {
			for(ServicePlan it : availablePlans) {
				if(Objects.equals(it.maxMemory, maxMemory) &&
					!it.customMaxMemory &&
					!it.customCores &&
						((maxCores == 1 && (it.maxCores == null || it.maxCores == 0)) || Objects.equals(it.maxCores, maxCores)) && (it.coresPerSocket == null || it.coresPerSocket == 1)){
					matchedPlans.add(it);
				}
			}
		} else {
			for(ServicePlan it : availablePlans) {
				if(Objects.equals(it.maxMemory, maxMemory) &&
						((maxCores == 1 && (it.maxCores == null || it.maxCores == 0)) || Objects.equals(it.maxCores, maxCores)) &&
						!it.customMaxMemory &&
						!it.customCores &&
					Objects.equals(it.coresPerSocket, coresPerSocket)){
					matchedPlans.add(it);
				}
			}
		}

		if(matchedPlans.size() == 0) {
			for(ServicePlan it : availablePlans) {
				if(Objects.equals(it.maxMemory, maxMemory) && it.customCores) {
					matchedPlans.add(it);
				}
			}
		}

		//check globals
		if(matchedPlans.size() == 0) {
			//we need to look by custom
			if((coresPerSocket == null || coresPerSocket == 0) || coresPerSocket == 1) {
				for(ServicePlan it : availablePlans) {
					if(((maxCores == 1 && (it.maxCores == null || it.maxCores == 0)) || Objects.equals(it.maxCores, maxCores)) && (it.coresPerSocket == null || it.coresPerSocket == 1) && it.customMaxMemory) {
						matchedPlans.add(it);
					}
				}
			} else {
				for(ServicePlan it : availablePlans) {
					if(((maxCores == 1 && (it.maxCores == null || it.maxCores == 0)) || Objects.equals(it.maxCores, maxCores)) && Objects.equals(it.coresPerSocket, coresPerSocket) && it.customMaxMemory){
						matchedPlans.add(it);
					}
				}
			}
		}

		if(matchedPlans.size() == 0) {
			for(ServicePlan it : availablePlans) {
				if(it.customMaxMemory && it.customCores) {
					matchedPlans.add(it);
				}
			}
		}

		if(matchedPlans.size() != 0) {
			return matchedPlans.iterator().next();
		} else {
			return fallbackPlan;
		}
	}
}
