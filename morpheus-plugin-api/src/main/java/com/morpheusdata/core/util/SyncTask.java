package com.morpheusdata.core.util;

import com.morpheusdata.model.dto.NetworkDomainSyncMatchDto;
import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;

import java.util.Collection;
import java.util.List;

public class SyncTask<T, I, J> {

	private List<MatchFunction<T, I>> matchFunctions;
	private OnDeleteFunction<T> onDeleteFunction;
	private ConnectableObservable<T> domainRecords;
	private Integer bufferSize = 50;
	private Collection<I> apiItems;
	private OnLoadObjectDetailsFunction<UpdateItemDto<T,I>,UpdateItem<T,J>> onLoadObjectDetailsFunction;
	private OnUpdateFunction<UpdateItem<T,J>> onUpdateFunction;
	private OnAddFunction<I> onAddFunction;

	public SyncTask(Observable<T> domainRecords, Collection<I> apiItems) {
		this.domainRecords = domainRecords.publish();
		this.apiItems = apiItems;
	}

	public SyncTask<T, I, J> addMatchFunction(MatchFunction<T, I> matchFunction) {
		matchFunctions.add(matchFunction);
		return this;
	}

	public SyncTask<T, I, J> onDelete(OnDeleteFunction<T> deleteFunction) {
		this.onDeleteFunction = deleteFunction;
		return this;
	}

	public SyncTask<T, I, J> onAdd(OnAddFunction<I> onAddFunction) {
		this.onAddFunction = onAddFunction;
		return this;
	}

	public SyncTask<T, I, J> onUpdate(OnUpdateFunction<UpdateItem<T,J>> onUpdateFunction) {
		this.onUpdateFunction = onUpdateFunction;
		return this;
	}



	public SyncTask<T, I, J> withLoadObjectDetails(OnLoadObjectDetailsFunction<UpdateItemDto<T,I>,UpdateItem<T,J>> onLoadObjectDetailsFunction) {
		this.onLoadObjectDetailsFunction = onLoadObjectDetailsFunction;
		return this;
	}


	public void setBufferSize(Integer bufferSize) {
		this.bufferSize = bufferSize;
	}

	public SyncTask<T, I, J> withBufferSize(Integer bufferSize) {
		this.bufferSize = bufferSize;
		return this;
	}


	public class UpdateItemDto<T, I> {
		public T exsitingItem;
		public I masterItem;
	}

	public class UpdateItem<J, I> {
		public J exsitingItem;
		public I masterItem;
	}

	@FunctionalInterface
	public interface MatchFunction<T, I> {
		Boolean method(T existingItem, I masterItem);
	}

	@FunctionalInterface
	public interface OnDeleteFunction<T> {
		void method(List<T> itemsToDelete);
	}

	@FunctionalInterface
	public interface OnAddFunction<I> {
		void method(Collection<I> itemsToAdd);
	}

	@FunctionalInterface
	public interface OnUpdateFunction<T> {
		void method(List<T> itemsToUpdate);
	}


	@FunctionalInterface
	public interface OnLoadObjectDetailsFunction<T,I> {
		Observable<I> method(List<T> itemsToLoad);
	}

	private Boolean matchesExisting(T domainMatch) {
		for (MatchFunction matchFunction : matchFunctions) {
			for (I apiItem : apiItems) {
				if (matchFunction.method(domainMatch, apiItem)) {
					return true;
				}
			}
		}
		return false;
	}

	private UpdateItemDto<T,I> buildUpdateItemDto(T domainMatch) {
		for (MatchFunction matchFunction : matchFunctions) {
			for (I apiItem : apiItems) {
				if (matchFunction.method(domainMatch, apiItem)) {
					UpdateItemDto<T,I> updateItem = new UpdateItemDto<T,I>();
					updateItem.exsitingItem = domainMatch;
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
		domainRecords.filter((T domainMatch) -> {
			return !matchesExisting(domainMatch);
		})
				.buffer(bufferSize)
				.subscribe((List<T> itemsToDelete) -> {
					this.onDeleteFunction.method(itemsToDelete);
				});

		domainRecords.filter((T domainMatch) -> {
			return matchesExisting(domainMatch);
		}).map( (T domainMatch) -> {
			return buildUpdateItemDto(domainMatch);
		}).buffer(bufferSize).flatMap( (List<UpdateItemDto<T,I>> mapItems) -> {
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
