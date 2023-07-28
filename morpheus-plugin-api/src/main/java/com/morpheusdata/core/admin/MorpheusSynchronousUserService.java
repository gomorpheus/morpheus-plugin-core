package com.morpheusdata.core.admin;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.User;
import com.morpheusdata.model.projection.UserIdentity;
import com.morpheusdata.model.projection.UserIdentityProjection;

public interface MorpheusSynchronousUserService extends MorpheusSynchronousDataService<User>, MorpheusSynchronousIdentityService<UserIdentityProjection> {
}
