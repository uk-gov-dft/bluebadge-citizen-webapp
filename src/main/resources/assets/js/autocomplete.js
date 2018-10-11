import accessibleAutocomplete from 'accessible-autocomplete';

export default (el) => {
   if (el) {
      accessibleAutocomplete.enhanceSelectElement({
         selectElement: el,
      });
   }
};
