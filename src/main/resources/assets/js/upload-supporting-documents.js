export default () => {
	const $cantUpload = document.querySelector('#cant-upload');
	const $supportingDocsForm = document.querySelector('#supporting-docs-form');

	if ($supportingDocsForm && $cantUpload) {
		const $yesRadio = $supportingDocsForm.querySelector('input[value="true"]');

		const handleYesRadioChange = () => {
			$cantUpload.classList.add('show');
			$yesRadio.removeEventListener('change', handleYesRadioChange);
		}

		if ($yesRadio.checked) {
			$cantUpload.classList.add('show');
		} else {
			$yesRadio.addEventListener('change', handleYesRadioChange);
		}
	}
};
