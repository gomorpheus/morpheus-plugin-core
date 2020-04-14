package com.morpheusdata.model;

public enum Status {
	pending,
	denied,
	cancelled,
	provisioning,
	finishing, //Used if there are instance post processing tasks
	failed,
	resizing,
	running,
	warning,
	stopped,
	suspended,
	removing,
	restarting,
	cloning,
	restoring,
	stopping,
	starting,
	suspending,
	pendingRemoval,
	available,
	unknown
}
