export default class FileUploader {

	constructor (options) {
		if(!this.supportsDragAndDrop()) {
			console.warn('Drag and Drop is not supported on this browser');
			return;
		}

		if(!options || !options.el) {
			console.warn('No target element provided');
			return;
		}

		this.$isMobile = (typeof window.orientation !== "undefined") || (navigator.userAgent.indexOf('IEMobile') !== -1);
		this.$classPrefix = "govuk-file-uploader";

		this.$options = options;
		this.$options.maxFileSize = options.maxFileSize ||  10485760;
		this.$fileInput = options.el;
		this.$dropArea;
		this.$uploadBtn;
		this.$uploadIcon;
		this.$resetBtn;
		this.$addFileBtn;
		this.$screenAnnouncer;
		
		this.$container = this.renderFileUploader(options.container);
		this.$container.appendChild(this.renderDropArea());
		this.$container.appendChild(this.$screenAnnouncer);

		this.$imageMimeTypes = "image/jpeg, image/gif, image/png";

		this.$DROPAREA_STATE = {
			LOADING: this.$classPrefix + '--loading',
			ACTIVE: this.$classPrefix + '--active',
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
		this.$fileInput.addEventListener('change', this.selectFile);

		// Only register this event if multiple file uploads is enabled
		if(this.$fileInput.multiple && this.$addFileBtn) {
			this.$addFileBtn.addEventListener('click', this.uploadBtnClick);
		}
		
		// If device is mobile or tablet then we do not want to 
		// register drag and drop events
		if(!this.$isMobile) {
			this.$dropArea.addEventListener('drop', this.selectFile);
			
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
		this.makeScreenAnnouncement('files removed');

		setTimeout(() => this.$uploadBtn.focus(), 1350);
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
		if(file.type === "" || this.$fileInput.accept.indexOf(file.type) < 0) {
			this.makeScreenAnnouncement('Incorrect file type uploaded');
		} else if(this.$options.maxFileSize > 10485760){
			this.makeScreenAnnouncement('Uploaded file too large');
		} else {
			return true;
		}

		this.$container.classList.remove(this.$DROPAREA_STATE.ACTIVE);
		this.fireLifeCycleEvent('uploadRejected');
		return false;
    }

	beginFileUpload(file) {
		const xhr = new XMLHttpRequest();
		xhr.open('POST', this.$options.uploadPath, true);
		xhr.responseType = "json";
		this.$container.classList.add(this.$DROPAREA_STATE.LOADING);

		xhr.addEventListener('readystatechange', (e) => {

			if (xhr.readyState == 4 && xhr.status == 200) {
				if(xhr.response && xhr.response.success) {
					this.makeScreenAnnouncement("File uploaded: " + file.fileName);
					this.fireLifeCycleEvent('uploaded', xhr.response);
					this.$screenAnnouncer.focus();
				} else {
					this.fireLifeCycleEvent('uploadError', xhr.response);
				}

				this.$container.classList.remove(this.$DROPAREA_STATE.LOADING);
				this.$container.classList.remove(this.$DROPAREA_STATE.ACTIVE);
			}
		});

		const formData = new FormData();
		formData.append(this.$fileInput.name, file, file.name);
		this.fireLifeCycleEvent('beforeUpload', file, formData);

		xhr.send(formData);
	}

	renderFileUploader(container) {
		container.classList.add(this.$classPrefix);

		const legacy = document.createElement("div");
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