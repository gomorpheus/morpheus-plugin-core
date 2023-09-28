package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.WikiPage;
import com.morpheusdata.model.projection.WikiPageIdentityProjection;

public interface MorpheusSynchronousWikiPageService extends MorpheusSynchronousDataService<WikiPage, WikiPageIdentityProjection>, MorpheusSynchronousIdentityService<WikiPageIdentityProjection> {
}
