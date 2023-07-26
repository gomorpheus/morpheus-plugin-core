package com.morpheusdata.core.admin;

import com.morpheusdata.core.MorpheusSynchronousDataIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.User;
import com.morpheusdata.model.projection.UserIdentity;

public interface MorpheusSynchronousUserService extends MorpheusSynchronousDataService<User>, MorpheusSynchronousDataIdentityService<UserIdentity> {
}
