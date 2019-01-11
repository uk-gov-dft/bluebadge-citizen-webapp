export default () => {
	const $cantUpload = document.querySelector('#cant-upload');
	const $supportingDocsForm = document.querySelector('#supporting-docs-form');

	const handleYesRadioChange = () => {
		$cantUpload.classList.add('show');
		$yesRadio.removeEventListener('change', handleYesRadioChange);
	}

	if ($supportingDocsForm && $cantUpload) {
		const $yesRadio = $supportingDocsForm.querySelector('input[value="true"]');

		if ($yesRadio.checked) {
			$cantUpload.classList.add('show');
		} else {
			$yesRadio.addEventListener('change', handleYesRadioChange);
		}
	}
};
