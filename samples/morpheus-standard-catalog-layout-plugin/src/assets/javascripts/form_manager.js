//= require handlebars-runtime
//= require_tree templates
//= require i18nHelper


window.optionTypeFormManager = function() {

	var optionTemplate = HandlebarsCustom.templates['plugin-configurable-option'];
	var formGroupTemplate = HandlebarsCustom.templates['plugin-form-group'];
	var optionTypes;
	var options;
	var wrapper;
	var loadUrl;
	var validateUrl;
	var optionValues;
	var formOptionObjects;
	var state = "uninitialized";
	var optionTypeForm;
	var fieldGroups = [];

	var init = function(elem, opts) {
		options = opts || {};
		optionTypes = _.get(opts, 'optionTypes', []);
		wrapper = elem;
		if(wrapper) {
			state = "initialized";
			if(opts.hasOwnProperty('loadUrl'))
				loadUrl = opts.loadUrl;
			if(opts.hasOwnProperty('validateUrl'))
				validateUrl = opts.validateUrl;
			if(opts.hasOwnProperty('optionValues'))
				optionValues = opts.optionValues;
			//load it
			//if(!loadUrl && opts.hasOwnProperty('layoutId')) {
				//set the loadUrl to get rid of the type specific stuff
			//}
			if(loadUrl) {
				loadOptions();
			}
			$(wrapper).on('change', "[type=checkbox]", updateCheckbox);

			elem.off("ajax:error");
			elem.on('ajax:error', function (evt, data) {
				var response = JSON.parse(data);
				showErrors(response.errors);
			});
			elem.off("ajax:fatal");
			elem.on('ajax:fatal', function (evt, data) {
				var response = JSON.parse(data);
				showErrors(response.errors);
			});
		} else {
			console.error("You must have a wrapper element to use this component");
		}
	};
	var updateCheckbox = function(event) {
		var hiddenField = $(event.target).siblings('input[type="hidden"]');
		var isChecked = event.target.checked;
		hiddenField.val(isChecked? 'on' : 'off');
	}

	var build = function(callback) {
		//default is always there as the catchall and is first if not included in the form
		if (!fieldGroups.some(function (group) {return group.code === 'default'})){
			fieldGroups.unshift({code: 'default'})
		}
		_.each(fieldGroups, function (group) {
			group.name = $L({code: group.name});
			if (!group.name) {
				$(wrapper).append("<div group-code=\""  + group.code +  "\"></div>");
			}
			else {
				$(wrapper).append(formGroupTemplate(group))
			}
		})
		var sortedTypes = _.sortBy(optionTypes, 'displayOrder')
		_.each(sortedTypes, function(optionType) {
			// if there is no field group set or it doesnt match a fieldGroup in the form then override the field group to be default
			if (!optionType.fieldGroup || !fieldGroups.some(function (group) {return group.code === optionType.fieldGroup})) {
				optionType.fieldGroup = 'default';
			}
			var obj = {
				optionTypeId: optionType.id,
				name: optionType.fieldContext + '.' + optionType.fieldName,
				selectedItemName: optionType.fieldContext + '.' + optionType.fieldName + '.item',
				errorName:optionType.fieldName,
				context: optionType.fieldContext,
				defaultLabel: optionType.fieldLabel,
				value: optionType.defaultValue,
				defaultValue: optionType.defaultValue,
				required: optionType.required,
				excludeFromSearch: optionType.excludeFromSearch || false,
				hideLock: true,
				fieldClass: optionType.fieldClass,
				dependsOnCode: optionType.dependsOnCode,
				visibleOnCode: optionType.visibleOnCode,
				requireOnCode: optionType.requireOnCode,
				dependsOnValue: optionType.dependsOnValue,
				code: optionType.code || optionType.fieldName || optionType.id,
				id: optionType.id,
				contextualDefault: optionType.contextualDefault,
				optionSource: optionType.optionSource,
				optionList: optionType.optionList,
				noBlank: optionType.noBlank,
				placeHolder: optionType.placeHolder,
				extraParams: {optionTypeId: optionType.id},
				isMulti:_.get(optionType, 'config.multiSelect', false) === "on" || _.get(optionType, 'config.multiSelect', false) === "true",
				suggestionTemplatePath:_.get(optionType, 'config.suggestionTemplatePath', undefined),
				editable: optionType.editable,
				helpBlock: optionType.helpBlock,
				helpBlockFieldCode: optionType.helpBlockFieldCode
			}
			if (optionType.visibleOnCode) {
				obj.fieldClass = 'visible-on-input';
			}
			if (optionType.requireOnCode) {
				obj.fieldClass = 'require-on-input';
			}
			obj.lockName = obj.name;
			if(obj.isMulti === 'on') {
				obj.isMulti = true;
			}
			if(optionType.type != 'hidden') {
				obj.label = optionType.fieldCode || "gomorpheus.label.option." + _.get(optionType,'fieldLabel', "").toLowerCase().replace(/\s/g,'');
			}
			switch(optionType.type) {
				case "hidden":
					obj.isHidden = true;
					obj.hidden = true;
					break;
				case "text":
					obj.isText = true;
					break;
				case "number":
					obj.isNumber = true;
					break;
				case "password":
					obj.isPassword = true;
					break;
				case "typeahead":
					if (!obj.isMulti && Array.isArray(obj.value)) {
						obj.value = obj.value[0];
						obj.values = [obj.value[0]];
					}
					else {
						obj.values = obj.defaultValue;
					}
					obj.isMultiTypeahead = true;
					obj.optionSource = '/options/typeahead';
					obj.configStr = JSON.stringify(obj);
					break;
				case "checkbox":
					obj.isCheckbox = true;
					obj.hiddenName = obj.name.split('.')
					obj.hiddenName[obj.hiddenName.length - 1] = '_' + obj.hiddenName[obj.hiddenName.length - 1];
					obj.hiddenName = obj.hiddenName.join('.');
					obj.isChecked = false;
					obj.checkedValue = 'off';
					if (obj.value == 'on' || obj.value == true) {
						obj.checkedValue = 'on';
						obj.isChecked = true;
					}
					break;
				case "select":
					obj.isSelect = true;
					if(obj.required && obj.value) {
						obj.noBlank = true;
					}
					obj.configStr = JSON.stringify(obj);
					break;
				case "tileSelect":
					obj.isRadio = true;
					obj.fieldClass += ' tile';
					if(obj.required && obj.value) {
						obj.noBlank = true;
					}
					obj.configStr = JSON.stringify(obj);
					break;
				case "radio":
					obj.isRadio = true;
					obj.configStr = JSON.stringify(obj);
					break;
				case "textarea":
					obj.isTextarea = true;
					break;
			}
			var fieldLabel = (optionType.fieldLabel || optionType.code || "");
			optionType.defaultErrorMessage = 'You must enter a ' + fieldLabel.toLowerCase();
			$(wrapper).find('[group-code="' + optionType.fieldGroup + '"]').append(optionTemplate(obj));
		});
		state = "shown";
		if(callback) {
			callback();
		}
		wrapper.trigger("options::loaded");
		formOptionObjects = sortedTypes;
		$(document).trigger('option-form:loaded');
	};

	var update = function(optTypes) {
		clear()
		optionTypes = optTypes;
		build();
	};

	var reload = function () {

	};

	var clear = function () {
		$(wrapper).empty();
		state = "cleared";
	};

	var builOptionTypeHTML = function(optionType) {
		return optionTemplate(optionType)
	};

	var clearOptionValues = function() {
		optionValues = undefined;
	}

	var loadOptions = function() {
		$.ajax({
			url: loadUrl,
			method: 'GET',
			cache: false,
			success: function(response) {
				var configValues = _.get(response, 'optionValues')
				//if current values returned - use them
				if(configValues)
					optionValues = configValues;
				//if we have option values
				if(optionValues) {
					_.each(response.optionTypes, function(option) {
						var externalDefault = _.get(optionValues, option.fieldName);
						if (!externalDefault && option.fieldContext.search('customOptions') != -1) {
							externalDefault = _.get(optionValues, 'customOptions.' + option.fieldName);
						}
						if(externalDefault)
							option.defaultValue = externalDefault;
					});
				}
				update(response.optionTypes);
			},
			error: function(response) {

			}
		});
	}

	var validateOptions = function(validationURL, data) {
		var urlEncOpts = wrapper.closest('form').find('[name^="config.customOptions."]').serialize();
		validationURL = validationURL || '/provisioning/instances/custom-options/' + options.layoutId + "?asJSON=true&co.validate=true&" + urlEncOpts;
		return $.ajax({
			url: validationURL,
			method: 'POST',
			contentType: "application/json; charset=utf-8",
			dataType: "json",
			data: JSON.stringify(data),
			cache: false
		});
	};

	var findFormObject = function(key) {
		return formOptionObjects.find(obj => { return obj.code === key });
	};

	var showErrors = function (errors) {
		//clear old errors
		$(wrapper).find('[error-for]').each(function () {
			var helperBlock = $(this).attr('block-text')
			$(this).empty();
		});
		$(wrapper).find('.has-error').removeClass('has-error');
		//show new errors
		for (var key in errors) {
			var block = $(wrapper).find('[error-for="' + key + '"]')
			block.closest('.form-group').addClass('has-error');
			block.text(errors[key]);
		}
	};

	return {
		init: init,
		reload: reload,
		update: update,
		clear: clear,
		validate: validateOptions,
		build: build,
		findFormObject: findFormObject,
		showErrors: showErrors,
		clearOptionValues: clearOptionValues
	};

};
