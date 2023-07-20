package com.morpheusdata.core;

/**
 * Renders tabs within a custom Cluster in Morpheus. This could be useful for providing additional information on
 * a Kubernetes/Docker or KVM Cluster. Say for example some type of Prometheus data could be displayed. this could assist
 * with that.
 *
 * NOTE: This provider has moved to {@link com.morpheusdata.core.providers.ClusterTabProvider}
 *
 * @author David Estes
 * @see com.morpheusdata.core.providers.ClusterTabProvider
 * @deprecated
 */
@Deprecated
public interface ClusterTabProvider extends com.morpheusdata.core.providers.ClusterTabProvider {
}
