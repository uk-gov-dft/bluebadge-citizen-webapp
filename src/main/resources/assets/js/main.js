require('@babel/polyfill');

import All from 'govuk-frontend/all';
import AutoComplete from './autocomplete';
import GAClickTracker from './ga-tracker';
import DFT_FileUploader from "./dft-file-uploader-wrapper";

All.initAll();

GAClickTracker();

window.onload = () => {
    new DFT_FileUploader();

    const forms = Array.from(document.querySelectorAll('[data-prevent-double-submission]'));
    if(forms.length > 0) {
        forms.forEach(form => preventDoubleSubmission(form));
    }
}

const isBrowser_IE = () => {
    const ua = window.navigator.userAgent;
    const msie = ua.indexOf("MSIE ");

    if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./)) {
        return true;
    }

    return false;
}

const select_autocomplete = Array.from(document.getElementsByClassName('select_autocomplete'));

if (select_autocomplete.length > 0) {
    select_autocomplete.forEach(select => {
            AutoComplete({
                defaultValue: '',
                autoselect: true,
                selectElement: select,
                onConfirm: (query) => {
                    const input = document.getElementById(select.id.replace('-select', ''));
                    const value = input.value;

                    if (value === '' || value === null) {
                        Array.from(select.options).forEach(option => option.selected = false);
                    }

                    const requestedOption = Array.from(select.options).find((option) => {
                        const optionValue = option.innerText && option.innerText.trim();
                        return optionValue === query;
                    });

                    if (requestedOption) {
                        requestedOption.selected = true;
                    }
                },
            });
        }
    )

}
