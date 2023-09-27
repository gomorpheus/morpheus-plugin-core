package com.morpheusdata.core.policy;


import com.morpheusdata.model.PolicyType;
import io.reactivex.rxjava3.core.Single;

/**
 * This Context deals with interactions related to {@link com.morpheusdata.model.PolicyType} objects. It can normally
 * be accessed via the primary {@link com.morpheusdata.core.MorpheusContext} via the {@link MorpheusPolicyService}
 * <p><strong>Examples:</strong></p>
 * <pre>{@code
 * morpheusContext.getPolicy().getType()
 * }</pre>
 * @see MorpheusPolicyService
 * @since 0.12.2
 * @author David Estes
 */
public interface MorpheusPolicyTypeService {
	/**
	 * Find a Policy Type by code
	 * @param code Name of the type
	 * @return An instance of PolicyType
	 */
	Single<PolicyType> findByCode(String code);
}
