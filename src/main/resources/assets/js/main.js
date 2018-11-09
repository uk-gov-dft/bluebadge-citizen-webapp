require('@babel/polyfill');

import All from 'govuk-frontend/all';
import AutoComplete from './autocomplete';
import GAClickTracker from './ga-tracker';
import FileUploader from "./file-upload";

All.initAll();

GAClickTracker();

window.onload = () => {
    const continueBtn = document.getElementById("continue-btn");

    new FileUploader().init({

        init: () => {
            continueBtn.style.display = 'none';
        },

        uploaded: () => {
            continueBtn.style.display = 'block';
        }
    });
}

const select = document.getElementById('councilShortCode');

if (select) {
    AutoComplete({
        defaultValue: '',
        autoselect: true,
        selectElement: document.getElementById('councilShortCode'),
        onConfirm: (query) => {
            const input = document.getElementById('councilShortCode');
            const value = input && input.value;

            if (value === '' || value === null) {
                Array.from(select.options).forEach(option => option.selected = false);
            }

            const requestedOption = Array.from(select.options).find((option) => {
                return (option.innerText || option.textContent) === query;
            });

            if (requestedOption) {
                requestedOption.selected = true;
            }
        },
    });
}
