package com.morpheusdata.core.admin;

import com.morpheusdata.core.MorpheusDataIdentityService;
import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.User;
import com.morpheusdata.model.projection.UserIdentity;

public interface MorpheusUserService extends MorpheusDataService<User>, MorpheusDataIdentityService<UserIdentity> {
}
