import All from 'govuk-frontend/all';
import accessibleAutocomplete from 'accessible-autocomplete';

All.initAll();

const el = document.querySelector('#local-council-autocomplete');

if (el) {
   accessibleAutocomplete.enhanceSelectElement({
      selectElement: el
   });
}