require('@babel/polyfill');

import All from 'govuk-frontend/all';
import AutoComplete from './autocomplete';
import GAClickTracker from './ga-tracker';
import FileUploader from "./file-upload";

All.initAll();

GAClickTracker();

window.onload = () => {
    initFileUploader();
    preventDoubleSubmit();
}

const isBrowser_IE = () => {
    const ua = window.navigator.userAgent;
    const msie = ua.indexOf("MSIE ");

    if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./)) {
        return true;
    }

    return false;
}

const initFileUploader = () => {

    // uncomment this code to stop support for IE browsers.
    // Make sure to recompile js code using `gulp js` command
    /*if(isBrowser_IE()){
        return;
    }*/

    const el = document.querySelectorAll('input[type="file"]').item(0);
    const container = document.getElementById("proveIdentity-fileUploaderContainer");
    const continueBtn = document.getElementById("proveIdentity-continue-btn");
    const fileUploaderErrorBox = document.getElementsByClassName('file-uploader-error').item(0);

    if(el && container) {
        new FileUploader({
            el: document.querySelectorAll('input[type="file"]').item(0),
            container: container,
            uploadPath: "/prove-identity-ajax",
            reset: () => {
                continueBtn.style.display = 'none';
                fileUploaderErrorBox.classList.remove('file-uploader-error--active');
            },
            uploaded: () => {
                continueBtn.style.display = 'inline-block';
                fileUploaderErrorBox.classList.remove('file-uploader-error--active');
            },
            uploadError: () => {
                fileUploaderErrorBox.classList.remove('file-uploader-error--active');
                fileUploaderErrorBox.classList.remove('file-uploader-error--error');
                fileUploaderErrorBox.classList.remove('file-uploader-error--rejected');
                if(!fileUploaderErrorBox.classList.contains('file-uploader-error--active')) {
                    fileUploaderErrorBox.classList.add('file-uploader-error--active');
                    fileUploaderErrorBox.classList.add('file-uploader-error--error');
                }
                fileUploaderErrorBox.focus();
            },
            uploadRejected: () => {
                fileUploaderErrorBox.classList.remove('file-uploader-error--active');
                fileUploaderErrorBox.classList.remove('file-uploader-error--error');
                fileUploaderErrorBox.classList.remove('file-uploader-error--rejected');
                if(!fileUploaderErrorBox.classList.contains('file-uploader-error--active')) {
                    fileUploaderErrorBox.classList.add('file-uploader-error--active');
                    fileUploaderErrorBox.classList.add('file-uploader-error--rejected');
                }
                fileUploaderErrorBox.focus();
            }
        });
    }
}

const preventDoubleSubmit = () =>{
    var forms = document.getElementsByClassName('prevent_double_form');
    [].forEach.call(forms,  function(f){
        f.addEventListener('submit', event => {
            event.preventDefault();
            f.submit();
            for(var i=0; i<f.length; i++){f[i].disabled = true;}
        });
    });
}

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
