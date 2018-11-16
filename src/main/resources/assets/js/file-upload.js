export default class FileUploader {

	constructor (options) {
		if(!window.FileReader || !window.DragEvent) {
			console.warn('File Uploader Component is not supported on this browser');
			return;
		}

		if(!options || !options.el) {
			console.warn('No target element provided');
			return;
		}

		this.$isMobile = (typeof window.orientation !== "undefined") || (navigator.userAgent.indexOf('IEMobile') !== -1);

		this.$options = options;
		this.$fileInput = options.el;
		this.$dropArea;
		this.$previewHolder;
		this.$uploadBtn;
		this.$uploadIcon;
		this.$resetBtn;
		this.$addFileBtn;
		this.$screenAnnouncer;
		this.$csrfToken;

		const csrfTokenField = document.querySelectorAll('input[name="_csrf"]').item(0);
		if(csrfTokenField) {
			this.$csrfToken = csrfTokenField.value;
		}

		this.$container = this.renderFileUploader(options.container);
		this.$container.appendChild(this.renderPreview());
		this.$container.appendChild(this.renderDropArea());
		this.$container.appendChild(this.$screenAnnouncer);

		this.$imageMimeTypes = "image/jpeg, image/gif, image/png";

		this.$DROPAREA_STATE = {
			LOADING: 'file-uploader--loading',
			ACTIVE: 'file-uploader--active',
			PREVIEW_MODE: 'file-uploader--preview-mode',
		}

		this.registerEvents();
		this.fireLifeCycleEvent('init');
	}

	registerEvents() {
		this.uploadBtnClick = this.uploadBtnClick.bind(this);
		this.resetFileSelection = this.resetFileSelection.bind(this);
		this.selectFile = this.selectFile.bind(this);
		
		this.$uploadBtn.addEventListener('click', this.uploadBtnClick);
		this.$uploadIcon.addEventListener('click', this.uploadBtnClick);
		this.$resetBtn.addEventListener('click', this.resetFileSelection);
		this.$fileInput.addEventListener('change', this.selectFile);

		if(this.$fileInput.multiple && this.$addFileBtn) {
			this.$addFileBtn.addEventListener('click', this.uploadBtnClick);
		}

		if(!this.$isMobile) {
			this.$dropArea.addEventListener('drop', this.selectFile);
			
			['dragenter', 'dragover', 'dragleave', 'drop', document.body].forEach(eventName => {
				this.$dropArea.addEventListener(eventName, event => {
					this.preventDefault(event);

					if(eventName === 'dragenter' ||eventName === 'dragover') {
						this.$dropArea.addEventListener(eventName, () => {
							this.$container.classList.add(this.$DROPAREA_STATE.ACTIVE);
						});
					}

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
		this.$previewHolder.innerHTML = '';
		this.$container.classList.remove(this.$DROPAREA_STATE.PREVIEW_MODE);
		this.fireLifeCycleEvent('reset');
		this.makeScreenAnnouncement('files removed');
	}

	selectFile(event) {
		const files = event.target.files || event.dataTransfer.files;
		
		if(this.$fileInput.multiple && files.length > 0){
			Array.from(files)
				.filter(file => this.validateFile(file))
				.map(file => this.beginFileUpload(file));
		} else if(files.length > 0 && this.validateFile(files.item(0))) {
			this.beginFileUpload(files.item(0));
		}

	}

	validateFile(file) {
		// !file.type.match('image.*') try this instead
		if(this.$fileInput.accept.includes(file.type) && file.size <= 10485760) {
			return file;
		}

		this.makeScreenAnnouncement('Incorrect file type uploaded');
		this.$container.classList.remove(this.$DROPAREA_STATE.ACTIVE);
	}

	beginFileUpload(file) {
		const xhr = new XMLHttpRequest();
		xhr.open('POST', this.$options.uploadPath, true);
		xhr.responseType = "json";
		this.$container.classList.add(this.$DROPAREA_STATE.LOADING);

		xhr.addEventListener('readystatechange', (e) => {

			if (xhr.readyState == 4 && xhr.status == 200) {
				if(xhr.response && xhr.response.success) {
					this.showPreview(xhr.response.artifact);
					this.fireLifeCycleEvent('uploaded');
				} else {
					this.$container.classList.remove(this.$DROPAREA_STATE.ACTIVE);
					this.fireLifeCycleEvent('uploadError');
				}

				this.$container.classList.remove(this.$DROPAREA_STATE.LOADING);
			}
		});

		
		const formData = new FormData();
		formData.append('_csrf', this.$csrfToken);
		formData.append(this.$fileInput.name, file, file.name);
		xhr.send(formData);
	}

	showPreview(response) {
		const filePreview = this.renderFilepreview(response);
		this.$previewHolder.appendChild(filePreview);
		this.$container.classList.remove(this.$DROPAREA_STATE.ACTIVE);

		if(!this.$container.classList.contains(this.$DROPAREA_STATE.PREVIEW_MODE)) {
			this.$container.classList.add(this.$DROPAREA_STATE.PREVIEW_MODE);
		}
	}

	renderFileUploader(container) {
		container.classList.add('file-uplaoder');

		const legacy = document.createElement("div");
		legacy.classList.add('file-uploader__legacy');

		while (container.childNodes.length > 0) {
			legacy.appendChild(container.childNodes[0]);
		}
		
		container.appendChild(legacy);

		this.$screenAnnouncer = document.createElement('p');
		this.$screenAnnouncer.classList.add("file-uploader__announcer");
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

		if(this.$fileInput.multiple) {
			const dropArea_caption = document.createElement('p');
			dropArea_caption.classList.add('drop-area__caption');
			dropArea_caption.innerHTML = this.getDataAttrValue('label-multiFile-caption') || "(You can upload multiple photos)";
			dropArea_instructions.appendChild(dropArea_caption);
		}

		const dropArea_loader = document.createElement('p');
		dropArea_loader.classList.add('drop-area__loader');
		dropArea_loader.innerHTML = this.getDataAttrValue('loader-text') || "Uploading files...";
		dropArea_instructions.appendChild(dropArea_loader);

		const dropArea = document.createElement('div');
		dropArea.classList.add('drop-area');
		dropArea.appendChild(dropArea_iconWrapper);
		dropArea.appendChild(dropArea_instructions);

		this.$dropArea = dropArea;

		return dropArea;
	}

	getUploadIcon() {
		const svg = document.createElementNS("http://www.w3.org/2000/svg", "svg");
		const path = document.createElementNS("http://www.w3.org/2000/svg", "path");
		svg.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xlink", "http://www.w3.org/1999/xlink");

		if (this.$isMobile) {
			svg.setAttribute("viewBox", "0 0 512 512");
			path.setAttribute("d", "M512 144v288c0 26.5-21.5 48-48 48H48c-26.5 0-48-21.5-48-48V144c0-26.5 21.5-48 48-48h88l12.3-32.9c7-18.7 24.9-31.1 44.9-31.1h125.5c20 0 37.9 12.4 44.9 31.1L376 96h88c26.5 0 48 21.5 48 48zM376 288c0-66.2-53.8-120-120-120s-120 53.8-120 120 53.8 120 120 120 120-53.8 120-120zm-32 0c0 48.5-39.5 88-88 88s-88-39.5-88-88 39.5-88 88-88 88 39.5 88 88z");
		} else {
			svg.setAttribute("viewBox", "0 0 32 32");
			path.setAttribute("d", "M16 1l-15 15h9v16h12v-16h9z");
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

		return "Upload photo";
	}

	renderPreview() {
		const previewHolder = document.createElement('div');
		previewHolder.classList.add('preview__holder');
		this.$previewHolder = previewHolder;

		const previewResetBtn = document.createElement('button');
		previewResetBtn.classList.add('preview__resetBtn');
		previewResetBtn.innerText = this.getDataAttrValue('preview-reset-button') || "Use a different photo or scan";
		this.$resetBtn = previewResetBtn;

		const previewHeading = document.createElement('h3');
		previewHeading.innerText = this.getDataAttrValue('preview-title') || "This is your upload";
		previewHeading.classList.add('preview__heading');

		const fileUploaderPreview = document.createElement('div');
		fileUploaderPreview.classList.add('preview');
		fileUploaderPreview.appendChild(previewHeading);
		fileUploaderPreview.appendChild(previewHolder);

		if(this.$fileInput.multiple) {
			const addFileBtn = document.createElement('button');
			addFileBtn.classList.add('preview__addFileBtn');
			addFileBtn.innerText = this.getDataAttrValue('preview-addFile-button') || "Add another photo or scan";
			this.$addFileBtn = addFileBtn;
			fileUploaderPreview.appendChild(addFileBtn);
		}

		fileUploaderPreview.appendChild(previewResetBtn);

		return fileUploaderPreview;
	}

	renderFilepreview(response) {
		const elPreviewItem = document.createElement('div');
		elPreviewItem.classList.add('preview__item');

		if(response.type === "file") {
			const elP = document.createElement('p');
			elP.innerText = response.fileName;

			const elSpan = document.createElement('span');
			elSpan.innerText = "(Preview unavailable)";
			elP.appendChild(elSpan);
			elPreviewItem.appendChild(elP);
		} else {
			const elImg = document.createElement('img');
			elImg.src = response.signedUrl;
			elPreviewItem.appendChild(elImg);
			
			/*const reader = new FileReader();
			reader.readAsDataURL(file);
			reader.onloadend = function() {
				elImg.src = reader.result;
				elPreviewItem.appendChild(elImg);
			}*/
		}

		this.makeScreenAnnouncement("File uploaded: " + response.fileName);

		return elPreviewItem;
	}

	fireLifeCycleEvent(eventName, ...options) {
		if(this.$options && this.$options[eventName] && typeof this.$options[eventName] === 'function') {
			this.$options[eventName].call(options);
		}
	}

	getDataAttrValue(key) {
		if(this.$container){
			return this.$container.getAttribute(`data-${key}`);
		}
		return null;
	}

	makeScreenAnnouncement(announcement) {
		if(this.$screenAnnouncer) {
			this.$screenAnnouncer.innerText = announcement;
		}
	}
	
}