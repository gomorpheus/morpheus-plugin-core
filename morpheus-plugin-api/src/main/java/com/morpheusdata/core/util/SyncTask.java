package com.morpheusdata.core.util;

import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;
import java.util.Collection;
import java.util.List;

public class SyncTask<Projection, ApiItem, Model> {

	private List<MatchFunction<Projection, ApiItem>> matchFunctions;
	private OnDeleteFunction<Projection> onDeleteFunction;
	private ConnectableObservable<Projection> domainRecords;
	private Integer bufferSize = 50;
	private Collection<ApiItem> apiItems;
	private OnLoadObjectDetailsFunction<UpdateItemDto<Projection, ApiItem>,UpdateItem<Projection, Model>> onLoadObjectDetailsFunction;
	private OnUpdateFunction<UpdateItem<Projection, Model>> onUpdateFunction;
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

	public SyncTask<Projection, ApiItem, Model> onUpdate(OnUpdateFunction<UpdateItem<Projection, Model>> onUpdateFunction) {
		this.onUpdateFunction = onUpdateFunction;
		return this;
	}



	public SyncTask<Projection, ApiItem, Model> withLoadObjectDetails(OnLoadObjectDetailsFunction<UpdateItemDto<Projection, ApiItem>,UpdateItem<Projection, Model>> onLoadObjectDetailsFunction) {
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


	public class UpdateItemDto<Projection, ApiItem> {
		public Projection existingItem;
		public ApiItem masterItem;
	}

	public class UpdateItem<Model, ApiItem> {
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
		for (MatchFunction matchFunction : matchFunctions) {
			for (ApiItem apiItem : apiItems) {
				if (matchFunction.method(domainMatch, apiItem)) {
					return true;
				}
			}
		}
		return false;
	}

	private UpdateItemDto<Projection, ApiItem> buildUpdateItemDto(Projection domainMatch) {
		for (MatchFunction matchFunction : matchFunctions) {
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
		return null;
	}


	public void start() {
		// do all the subscribe crapola;
		//delete missing
		domainRecords.filter((Projection domainMatch) -> {
			return !matchesExisting(domainMatch);
		}).buffer(bufferSize).subscribe((List<Projection> itemsToDelete) -> {
			this.onDeleteFunction.method(itemsToDelete);
		});

		domainRecords.filter((Projection domainMatch) -> {
			return matchesExisting(domainMatch);
		}).map( (Projection domainMatch) -> {
			return buildUpdateItemDto(domainMatch);
		}).buffer(bufferSize).flatMap( (List<UpdateItemDto<Projection, ApiItem>> mapItems) -> {
			return onLoadObjectDetailsFunction.method(mapItems).buffer(50);
		}).doOnComplete( ()-> {
			onAddFunction.method(apiItems);
		}).doOnError( (Throwable t) -> {
			//log.error;
		}).subscribe( (updateItems) -> {
			onUpdateFunction.method(updateItems);
		});

		domainRecords.connect();
	}
}
