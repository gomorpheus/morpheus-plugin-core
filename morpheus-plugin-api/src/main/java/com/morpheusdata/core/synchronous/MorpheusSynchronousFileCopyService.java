package com.morpheusdata.core.synchronous;

import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.response.ServiceResponse;

import java.io.InputStream;

public interface MorpheusSynchronousFileCopyService {

	/**
	 * Copy a file to the target server.
	 * @param server The target server
	 * @param fileName name of the copied file
	 * @param filePath path on the server, including the file name (/some/path/file.txt), to place the file copy.
	 * @param sourceStream source {@link InputStream} to copy to the server
	 * @param contentLength size of the file to be copied
	 * @return {@link ServiceResponse} containing the success status of the copy operation
	 */
	ServiceResponse copyToServer(ComputeServer server, String fileName, String filePath, InputStream sourceStream, Long contentLength);
}
