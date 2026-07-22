package com.morpheusdata.core.action;

public class ActionInfo {

	public String key;
	public String namespace;
	public String name;
	public String messageCode;
	public ActionState state = ActionState.ENABLED;

	public boolean isEnabled() {
		return state == ActionState.ENABLED;
	}


	public enum ActionState {

		ENABLED,
		DISABLED,
		NA

	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public ActionState getState() {
		return state;
	}

	public void setState(ActionState state) {
		this.state = state;
	}
}
