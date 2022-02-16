package com.morpheusdata.core.policy;

import com.morpheusdata.model.Account;
import com.morpheusdata.model.NetworkPool;
import com.morpheusdata.model.Policy;
import com.morpheusdata.model.PolicyType;
import io.reactivex.Observable;

/**
 * This service deals with interactions as it relates to Policies. Methods are provided for managing/listing policy information.
 * An example might be to use a custom report provider to access policy data and provide quota data on the policies.
 *
 * @author David Estes
 * @since 0.12.1
 * @see com.morpheusdata.model.Policy
 */
public interface MorpheusPolicyService {
	/**
	 * Returns the {@link PolicyType} related service for fetching type related information.
	 * @return an instance of the PolicyType Service for fetching related type information.
	 */
	MorpheusPolicyTypeService getType();

	Observable<Policy> listAllByAccount(Account account);

	Observable<Policy> listAllByAccountAndEnabled(Account account,Boolean enabled);


}
