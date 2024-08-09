/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
					if(updateStr != null && !updateStr.isEmpty()) {
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

	Closure getProgressCallback() {
		return this.progressCallback;
	}

	void setPercent(int percent) {
		this.progressPercent = percent;
	}

	int getPercent() {
		return this.progressPercent;
	}

}
