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

package com.morpheusdata.core.cypher;

import com.morpheusdata.model.Account;
import com.morpheusdata.model.User;

/**
 * A class to hold the various properties required when accessing {@link MorpheusCypherService}
 * @since 0.10.0
 * @author Bob Whiton
 */
public class CypherAccess {

	public Account account;

	/**
	 * an object reference that can be checked against instead of a temporal value for key invalidation
	 */
	public String leaseObjectRef;
	public User user;

	public CypherAccess(Account account) {
		this.account = account;
	}

	public CypherAccess(Account account, String leaseObjectRef) {
		this.account = account;
		this.leaseObjectRef = leaseObjectRef;
	}

	public CypherAccess(Account account, String leaseObjectRef, User user) {
		this.account = account;
		this.leaseObjectRef = leaseObjectRef;
		this.user = user;
	}
}
