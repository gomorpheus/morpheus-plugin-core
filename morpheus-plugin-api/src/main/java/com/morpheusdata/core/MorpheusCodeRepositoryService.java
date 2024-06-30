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

package com.morpheusdata.core;

import com.morpheusdata.model.CodeRepository;
import com.morpheusdata.response.CodeRepositoryResponse;
import io.reactivex.rxjava3.core.Maybe;

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
