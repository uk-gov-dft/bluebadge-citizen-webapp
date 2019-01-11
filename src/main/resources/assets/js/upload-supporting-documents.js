export default () => {
	const $cantUpload = document.querySelector('#cant-upload');
	const $supportingDocsForm = document.querySelector('#supporting-docs-form');

	if ($supportingDocsForm && $cantUpload) {
		const $yesRadio = $supportingDocsForm.querySelector('input[value="true"]');

		const handleYesRadioChange = () => {
			console.log('Run me');
			$cantUpload.classList.add('show');
			$yesRadio.removeEventListener('change', handleYesRadioChange);
		}

		$yesRadio.addEventListener('change', handleYesRadioChange);
	}
};
