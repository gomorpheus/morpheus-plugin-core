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
