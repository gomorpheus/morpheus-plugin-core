package com.morpheusdata.core.network;

/**
 * This Context deals with interactions related to {@link com.morpheusdata.model.NetworkPoolIp} objects. It can normally
 * be accessed via the primary {@link com.morpheusdata.core.MorpheusContext} via the {@link MorpheusNetworkContext} and
 * finally via the {@link MorpheusNetworkPoolContext} traversal.
 * Network Pool Ip Records are Host Records entities within an IPAM Service. These are create/destroyed based on provisioning
 * integrations as well as syncing with Pool Server Integration types.
 *
 * <p><strong>Examples:</strong></p>
 * <pre>{@code
 * morpheusContext.getNetwork().getPool().getPoolIp()
 * }</pre>
 *
 * @see MorpheusNetworkPoolContext
 * @author David Estes
 */
public interface MorpheusNetworkPoolIpContext {
}
