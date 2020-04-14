package com.morpheusdata.vmware.util


import com.github.stephenc.javaisotools.sabre.*

class ISOImageByteArrayHandler implements StreamHandler {

	private SeekableByteArrayOutputStream content
	private long position = 0

	ISOImageByteArrayHandler() {
		content = new SeekableByteArrayOutputStream()
	}

	void startDocument() throws HandlerException {
		// nothing to do here
	}

	void startElement(Element element) throws HandlerException {
		// nothing to do here
	}

	void data(DataReference reference) throws HandlerException {
		InputStream inputStream = null
		byte[] buffer = null
		int bytesToRead = 0
		int bytesHandled = 0
		int bufferLength = 65535
		long lengthToWrite = 0
		long length = 0
		try {
			buffer = new byte[bufferLength]
			length = reference.getLength()
			lengthToWrite = length
			inputStream = reference.createInputStream()
			while (lengthToWrite > 0) {
				if(lengthToWrite > bufferLength)
					bytesToRead = bufferLength
				else
					bytesToRead = (int)lengthToWrite
				bytesHandled = inputStream.read(buffer, 0, bytesToRead)
				if(bytesHandled == -1)
					throw new HandlerException("Cannot read all data from reference.")
				content.write(buffer, 0, bytesHandled)
				lengthToWrite -= bytesHandled
				position += bytesHandled
			}
		} catch (IOException e) {
			throw new HandlerException(e)
		} finally {
			try {
				if(inputStream != null) {
					inputStream.close()
					inputStream = null
				}
			} catch (IOException e) {
			}
		}
	}

	Fixup fixup(DataReference reference) throws HandlerException {
		Fixup fixup = new ISOByteArrayFixup(content, position, reference.getLength())
		data(reference)
		return fixup
	}

	long mark() throws HandlerException {
		return position
	}

	void endElement() throws HandlerException {
		// nothing to do here
	}

	void endDocument() throws HandlerException {

	}

	OutputStream getOutputStream() {
		return content
	}

	byte[] getByteArray() {
		return content.toByteArray()
	}

}
