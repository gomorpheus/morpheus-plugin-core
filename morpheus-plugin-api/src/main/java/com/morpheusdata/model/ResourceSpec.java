package com.morpheusdata.model;

public class ResourceSpec extends MorpheusModel implements IModelUuidCodeName {

	protected ResourceSpecTemplate template;
	protected String name;
	protected String code;
	protected String category;
	protected String templateContent;
	protected String specContent;
	protected String mappingContent;
	protected String templateParameters;
	protected String resourceName;
	protected String resourcePath;
	protected String resourceType;
	protected String resourceContent;
	protected String resourceVersion = "1";
	protected String sourceType;
	protected String contentType;
	protected String config;

	protected CodeRepository repository;
	protected String repositoryPath;
	protected String repositoryRef;
	//integration
	protected String internalId;
	protected String externalId;
	protected String externalType;
	protected String refType;
	protected Long refId;
	protected String subRefType;
	protected Long subRefId;

	//audit
	protected Boolean isolated = false;
	protected String uuid;

	public ResourceSpecTemplate getTemplate() {
		return template;
	}

	public void setTemplate(ResourceSpecTemplate template) {
		this.template = template;
		markDirty("template", template);
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	public String getTemplateContent() {
		return templateContent;
	}

	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
		markDirty("templateContent", templateContent);
	}

	public String getSpecContent() {
		return specContent;
	}

	public void setSpecContent(String specContent) {
		this.specContent = specContent;
		markDirty("specContent", specContent);
	}

	public String getMappingContent() {
		return mappingContent;
	}

	public void setMappingContent(String mappingContent) {
		this.mappingContent = mappingContent;
		markDirty("mappingContent", mappingContent);
	}

	public String getTemplateParameters() {
		return templateParameters;
	}

	public void setTemplateParameters(String templateParameters) {
		this.templateParameters = templateParameters;
		markDirty("templateParameters", templateParameters);
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
		markDirty("resourceName", resourceName);
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
		markDirty("resourcePath", resourcePath);
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
		markDirty("resourceType", resourceType);
	}

	public String getResourceContent() {
		return resourceContent;
	}

	public void setResourceContent(String resourceContent) {
		this.resourceContent = resourceContent;
		markDirty("resourceContent", resourceContent);
	}

	public String getResourceVersion() {
		return resourceVersion;
	}

	public void setResourceVersion(String resourceVersion) {
		this.resourceVersion = resourceVersion;
		markDirty("resourceVersion", resourceVersion);
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
		markDirty("sourceType", sourceType);
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
		markDirty("contentType", contentType);
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
		markDirty("config", config);
	}

	public CodeRepository getRepository() {
		return repository;
	}

	public void setRepository(CodeRepository repository) {
		this.repository = repository;
		markDirty("repository", repository);
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

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public String getExternalType() {
		return externalType;
	}

	public void setExternalType(String externalType) {
		this.externalType = externalType;
		markDirty("externalType", externalType);
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
		markDirty("refType", refType);
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
		markDirty("refId", refId);
	}

	public String getSubRefType() {
		return subRefType;
	}

	public void setSubRefType(String subRefType) {
		this.subRefType = subRefType;
		markDirty("subRefType", subRefType);
	}

	public Long getSubRefId() {
		return subRefId;
	}

	public void setSubRefId(Long subRefId) {
		this.subRefId = subRefId;
		markDirty("subRefId", subRefId);
	}

	public Boolean getIsolated() {
		return isolated;
	}

	public void setIsolated(Boolean isolated) {
		this.isolated = isolated;
		markDirty("isolated", isolated);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}
}
