package com.morpheusdata.core.util;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.observables.ConnectableObservable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * This Utility Class provides an rxJava compatible means for syncing remote API objects with local/morpheus backed models
 * in a persistent database. This handles an efficeint way to match data projection objects with api objects and batches
 * updates to the backend database for efficient sync. This should be considered the standard method for caching objects
 * within a {@link com.morpheusdata.core.CloudProvider} and many other provider types.
 *
 * <p><strong>Example:</strong></p>
 * <pre>{@code
 * Observable<NetworkDomainSyncProjection> domainRecords = morpheusContext.network.listNetworkDomainSyncMatch(poolServer.integration.id)
 *
 * SyncTask<NetworkDomainSyncProjection,Map,NetworkDomain> syncTask = new SyncTask(domainRecords, apiItems as Collection<Map>)
 * syncTask.addMatchFunction { NetworkDomainSyncProjection domainObject, Map apiItem ->
 *     domainObject.externalId == apiItem.'_ref'
 * }.addMatchFunction { NetworkDomainSyncProjection domainObject, Map apiItem ->
 *     domainObject.name == apiItem.name
 * }.onDelete {removeItems ->
 *     morpheusContext.network.removeMissingZones(poolServer.integration.id, removeItems)
 * }.onAdd { itemsToAdd ->
 *     while (itemsToAdd?.size() > 0) {
 *         List chunkedAddList = itemsToAdd.take(50)
 *         itemsToAdd = itemsToAdd.drop(50)
 *         addMissingZones(poolServer, chunkedAddList)
 *     }
 * }.withLoadObjectDetails { List<SyncTask.UpdateItemDto<NetworkDomainSyncProjection,Map>> updateItems ->
 *     return morpheusContext.network.listNetworkDomainsById(updateItems.collect{it.existingItem.id} as Collection<Long>)
 * }.onUpdate { List<SyncTask.UpdateItem<NetworkDomain,Map>> updateItems ->
 *     updateMatchedZones(poolServer, updateItems)
 * }.start()
 * }</pre>
 * @author David Estes
 * @param <Projection> The Projection Class Object used for matching the api object to the database object.
 *                     Typically this Projection Object has fewer properties like {@code id},{@code externalId}, or {@code name}
 * @param <ApiItem> The Class Object representing the individual API result object coming back in the Collection
 * @param <Model> The Model Class that the Projection Class is a subset of. This is the Class that needs to be updated
 *                with changes
 */
public class SyncTask<Projection, ApiItem, Model> {

	private final List<MatchFunction<Projection, ApiItem>> matchFunctions = new ArrayList<>();
	private OnDeleteFunction<Projection> onDeleteFunction;
	private final ConnectableObservable<Projection> domainRecords;
	private Integer bufferSize = 50;
	private final Collection<ApiItem> apiItems;
	private OnLoadObjectDetailsFunction<UpdateItemDto<Projection, ApiItem>,UpdateItem<Model, ApiItem>> onLoadObjectDetailsFunction;
	private OnUpdateFunction<UpdateItem<Model, ApiItem>> onUpdateFunction;
	private OnAddFunction<ApiItem> onAddFunction;

	public SyncTask(Observable<Projection> domainRecords, Collection<ApiItem> apiItems) {
		this.domainRecords = domainRecords.publish();
		this.apiItems = apiItems;
	}

	public SyncTask<Projection, ApiItem, Model> addMatchFunction(MatchFunction<Projection, ApiItem> matchFunction) {
		matchFunctions.add(matchFunction);
		return this;
	}

	public SyncTask<Projection, ApiItem, Model> onDelete(OnDeleteFunction<Projection> deleteFunction) {
		this.onDeleteFunction = deleteFunction;
		return this;
	}

	public SyncTask<Projection, ApiItem, Model> onAdd(OnAddFunction<ApiItem> onAddFunction) {
		this.onAddFunction = onAddFunction;
		return this;
	}

	public SyncTask<Projection, ApiItem, Model> onUpdate(OnUpdateFunction<UpdateItem<Model, ApiItem>> onUpdateFunction) {
		this.onUpdateFunction = onUpdateFunction;
		return this;
	}



	public SyncTask<Projection, ApiItem, Model> withLoadObjectDetails(OnLoadObjectDetailsFunction<UpdateItemDto<Projection, ApiItem>,UpdateItem<Model, ApiItem>> onLoadObjectDetailsFunction) {
		this.onLoadObjectDetailsFunction = onLoadObjectDetailsFunction;
		return this;
	}


	public void setBufferSize(Integer bufferSize) {
		this.bufferSize = bufferSize;
	}

	public SyncTask<Projection, ApiItem, Model> withBufferSize(Integer bufferSize) {
		this.bufferSize = bufferSize;
		return this;
	}


	public static class UpdateItemDto<Projection, ApiItem> {
		public Projection existingItem;
		public ApiItem masterItem;
	}

	public static class UpdateItem<Model, ApiItem> {
		public Model existingItem;
		public ApiItem masterItem;
	}

	@FunctionalInterface
	public interface MatchFunction<Projection, ApiItem> {
		Boolean method(Projection existingItem, ApiItem masterItem);
	}

	@FunctionalInterface
	public interface OnDeleteFunction<Projection> {
		void method(List<Projection> itemsToDelete);
	}

	@FunctionalInterface
	public interface OnAddFunction<ApiItem> {
		void method(Collection<ApiItem> itemsToAdd);
	}

	@FunctionalInterface
	public interface OnUpdateFunction<UpdateItems> {
		void method(List<UpdateItems> itemsToUpdate);
	}


	@FunctionalInterface
	public interface OnLoadObjectDetailsFunction<UpdateItemProjection,UpdateItem> {
		Observable<UpdateItem> method(List<UpdateItemProjection> itemsToLoad);
	}

	private Boolean matchesExisting(Projection domainMatch) {
		for (MatchFunction<Projection,ApiItem> matchFunction : matchFunctions) {
			for (ApiItem apiItem : apiItems) {
				if (matchFunction.method(domainMatch, apiItem)) {
					return true;
				}
			}
		}
		return false;
	}


	private UpdateItemDto<Projection, ApiItem> buildUpdateItemDto(Projection domainMatch) {
		for (MatchFunction<Projection,ApiItem> matchFunction : matchFunctions) {
			for (ApiItem apiItem : apiItems) {
				if (matchFunction.method(domainMatch, apiItem)) {
					UpdateItemDto<Projection, ApiItem> updateItem = new UpdateItemDto<Projection, ApiItem>();
					updateItem.existingItem = domainMatch;
					updateItem.masterItem = apiItem;
					apiItems.remove(apiItem); //clear the list out
					return updateItem;
				}
			}
		}
		return new UpdateItemDto<Projection, ApiItem>();
	}

	private void addMissing(Collection<ApiItem> addItems) {
		ArrayList<ApiItem> chunkedAddItems = new ArrayList<>();
		int bufferCounter=0;
		for(ApiItem addItem : addItems) {
			if(bufferCounter < bufferSize) {
				chunkedAddItems.add(addItem);
				bufferCounter++;
			} else {
				onAddFunction.method(chunkedAddItems);
				chunkedAddItems = new ArrayList<>();
				chunkedAddItems.add(addItem);
				bufferCounter = 1;
			}
		}

		if(chunkedAddItems.size() > 0) {
			onAddFunction.method(chunkedAddItems);
		}
	}

	public void start() {
		//do all the subscribe crapola;
		//delete missing
		domainRecords.filter((Projection domainMatch) -> {
			return !matchesExisting(domainMatch);
		}).buffer(bufferSize).subscribe((List<Projection> itemsToDelete) -> {
			this.onDeleteFunction.method(itemsToDelete);
		});

		domainRecords.filter(this::matchesExisting).map(this::buildUpdateItemDto).buffer(bufferSize).flatMap( (List<UpdateItemDto<Projection, ApiItem>> mapItems) -> {
			return onLoadObjectDetailsFunction.method(mapItems).buffer(bufferSize);
		}).doOnComplete( ()-> {
			addMissing(apiItems);
		}).doOnError( (Throwable t) -> {
			//log.error;
		}).subscribe( (updateItems) -> {
			onUpdateFunction.method(updateItems);
		});

		domainRecords.connect();
	}

	public Observable<Boolean> observe() {

		Observable<Boolean> syncObservable = Observable.create(new ObservableOnSubscribe<Boolean>() {
			@Override
			public void subscribe(@NonNull ObservableEmitter<Boolean> emitter) throws Exception {
				try {
					start();
					emitter.onNext(true);
					emitter.onComplete();
				} catch(Exception e) {
					emitter.onError(e);
				}
			}
		});
		return syncObservable;
	}
}
