package com.morpheusdata.model.system;

import com.morpheusdata.model.ActionType;
import com.morpheusdata.model.MorpheusModel;
import com.morpheusdata.model.OptionType;
import com.morpheusdata.model.TaskSet;

import java.util.ArrayList;
import java.util.List;

/**
 * This model represents a configurable component type of a {@link com.morpheusdata.model.system.System}.  Examples of
 * component types that make up a system are Bare Metal Hosts, Network Switches, Storage arrays, etc.
 */
public class SystemComponentType extends MorpheusModel {
	protected String name;
	protected String code;
	protected String description;
	protected String category;
	protected Boolean active = true;
	protected List<OptionType> optionTypes = new ArrayList<>();
	protected List<TaskSet> initializeWorkflows = new ArrayList<>();
	protected List<TaskSet> updateWorkflows = new ArrayList<>();
	protected List<ActionType> actions = new ArrayList<>();
	protected Class<? extends MorpheusModel> modelType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		markDirty("name", name);
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		markDirty("code", code);
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		markDirty("description", description);
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		markDirty("category", category);
		this.category = category;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		markDirty("active", active);
		this.active = active;
	}

	public List<OptionType> getOptionTypes() {
		return optionTypes;
	}

	public void setOptionTypes(List<OptionType> optionTypes) {
		markDirty("optionTypes", optionTypes);
		this.optionTypes = optionTypes;
	}

	public List<TaskSet> getInitializeWorkflows() {
		return initializeWorkflows;
	}

	public void setInitializeWorkflows(List<TaskSet> initializeWorkflows) {
		markDirty("initializeWorkflows", initializeWorkflows);
		this.initializeWorkflows = initializeWorkflows;
	}

	public List<TaskSet> getUpdateWorkflows() {
		return updateWorkflows;
	}

	public void setUpdateWorkflows(List<TaskSet> updateWorkflows) {
		markDirty("updateWorkflows", updateWorkflows);
		this.updateWorkflows = updateWorkflows;
	}

	/**
	 * Actions surfaced on components built from this type. Each {@link ActionType} is a stored
	 * definition whose {@code providerCode} joins it to the runtime {@code ActionProvider}
	 * (the provider's {@code key}, or dotted {@code namespace.key}).
	 * @return the action types available on this component type
	 */
	public List<ActionType> getActions() {
		return actions;
	}

	public void setActions(List<ActionType> actions) {
		markDirty("actions", actions);
		this.actions = actions;
	}

	public Class<? extends MorpheusModel> getModelType() {
		return modelType;
	}

	public void setModelType(Class<? extends MorpheusModel> modelType) {
		this.modelType = modelType;
	}
}
