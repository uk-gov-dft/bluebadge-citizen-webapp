require('@babel/polyfill');

import All from 'govuk-frontend/all';
import AutoComplete from './autocomplete';
import GAClickTracker from './ga-tracker';
import FileUploader from "./file-upload";

All.initAll();

GAClickTracker();

window.onload = () => {
    initFileUloader();
}

const isBrowser_IE = () => {
    const ua = window.navigator.userAgent;
    const msie = ua.indexOf("MSIE ");

    if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./)) {
        return true;
    }

    return false;
}

const initFileUloader = () => {

    // uncomment this code to stop support for IE browsers.
    // Make sure to recompile js code using `gulp js` command
    /*if(isBrowser_IE()){
        return;
    }*/

    const el = document.querySelectorAll('input[type="file"]').item(0);
    const container = document.getElementById("file-uploader-container");
    const continueBtn = document.getElementById("proveIdentity-continue-btn");
    const fileUploaderErrorBox = document.getElementsByClassName('file-uploader-error').item(0);

    if(el && container) {
        new FileUploader({
            el: document.querySelectorAll('input[type="file"]').item(0),
            container: container,
            uploadPath: "http://localhost:8780/prove-identity-ajax",
            reset: () => {
                continueBtn.style.display = 'none';
            },
            uploaded: () => {
                continueBtn.style.display = 'inline-block';
                fileUploaderErrorBox.classList.remove('file-uploader-error--active');
            },
            uploadError: () => {
                fileUploaderErrorBox.classList.add('file-uploader-error--active');
                fileUploaderErrorBox.focus();
            }
        });
    }
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
