package com.morpheusdata.core;

import com.morpheusdata.model.WikiPage;
import com.morpheusdata.model.projection.WikiPageIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing Wiki Pages in Morpheus
 */
public interface MorpheusWikiPageService {

	/**
	 * Get a list of WikiPage projections based on the refIds and refType
	 * @param refType the refType to match on. Typically 'ComputeServer'
	 * @param refIds the refIds to match on. Typically the ids of the Compute Servers
	 * @return Observable stream of sync projection
	 */
	Observable<WikiPageIdentityProjection> listSyncProjections(String refType, List<Long> refIds);

	/**
	 * Get a list of WikiPage objects from a list of projection ids
	 * @param ids WikiPage ids
	 * @return Observable stream of WikiPage
	 */
	Observable<WikiPage> listById(Collection<Long> ids);

	/**
	 * Save updates to existing WikiPages
	 * @param wikiPages updated WikiPages
	 * @return success
	 */
	Single<Boolean> save(List<WikiPage> wikiPages);

	/**
	 * Create new WikiPages in Morpheus
	 * @param wikiPages new WikiPages to persist
	 * @return success
	 */
	Single<Boolean> create(List<WikiPage> wikiPages);

	/**
	 * Create and return a new WikiPage in Morpheus
	 * @param wikiPage new WikiPage to persist
	 * @return the wiki page
	 */
	Single<WikiPage> create(WikiPage wikiPage);

	/**
	 * Remove persisted WikiPages from Morpheus
	 * @param wikiPages WikiPages to delete
	 * @return success
	 */
	Single<Boolean> remove(List<WikiPageIdentityProjection> wikiPages);
}
