package com.morpheusdata.vmware.util

import groovy.transform.CompileStatic
import groovy.util.logging.Log
import groovy.util.logging.Commons

@Commons
@CompileStatic
class VmwareLeaseProgressInputStream extends InputStream {

	private InputStream sourceStream
	private VmwareLeaseProgressUpdater leaseUpdater
	private Long counter = 0
	private Long totalCount
	private Integer lastPercentComplete
	private Integer totalFiles
	private Integer currentFile

	VmwareLeaseProgressInputStream(InputStream sourceStream, VmwareLeaseProgressUpdater leaseUpdater, Long totalCount, Integer totalFiles=1, Integer currentFile=0) {
		this.sourceStream = sourceStream
		this.leaseUpdater = leaseUpdater
		this.currentFile = currentFile
		this.totalFiles = totalFiles
		this.totalCount = totalCount
	}

	@Override
	int read() throws IOException {
		int c = sourceStream.read()
		counter ++
		if(counter > 0 && (counter % 1000000) == 0)
			updateProgress()
		return c
	}

	@Override
	int read(byte[] b, int off, int len) throws IOException {
		int c = sourceStream.read(b,off,len)
		counter += c
		if(counter > 0)
			updateProgress()
		return c
	}

	void consume() {
		while(read() != -1) {

		}
	}

	void close() {
		super.close()
	}

	def updateProgress() {
		int progressPercent
		if(totalCount <= 0) {
			progressPercent = (int)(100 * currentFile / totalFiles)
			if(progressPercent < 100) {
				progressPercent++
			}
		} else {
			progressPercent = (int)(((counter * 100) / (totalCount * totalFiles)) + (100 * currentFile / totalFiles))
		}

		if(lastPercentComplete == null || progressPercent > lastPercentComplete) {
			log.debug "vmware transfer progress: ${progressPercent}"
			lastPercentComplete = progressPercent
		}
		leaseUpdater?.setPercent(progressPercent)
	}

}
