export default class FileUploader {

	constructor (options) {
		if(!this.supportsDragAndDrop() || !window.File) {
			console.warn('File Uploader is not supported on this browser');
			return;
		}

		if(!options || !options.el) {
			console.warn('No target element provided');
			return;
		}

		this.$isMobile = (typeof window.orientation !== 'undefined') || (navigator.userAgent.indexOf('IEMobile') !== -1);
		this.$classPrefix = 'govuk-file-uploader';

		this.$options = options;
		this.$options.maxFileSize = parseInt(options.maxFileSize) ||  10485760;
		this.$fileInput = options.el;
		this.$allowMultipleFileUploads = this.$fileInput.multiple;
		this.$maxFileUploadLimit  = parseInt(options.maxFileUploadLimit) || null;
		this.$dropArea;
		this.$uploadBtn;
		this.$uploadIcon;
		this.$resetBtn;
		this.$addFileBtn;
		this.$screenAnnouncer;

		this.$totalFilesUploaded = parseInt(options.totalFilesUploaded) || 0;
		
		this.$container = this.renderFileUploader(options.container);
		this.$container.appendChild(this.renderDropArea());
		this.$container.appendChild(this.$screenAnnouncer);

		this.$imageMimeTypes = 'image/jpeg, image/gif, image/png';

		this.$DROPAREA_STATE = {
			LOADING: this.$classPrefix + '--loading',
			ACTIVE: this.$classPrefix + '--active',
		}

		this.$ANNOUNCEMENTS = {
			rejectedFile: 'File uploaded is too large or of the incorrect type',
			rejectedFiles: 'Files uploaded are too large or of the incorrect type',
			filesUploaded: 'Your files have been uploaded',
			filesRemoved: 'Your files have been removed',
			uploadLimitExceeded: 'You cannot upload more than ' + this.$maxFileUploads + ' files'
		}

		this.registerEvents();
		this.fireLifeCycleEvent('init');
	}

    supportsDragAndDrop(){
        const div = document.createElement('div');
        return ('draggable' in div) || ('ondragstart' in div && 'ondrop' in div);
    }

	registerEvents() {
		this.uploadBtnClick = this.uploadBtnClick.bind(this);
		this.resetFileSelection = this.resetFileSelection.bind(this);
		this.selectFile = this.selectFile.bind(this);
		
		this.$uploadBtn.addEventListener('click', this.uploadBtnClick);
		this.$uploadIcon.addEventListener('click', this.uploadBtnClick);
		this.$fileInput.addEventListener('change', event => this.selectFile(event.target.files), false);

		// Only register this event if multiple file uploads is enabled
		if(this.$allowMultipleFileUploads && this.$addFileBtn) {
			this.$addFileBtn.addEventListener('click', this.uploadBtnClick);
		}
		
		// If device is mobile or tablet then we do not want to 
		// register drag and drop events
		if(!this.$isMobile) {
			this.$dropArea.addEventListener('drop', event => this.selectFile(event.dataTransfer.files));
			
			['dragenter', 'dragover', 'dragleave', 'drop', document.body].forEach(eventName => {
				this.$dropArea.addEventListener(eventName, event => {
					this.preventDefault(event);

					// Adds active state class to the drop area when item is dragged into
					// the drop zone.
					if(eventName === 'dragenter' ||eventName === 'dragover') {
						this.$dropArea.addEventListener(eventName, () => {
							this.$container.classList.add(this.$DROPAREA_STATE.ACTIVE);
						});
					}
					
					// Removes active state class to the drop area when item is dragged out
					// from the drop zone.
					if(eventName === 'dragleave' ||eventName === 'drop') {
						this.$dropArea.addEventListener(eventName, () => {
							this.$container.classList.remove(this.$DROPAREA_STATE.ACTIVE)
						});
					}
				});
			});
		}
	}

	preventDefault(event) {
		if(event) {
			event.preventDefault();
			event.stopPropagation();
		}
	}

	uploadBtnClick(event) {
		event.preventDefault();
		this.$fileInput.click();
	}

	resetFileSelection(event) {
		event.preventDefault();
		this.$fileInput.value = '';
		this.fireLifeCycleEvent('reset');
		this.makeScreenAnnouncement(this.$ANNOUNCEMENTS.filesRemoved);

		setTimeout(() => this.$uploadBtn.focus(), 1350);
	}

	selectFile(files) {
		if (this.$maxFileUploadLimit !== null &&
			files.length > this.$maxFileUploadLimit ||
			this.$totalFilesUploaded >= this.$maxFileUploadLimit ||
			this.$totalFilesUploaded + files.length > this.$maxFileUploadLimit) {
				this.makeScreenAnnouncement(this.$ANNOUNCEMENTS.uploadLimitExceeded);
				this.$fileInput.value = '';
				this.fireLifeCycleEvent('uploadError', 'MAX_FILE_UPLOAD_LIMIT_EXCEEDED');
		} else {

			const validFiles = Array.from(files).filter(file => this.validateFile(file));

			if(validFiles.length !== files.length) {
				this.$container.classList.remove(this.$DROPAREA_STATE.ACTIVE);
				this.makeScreenAnnouncement(this.$allowMultipleFileUploads ? this.$ANNOUNCEMENTS.rejectedFiles : this.$ANNOUNCEMENTS.rejectedFile);
				this.$fileInput.value = '';
				this.fireLifeCycleEvent('uploadError', 'INVALID_FILES_UPLOADED');
			} else {
				this.beginFileUpload(validFiles);
			}
		}
	}

	validateFile(file) {
		if(file.type === '' ||
			this.$fileInput.accept.indexOf(file.type) < 0 ||
			file.size > this.$options.maxFileSize) {
			return null;
		}

		return file;
   }

	beginFileUpload(files) {
		const xhr = new XMLHttpRequest();
		xhr.open('POST', this.$options.uploadPath, true);
		this.$container.classList.add(this.$DROPAREA_STATE.LOADING);

		xhr.addEventListener('readystatechange', (e) => {

			if (xhr.readyState == 4 && xhr.status == 200) {
				const resp = JSON.parse(xhr.response);

				if(resp && resp.success) {
					this.makeScreenAnnouncement(this.$ANNOUNCEMENTS.filesUploaded);
					this.fireLifeCycleEvent('uploaded', resp, files);
					this.$totalFilesUploaded =+ parseInt(files.length);
					this.$screenAnnouncer.focus();
				} else {
					this.fireLifeCycleEvent('uploadError', 'REQUEST_UNSUCCESSFUL');
				}
				
				this.$fileInput.value = '';
				this.$container.classList.remove(this.$DROPAREA_STATE.LOADING);
				this.$container.classList.remove(this.$DROPAREA_STATE.ACTIVE);
			}
		});

		const formData = new FormData();
		files.forEach(file => formData.append(this.$fileInput.name, file));
		this.fireLifeCycleEvent('beforeUpload', files, formData);

		xhr.send(formData);
	}

	renderFileUploader(container) {
		container.classList.add(this.$classPrefix);

		const legacy = document.createElement('div');
		legacy.classList.add(this.$classPrefix + '__legacy');

		while (container.childNodes.length > 0) {
			legacy.appendChild(container.childNodes[0]);
		}
		
		container.appendChild(legacy);

		this.$screenAnnouncer = document.createElement('p');
		this.$screenAnnouncer.classList.add(this.$classPrefix + '__announcer');
		this.$screenAnnouncer.setAttribute('aria-live', 'polite');

		return container;
	}

	renderDropArea() {
		const dropArea_svg = this.getUploadIcon();

		const dropArea_icon = document.createElement('div');
		dropArea_icon.classList.add('drop-area__icon-wrapper');
		dropArea_icon.appendChild(dropArea_svg);

		const dropArea_iconWrapper = document.createElement('div');
		dropArea_iconWrapper.classList.add('drop-area__icon');
		dropArea_iconWrapper.appendChild(dropArea_icon);
		this.$uploadIcon = dropArea_iconWrapper;
			
		const dropArea_button = document.createElement('button');
		dropArea_button.classList.add('drop-area__btn');
		dropArea_button.innerHTML = this.getUploadButtonLabel();

		this.$uploadBtn = dropArea_button;

		const dropArea_instructions = document.createElement('div');
		dropArea_instructions.classList.add('drop-area__instructions');
		dropArea_instructions.appendChild(dropArea_button);

		if(this.$allowMultipleFileUploads) {
			const dropArea_caption = document.createElement('p');
			dropArea_caption.classList.add('drop-area__caption');
			dropArea_caption.innerHTML = this.getDataAttrValue('label-multiFile-caption') || '(You can upload multiple photos)';
			dropArea_instructions.appendChild(dropArea_caption);
		}

		const dropArea_loader = document.createElement('p');
		dropArea_loader.classList.add('drop-area__loader');
		dropArea_loader.innerHTML = this.getDataAttrValue('loader-text') || 'Uploading files...';
		dropArea_instructions.appendChild(dropArea_loader);

		const dropArea = document.createElement('div');
		dropArea.classList.add('drop-area');
		dropArea.appendChild(dropArea_iconWrapper);
		dropArea.appendChild(dropArea_instructions);

		this.$dropArea = dropArea;
		this.$dropArea.id = this.$fileInput.name + "-droparea";

		return dropArea;
	}

	getUploadIcon() {
		const svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
		const path = document.createElementNS('http://www.w3.org/2000/svg', 'path');
		svg.setAttributeNS('http://www.w3.org/2000/xmlns/', 'xmlns:xlink', 'http://www.w3.org/1999/xlink');

		if (this.$isMobile) {
			svg.setAttribute('viewBox', '0 0 512 512');
			path.setAttribute('d', 'M512 144v288c0 26.5-21.5 48-48 48H48c-26.5 0-48-21.5-48-48V144c0-26.5 21.5-48 48-48h88l12.3-32.9c7-18.7 24.9-31.1 44.9-31.1h125.5c20 0 37.9 12.4 44.9 31.1L376 96h88c26.5 0 48 21.5 48 48zM376 288c0-66.2-53.8-120-120-120s-120 53.8-120 120 53.8 120 120 120 120-53.8 120-120zm-32 0c0 48.5-39.5 88-88 88s-88-39.5-88-88 39.5-88 88-88 88 39.5 88 88z');
		} else {
			svg.setAttribute('viewBox', '0 0 32 32');
			path.setAttribute('d', 'M16 1l-15 15h9v16h12v-16h9z');
		}

		svg.appendChild(path);
		return svg;
	}

	getUploadButtonLabel() {
		const mobileLabel = this.getDataAttrValue('label-mobile');
		const desktopLabel = this.getDataAttrValue('label-desktop');

		if(this.$isMobile && mobileLabel) {
			return mobileLabel;
		} else if(desktopLabel) {
			return desktopLabel;
		}

		return 'Upload photo';
	}

    makeScreenAnnouncement(announcement) {
        if(this.$screenAnnouncer) {
            this.$screenAnnouncer.innerText = announcement;
        }
    }

    fireLifeCycleEvent(eventName, ...options) {
		 if(this.$options && this.$options[eventName] && typeof this.$options[eventName] === 'function') {
			 this.$options[eventName](...options);
		}
	}

	getDataAttrValue(key) {
		if(this.$container){
			return this.$container.getAttribute(`data-${key}`);
		}
		return null;
	}
}
