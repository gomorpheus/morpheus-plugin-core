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
 * Any plugin for Morpheus that provides DNS Related integration points should use this for implementing
 * DNS Related Services used throughout the orchestration process
 *
 * NOTE: This Provider has been replaced by {@link com.morpheusdata.core.providers.DNSProvider}.
 *
 * @since 0.8.0
 * @deprecated
 * @see com.morpheusdata.core.providers.DNSProvider
 * @author David Estes
 */
@Deprecated
public interface DNSProvider extends com.morpheusdata.core.providers.DNSProvider {
}
