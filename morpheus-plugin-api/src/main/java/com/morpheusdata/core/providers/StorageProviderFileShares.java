package com.morpheusdata.core.providers;

import com.morpheusdata.model.StorageBucket;
import com.morpheusdata.response.ServiceResponse;

import java.util.Collection;
import java.util.Map;

/**
 * This Provider interface is used in combination with {@link StorageProvider} to define a
 * {@link com.morpheusdata.model.StorageServerType} that can create and delete file share storage
 * buckets. These could be like Samba file shares or NFS file shares.
 *
 * @since 0.15.1
 * @see StorageProvider
 */
public interface StorageProviderFileShares {
	ServiceResponse createFileShare(StorageBucket storageShare, Map opts);

	ServiceResponse updateFileShare(StorageBucket storageShare, Map opts);

	ServiceResponse deleteFileShare(StorageBucket storageShare, Map opts);

	Collection<String> getFileShareProviderTypes();
}
