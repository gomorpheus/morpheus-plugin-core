console.log("Script loaded from AssetPipeline");

// Example of adding the _csrf token to form submit
document.addEventListener("submit", function(e){
	if(e.target.id == 'test-posta') {
		var csrfValue = this.querySelector("meta[name='_csrf']").attributes['content'].value;
		var csrfEl = this.querySelector('form#test-post input');
		csrfEl.value = csrfValue;
	}
});
console.log("Done Script loaded from AssetPipeline");
