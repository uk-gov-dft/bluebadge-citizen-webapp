import All from 'govuk-frontend/all';
import accessibleAutocomplete from 'accessible-autocomplete';

All.initAll();

const el = document.querySelector('#councilShortCode');

if (el) {
   accessibleAutocomplete.enhanceSelectElement({
      selectElement: el,
   });
}
