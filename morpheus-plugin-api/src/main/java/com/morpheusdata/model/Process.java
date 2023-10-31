package com.morpheusdata.model;

public class Process extends MorpheusModel {

	public ProcessEvent.ProcessType type;
	public String username;

	public ProcessEvent.ProcessType getType() {
		return type;
	}

	public void setType(ProcessEvent.ProcessType type) {
		this.type = type;
	}
}
