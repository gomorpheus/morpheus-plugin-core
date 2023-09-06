package com.morpheusdata.core.cloud;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.core.data.DataQuery;
import com.morpheusdata.model.CloudFolder;
import com.morpheusdata.model.projection.CloudFolderIdentity;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link CloudFolder} in Morpheus. It can normally
 * be accessed via the {@link com.morpheusdata.core.MorpheusComputeServerService}
 * <p><strong>Examples:</strong></p>
 * <pre>{@code
 * morpheusContext.getCloud().getFolder()
 * }</pre>
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
