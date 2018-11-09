const FileUploader = function () {
	this.$dropArea = document.getElementsByClassName('drop-area').item(0);
	this.$input = document.querySelectorAll('input[type="file"]').item(0);
	this.$upload_btn = document.getElementsByClassName('govuk-button--upload-btn').item(0);
	this.$preview_holder = document.getElementsByClassName('drop-area__preview-holder').item(0);
	this.$preview_reset_link = document.getElementsByClassName('drop-area__preview-reset-link').item(0);

	this.$form_control = this.$input.parentNode;

	this.$image_mime_types = "image/jpeg, image/gif, image/png";

	this.$DROPAREA_STATE = {
		ERROR: 'drop-area--error',
		ACTIVE: 'drop-area--active',
		PREVIEW_MODE: 'drop-area--preview-mode'
	}
};

FileUploader.prototype.init = function(options) {

	if(!window.FileReader || !window.DragEvent) {
		console.warning('File Uploader Component cannot be supported by this browser');
		return;
	}

	// need to support multiple inputs
	const $input = this.$input;
	const $dropArea = this.$dropArea;
	const $DROPAREA_STATE = this.$DROPAREA_STATE;
	const $upload_btn = this.$upload_btn;
	const $preview_holder = this.$preview_holder;
	const $preview_reset_link = this.$preview_reset_link;

	// this.$form_control.classList.add('govuk-visually-hidden');

	if(options && options.init && typeof options.init === 'function') {
		options.init.call($input, $dropArea);
	}

	/**
		Initialize Events
	**/
	/*
	$upload_btn.addEventListener('click', this.buttonClick);
	$preview_reset_link.addEventListener('click', this.cancelSelection);
	$input.addEventListener('change', this.selectFile);
	$dropArea.addEventListener('drop', this.selectFile);

	['dragenter', 'dragover', 'dragleave', 'drop'].forEach();
	['dragenter', 'dragover'].forEach();
	['dragleave', 'drop'].forEach();

	this.buttonClick = function (event) {
		event.preventDefault();
		$input.click();
	}

	this.selectFile = function () {
		Array.from(this.files).forEach(this.renderPreview);
	}

	this.cancelSelection = function (event) {
		event.preventDefault();
		$input.value = '';
		$preview_holder.innerHTML = null;
		this.$dropArea.classList.remove($DROPAREA_STATE.PREVIEW_MODE);
	}

	this.renderPreview = function (file) {

	}
	/// - new code end
	*/

	$input.addEventListener('change', () => {
		Array.from($input.files).forEach(this.render_preview);
	});

	$upload_btn.addEventListener('click', event => {
		event.preventDefault();
		event.stopPropagation();
		$input.click();
	});

	$preview_reset_link.addEventListener('click', (event) => {
		event.preventDefault();
		$input.value = '';
		$preview_holder.innerHTML = null;
		$dropArea.classList.remove($DROPAREA_STATE.PREVIEW_MODE);
	});

	['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
		$dropArea.addEventListener(eventName, event => {
			event.preventDefault();
			event.stopPropagation();
		});
		document.body.addEventListener(eventName, event => {
			event.preventDefault();
			event.stopPropagation();
		});
	});

	['dragenter', 'dragover'].forEach(eventName => {
		$dropArea.addEventListener(eventName, () => $dropArea.classList.add($DROPAREA_STATE.ACTIVE));
	});

	['dragleave', 'drop'].forEach(eventName => {
		$dropArea.addEventListener(eventName, () => $dropArea.classList.remove($DROPAREA_STATE.ACTIVE));
	});
	
	$dropArea.addEventListener('drop', (event) => {
		Array.from(event.dataTransfer.files).forEach(this.render_preview);
	});

	this.render_preview = (file) => {
		const reader = new FileReader();
		reader.readAsDataURL(file);

		reader.onloadend = () => {
			const el_preview_item = document.createElement('div');
			el_preview_item.classList.add('drop-area__preview-item');

			if(this.$image_mime_types.includes(file.type)) {
				const el_img = document.createElement('img');
				
				el_img.src = reader.result;
				el_preview_item.appendChild(el_img);
				$preview_holder.appendChild(el_preview_item);
				$dropArea.classList.add($DROPAREA_STATE.PREVIEW_MODE);
				
				if(options && options.uploaded && typeof options.uploaded === 'function') {
					options.uploaded.call();
				}
			}
		}
	}
}

export default FileUploader;

/*
let dropArea = document.getElementById("drop-area")

// Prevent default drag behaviors
;['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
  dropArea.addEventListener(eventName, preventDefaults, false)   
  document.body.addEventListener(eventName, preventDefaults, false)
})

// Highlight drop area when item is dragged over it
;['dragenter', 'dragover'].forEach(eventName => {
  dropArea.addEventListener(eventName, highlight, false)
})

;['dragleave', 'drop'].forEach(eventName => {
  dropArea.addEventListener(eventName, unhighlight, false)
})

// Handle dropped files
dropArea.addEventListener('drop', handleDrop, false)

function preventDefaults (e) {
  e.preventDefault()
  e.stopPropagation()
}

function highlight(e) {
  dropArea.classList.add('highlight')
}

function unhighlight(e) {
  dropArea.classList.remove('active')
}

function handleDrop(e) {
  var dt = e.dataTransfer
  var files = dt.files

  handleFiles(files)
}

if ($('#dummiesToShow').attr('data-trigger') == "true") {
  $('#dragDropInstructions, #cantUploadDetails, #help-uploading, #drop-area').hide();
  $('#fileUploadedBox, #fileAlreadyUploaded').show();
}

let uploadProgress = []
let progressBar = document.getElementById('progress-bar')

function initializeProgress(numFiles) {
  progressBar.value = 0
  uploadProgress = []

  document.getElementById('dragDropInstructions').classList.add('hidden')
  document.getElementById('progressContainer').classList.remove('hidden')

  if(numFiles === 1) {

  } else {
    $('#thisUploadHeading').text('These are your uploads')
  }

  for(let i = numFiles; i > 0; i--) {
    uploadProgress.push(0)
  }
}

function updateProgress(fileNumber, percent) {
  uploadProgress[fileNumber] = percent
  let total = uploadProgress.reduce((tot, curr) => tot + curr, 0) / uploadProgress.length
  console.debug('update', fileNumber, percent, total)
  progressBar.value = total
}

function handleFiles(files) {
  files = Array.from(files)
  initializeProgress(files.length)
  files.forEach(uploadFile)
  files.forEach(previewFile)
}

function previewFile(file) {
  let reader = new FileReader()
  reader.readAsDataURL(file)
  reader.onloadend = function() {
    let img = document.createElement('img')
    img.src = reader.result
    document.getElementById('fileUploadedBox').classList.remove('hidden')

    if(document.getElementById('help-uploading')) {
      document.getElementById('cantUploadDetails').classList.add('hidden')  
      document.getElementById('help-uploading').classList.add('hidden')
    }
    
    document.getElementById('drop-area').classList.add('hidden')
    
    document.getElementById('gallery').appendChild(img)
    document.getElementById('theNameOfFile').value = "File uploaded: " + file.name
    document.getElementById('theNameOfFile').focus()  
  }
}

function uploadFile(file, i) {
  var url = 'https://api.cloudinary.com/v1_1/henryneves/image/upload'
  var xhr = new XMLHttpRequest()
  var formData = new FormData()
  xhr.open('POST', url, true)
  xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest')

  // Update progress (can be used to show progress indicator)
  xhr.upload.addEventListener("progress", function(e) {
    updateProgress(i, (e.loaded * 100.0 / e.total) || 100)
  })

  xhr.addEventListener('readystatechange', function(e) {
    if (xhr.readyState == 4 && xhr.status == 200) {
      updateProgress(i, 100) // <- Add this
    }
    else if (xhr.readyState == 4 && xhr.status != 200) {
      // Error. Inform the user
    }
  })

  formData.append('upload_preset', 'cswfg8zn')
  formData.append('file', file)
  xhr.send(formData)
}

$('.upload-arrow-container').on('click', function() {
  $('#fileElem').trigger('click');
});
*/
