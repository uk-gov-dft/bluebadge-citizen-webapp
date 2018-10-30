import All from 'govuk-frontend/all';
import AutoComplete from './autocomplete';
import GAClickTracker from './ga-tracker';

All.initAll();

GAClickTracker();

const select = document.getElementById("councilShortCode");

if(select) {
    AutoComplete({
        defaultValue: '',
        autoselect: true,
        selectElement: document.getElementById('councilShortCode'),
        onConfirm: (query) => {
            const input = document.getElementById("councilShortCode");
            const value = input && input.value;

            if(value === '' || value === null) {
                Array.from(select.options).forEach(option => option.selected = false);
            }

            const requestedOption = Array.from(select.options).find((option) => ((option.innerText || option.textContent) === query));

            if(requestedOption) {
                requestedOption.selected = true;
            }
        },
    });
}
