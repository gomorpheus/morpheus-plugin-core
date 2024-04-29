package com.morpheusdata.core.util

import spock.lang.Specification

import groovy.lang.Closure

import java.util.concurrent.CountDownLatch

class ProgressUpdaterSpec extends Specification {

	def "setProgressCallback sets the callback correctly"() {
		given: "A ProgressUpdater instance and a Closure instance"
		Closure closure = { percent -> println("Progress: ${percent}%") }
		ProgressUpdater progressUpdater = new ProgressUpdater()

		when: "setProgressCallback is called"
		progressUpdater.setProgressCallback(closure)

		then: "The progressCallback of the ProgressUpdater should be the same as the Closure instance"
		progressUpdater.getProgressCallback() == closure
	}

	def "setProgressCallback sets the callback correctly"() {
		given: "A ProgressUpdater instance and a mock Consumer"
		def callbackCalled = 0;
		def latch = new CountDownLatch(1)
		ProgressUpdater progressUpdater = new ProgressUpdater(3)

		when: "progressUpdater is started"
		progressUpdater.start()

		and: "setProgressCallback is set"
		progressUpdater.setProgressCallback({ callbackCalled++; latch.countDown() })

		and: "progress is updated"
		progressUpdater.setPercent(50)

		and: "wait for progress callback to be called"
		latch.await()

		then: "The progressCallback of the ProgressUpdater should be called with the correct percent"
		assert callbackCalled == 1

		cleanup:
		progressUpdater.interrupt()
	}
}
