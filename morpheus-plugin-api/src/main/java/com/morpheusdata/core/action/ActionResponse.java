package com.morpheusdata.core.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActionResponse {

	/**
	 * The freshly-evaluated state after running the action (e.g. ENABLED -> DISABLED after
	 * "Start Server" completes), so the UI can update the action button
	 */
	public ActionInfo.ActionState state;

	/**
	 * Per-target outcome for bulk execution. For a single-target action this holds one entry.
	 */
	public List<ActionItemResult> results = new ArrayList<>();


	public Map data;

	public ActionInfo.ActionState getState() {
		return state;
	}

	public void setState(ActionInfo.ActionState state) {
		this.state = state;
	}

	public List<ActionItemResult> getResults() {
		return results;
	}

	public void setResults(List<ActionItemResult> results) {
		this.results = results;
	}

	public Map getData() {
		return data;
	}

	public void setData(Map data) {
		this.data = data;
	}
}
