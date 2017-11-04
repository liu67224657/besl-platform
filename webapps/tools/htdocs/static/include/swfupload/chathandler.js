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

function fileDialogStart() {
    $("#relLinkPhoto,#relMusic,#relVideo,#relTag,#relFriend,#faceShow,#relPhoto").unbind('click').css("color", "#ccc");
}

function fileDialogComplete(numFilesSelected, numFilesQueued) {
    try {
        if (numFilesSelected == 0) {
            upLoadRel();
            $("#relLinkPhoto,#relMusic,#relVideo,#relTag,#relFriend,#faceShow,#relPhoto").css('color', '#fff');
        } else {
            this.startUpload();
        }
    } catch (ex) {
        this.debug(ex);
    }
}

function uploadStart(file) {
    $("#post_chat_submit").attr("class", "rel_but_ds").attr("disabled", true);
    $('#close_chat_image').unbind('click');
    if (checkLocalImageNumber(this)) {
        beforePostPhoto(++imgLocaL);
    }
}

function uploadProgress(file, bytesLoaded) {
    if (!imageUploadLimit(blogContent.image.length)) {
        return false;
    }

    try {
        var percent = Math.ceil((bytesLoaded / file.size) * 100);

        var progress = new FileProgress(file, this.customSettings.upload_target);
        progress.setProgress(percent);
        progress.setStatus(" Uploading...");
        progress.toggleCancel(true, this);
    } catch (ex) {
        this.debug(ex);
    }
}
function uploadSuccess(file, serverData) {
    if (!imageUploadLimit(blogContent.image.length)) {
        return false;
    }

    try {
        var progress = new FileProgress(file, this.customSettings.upload_target);
        var jsonData = eval('(' + serverData + ')');
        if (jsonData.status_code == "1") {
            var ssimg = genImgDomain(jsonData.result[0].ss, this.customSettings.resource_path);
            progress.setStatus(" Upload Success");
            postLocalPhoto(progress.fileProgressID, ssimg, file.name, jsonData.result[0]);
        } else {
            if (jsonData.msg == 'token_faild') {
                window.location.href = "/loginpage?reurl=" + encodeURIComponent(window.location.href);
            } else {
                upLoadRel();
                $("#relLinkPhoto,#relMusic,#relVideo,#relTag,#relFriend,#faceShow,#relPhoto").css('color', '#fff');
                jmAlert(jsonData.msg,false,1000);
                canclePhoto(progress.fileProgressID);
            }
        }
    } catch (ex) {
        this.debug(ex);
    }
}

function uploadComplete(file) {
    try {
        if (this.getStats().files_queued <= 0) {
            $("#post_chat_submit").attr("class", "rel_but").removeAttr("disabled");
            upLoadRel();
            $("#relLinkPhoto,#relMusic,#relVideo,#relTag,#relFriend,#faceShow,#relPhoto").css('color', '#fff');
            $('#close_chat_image').bind('click', function() {
                $(".reldiv").css("display", "none");
            });
            $("#chat_content").focus();
        }
    } catch (ex) {
        this.debug(ex);
    }
}

function uploadError(file, errorCode, message) {
    var imageName = "error.gif";
    var progress = new FileProgress(file, this.customSettings.upload_target);
    canclePhoto(progress.fileProgressID);
    try {
        switch (errorCode) {
            case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
                break;
            case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
                this.cancelQueue();
                recoverPostChat();
                break;
            case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
                this.cancelQueue();
                recoverPostChat();
                break;
            case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
                this.cancelQueue();
                recoverPostChat();
                break;
            case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
                this.cancelQueue();
                recoverPostChat();
                break;
            default:
                alert(message);
                break;
        }
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
    this.fileProgressID = imgLocaL;
    this.fileProgressWrapper = document.getElementById("preview_photo_" + this.fileProgressID);
    if (!this.fileProgressWrapper) {
        this.fileProgressWrapper = document.getElementById("preview_photo_" + this.fileProgressID);
    }

    this.height = this.fileProgressWrapper.offsetHeight;

}


FileProgress.prototype.setStatus = function (status) {
    //this.fileProgressElement.childNodes[2].innerHTML = status;
    //this.fileProgressWrapper.children[2].childNodes[2].childNodes[1].childNodes[0].nodeValue = status;
};


function checkLocalImageNumber(swf) {
    if (!imageUploadLimit(blogContent.image.length)) {
        alert('只能上传20张图片');
        swf.cancelQueue();
        return false;
    }
    return true;
}

function recoverPostChat() {
    $("#post_chat_submit").attr("class", "rel_but").removeAttr("disabled");
    $('#close_chat_image').bind('click', function() {
        $(".reldiv").css("display", "none");
    });
}