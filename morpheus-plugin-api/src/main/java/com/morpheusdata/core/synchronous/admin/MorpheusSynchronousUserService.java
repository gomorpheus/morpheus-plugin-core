package com.morpheusdata.core.synchronous.admin;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.User;
import com.morpheusdata.model.projection.UserIdentityProjection;

public interface MorpheusSynchronousUserService extends MorpheusSynchronousDataService<User,UserIdentityProjection>, MorpheusSynchronousIdentityService<UserIdentityProjection> {
}
