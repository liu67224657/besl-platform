function fileQueueError(file, errorCode, message) {
    try {
        var imageName = "error.gif";
        var errorName = "";
        if (errorCode === SWFUpload.errorCode_QUEUE_LIMIT_EXCEEDED) {
            errorName = "You have attempted to queue too many files.";
        }

        if (errorName !== "") {
            alert(errorName + "fileQueueError");
            return;
        }

        switch (errorCode) {
            case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
                alert("图片容量为0");
                break;
            case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
                imageName = "toobig.gif";
                alert("图片太大了");
                break;
            case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
            default:
                alert("超出一次最多上传图片张数");
                break;
        }
    } catch (ex) {
        this.debug(ex);
    }

}

function fileDialogComplete(numFilesSelected, numFilesQueued) {
    try {
        this.startUpload();
    } catch (ex) {
        this.debug(ex);
    }
}

function uploadStart(file) {

    $('#loading').css('display','');
}

function uploadProgress(file, bytesLoaded) {
    try {
        var percent = Math.ceil((bytesLoaded / file.size) * 100);
        var progress = new FileProgress(file, this.customSettings.upload_target);
        progress.setProgress(percent);

    } catch (ex) {
        this.debug(ex);
    }
}
function uploadSuccess(file, serverData) {
    try {
        var jsonData = eval('(' + serverData + ')');
        if (jsonData.status_code == "1") {
            var largeLogoSrc = genImgDomain(jsonData.result[0], DOMAIN);
            $('#img_game_logo').attr('src',largeLogoSrc);
            $('#iconURL').val(jsonData.result[0]);
            alert('上传成功');

        } else {
            if (jsonData.msg == 'token_faild') {
                alert('登录失败');
            } else {
                 if(jsonData.msg==''){
                    alert('上传失败');
                }else{
                   alert(jsonData.msg);
                }
            }
        }
    } catch (ex) {
        this.debug(ex);
    }
}

function uploadComplete(file) {
    try {
        if (this.getStats().files_queued <= 0) {
             $('#loading').css('display','none');
        }
    } catch (ex) {
        this.debug(ex);
    }
}

function uploadError(file, errorCode, message) {
    try {
        switch (errorCode) {
            case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
                break;
            case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
                this.cancelQueue();
                break;
            case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
                this.cancelQueue();
                break;
            case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
                this.cancelQueue();
                break;
            case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
                this.cancelQueue();
                break;
            default:
                alert(message);
                break;
        }
    } catch (ex3) {
        this.debug(ex3);
    }
}


/* ******************************************
 *	FileProgress Object
 *	Control object for displaying file info
 * ****************************************** */

function FileProgress(file, targetID) {
    this.fileProgressID = targetID;
    this.fileProgressWrapper = document.getElementById("preview_photo_" + this.fileProgressID);
    if (!this.fileProgressWrapper) {
        this.fileProgressWrapper = document.getElementById("preview_photo_" + this.fileProgressID);
    }

    this.height = this.fileProgressWrapper.offsetHeight;
}


FileProgress.prototype.setStatus = function (status) {

};
