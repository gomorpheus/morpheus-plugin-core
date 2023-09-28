package com.morpheusdata.core;

import com.morpheusdata.model.CodeRepository;
import com.morpheusdata.response.CodeRepositoryResponse;
import io.reactivex.Maybe;

/**
 * Context methods for managing code repositories
 * @author bdwheeler
 * @since 0.15.1
 */
public interface MorpheusCodeRepositoryService extends MorpheusDataService<CodeRepository, CodeRepository> {

	/**
	 * Fetches the contents of a code repos
	 * @param repositoryId the id of the repository in Morpheus
	 * @return a CodeRepositoryResponse with the contents of the repo generated
	 */
	Maybe<CodeRepositoryResponse> fetchCodeRepository(Long repositoryId, String branch);

}
