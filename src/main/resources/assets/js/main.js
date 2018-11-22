require('@babel/polyfill');

import All from 'govuk-frontend/all';
import AutoComplete from './autocomplete';
import GAClickTracker from './ga-tracker';

All.initAll();

GAClickTracker();

const select_autocomplete = Array.from(document.getElementsByClassName('select_autocomplete'));

if (select_autocomplete.length > 0) {
    select_autocomplete.forEach(select => {
            AutoComplete({
                defaultValue: '',
                autoselect: true,
                selectElement: select,
                onConfirm: (query) => {
                    const trimmedQuery = query.trim();
                    const input = document.getElementById(select.id.replace('-select', ''));
                    const value = input.value;

                    if (value === '' || value === null) {
                        Array.from(select.options).forEach(option => option.selected = false);
                    }

                    const requestedOption = Array.from(select.options).find((option) => {
                        return (option.innerText.trim() || option.textContent.trim()) === trimmedQuery;
                    });

                    if (requestedOption) {
                        requestedOption.selected = true;
                    }
                },
            });
        }
    )

}
