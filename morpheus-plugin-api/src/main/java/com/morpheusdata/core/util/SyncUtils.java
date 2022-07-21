package com.morpheusdata.core.util;

import com.morpheusdata.model.Account;
import com.morpheusdata.model.ServicePlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

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
				if(pl.visibility == "public" ||
						(pl.account != null && pl.account.getId() == account.getId()) ||
						(pl.owner != null && pl.owner.getId() == account.getId()) ||
						(pl.account == null && pl.visibility == "public")) {
					availablePlans.add(pl);
				}
			}
			if(existingPlan != null) {
				if(existingPlan.visibility == "public" ||
						(existingPlan.account != null && existingPlan.account.getId() == account.getId()) ||
						(existingPlan.owner != null && existingPlan.owner.getId() == account.getId()) ||
						(existingPlan.account == null && existingPlan.visibility == "public") ) {
					existingPlan = existingPlan;
				} else {
					existingPlan = null; //we have to correct a plan discrepency due to permissions on the vm
				}
			}
		}

		//first lets try to find a match by zone and an exact match at that
		if(existingPlan != null && existingPlan.getId() != fallbackPlan.getId()) {
			if((existingPlan.maxMemory == maxMemory || existingPlan.customMaxMemory) &&
					((existingPlan.maxCores == 0 && maxCores == 1) || existingPlan.maxCores == maxCores || existingPlan.customCores) &&
					((coresPerSocket == null || coresPerSocket == 0) || (existingPlan.coresPerSocket == coresPerSocket || existingPlan.customCores))) {
				return existingPlan; //existingPlan is still sufficient
			}
		}
		Collection<ServicePlan> matchedPlans = new ArrayList<ServicePlan>();
		if((coresPerSocket == null || coresPerSocket == 0) || coresPerSocket == 1) {
			for(ServicePlan it : availablePlans) {
				if(it.maxMemory == maxMemory &&
						it.customMaxMemory != true &&
						it.customCores != true &&
						((maxCores == 1 && (it.maxCores == null || it.maxCores == 0)) || it.maxCores == maxCores) && (it.coresPerSocket == null || it.coresPerSocket == 1)){
					matchedPlans.add(it);
				}
			}
		} else {
			for(ServicePlan it : availablePlans) {
				if(it.maxMemory == maxMemory &&
						((maxCores == 1 && (it.maxCores == null || it.maxCores == 0)) || it.maxCores == maxCores) &&
						it.customMaxMemory != true &&
						it.customCores != true &&
						it.coresPerSocket == coresPerSocket){
					matchedPlans.add(it);
				}
			}
		}

		if(matchedPlans.size() == 0) {
			for(ServicePlan it : availablePlans) {
				if(it.maxMemory == maxMemory && it.customCores) {
					matchedPlans.add(it);
				}
			}
		}

		//check globals
		if(matchedPlans.size() == 0) {
			//we need to look by custom
			if((coresPerSocket == null || coresPerSocket == 0) || coresPerSocket == 1) {
				for(ServicePlan it : availablePlans) {
					if(((maxCores == 1 && (it.maxCores == null || it.maxCores == 0)) || it.maxCores == maxCores) && (it.coresPerSocket == null || it.coresPerSocket == 1) && it.customMaxMemory) {
						matchedPlans.add(it);
					}
				}
			} else {
				for(ServicePlan it : availablePlans) {
					if(((maxCores == 1 && (it.maxCores == null || it.maxCores == 0)) || it.maxCores == maxCores) && it.coresPerSocket == coresPerSocket && it.customMaxMemory){
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
