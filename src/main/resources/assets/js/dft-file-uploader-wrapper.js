import FileUploader from "./file-upload";

export default class DFT_FileUploader {

	constructor () {
		this.$dftFuContainer = document.getElementById("dft-fu-wrapper");
		this.$previewHolder = document.getElementById("dft-fu-wrapper__previewHolder");

		console.log(this.$previewHolder);
	
		this.$errorSummaryBody = document.getElementById("dft-fu-wrapper__error-body");
		this.$generalErrorMessage = this.getDataAttrValue('error-label') || "File could not be uploaded";
		this.$uploadRejectErrorMessage = this.getDataAttrValue('error-label-reject') || this.$generalErrorMessage;

		this.$resetButton = document.getElementById("preview-reset-btn");
		this.$showOnSuccessElements = Array.from(document.querySelectorAll("[data-file-uploader-show-on-success]"));

		if(this.$resetButton) {
			this.resetButtonClick = this.resetButtonClick.bind(this);
			this.$resetButton.addEventListener('click', this.resetButtonClick);
		}

		const el = document.querySelectorAll('input[type="file"]').item(0);

		if (!el || el.type !== "file") {
			console.warn('No target element provided or not of type file');
			return;
		}

		const options = {
			el: el,
			container: document.getElementById("dft-fu-wrapper__fileUploader"),
			uploadPath: "/prove-identity-ajax",

			// Life cycle methods
			beforeUpload: this.beforeUpload.bind(this),
			uploaded: this.uploaded.bind(this),
			uploadError: this.uploadError.bind(this),
			uploadRejected: this.uploadRejected.bind(this),
		}

		this.$fu = new FileUploader(options);
	}
	
	beforeUpload(file, formData) {
		const csrfTokenField = document.querySelectorAll('input[name="_csrf"]');
		if (csrfTokenField.length > 0) {
			formData.append("_csrf", csrfTokenField.item(0).value);
		}
		
		if (this.$previewHolder) {
			this.$previewHolder.innerHTML = "";
		}
	}
	
	uploaded(response) {
		if(response.success) {
			const previewItem = this.createFilePreview(response.artifact);
			this.$previewHolder.appendChild(previewItem);
			this.$dftFuContainer.classList.add("preview-mode");
			this.$dftFuContainer.classList.remove("error-mode");
			this.$showOnSuccessElements.forEach(el => el.classList.add('show'));
		}
	}
	
	uploadError() {
		this.handleError(this.$generalErrorMessage);
		this.$showOnSuccessElements.forEach(el => el.classList.remove('show'));
	}

	uploadRejected() {
		this.handleError(this.$uploadRejectErrorMessage);
		this.$showOnSuccessElements.forEach(el => el.classList.remove('show'));
	}

	handleError(errorMessage) {
		this.$dftFuContainer.classList.remove('preview-mode');
		this.$dftFuContainer.classList.add('error-mode');
		this.$errorSummaryBody.innerText = errorMessage;
	}
	
	resetButtonClick(event) {
		this.$fu.resetFileSelection(event);
		this.$previewHolder.innerHTML = "";
		this.$dftFuContainer.classList.remove("preview-mode");
		this.$dftFuContainer.classList.remove("error-mode");
		this.$showOnSuccessElements.forEach(el => el.classList.remove('show'));
	}

	createFilePreview(response) {
		const previewItem = document.createElement('div');
		previewItem.classList.add('preview__item');

		if(response.type === "image") {
			const img = document.createElement('img');
			img.src = response.signedUrl;
			img.alt = response.fileName;
			previewItem.appendChild(img);
		} else {
			const p = document.createElement('p');
			p.innerText = response.fileName;

			const span = document.createElement('span');
			span.innerText = "(Preview unavailable)";
			
			p.appendChild(span);
			previewItem.appendChild(p);
		}

		return previewItem;
	}

	getDataAttrValue(key) {
		if(this.$dftFuContainer){
			return this.$dftFuContainer.getAttribute(`data-${key}`);
		}
		return null;
	}

}
