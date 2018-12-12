import FileUploader from './file-upload';

export default class DFT_FileUploader {

	constructor () {

		if(!this.supportsDragAndDrop) {
			return;
		}

		this.$classPrefix = 'dft-fu';

		return Array.from(document.getElementsByClassName(this.$classPrefix))
			.map(container => this.init(container));
	}

	init(container) {

		this.$dftFuContainer = container;
		this.$input = document.getElementsByClassName('dft-fu-file-upload').item(0);
		this.$previewHolder = this.getChildElement('-preview__holder');
		this.$resetButton = this.getChildElement('__reset-btn');
		this.$errorSummaryBody = this.getChildElement('-error-summary__body');
		this.$showOnSuccessElements = Array.from(document.querySelectorAll('[data-file-uploader-show-on-success]'));
		this.$addMore = this.getChildElement('__add-file-button');

		this.$generalErrorMessage = this.getDataAttrValue('error-label') || 'File could not be uploaded';
		this.$uploadRejectErrorMessage = this.getDataAttrValue('error-label-reject') || this.$generalErrorMessage;

		this.$state = {
			preview: this.$classPrefix + '--preview',
			error: this.$classPrefix + '--error',
		}

		if (this.$resetButton) {
			this.resetButtonClick = this.resetButtonClick.bind(this);
			this.$resetButton.addEventListener('click', this.resetButtonClick);
		}

		const options = {
			el: this.$input,
			container: document.getElementsByClassName('dft-fu-file-uploader').item(0),
			uploadPath: this.getDataAttrValue('ajax-request-url'),

			// Life cycle methods
			beforeUpload: this.beforeUpload.bind(this),
			uploaded: this.uploaded.bind(this),
			uploadError: this.uploadError.bind(this),
			uploadRejected: this.uploadRejected.bind(this),
		}

		this.$fu = new FileUploader(options);

		if (this.$addMore) {
			this.$addMore.addEventListener('click', this.$fu.uploadBtnClick);
		}

	}

	getChildElement(classSuffixName) {
		const els = this.$dftFuContainer.getElementsByClassName(this.$classPrefix + classSuffixName);
		return els.length > 0 ? els.item(0) : null;
	}
	
	beforeUpload(file, formData) {
		const csrfTokenField = document.querySelectorAll('input[name="_csrf"]');
		if (csrfTokenField.length > 0) {
			formData.append('_csrf', csrfTokenField.item(0).value);
		}
		
		if (this.$previewHolder) {
			this.$previewHolder.innerHTML = '';
		}
	}
	
	uploaded(response) {
		if(response.success) {
			const previewItem = this.createFilePreview(response.artifact);
			this.$previewHolder.appendChild(previewItem);
			this.$dftFuContainer.classList.add(this.$state.preview);
			this.$dftFuContainer.classList.remove(this.$state.error);
			// this.$showOnSuccessElements.forEach(el => el.classList.add('show'));
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
		this.$dftFuContainer.classList.remove(this.$state.preview);
		this.$dftFuContainer.classList.add(this.$state.error);
		this.$errorSummaryBody.innerText = errorMessage;
	}
	
	resetButtonClick(event) {
		this.$fu.resetFileSelection(event);
		this.$previewHolder.innerHTML = '';
		this.$dftFuContainer.classList.remove(this.$state.preview);
		this.$dftFuContainer.classList.remove(this.$state.error);
		this.$showOnSuccessElements.forEach(el => el.classList.remove('show'));
	}

	createFilePreview(response) {
		const previewItem = document.createElement('div');
		previewItem.classList.add('dft-fu-preview__item');

		if(response.type === 'image') {
			const img = document.createElement('img');
			img.src = response.signedUrl;
			img.alt = response.fileName;
			previewItem.appendChild(img);
		} else {
			const span = document.createElement('span');
			span.innerText = '(Preview unavailable)';

			previewItem.classList.add('dft-fu-preview__item--unavailable');
			previewItem.innerText = response.fileName;
			
			previewItem.appendChild(span);
		}

		return previewItem;
	}

	getDataAttrValue(key) {
		if(this.$dftFuContainer){
			return this.$dftFuContainer.getAttribute(`data-${key}`);
		}
		return null;
	}

	supportsDragAndDrop(){
		const div = document.createElement('div');
		return ('draggable' in div) || ('ondragstart' in div && 'ondrop' in div);
  }

}
