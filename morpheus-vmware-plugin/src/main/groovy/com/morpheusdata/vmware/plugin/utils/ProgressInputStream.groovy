package com.morpheusdata.vmware.plugin.utils
import groovy.transform.CompileStatic

@CompileStatic
class ProgressInputStream extends InputStream {

	private InputStream sourceStream
	private Long counter = 0
	private Long totalCount
	private ProgressUpdater progressUpdater
	private Integer totalFiles
	private Integer currentFile

	ProgressInputStream(InputStream sourceStream, Long totalCount, Integer totalFiles=1, Integer currentFile=0) {
		this.sourceStream = sourceStream
		this.totalCount = totalCount
		this.currentFile = currentFile
		this.totalFiles = totalFiles
		progressUpdater = new ProgressUpdater(1000)
		progressUpdater.start()
	}

	ProgressInputStream(InputStream sourceStream, Long totalCount, Integer totalFiles=1, Integer currentFile=0, String updateStr) {
		this.sourceStream = sourceStream
		this.totalCount = totalCount
		this.currentFile = currentFile
		this.totalFiles = totalFiles
		progressUpdater = new ProgressUpdater(1000, updateStr)
		progressUpdater.start()
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
		int c = sourceStream.read(b, off, len)
		counter += c
		if(counter > 0)
			updateProgress()
		return c
	}

	void setProgressCallback(Closure progressCallback) {
		this.progressUpdater.setProgressCallback(progressCallback)
	}

	void consume() {
		while(read() != -1) {

		}
	}

	Long getOffset() {
		return counter
	}

	void close() {
		if(sourceStream) {
			sourceStream.close()
		}
		super.close()
		if(progressUpdater) {
			progressUpdater.interrupt()
		}
	}

	def updateProgress() {
		if(totalCount <= 0) {
			return
		}
		def progressPercent = (int)(((counter * 100) / (totalCount * totalFiles)) + (100 * currentFile / totalFiles))

		if(progressUpdater) {
			progressUpdater.setPercent(progressPercent)
		}
	}

}
