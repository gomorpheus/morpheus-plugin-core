package com.morpheusdata.vmware.plugin.utils
import groovy.transform.CompileStatic
import groovy.util.logging.Commons

@Commons
@CompileStatic
class CacheableInputStream extends InputStream {
	
	private InputStream sourceStream
	private File cacheFile
	private OutputStream fileOutputStream
	private Long sourceContentLength
	public static BigDecimal LOW_DISK_THRESHOLD = 85

	CacheableInputStream(InputStream sourceStream, File cacheFile, Long contentLength = null) {
		this.sourceStream = sourceStream
		this.cacheFile = cacheFile
		this.sourceContentLength = contentLength
		BigDecimal spaceUsed = 0

		Boolean skipCache = false
		//we dont cache in prod anymore as it takes up too much space
		return
	}

	@Override
	int read() throws IOException {
		int c = sourceStream.read()
		if(fileOutputStream) {
			fileOutputStream.write(c)
		}
		return c
	}

	@Override
	int read(byte[] b, int off, int len) throws IOException {
		int c = sourceStream.read(b,off,len)
		if(fileOutputStream && c > 0) {
			fileOutputStream.write(b, off, c)
		}
		return c
	}

	void consume() {
		while(read() != -1) {

		}
	}

	void close() {
		if(fileOutputStream) {
			fileOutputStream.flush()
			fileOutputStream.close()
			if(sourceContentLength && sourceContentLength != cacheFile.size()) {
				cacheFile.delete()
			}
		}
		super.close()
	}
}
