package com.morpheusdata.model.system;

import com.morpheusdata.model.ActionType;
import com.morpheusdata.model.MorpheusModel;
import com.morpheusdata.model.TaskSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SystemTypeLayout extends MorpheusModel {
	protected String code;
	protected String name;
	protected String description;
	protected String version;
	protected List<SystemComponentType> components = new ArrayList<>();
	protected List<TaskSet> initializeWorkflows = new ArrayList<>();
	protected List<TaskSet> updateWorkflows = new ArrayList<>();
	protected List<ActionType> actions = new ArrayList<>();
	protected Boolean enabled = true;
	protected Boolean importable = false;
	protected Boolean creatable = true;
	protected SystemType systemType;
	@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = com.morpheusdata.model.serializers.ModelAsIdOnlySerializer.class)
	protected com.morpheusdata.model.ConfigurationWorkflow configurationWorkflow;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		markDirty("code", code);
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		markDirty("name", name);
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		markDirty("description", description);
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		markDirty("version", version);
		this.version = version;
	}

	public List<SystemComponentType> getComponents() {
		return components;
	}

	public void setComponents(List<SystemComponentType> components) {
		markDirty("components", components);
		this.components = components;
	}

	public List<TaskSet> getInitializeWorkflows() {
		return initializeWorkflows;
	}

	public void setInitializeWorkflows(List<TaskSet> taskSets) {
		markDirty("initializeWorkflows", taskSets);
		this.initializeWorkflows = taskSets;
	}

	public List<TaskSet> getUpdateWorkflows() {
		return updateWorkflows;
	}

	public void setUpdateWorkflows(List<TaskSet> taskSets) {
		markDirty("updateWorkflows", taskSets);
		this.updateWorkflows = taskSets;
	}

	/**
	 * Actions surfaced on systems built from this layout. Each {@link ActionType} is a stored
	 * definition whose {@code providerCode} joins it to the runtime {@code ActionProvider}
	 * (the provider's {@code key}, or dotted {@code namespace.key}).
	 * @return the action types available on this layout
	 */
	public List<ActionType> getActions() {
		return actions;
	}

	public void setActions(List<ActionType> actions) {
		markDirty("actions", actions);
		this.actions = actions;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		markDirty("enabled", enabled);
		this.enabled = enabled;
	}

	public Boolean getImportable() {
		return importable;
	}

	public void setImportable(Boolean importable) {
		markDirty("importable", importable);
		this.importable = importable;
	}

	public Boolean getCreatable() {
		return creatable;
	}

	public void setCreatable(Boolean creatable) {
		markDirty("creatable", creatable);
		this.creatable = creatable;
	}

	public SystemType getSystemType() {
		return systemType;
	}

	public void setSystemType(SystemType systemType) {
		this.systemType = systemType;
	}

	public com.morpheusdata.model.ConfigurationWorkflow getConfigurationWorkflow() {
		return configurationWorkflow;
	}

	public void setConfigurationWorkflow(com.morpheusdata.model.ConfigurationWorkflow configurationWorkflow) {
		this.configurationWorkflow = configurationWorkflow;
		markDirty("configurationWorkflow", configurationWorkflow);
	}

	public Map<String, List<SystemComponentType>> getGroupedComponents() {
		return this.components.stream()
				.collect(Collectors.groupingBy(type -> type.getCode()));
	}

	public List<SystemComponentType> getComponentsByCode(String code) {
		return this.components.stream()
				.filter(component -> component.getCode().equals(code))
				.collect(Collectors.toList());
	}


}
