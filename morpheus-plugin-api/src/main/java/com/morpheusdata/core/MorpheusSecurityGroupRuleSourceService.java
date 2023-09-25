package com.morpheusdata.core;

import com.morpheusdata.model.SecurityGroupRuleSource;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;

/**
 * Context methods for syncing SecurityGroupRuleApplications in Morpheus
 */
public interface MorpheusSecurityGroupRuleSourceService extends MorpheusDataService<SecurityGroupRuleSource, SecurityGroupRuleSource> {

	/**
	 * Fetch the SecurityGroupRuleSources given a list of ids
	 * @param ids list of ids
	 * @return Observable list of SecurityGroupRuleSources
	 */
	@Deprecated(since="0.15.4")
	Observable<SecurityGroupRuleSource> listByIds(List<Long> ids);

	/**
	 * Save updates to existing SecurityGroupRuleSources
	 * @param securityGroupRuleSources SecurityGroupRuleSources to update
	 * @return whether the save was successful
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<SecurityGroupRuleSource> securityGroupRuleSources);

	/**
	 * Remove SecurityGroupRuleSources from Morpheus
	 * @param securityGroupRuleSources SecurityGroupRuleSources to remove
	 * @return whether the removal was successful
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<SecurityGroupRuleSource> securityGroupRuleSources);
}
