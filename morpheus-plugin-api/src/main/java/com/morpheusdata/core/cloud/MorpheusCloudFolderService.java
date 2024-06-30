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

package com.morpheusdata.core.cloud;
import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.core.data.DataQuery;
import com.morpheusdata.model.CloudFolder;
import com.morpheusdata.model.projection.CloudFolderIdentity;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link CloudFolder} in Morpheus. It can normally
 * be accessed via the {@link com.morpheusdata.core.cloud.MorpheusCloudService}.
 * <p><strong>This service is accessible in the {@link MorpheusContext} via the following traversal path:</strong> <br>
 * {@code morpheusContext.getAsync().getCloud().getFolder()}</p>
 *
 * @author Bob Whiton
 */
public interface MorpheusCloudFolderService extends MorpheusDataService<CloudFolder,CloudFolderIdentity>, MorpheusIdentityService<CloudFolderIdentity> {

	/**
	 * Get a list of {@link CloudFolder} projections based on Cloud id
	 *
	 * @param cloudId  Cloud id
	 * @return Observable stream of sync projection
	 * @deprecated This is being replaced by {@link MorpheusCloudFolderService#listIdentityProjections(DataQuery)}
	 */
	@Deprecated(since="0.15.3",forRemoval = true)
	Observable<CloudFolderIdentity> listSyncProjections(Long cloudId);

	/**
	 * Get the default CloudFolder
	 * @param cloudId The id of the cloud
	 * @param accountId The id of the account
	 * @param siteId The id of the site (optional)
	 * @param servicePlanId The id of the ServicePlan (optional)
	 * @return The default CloudFolder
	 */
	Single<CloudFolder> getDefaultFolderForAccount(Long cloudId, Long accountId, Long siteId, Long servicePlanId);

	/**
	 * Get the default image CloudFolder
	 * @param cloudId The id of the cloud
	 * @param accountId The id of the account
	 * @return The default image CloudFolder
	 */
	Single<CloudFolder> getDefaultImageFolderForAccount(Long cloudId, Long accountId);

}
