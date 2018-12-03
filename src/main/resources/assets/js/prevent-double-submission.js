export default (form) => {

	if(form) {
		let isSubmitted = false;
		const button = form.getElementsByTagName("button");
		
		form.addEventListener('submit', event => {
			event.preventDefault();

			if(isSubmitted) {
				return;
			}

			isSubmitted = true;
			button.length > 0 && (button.item(0).disabled = true);
			form.submit();
		});
	}
}