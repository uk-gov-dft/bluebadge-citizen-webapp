const EVENT_DETAULTS = {
	link_action: 'click',
	link_category: 'Internal link',
	page_action: 'view',
	page_category: 'page',
};

const GAEvent = (action, category, label) => {
	window.gtag('event', action, {
		event_category: category,
		event_label: label,
	});
};

const initLinkTracking = (elements) => {
	for (let i = 0; i < elements.length; i++) {
		const el = elements[i];
		el.addEventListener('click', (event) => {
			const label = el.getAttribute('data-ga-track-link') || event.target.href;
			const action = el.getAttribute('data-ga-track-link-action') || EVENT_DETAULTS.link_action;
			const category = el.getAttribute('data-ga-track-link-category') || EVENT_DETAULTS.link_category;
			GAEvent(action, category, label);
		});
	}
};

const initPageTracking = (page) => {
	const label = page.getAttribute('data-ga-track-page') || document.title;
	const action = page.getAttribute('data-a-track-page-action') || EVENT_DETAULTS.page_action;
	const category = page.getAttribute('data-ga-track-page-category') || EVENT_DETAULTS.page_category;
	GAEvent(action, category, label);
};

export default {
	init: () => {
		const links = document.querySelectorAll('[data-ga-track-link]');
		const htmlEl = document.querySelectorAll('[data-ga-track-page]');

		if (links.length > 0 && window.gtag) {
			initLinkTracking(links);
		}

		if (htmlEl.length > 0 && window.gtag) {
			initPageTracking(htmlEl[0]);
		}
	},
};
