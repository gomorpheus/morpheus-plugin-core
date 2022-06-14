package com.morpheusdata.vmware.plugin.utils

import groovy.transform.CompileStatic
import groovy.util.logging.Commons

@Commons
@CompileStatic
class ProgressUpdater extends Thread {
	private int lastPercent=0
	private int progressPercent = 0
	private int updateInterval
	private Closure progressCallback
	private String updateStr

	ProgressUpdater(int updateInterval) {
		this.updateInterval = updateInterval
	}

	ProgressUpdater(int updateInterval, String updateStr) {
		this.updateInterval = updateInterval
		this.updateStr = updateStr
	}

	void run() {
		while (true) {
			try {
				if(progressPercent > lastPercent) {
					this.progressCallback?.call(progressPercent)
					if(updateStr != null && updateStr != '') {
						log.debug("${updateStr}: ${progressPercent}%")
					} else {
						log.debug("stream progress: ${progressPercent}%")
					}
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

	void setProgressCallback(Closure progressCallback) {
		this.progressCallback = progressCallback
	}


	void setPercent(int percent) {
		this.progressPercent = percent
	}

}
