function preLoad() {
	if (!this.support.loading) {
		if ($.browser.msie && ($.browser.version == "6.0") && !$.support.style) {
            alert("您需要Flash Player的9.028或以上使用SWFUpload.");

        }else{
            alert("您需要Flash Player的9.028或以上使用SWFUpload.");
            return false;
        }
	}
}
function loadFailed() {
	alert("Something went wrong while loading SWFUpload. If this were a real application we'd clean up and then give you an alternative");
}

function fileQueueError(file, errorCode, message) {
	try {
		var imageName = "error.gif";
		var errorName = "";
		if (errorCode === SWFUpload.errorCode_QUEUE_LIMIT_EXCEEDED) {
			errorName = "You have attempted to queue too many files.";
		}

		if (errorName !== "") {
			alert(errorName+"fileQueueError");
			return;
		}

		switch (errorCode) {
		case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
			imageName = "zerobyte.gif";
			break;
		case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
			imageName = "toobig.gif";
			alert("图片太大了");
			break;
		case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
		case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
		default:
			//alert(message+"fileQueueError");
			alert("超出一次最多上传图片张数");
			break;
		}

		addImage("images/" + imageName);

	} catch (ex) {
		this.debug(ex);
	}

}

function fileDialogComplete(numFilesSelected, numFilesQueued) {
	try {
		//if (numFilesQueued > 0) {
		    //this.startResizedUpload(this.getFile(0).ID, 100, 100, SWFUpload.RESIZE_ENCODING.JPEG, 100);
		//}
		this.startUpload();
	} catch (ex) {
		this.debug(ex);
	}
}

function uploadProgress(file, bytesLoaded) {

	try {
		var percent = Math.ceil((bytesLoaded / file.size) * 100);

		var progress = new FileProgress(file,  this.customSettings.upload_target);
		progress.setProgress(file.id,percent);
		progress.setStatus(" Uploading...");
		progress.toggleCancel(true, this);
	} catch (ex) {
		this.debug(ex);
	}
}

function uploadSuccess(file, serverData) {
	try {
		var progress = new FileProgress(file,  this.customSettings.upload_target);
		var jsonData =  eval('(' + serverData + ')');
		if(jsonData.status_code == "1"){
			var simg = genImgDomain(jsonData.result[0].s,this.customSettings.resource_path);
			progress.setStatus(" Upload Success");
            uploadsuc(file.id,simg,jsonData.result[0]);
		}else{
			addImage(this.customSettings.lib_path+"/static/swfupload/images/error.gif");
			progress.setStatus("Error.");
			progress.toggleCancel(false);
			alert(serverData+" Upload Success");
		}


	} catch (ex) {
		this.debug(ex);
	}
}

function uploadComplete(file) {
	try {
		/*  I want the next upload to continue automatically so I'll call startUpload here */
		if (this.getStats().files_queued > 0) {
			//this.startResizedUpload(this.getFile(0).ID, 100, 100, SWFUpload.RESIZE_ENCODING.JPEG, 100);
			var progress = new FileProgress(file,  this.customSettings.upload_target);
			var textArea = progress.setUploadTextArea(file.name);
			if(textArea == "0"){
				this.startUpload();
			}
		} else {
			var progress = new FileProgress(file,  this.customSettings.upload_target);
			progress.setComplete(file.name);
			progress.setStatus(" upload Success ");
			progress.toggleCancel(false);
		}
	} catch (ex) {
		this.debug(ex);
	}
}

function uploadError(file, errorCode, message) {
	var imageName =  "error.gif";
	var progress;
	try {
		switch (errorCode) {
		case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
			try {
				progress = new FileProgress(file,  this.customSettings.upload_target);
				progress.setCancelled();
				progress.toggleCancel(false);
			}
			catch (ex1) {
				this.debug(ex1);
			}
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
			try {
				progress = new FileProgress(file,  this.customSettings.upload_target);
				progress.setCancelled();
				progress.setStatus("Stopped");
				progress.toggleCancel(true);
			}
			catch (ex2) {
				this.debug(ex2);
			}
		case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
			imageName = "uploadlimit.gif";
			break;
		default:
			alert(message);
			break;
		}

		addImage("images/" + imageName);

	} catch (ex3) {
		this.debug(ex3);
	}

}


function addImage(src) {
	var newImg = document.createElement("img");
	newImg.style.margin = "5px";
	document.getElementById("thumbnails").appendChild(newImg);
	if (newImg.filters) {
		try {
			newImg.filters.item("DXImageTransform.Microsoft.Alpha").opacity = 0;
		} catch (e) {
			// If it is not set initially, the browser will throw an error.  This will set it if it is not set yet.
			newImg.style.filter = 'progid:DXImageTransform.Microsoft.Alpha(opacity=' + 0 + ')';
		}
	} else {
		newImg.style.opacity = 0;
	}

	newImg.onload = function () {
		fadeIn(newImg, 0);
	};
	newImg.src = src;
}

function fadeIn(element, opacity) {
	var reduceOpacityBy = 5;
	var rate = 30;	// 15 fps


	if (opacity < 100) {
		opacity += reduceOpacityBy;
		if (opacity > 100) {
			opacity = 100;
		}

		if (element.filters) {
			try {
				element.filters.item("DXImageTransform.Microsoft.Alpha").opacity = opacity;
			} catch (e) {
				// If it is not set initially, the browser will throw an error.  This will set it if it is not set yet.
				element.style.filter = 'progid:DXImageTransform.Microsoft.Alpha(opacity=' + opacity + ')';
			}
		} else {
			element.style.opacity = opacity / 100;
		}
	}

	if (opacity < 100) {
		setTimeout(function () {
			fadeIn(element, opacity);
		}, rate);
	}
}



/* ******************************************
 *	FileProgress Object
 *	Control object for displaying file info
 * ****************************************** */

function FileProgress(file, targetID) {
	this.fileProgressID = file.id;
	this.fileProgressWrapper = document.getElementById(this.fileProgressID);
	if (!this.fileProgressWrapper) {
        textPostPhotoLi(file.name,file.id)
        this.fileProgressWrapper = document.getElementById(this.fileProgressID);

	} else {
		
	}
	this.height = this.fileProgressWrapper.offsetHeight;

}
FileProgress.prototype.setProgress = function (fileid,percentage) {
    updatePress(fileid,percentage+"%");
//	this.fileProgressWrapper.children[2].childNodes[2].childNodes[0].childNodes[0].nodeValue =  percentage + "%";
//	this.fileProgressWrapper.children[2].childNodes[1].childNodes[0].style.width = percentage*5.3+"px";
};
FileProgress.prototype.setComplete = function (filename) {
//    this.fileProgressWrapper.children[0].className = "pic";
//    this.fileProgressWrapper.children[0].childNodes[0].nodeValue = subfilename(filename,5);
//    this.fileProgressWrapper.children[0].title=filename;
//    this.fileProgressWrapper.children[1].className = "x";
//    this.fileProgressWrapper.children[1].childNodes[0].nodeValue = "";
};
FileProgress.prototype.setError = function () {
	this.fileProgressElement.className = "progressContainer red";
	this.fileProgressElement.childNodes[3].className = "progressBarError";
	this.fileProgressElement.childNodes[3].style.width = "";

};
FileProgress.prototype.setCancelled = function () {
	this.fileProgressElement.className = "progressContainer";
	this.fileProgressElement.childNodes[3].className = "progressBarError";
	this.fileProgressElement.childNodes[3].style.width = "";

};
FileProgress.prototype.setStatus = function (status) {
	//this.fileProgressElement.childNodes[2].innerHTML = status;
	//this.fileProgressWrapper.children[2].childNodes[2].childNodes[1].childNodes[0].nodeValue = status;
};

FileProgress.prototype.setUploadingimg = function (imgpath,s,m,b,ss) {
    uploadsuc(this.fileProgressWrapper.id,imgpath,imgMap);
};

FileProgress.prototype.setUploadTextArea = function (filename) {
//    this.fileProgressWrapper.children[0].className = "pic";
//    this.fileProgressWrapper.children[0].childNodes[0].nodeValue = subfilename(filename,5);
//    this.fileProgressWrapper.children[0].title=filename;
//    this.fileProgressWrapper.children[1].className = "x";
//    this.fileProgressWrapper.children[1].childNodes[0].nodeValue = "";
	return "0";
};



FileProgress.prototype.toggleCancel = function (show, swfuploadInstance) {
	this.fileProgressElement.childNodes[0].style.visibility = show ? "visible" : "hidden";
	if (swfuploadInstance) {
		var fileID = this.fileProgressID;
		this.fileProgressElement.childNodes[0].onclick = function () {
			swfuploadInstance.cancelUpload(fileID);
			return false;
		};
	}
};