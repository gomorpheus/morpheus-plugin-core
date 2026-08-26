package com.morpheusdata.sample.centralaction;

import com.morpheusdata.core.Plugin;

/**
 * Sample {@link Plugin} that registers a single {@link SampleCentralActionProvider} so a
 * Morpheus Central Service {@code plugin-provider-push-action} can be handled by this plugin
 * without a core release.
 * <p>
 * The provider is registered in {@link #initialize()} via {@link Plugin#registerProvider}, exactly
 * as the sibling {@code SampleVolumeTabPlugin} registers its provider. Core selects the provider by
 * matching the dispatched {@code payload.code} against the provider's {@code getCode()}.
 */
public class SampleCentralActionPlugin extends Plugin {

	@Override
	public String getCode() {
		return "sample-central-action-plugin";
	}

	@Override
	public void initialize() {
		this.setName("Sample Central Action Plugin");
		this.registerProvider(new SampleCentralActionProvider(this, this.morpheus));
	}

	@Override
	public void onDestroy() {
		// nothing to clean up for the sample
	}
}
