package com.morpheusdata.model;

public class FileContent {
    
    String name
	String code
	String fileVersion = '1'
	AccountIntegration integration
	CodeRepository repository
	StorageBucket storageProvider
	String contentType
	String sourceType = 'local' //git, url, storage etc
	String contentPath
	String content
	String contentRef
	Boolean enabled = true
	Boolean editable = true
	String internalId
	String externalId
	String uuid = java.util.UUID.randomUUID()
	//audit
	Date dateCreated
	Date lastUpdated
	String createdBy = 'system'
	String updatedBy = 'system'
    
}
