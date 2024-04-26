package com.morpheusdata.core.util;

import groovy.lang.Closure;
import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
class ProgressUpdater extends Thread {
	private int lastPercent=0;
	private int progressPercent=0;
	private int updateInterval;
	private Closure progressCallback;
	private String updateStr;

	static Logger log = LoggerFactory.getLogger(ProgressUpdater.class);

	ProgressUpdater() {
	}
	ProgressUpdater(int updateInterval) {
		this.updateInterval = updateInterval;
	}

	ProgressUpdater(int updateInterval, String updateStr) {
		this.updateInterval = updateInterval;
		this.updateStr = updateStr;
	}

	public void run() {
		while (true) {
			try {
				if(progressPercent > lastPercent) {
					if(this.progressCallback != null ) {
						this.progressCallback.call(progressPercent);
					}
					if(updateStr != null && updateStr != "") {
						log.debug("{}: {}%", updateStr, progressPercent);
					} else {
						log.debug("stream progress: {}%", progressPercent);
					}
					lastPercent = progressPercent;
				}
				Thread.sleep(updateInterval);
			} catch(InterruptedException ie) {
				break;
			} catch(Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	void setProgressCallback(Closure progressCallback) {
		this.progressCallback = progressCallback;
	}


	void setPercent(int percent) {
		this.progressPercent = percent;
	}

}
