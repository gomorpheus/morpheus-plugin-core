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

import java.io.IOException;
import java.io.InputStream;

public class ProgressInputStream extends InputStream {

	private InputStream sourceStream;
	private Long counter = 0l;
	private Long totalCount;
	private ProgressUpdater progressUpdater;
	private Integer totalFiles;
	private Integer currentFile;

	public ProgressInputStream(InputStream sourceStream, Long totalCount, Integer totalFiles, Integer currentFile) {
		this.sourceStream = sourceStream;
		this.totalCount = totalCount;
		if(this.totalFiles == null) {
			this.totalFiles = 1;
		}
		this.currentFile = currentFile;
		if(this.currentFile == null) {
			this.currentFile = 0;
		}
		this.totalFiles = totalFiles;
		progressUpdater = new ProgressUpdater(1000);
		progressUpdater.start();
	}

	public ProgressInputStream(InputStream sourceStream, Long totalCount, Integer totalFiles, Integer currentFile, String updateStr) {
		this.sourceStream = sourceStream;
		this.totalCount = totalCount;
		this.currentFile = currentFile;
		if(this.currentFile == null) {
			this.currentFile = 0;
		}
		this.totalFiles = totalFiles;
		if(this.totalFiles == null) {
			this.totalFiles = 1;
		}
		progressUpdater = new ProgressUpdater(1000, updateStr);
		progressUpdater.start();
	}

	@Override
	public int read() throws IOException {
		int c = sourceStream.read();
		counter++;
		if(counter > 0 && (counter % 1000000) == 0)
			updateProgress();
		return c;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int c = sourceStream.read(b, off, len);
		counter += c;
		if(counter > 0)
			updateProgress();
		return c;
	}

	void setProgressCallback(Closure progressCallback) {
		this.progressUpdater.setProgressCallback(progressCallback);
	}

	void consume() throws IOException {
		while(read() != -1) {

		}
	}

	Long getOffset() {
		return counter;
	}

	public void close() throws IOException {
		if(sourceStream != null) {
			sourceStream.close();
		}
		super.close();
		if(progressUpdater != null) {
			progressUpdater.interrupt();
		}
	}

	public void updateProgress() {
		if(totalCount <= 0) {
			return;
		}
		Integer progressPercent = (int)(((counter * 100) / (totalCount * totalFiles)) + (100 * currentFile / totalFiles));

		if(progressUpdater != null) {
			progressUpdater.setPercent(progressPercent);
		}
	}

}
