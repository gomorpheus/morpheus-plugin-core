HandlebarsCustom.registerHelper('i18n', function(code, params, defaultString) {
	if (params === undefined) {
		params = [];
	}
	if (!Array.isArray(params)) {
		params = [params];
	}
  return $L({code:code, params:params, default: defaultString})
});