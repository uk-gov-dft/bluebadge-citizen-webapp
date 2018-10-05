export default () => {
	setTimeout(() => {
		if (!window.ga) {
			console.error('Google Analytics library not found');
			return;
		}

		const links = document.querySelectorAll('[data-ga-track]');

		links.forEach((link) => {
			link.addEventListener('click', (event) => {
				window.ga('send', 'event', {
					eventCategory: 'Outbound Link',
					eventAction: 'direct gov click',
					eventLabel: event.target.href,
					transport: 'beacon',
				});
			});
		});
	}, 1000);
};
