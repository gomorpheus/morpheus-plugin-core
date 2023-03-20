package com.morpheusdata.cloud

import com.morpheusdata.model.OptionType
import com.morpheusdata.core.Plugin

/**
 * An example plugin to demonstrate an implementation of the NetworkProvider
 */
class GooglePlugin extends Plugin {

	@Override
	String getCode() {
		return 'morpheus-google-plugin'
	}

	@Override
	void initialize() {
		this.name = 'Google Plugin'

		// Some example settings for the plugin (not used)
		this.settings << new OptionType(
			name: 'Text Field',
			code: 'google-plugin-text-field',
			fieldName: 'textField',
			displayOrder: 0,
			fieldLabel: 'Text Example',
			required: true,
			inputType: OptionType.InputType.TEXT
		)

		this.settings << new OptionType(
			name: 'Number Field',
			code: 'google-plugin-number-field',
			fieldName: 'numberField',
			displayOrder: 1,
			fieldLabel: 'Number Example',
			inputType: OptionType.InputType.NUMBER
		)

		this.settings << new OptionType(
			name: 'Password Field',
			code: 'google-plugin-password-field',
			fieldName: 'passwordField',
			displayOrder: 2,
			fieldLabel: 'Password Example',
			inputType: OptionType.InputType.PASSWORD
		)

		GoogleCloudProvider cloudProvider = new GoogleCloudProvider(this, morpheus)
		GoogleOptionSourceProvider optionSourceProvider = new GoogleOptionSourceProvider(this, morpheus)
		GoogleNetworkProvider networkProvider = new GoogleNetworkProvider(this, morpheus)

		pluginProviders.put(cloudProvider.code, cloudProvider)
		pluginProviders.put(optionSourceProvider.code, optionSourceProvider)
		pluginProviders.put(networkProvider.code, networkProvider)
	}

	@Override
	void onDestroy() {

	}
}
