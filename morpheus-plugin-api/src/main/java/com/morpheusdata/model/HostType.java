package com.morpheusdata.model;

public enum HostType {
	container,
	vm,
	bareMetal,
	unmanaged,
	/*
	 * @deprecated use vm instead
	 */
	@Deprecated(since="0.15.12", forRemoval = true)
	server
}
