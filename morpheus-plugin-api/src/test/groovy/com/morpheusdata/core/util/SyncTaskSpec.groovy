package com.morpheusdata.core.util

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.annotations.NonNull
import spock.lang.Specification

class SyncTaskSpec extends Specification {

	void "add-update-remove with Strings"() {
		given:
		List itemsAdded = []
		List itemsUpdated = []
		List itemsRemoved = []

		List domainItems = ["abc", "def"]
		List apiItems = ["abc", "ghi"]

		// list of all domain items
		Observable domainObservable = Observable.create(new ObservableOnSubscribe<String>() {
			@Override
			void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
				try {
					List<String> items = domainItems
					for (item in items) {
						emitter.onNext(item)
					}
					emitter.onComplete()
				} catch(Exception e) {
					emitter.onError(e)
				}
			}
		}).doOnError({ throwable -> println(throwable.message) })

		// list of matching domain items to update
		Observable loadDetailsObservable = Observable.create(new ObservableOnSubscribe<String>() {
			@Override
			void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
				try {
					List<String> items = ['abc']
					for (item in items) {
						emitter.onNext(item)
					}
					emitter.onComplete()
				} catch(Exception e) {
					emitter.onError(e)
				}
			}
		}).doOnError({ throwable -> println(throwable.message) })


		when:
		new SyncTask(domainObservable, apiItems)
				.addMatchFunction { String a, String b ->
					a == b
				}
				.onAdd {itemsAdded += it}
				.withLoadObjectDetails { loadDetailsObservable}
				.onUpdate {itemsUpdated += it}
				.onDelete {itemsRemoved += it}
				.start()

		then:
		itemsRemoved == ['def']
		itemsUpdated == ['abc']
		itemsAdded == ['ghi']
	}
	
}
