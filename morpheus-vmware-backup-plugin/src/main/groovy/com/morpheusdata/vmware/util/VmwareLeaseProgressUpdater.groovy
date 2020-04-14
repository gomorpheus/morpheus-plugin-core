package com.morpheusdata.vmware.util

import com.vmware.vim25.mo.HttpNfcLease
import groovy.transform.CompileStatic

@CompileStatic
class VmwareLeaseProgressUpdater extends Thread {
	private HttpNfcLease httpNfcLease = null
	private int progressPercent = 0
	private int lastPercent = 0
	private int updateInterval
	private Closure progressCallback

	VmwareLeaseProgressUpdater(HttpNfcLease httpNfcLease, int updateInterval) {
		this.httpNfcLease = httpNfcLease
		this.updateInterval = updateInterval
	}

	void setProgressCallback(Closure progressCallback) {
		this.progressCallback = progressCallback
	}

	void run() {
		while(true) {
			try {
				httpNfcLease.httpNfcLeaseProgress(progressPercent)
				if(this.progressCallback && progressPercent > lastPercent) {
					this.progressCallback.call(progressPercent)
					lastPercent = progressPercent
				}
				Thread.sleep(updateInterval)
			} catch(InterruptedException ie) {
				break
			} catch(Exception e) {
				throw new RuntimeException(e)
			}
		}
	}

	void setPercent(int percent) {
		this.progressPercent = percent
	}

}
