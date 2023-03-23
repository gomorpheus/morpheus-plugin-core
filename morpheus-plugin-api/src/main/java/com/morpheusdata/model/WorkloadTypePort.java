package com.morpheusdata.model;

public class WorkloadTypePort extends MorpheusModel implements IModelCodeName {

	protected String code;
	protected String name;
	protected String shortName;
	protected Integer sortOrder;
	protected Integer internalPort;
	protected Integer externalPort;
	protected Boolean export;
	protected Boolean visible;
	protected Boolean loadBalance;
	protected Boolean link;
	protected Boolean customType;
	protected Boolean primaryPort;
	protected String exportName;
	protected String evarName;
	protected Boolean allowMapping;
	protected Boolean unmanagedEnabled;
	protected String loadBalanceProtocol;
	protected Integer loadBalancePort;

	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getShortName() {
		return shortName;
	}
	
	public void setShortName(String shortName) {
		this.shortName = shortName;
		markDirty("shortName", shortName);
	}

	public Integer getSortOrder() {
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
		markDirty("sortOrder", sortOrder);
	}

	public Integer getInternalPort() {
		return internalPort;
	}
	
	public void setInternalPort(Integer internalPort) {
		this.internalPort = internalPort;
		markDirty("internalPort", internalPort);
	}

	public Integer getExternalPort() {
		return externalPort;
	}
	
	public void setExternalPort(Integer externalPort) {
		this.externalPort = externalPort;
		markDirty("externalPort", externalPort);
	}

	public Boolean getExport() {
		return export;
	}
	
	public void setExport(Boolean export) {
		this.export = export;
		markDirty("export", export);
	}

	public Boolean getVisible() {
		return visible;
	}
	
	public void setVisible(Boolean visible) {
		this.visible = visible;
		markDirty("visible", visible);
	}

	public Boolean getLoadBalance() {
		return loadBalance;
	}
	
	public void setLoadBalance(Boolean loadBalance) {
		this.loadBalance = loadBalance;
		markDirty("loadBalance", loadBalance);
	}

	public Boolean getLink() {
		return link;
	}
	
	public void setLink(Boolean link) {
		this.link = link;
		markDirty("link", link);
	}

	public Boolean getCustomType() {
		return customType;
	}
	
	public void setCustomType(Boolean customType) {
		this.customType = customType;
		markDirty("customType", customType);
	}

	public Boolean getPrimaryPort() {
		return primaryPort;
	}
	
	public void setPrimaryPort(Boolean primaryPort) {
		this.primaryPort = primaryPort;
		markDirty("primaryPort", primaryPort);
	}

	public String getExportName() {
		return exportName;
	}
	
	public void setExportName(String exportName) {
		this.exportName = exportName;
		markDirty("exportName", exportName);
	}

	public String getEvarName() {
		return evarName;
	}
	
	public void setEvarName(String evarName) {
		this.evarName = evarName;
		markDirty("evarName", evarName);
	}

	public Boolean getAllowMapping() {
		return allowMapping;
	}
	
	public void setAllowMapping(Boolean allowMapping) {
		this.allowMapping = allowMapping;
		markDirty("allowMapping", allowMapping);
	}

	public Boolean getUnmanagedEnabled() {
		return unmanagedEnabled;
	}
	
	public void setUnmanagedEnabled(Boolean unmanagedEnabled) {
		this.unmanagedEnabled = unmanagedEnabled;
		markDirty("unmanagedEnabled", unmanagedEnabled);
	}

	public String getLoadBalanceProtocol() {
		return loadBalanceProtocol;
	}
	
	public void setLoadBalanceProtocol(String loadBalanceProtocol) {
		this.loadBalanceProtocol = loadBalanceProtocol;
		markDirty("loadBalanceProtocol", loadBalanceProtocol);
	}

	public Integer getLoadBalancePort() {
		return loadBalancePort;
	}
	
	public void setLoadBalancePort(Integer loadBalancePort) {
		this.loadBalancePort = loadBalancePort;
		markDirty("loadBalancePort", loadBalancePort);
	}

}
