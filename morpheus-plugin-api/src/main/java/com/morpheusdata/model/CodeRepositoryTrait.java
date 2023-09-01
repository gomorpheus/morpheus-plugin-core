package com.morpheusdata.model;

public class CodeRepositoryTrait extends MorpheusModel {

	protected CodeRepository repository;
	protected CodeRepositoryTraitType type;
	protected String repositoryPath;
	protected String repositoryRef;
	protected String uuid;

	public CodeRepository getRepository() {
		return repository;
	}
	
	public void setRepository(CodeRepository repository) {
		this.repository = repository;
		markDirty("repository", repository);
	}

	public CodeRepositoryTraitType getType() {
		return type;
	}
	
	public void setType(CodeRepositoryTraitType type) {
		this.type = type;
		markDirty("type", type);
	}

	public String getRepositoryPath() {
		return repositoryPath;
	}
	
	public void setRepositoryPath(String repositoryPath) {
		this.repositoryPath = repositoryPath;
		markDirty("repositoryPath", repositoryPath);
	}

	public String getRepositoryRef() {
		return repositoryRef;
	}
	
	public void setRepositoryRef(String repositoryRef) {
		this.repositoryRef = repositoryRef;
		markDirty("repositoryRef", repositoryRef);
	}

	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}

}