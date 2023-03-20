package com.morpheusdata.core.util

import groovy.util.logging.Slf4j
import spock.lang.Specification

@Slf4j
class SyncListSpec extends Specification {

	void "add-update-remove with Strings"() {
		given:
		SyncList.MatchFunction matchFunction = { a, b ->
			a == b
		}
		Collection<String> existingItems = ["abc", "def"]
		Collection<String> masterItems = ["abc", "ghi"]

		when:
		SyncList<String, String> syncList = new SyncList(matchFunction)
		SyncList.SyncResult<String, String> result = syncList.buildSyncLists(existingItems, masterItems)

		then:
		result.removeList.size() == 1
		result.removeList[0] == 'def'

		result.updateList.size() == 1
		result.updateList[0].existingItem == 'abc'
		result.updateList[0].masterItem == 'abc'
		result.updateList[0].duplicate == false

		result.addList.size() == 1
		result.addList[0] == 'ghi'
	}

	void "add-update-remove with Strings - no master items"() {
		given:
		SyncList.MatchFunction matchFunction = { a, b ->
			a == b
		}
		Collection<String> existingItems = ["abc", "def"]
		Collection<String> masterItems = []

		when:
		SyncList<String, String> syncList = new SyncList(matchFunction)
		SyncList.SyncResult<String, String> result = syncList.buildSyncLists(existingItems, masterItems)

		then:
		result.removeList.size() == 2
		result.updateList.size() == 0
		result.addList.size() == 0
	}

	void "add-update-remove with Strings - no existing items"() {
		given:
		SyncList.MatchFunction matchFunction = { a, b ->
			a == b
		}
		Collection<String> existingItems = []
		Collection<String> masterItems = ["abc", "def"]

		when:
		SyncList<String, String> syncList = new SyncList(matchFunction)
		SyncList.SyncResult<String, String> result = syncList.buildSyncLists(existingItems, masterItems)

		then:
		result.removeList.size() == 0
		result.updateList.size() == 0
		result.addList.size() == 2
	}

	void "add-update-remove with Strings - Map and String "() {
		given:
		SyncList.MatchFunction matchFunction = { Integer a, Map b ->
			a == b.id
		}
		Collection<Integer> existingItems = [2, 3]
		Collection<Map> masterItems = [[id: 1, name: 'some name'],[id: 2, name: 'another name']]

		when:
		SyncList<Integer, Map> syncList = new SyncList(matchFunction)
		SyncList.SyncResult<Integer, Map> result = syncList.buildSyncLists(existingItems, masterItems)

		then:
		result.removeList.size() == 1
		result.removeList[0] == 3
		result.updateList.size() == 1
		result.updateList[0].existingItem == 2
		result.updateList[0].masterItem.id == 2
		result.addList.size() == 1
		result.addList[0].id == 1
	}
}
