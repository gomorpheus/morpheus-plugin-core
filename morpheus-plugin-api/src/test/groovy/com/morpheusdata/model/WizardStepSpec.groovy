package com.morpheusdata.model

import spock.lang.Specification

class WizardStepSpec extends Specification {

	void "hideStepName defaults to false"() {
		given:
		WizardStep step = new WizardStep()

		expect:
		step.hideStepName == false
	}

	void "setHideStepName marks property dirty"() {
		given:
		WizardStep step = new WizardStep()

		when:
		step.setHideStepName(true)

		then:
		step.hideStepName == true
		step.dirtyProperties.contains("hideStepName")
		step.dirtyPropertyValues.get("hideStepName") == true
	}
}
