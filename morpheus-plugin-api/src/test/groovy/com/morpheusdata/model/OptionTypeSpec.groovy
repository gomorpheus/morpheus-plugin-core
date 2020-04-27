package com.morpheusdata.model

import spock.lang.Specification

class OptionTypeSpec extends Specification {

	void "InputType toString"() {
		expect:
		str == inputType.toString()

		where:
		str           | inputType
		'text'        | OptionType.InputType.TEXT
		'textarea'    | OptionType.InputType.TEXTAREA
		'number'      | OptionType.InputType.NUMBER
		'checkbox'    | OptionType.InputType.CHECKBOX
		'select'      | OptionType.InputType.SELECT
		'radio'       | OptionType.InputType.RADIO
		'code-editor' | OptionType.InputType.CODE_EDITOR
	}
}
