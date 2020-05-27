package com.morpheusdata.model;

import java.util.List;

public class ProvisionApproval {
	public String externalId;
	public List<ItemStatus> itemStatus;

	public static class ItemStatus {
		String status;
		String externalId;
	}
}
