/*
 *  Copyright 2026 Morpheus Data, LLC.
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

package com.morpheusdata.model;

import com.morpheusdata.model.projection.AccountCredentialIdentityProjection;

import java.util.Date;

public class AccountCredentialLink extends AccountCredentialIdentityProjection {
	protected AccountCredential credential;

	//ownership
	protected Account account;

	protected User user;
	protected UserGroup group;
	protected Role role;
	protected Boolean defaultCredential;

	//ref
	protected String refType;
	protected String refUuid;
	protected String refName;

	//audit
	protected Date dateCreated;
	protected Date lastUpdated;

}
