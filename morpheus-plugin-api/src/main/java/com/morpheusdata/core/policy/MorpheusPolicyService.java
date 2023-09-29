package com.morpheusdata.core.policy;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.Account;
import com.morpheusdata.model.Policy;
import com.morpheusdata.model.PolicyType;
import io.reactivex.rxjava3.core.Observable;

/**
 * This service deals with interactions as it relates to Policies. Methods are provided for managing/listing policy information.
 * An example might be to use a custom report provider to access policy data and provide quota data on the policies.
 *
 * @author David Estes
 * @since 0.12.1
 * @see com.morpheusdata.model.Policy
 */
public interface MorpheusPolicyService extends MorpheusDataService<Policy, Policy> {
	/**
	 * Returns the {@link PolicyType} related service for fetching type related information.
	 * @return an instance of the PolicyType Service for fetching related type information.
	 */
	MorpheusPolicyTypeService getType();

	@Deprecated(since="0.15.4")
	Observable<Policy> listAllByAccount(Account account);

	@Deprecated(since="0.15.4")
	Observable<Policy> listAllByAccountAndEnabled(Account account,Boolean enabled);


}
