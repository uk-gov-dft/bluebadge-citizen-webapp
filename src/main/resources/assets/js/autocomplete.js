import accessibleAutocomplete from 'accessible-autocomplete';

export default class AutoComplete {
   static init(elementId) {
      const el = document.getElementById(elementId);

      if (el) {
         accessibleAutocomplete.enhanceSelectElement({
            selectElement: el,
         });
      }
   }
}
