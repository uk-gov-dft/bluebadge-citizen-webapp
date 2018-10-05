import All from 'govuk-frontend/all';
import AutoComplete from './autocomplete';
import GAClickTracker from './ga-tracker';

All.initAll();
GAClickTracker();
AutoComplete.init('councilShortCode');
