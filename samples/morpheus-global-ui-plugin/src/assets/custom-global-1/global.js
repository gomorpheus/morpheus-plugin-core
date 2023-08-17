alert("hello from the Global UI Plugin");
$(function() {
	if($('body').data('page') === 'instances/index') {
		alert("Hello from the instances page")
	}
});
