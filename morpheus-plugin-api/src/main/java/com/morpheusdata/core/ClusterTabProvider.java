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
