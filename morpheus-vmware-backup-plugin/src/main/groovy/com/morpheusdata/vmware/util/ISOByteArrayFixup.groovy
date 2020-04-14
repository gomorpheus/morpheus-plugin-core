package com.morpheusdata.vmware.util

import com.github.stephenc.javaisotools.sabre.*

class ISOByteArrayFixup implements Fixup {

	// private File file = null
	private SeekableByteArrayOutputStream isoFile = null
	private long position = 0
	private long available = 0
	private boolean closed = false

	ISOByteArrayFixup(SeekableByteArrayOutputStream isoFile, long position, long available) {
		this.isoFile = isoFile
		this.position = position
		this.available = available
	}

	void data(DataReference reference) throws HandlerException {
		InputStream inputStream = null
		byte[] buffer = null
		int bytesRead = 0
		// Test if fixup is still open
		if(!closed) {
			if(reference.getLength() > available) {
				throw new HandlerException("Fixup larger than available space.")
			}
			// Write fixup into file
			try {
				// Move to position in file
				isoFile.seek(this.position)
				// Copy data reference to position
				buffer = new byte[1024]
				inputStream = reference.createInputStream()
				while((bytesRead = inputStream.read(buffer, 0, 1024)) != -1) {
					isoFile.write(buffer, 0, bytesRead)
				}
				// Handle position and available placeholder bytes
				position += reference.getLength()
				available -= reference.getLength()
				isoFile.seek(this.isoFile.toByteArray().length)
				// Close the fixup
				// randomAccessFile.close();
			} catch (FileNotFoundException e) {
				throw new HandlerException(e)
			} catch (IOException e) {
				throw new HandlerException(e)
			}
		}
	}

	Fixup fixup(DataReference reference) throws HandlerException {
		throw new RuntimeException("Cannot yet handle fixup in fixup.")
	}

	long mark() throws HandlerException {
		return this.position
	}

	void close() throws HandlerException {
		try {
			this.closed = true
			// this.randomAccessFile.close();

		} /*catch (IOException e) {
			throw new HandlerException(e);
		} */ finally {

		}
	}

	boolean isClosed() {
		return this.closed
	}

	void addFixupListener(FixupListener listener) throws HandlerException {
		// TODO Auto-generated method stub
	}

	void removeFixupListener(FixupListener listener) throws HandlerException {
		// TODO Auto-generated method stub
	}

	long getPosition() {
		return this.position
	}

	long getAvailable() {
		return this.available
	}
}
