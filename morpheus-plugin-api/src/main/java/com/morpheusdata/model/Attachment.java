package com.morpheusdata.model;

import java.util.List;

public class Attachment extends MorpheusModel {

	String fileName;
	String contentType;
	Long fileSize;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
		markDirty("fileName", fileName);
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
		markDirty("contentType", contentType);
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
		markDirty("fileSize", fileSize);
	}
}
