import FileUploader from './file-upload';

export default class DFT_FileUploader {

	constructor () {

		if (!this.supportsDragAndDrop || !window.File) {
			return;
		}

		this.$classPrefix = 'dft-fu';

		return Array.from(document.getElementsByClassName(this.$classPrefix))
			.map(container => this.init(container));
	}

	init(container) {

		this.$dftFuContainer = container;
		
		const topLevel = document.getElementsByClassName('has-file-uploader');
		if(topLevel.length > 0) {
			topLevel.item(0).classList.add('has-file-uploader--activated');
		}

		this.$input = document.getElementsByClassName('dft-fu-file-upload').item(0);
		this.$previewHolder = this.getChildElement('-preview__holder');
		this.$resetButton = this.getChildElement('__reset-btn');
		this.$errorSummary = document.getElementById('dft-fu-error-summary');
		this.$errorSummaryBody = this.getChildElement('-error-summary__body');
		this.$showOnSuccessElements = Array.from(document.querySelectorAll('[data-file-uploader-show-on-success]'));
		this.$showIfHasArtifactsElements = Array.from(document.querySelectorAll('[data-file-uploader-show-if-has-artifacts]'));
		this.$addMore = this.getChildElement('__add-file-btn');
		
		this.$generalErrorMessage = this.getDataAttrValue('upload-error-message') || 'File could not be uploaded';
		this.$uploadRejectErrorMessage = this.getDataAttrValue('upload-reject-error-message') || "File uploaded was of incorrect format or file size has exceeded the limit";

		this.$endPoint = this.getDataAttrValue('ajax-request-url');
		this.$maxFileUploadLimit = this.getDataAttrValue('max-file-upload-limit');
		this.$currentFileCount = this.getDataAttrValue('current-file-count');

		this.$state = {
			preview: this.$classPrefix + '--preview',
			error: this.$classPrefix + '--error',
			loading: this.$classPrefix + '--loading'
		}

		if (this.$resetButton) {
			this.resetButtonClick = this.resetButtonClick.bind(this);
			this.$resetButton.addEventListener('click', this.resetButtonClick);
		}

		this.$options = {
			el: this.$input,
			container: document.getElementsByClassName('dft-fu-file-uploader').item(0),
			uploadPath: this.$endPoint,
			maxFileUploadLimit: this.$maxFileUploadLimit * 1,
			totalFilesUploaded: this.$currentFileCount * 1,

			// Life cycle methods
			beforeUpload: this.beforeUpload.bind(this),
			uploaded: this.uploaded.bind(this),
			uploadError: this.uploadError.bind(this),
		}

		this.$fu = new FileUploader(this.$options);

		if (this.$addMore) {
			this.$addMore.addEventListener('click', this.$fu.uploadBtnClick);
		}

	}

	getChildElement(classSuffixName) {
		const els = this.$dftFuContainer.getElementsByClassName(this.$classPrefix + classSuffixName);
		return els.length > 0 ? els.item(0) : null;
	}
	
	beforeUpload(file, formData) {
		this.$dftFuContainer.classList.add('dft-fu--disabled');
		this.$dftFuContainer.classList.remove(this.$state.error);
		this.$dftFuContainer.classList.add(this.$state.loading);
		this.$fu.makeScreenAnnouncement('Uploading files');

		const csrfTokenField = document.querySelectorAll('input[name="_csrf"]');
		if (csrfTokenField.length > 0) {
			formData.append('_csrf', csrfTokenField.item(0).value);
		}

		if (!this.$input.multiple && this.$previewHolder) {
			this.$previewHolder.innerText = '';
		}
	}
	
	uploaded(response) {
		this.$dftFuContainer.classList.remove('dft-fu--disabled');
		if (response.artifact.constructor === Array) {
			response.artifact.forEach(artifact => {
				const previewItem = this.createFilePreview(artifact);
				this.$previewHolder.appendChild(previewItem);
			});
		} else {
			const previewItem = this.createFilePreview(response.artifact);
			this.$previewHolder.appendChild(previewItem);
		}

		this.clearUploadHistory(false);
		this.$dftFuContainer.classList.add(this.$state.preview);
		this.$dftFuContainer.classList.remove(this.$state.error);
		this.$dftFuContainer.classList.remove(this.$state.loading);
		this.$showOnSuccessElements.forEach(el => el.classList.add('show'));
		this.updateIfHasArtifactElements();
	}

	uploadError(errorCode) {
		this.$dftFuContainer.classList.remove('dft-fu--disabled');
		this.$dftFuContainer.classList.remove(this.$state.loading);
		switch(errorCode) {
			case 'INVALID_FILES_UPLOADED':
			case 'MAX_FILE_UPLOAD_LIMIT_EXCEEDED':
				this.handleError(this.$uploadRejectErrorMessage);
				break;
			default:
				this.handleError(this.$generalErrorMessage);
				break;
		}
	}

	handleError(errorMessage) {
		this.$errorSummaryBody.innerText = errorMessage;
		this.$dftFuContainer.classList.add(this.$state.error);
		this.$dftFuContainer.classList.remove(this.$state.loading);
		this.$showOnSuccessElements.forEach(el => el.classList.remove('show'));
		this.updateIfHasArtifactElements();

		if (this.$errorSummary) {
			this.$errorSummary.focus();
		}
	}

	updateIfHasArtifactElements() {
		this.$showIfHasArtifactsElements.forEach(el => {
			if(this.$fu.$totalFilesUploaded > 0) {
				el.classList.add('show');
			} else {
				el.classList.remove('show');
			}
		});
	}

	
	resetButtonClick(event) {
		this.$fu.resetFileSelection(event);
		this.clearUploadHistory(true);
		this.$previewHolder.innerHTML = '';
		this.$dftFuContainer.classList.remove(this.$state.preview);
		this.$dftFuContainer.classList.remove(this.$state.error);
		this.$dftFuContainer.classList.remove(this.$state.loading);
		this.$showOnSuccessElements.forEach(el => el.classList.remove('show'));
		this.updateIfHasArtifactElements();
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

  clearUploadHistory(flag) {
	  this.$options.uploadPath = `${this.$endPoint}?clear=${flag}`;
	  if (flag) {
		  this.$fu.$totalFilesUploaded = 0;
	  }
  }

}
