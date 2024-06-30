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

import com.morpheusdata.core.providers.DNSProvider;

/**
 * Provides IP Address Management integration support for third party IPAM Vendors. An example might be Infoblox or Bluecat
 * These types of providers often provides DNS Support as well and in that event it is possible for a Provider to implement
 * both interfaces
 *
 * NOTE: This provider has been deprecated and moved {@link com.morpheusdata.core.providers.IPAMProvider}
 *
 * @see DNSProvider
 * @since 0.8.0
 * @deprecated
 * @see com.morpheusdata.core.providers.IPAMProvider
 * @author David Estes
 */
@Deprecated
public interface IPAMProvider extends com.morpheusdata.core.providers.IPAMProvider {
}
