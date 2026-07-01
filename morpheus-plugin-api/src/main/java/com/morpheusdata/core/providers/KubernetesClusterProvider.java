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

package com.morpheusdata.core.providers;

import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.model.ComputeServerGroup;
import com.morpheusdata.response.KubeApiCredentials;
import com.morpheusdata.response.ServiceResponse;

import java.util.Collection;
import java.util.Map;

/**
 * A specialization of {@link ClusterProvider} for Kubernetes-based clusters (managed control-plane
 * offerings such as EKS, AKS, and GKE, as well as self-managed kubeadm-style clusters).
 *
 * <p>On the appliance side this provider is backed by a host service that extends the built-in
 * {@code KubernetesHostService}. That means a plugin implementing this interface inherits the full
 * suite of core Kubernetes operations (namespace management, node drain/cordon, kubectl/helm
 * execution, service-account handling, brownfield node sync, etc.) for free. A plugin only needs to
 * implement the {@code Facet} interfaces below to supply the small, genuinely cloud-specific seams
 * where the managed offering differs from a vanilla cluster.</p>
 *
 * <p><strong>Routing model.</strong> The host service routes each overridable operation by checking
 * whether this provider implements the corresponding facet ({@code instanceof}); if it does, the
 * facet is invoked, otherwise the inherited core Kubernetes behavior runs. This mirrors the existing
 * facet convention on {@link ClusterProvider} ({@code ServerPowerFacet}, {@code ClusterUpdateFacet})
 * and on {@code ProvisionProvider}.</p>
 *
 * <p><strong>Refresh model.</strong> When a cluster is refreshed, the appliance runs the full
 * built-in Kubernetes sync <em>first</em> (node discovery, namespaces, workloads, etc.). Individual
 * pieces of that sync can be customized by implementing the facets below ({@code ApiAccessFacet},
 * {@code NodeSyncFacet}, the per-resource {@code *CacheFacet}s, etc.) &mdash; the host service
 * substitutes your facet wherever the core sync reaches one of those seams. After the built-in sync
 * completes, the base
 * {@link ClusterProvider#refresh(com.morpheusdata.model.ComputeServerGroup)} is invoked last. For
 * most Kubernetes clusters this final call is a no-op: the built-in sync has already done the work.
 * Only implement {@code refresh} when you have a genuine cloud-side concern to reconcile that the
 * core Kubernetes sync does not cover (for example cloud node-group or autoscaler state).</p>
 *
 * @since 1.4.x
 */
public interface KubernetesClusterProvider extends ClusterProvider {

	/**
	 * Kubernetes cluster providers default their provider type to {@code "kubernetes"} so that the
	 * appliance applies Kubernetes-specific category conventions and resolves the Kubernetes host
	 * service. Override only if a more specific type discriminator is required.
	 *
	 * @return the provider type discriminator, {@code "kubernetes"} by default
	 */
	@Override
	default String getProviderType() {
		return "kubernetes";
	}

	/**
	 * Managed Kubernetes offerings issue API credentials from the cloud control plane rather than from a
	 * long-lived in-cluster service account. Implement this facet to supply those bootstrap credentials;
	 * Morpheus uses them to create its own {@code morpheus} service account on the cluster and then owns
	 * the durable token and all subsequent plumbing.
	 *
	 * <p>Credentials are returned as a {@link KubeApiCredentials} bundle because offerings authenticate
	 * differently: EKS/GKE issue a bearer token while AKS issues a client certificate / key pair. This is
	 * the seam currently represented by {@code generateClusterToken} (EKS), {@code refreshKubeAPIToken}
	 * (AKS), and the equivalent token exchange (GKE).</p>
	 */
	interface CredentialsFacet {
		/**
		 * Obtain the bootstrap credentials used to authenticate against the managed cluster's Kubernetes
		 * API server.
		 *
		 * @param cluster the cluster whose API credentials should be (re)issued
		 * @return a {@link ServiceResponse} whose data carries the {@link KubeApiCredentials} on success
		 */
		ServiceResponse<KubeApiCredentials> getClusterApiCredentials(ComputeServerGroup cluster);
	}

	/**
	 * Implement this facet when the kubeconfig / API access document cannot be assembled generically
	 * from the cluster's {@code serviceUrl}, {@code serviceCert}, and token, and instead requires a
	 * cloud-specific construction (for example an exec-credential plugin stanza).
	 *
	 * <p>This is the seam currently represented by {@code getKubernetesApiAccess} /
	 * {@code buildKubeConfig} in the managed host services.</p>
	 */
	interface ApiAccessFacet {
		/**
		 * Produce the kubeconfig (API access document) used by Morpheus to administer the cluster.
		 *
		 * @param cluster the cluster to build access for
		 * @return a {@link ServiceResponse} whose data carries the kubeconfig contents on success
		 */
		ServiceResponse<String> getKubernetesApiAccess(ComputeServerGroup cluster);
	}

	/**
	 * Implement this facet to post-process the list of nodes discovered during a cluster sync. Managed
	 * offerings hide the control-plane nodes, so providers commonly use this hook to synthesize a
	 * placeholder master node (or otherwise reconcile the discovered list against existing records).
	 *
	 * <p>This is the seam currently represented by {@code postProcessListNodes} (EKS, AKS).</p>
	 */
	interface NodeSyncFacet {
		/**
		 * Adjust the discovered node list before Morpheus reconciles it against existing records.
		 *
		 * @param cluster         the cluster being synced
		 * @param discoveredNodes the raw node descriptors returned by the Kubernetes API
		 * @param existingNodes   the {@link ComputeServer} records Morpheus already tracks for the cluster
		 * @return the adjusted node list to use for reconciliation
		 */
		Collection<Map> postProcessNodeList(ComputeServerGroup cluster, Collection<Map> discoveredNodes, Collection<ComputeServer> existingNodes);
	}

	/**
	 * Implement this facet to delegate node-count changes to the cloud's managed autoscaling / node
	 * group API instead of Morpheus provisioning individual nodes. Providers that implement this facet
	 * should also return {@code true} from {@link ClusterProvider#supportsCloudScaling()}.
	 *
	 * <p>This is the seam currently represented by {@code doCloudScaling} (EKS, AKS, GKE).</p>
	 */
	interface CloudScalingFacet {
		/**
		 * Scale the cluster to the desired node count using the cloud-managed scaling API.
		 *
		 * @param cluster          the cluster to scale
		 * @param desiredNodeCount the target number of nodes
		 * @param opts             additional, provider-specific options
		 * @return a {@link ServiceResponse} indicating success or failure
		 */
		ServiceResponse scaleCluster(ComputeServerGroup cluster, Long desiredNodeCount, Map opts);
	}

	/**
	 * Implement this facet to reconcile cluster-level state after a server group is updated &mdash; most
	 * commonly to swap node {@code ComputeServerType}s and layouts when a cluster transitions between
	 * managed and unmanaged, and to regenerate API credentials as a result. The host service invokes
	 * this facet <em>after</em> the inherited core update has run.
	 *
	 * <p>This is the seam currently represented by {@code updateServerGroup} (EKS, AKS, GKE), where each
	 * managed offering reassigns its own provider-specific master/worker types.</p>
	 */
	interface ServerGroupUpdateFacet {
		/**
		 * Reconcile cluster-level state after a server group update.
		 *
		 * @param cluster the cluster that was updated
		 * @param opts    the update options that were applied
		 * @return a {@link ServiceResponse} indicating success or failure
		 */
		ServiceResponse updateServerGroup(ComputeServerGroup cluster, Map opts);
	}

	// -------------------------------------------------------------------------------------------------
	// Cache facets
	//
	// The built-in cluster sync caches each kind of Kubernetes resource in its own step (namespaces,
	// hosts, workloads, storage, etc.). Implement any of the facets below to *replace* an individual
	// cache step outright: the core step (list from the Kubernetes API + reconcile into the Morpheus
	// model noted on each facet) is skipped entirely and your implementation takes full ownership of
	// fetching and persisting via its own MorpheusContext. Every step you do not implement runs the
	// inherited core behavior unchanged. These are escape hatches for managed offerings whose resource
	// model differs from a vanilla cluster; they intentionally provide no partial scaffolding. Each
	// facet receives the cluster being synced and returns a {@link ServiceResponse} indicating success
	// or failure, and is routed by {@code instanceof} exactly like the facets above.
	// -------------------------------------------------------------------------------------------------

	/**
	 * Replace the "cache version" step. Core reads the cluster's Kubernetes API {@code /version} and
	 * records it on the cluster ({@code serviceVersion} plus a {@code versionData} config entry); no
	 * child records are synced.
	 */
	interface VersionCacheFacet {
		ServiceResponse cacheVersion(ComputeServerGroup cluster);
	}

	/**
	 * Replace the "cache namespaces" step. Core lists Kubernetes Namespaces and reconciles them into
	 * Morpheus {@code ComputeZonePool} records (category {@code "namespace"}).
	 */
	interface NamespaceCacheFacet {
		ServiceResponse cacheNamespaces(ComputeServerGroup cluster);
	}

	/**
	 * Replace the "cache hosts" (node sync) step. Core lists Kubernetes Nodes and reconciles them into
	 * {@code ComputeServer} records (with {@code ComputeCapacityInfo} for capacity). The returned
	 * {@link ServiceResponse} data may carry a {@code clusterStatus} entry, which the host service
	 * propagates as the cluster's overall status.
	 */
	interface HostCacheFacet {
		ServiceResponse cacheHosts(ComputeServerGroup cluster);
	}

	/**
	 * Replace the "cache service accounts" step. Core lists Kubernetes ServiceAccounts and reconciles
	 * them into {@code ReferenceData} records scoped to the cluster.
	 */
	interface ServiceAccountCacheFacet {
		ServiceResponse cacheServiceAccounts(ComputeServerGroup cluster);
	}

	/**
	 * Replace the "cache roles" step. Core lists Kubernetes (Cluster)Roles and reconciles them into
	 * {@code ReferenceData} records scoped to the cluster.
	 */
	interface RoleCacheFacet {
		ServiceResponse cacheRoles(ComputeServerGroup cluster);
	}

	/**
	 * Replace the "cache role bindings" step. Core lists Kubernetes (Cluster)RoleBindings and reconciles
	 * them into {@code ReferenceData} records scoped to the cluster.
	 */
	interface RoleBindingCacheFacet {
		ServiceResponse cacheRoleBindings(ComputeServerGroup cluster);
	}

	/**
	 * Replace the "cache endpoints" step. Core lists Kubernetes Endpoints and reconciles them into
	 * {@code ReferenceData} records scoped to the cluster.
	 */
	interface EndpointCacheFacet {
		ServiceResponse cacheEndpoints(ComputeServerGroup cluster);
	}

	/**
	 * Replace the "cache ingresses" step. Core lists Kubernetes Ingresses and reconciles them into
	 * {@code ReferenceData} records scoped to the cluster.
	 */
	interface IngressCacheFacet {
		ServiceResponse cacheIngresses(ComputeServerGroup cluster);
	}

	/**
	 * Replace the "cache services" step. Core lists Kubernetes Services and reconciles them into
	 * {@code ServiceEntry} records (with {@code ServiceEntryPort} / {@code ComputeSitePort} for exposed
	 * ports).
	 */
	interface ServiceCacheFacet {
		ServiceResponse cacheServices(ComputeServerGroup cluster);
	}

	/**
	 * Replace the "cache network policies" step. Core lists Kubernetes NetworkPolicies and reconciles
	 * them into {@code ReferenceData} records scoped to the cluster.
	 */
	interface NetworkPolicyCacheFacet {
		ServiceResponse cacheNetworkPolicies(ComputeServerGroup cluster);
	}

	/**
	 * Replace the "cache storage classes" step. Core lists Kubernetes StorageClasses and reconciles them
	 * into Morpheus {@code Datastore} records (category {@code "storageClass"}).
	 */
	interface StorageClassCacheFacet {
		ServiceResponse cacheStorageClasses(ComputeServerGroup cluster);
	}

	/**
	 * Replace the "cache secrets" step. Core lists Kubernetes Secrets and reconciles them into
	 * {@code ReferenceData} records scoped to the cluster.
	 */
	interface SecretCacheFacet {
		ServiceResponse cacheSecrets(ComputeServerGroup cluster);
	}

	/**
	 * Replace the "cache config maps" step. Core lists Kubernetes ConfigMaps and reconciles them into
	 * {@code ContainerTypeConfig} records scoped to the cluster (category {@code "configMap"}).
	 */
	interface ConfigMapCacheFacet {
		ServiceResponse cacheConfigMaps(ComputeServerGroup cluster);
	}

	/**
	 * Replace the "cache volume claims" step. Core lists Kubernetes PersistentVolumeClaims and
	 * reconciles them into {@code StorageVolume} records (with {@code StorageVolumeType} /
	 * {@code Datastore} linkage).
	 */
	interface VolumeClaimCacheFacet {
		ServiceResponse cacheVolumeClaims(ComputeServerGroup cluster);
	}

	/**
	 * Replace the "cache volumes" step. Core lists Kubernetes PersistentVolumes and reconciles them into
	 * {@code StorageVolume} records (with {@code StorageVolumeType} / {@code Datastore} linkage).
	 */
	interface VolumeCacheFacet {
		ServiceResponse cacheVolumes(ComputeServerGroup cluster);
	}

	/**
	 * Replace the "cache containers (workloads)" step. Core lists Kubernetes Pods, Deployments,
	 * ReplicaSets, StatefulSets and DaemonSets and reconciles them into Morpheus {@code Container} /
	 * {@code ContainerGroup} records (with per-workload {@code ComputeZonePool} categories).
	 */
	interface ContainerCacheFacet {
		ServiceResponse cacheContainers(ComputeServerGroup cluster);
	}

	/**
	 * Replace the "cache jobs" step. Core lists Kubernetes Jobs and reconciles them into
	 * {@code JobExecution} records (with {@code JobTemplate} / {@code JobType}).
	 */
	interface JobCacheFacet {
		ServiceResponse cacheJobs(ComputeServerGroup cluster);
	}

	/**
	 * Replace the "cache cron jobs" step. Core lists Kubernetes CronJobs and reconciles them into
	 * {@code JobExecution} / {@code JobTemplate} records (with {@code JobType}).
	 */
	interface CronJobCacheFacet {
		ServiceResponse cacheCronJobs(ComputeServerGroup cluster);
	}

	/**
	 * Replace the "cache events" step. Core lists Kubernetes Events and reconciles them into Morpheus
	 * {@code OperationEvent} records.
	 */
	interface EventCacheFacet {
		ServiceResponse cacheEvents(ComputeServerGroup cluster);
	}

	/**
	 * Replace the "cache join command" step. Core connects to a control-plane node to fetch the current
	 * {@code kubeadm} join token/command and stores it on the cluster; no child records are synced and
	 * no Kubernetes list call is made. Rarely relevant for managed offerings, where nodes join through
	 * the cloud provider rather than a kubeadm token.
	 */
	interface JoinCommandCacheFacet {
		ServiceResponse cacheJoinCommand(ComputeServerGroup cluster);
	}

	/**
	 * Replace the "cache helm status" step. Core inspects Helm release state for the cluster and updates
	 * the cluster's {@code helmStatus} config; no child records are synced and no Kubernetes list call
	 * is made.
	 */
	interface HelmStatusCacheFacet {
		ServiceResponse cacheHelmStatus(ComputeServerGroup cluster);
	}
}
