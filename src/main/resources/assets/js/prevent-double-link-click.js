export default (link) => {

	if(!link) {
		return;
	}

	let isClicked = false;

	link.addEventListener('click', event => {
		if(isClicked) {
			event.preventDefault();
			return false;
		}
		isClicked = true;
	});
}