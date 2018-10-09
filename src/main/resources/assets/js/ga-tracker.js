const EVENT_DETAULTS = {
	action: 'click',
	category: 'Internal link',
};

const createGTagEvent = (link, href) => {
	const label = link.getAttribute('data-ga-track-label');
	const action = link.getAttribute('data-ga-track-action') || EVENT_DETAULTS.action;
	const category = link.getAttribute('data-ga-track-category') || EVENT_DETAULTS.category;

	window.gtag('event', action, {
		event_category: category,
		event_label: label || href,
	});
};

const applyClickEvent = (els) => {
	for (let i = 0; i < els.length; i++) {
		const el = els[i];
		el.addEventListener('click', (event) => {
			createGTagEvent(el, event.target.href);
		});
	}
};

export default {
	init: () => {
		const links = document.querySelectorAll('[data-ga-track]');

		if (links && window.gtag) applyClickEvent(links);
	},
};
