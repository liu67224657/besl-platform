define(function(require, exports, module) {
    var $ = require('../../js/common/jquery-1.5.2');
    var joymealert = require('../../js/common/joymealert');
    var postImg = require('../../js/page/post-image');
    var common = require('../../js/common/common');
    var handler = {
        fileQueueError:function (file, errorCode, message) {
            try {
                var imageName = "error.gif";
                var errorName = "";
                if (errorCode === SWFUpload.QUEUE_LIMIT_EXCEEDED) {
                    errorName = "You have attempted to queue too many files.";
                }

                if (errorName !== "") {
                    joymealert.alert({text:errorName + "fileQueueError"});
                    return;
                }

                switch (errorCode) {
                    case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
//                        imageName = "zerobyte.gif";
                        break;
                    case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
//                        imageName = "toobig.gif";
                        this.cancelQueue();
                        $("#uploadErrorText").text("您的图片超过8M，请重新选择").show();
//                        "您的图片超过8M，请重新选择"
                        break;
                    case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
                    case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
                    default:
                        // joymealert.alert({text:"超出一次最多上传图片张数"});
                        break;
                }
            } catch (ex) {
                swfu.debug(ex);
            }
        },

        fileDialogComplete:function (numFilesSelected, numFilesQueued) {
            try {
                if (numFilesSelected == 0) {

                } else if (numFilesSelected > 40) {
                    $("#uploadErrorText").text("您的图片超过40张，请重新选择").show();
//                    joymealert.alert({text:"请不要选择超过40张图片"});
                } else {
                    if (numFilesQueued != 0) {
                        $("#ul_preview").undelegate('li');
                        $.each($(".edittool a"), function() {
                            $('#' + $(this).attr('id')).die();
                        });
                        $("#relPhotoDialog").die();
                        window.isOut = false;
                        window.swfu.startUpload();
                    }
                }
            } catch (ex) {
                swfu.debug(ex);
            }
        },

        uploadStart:function () {
            if (checkLocalImageNumber(swfu)) {
                $("#uploadErrorText").hide();//隐藏错误提示文字
                beforePostPhoto(imgLocaL);
            }
        },

        uploadSuccess:function (file, serverData) {
            try {
                var jsonData = eval('(' + serverData + ')');
                if (jsonData.status_code == "1") {
                    var ssimg = common.genImgDomain(jsonData.result[0].ss, swfu.customSettings.resource_path);

                    postImg.postLocalPhoto(imgLocaL, ssimg, file.name, jsonData.result[0]);
                    imgLocaL++;
                } else {
                    if (jsonData.msg == 'token_faild') {
                        window.location.href = "/loginpage?reurl=" + encodeURIComponent(window.location.href);
                    } else {
                        joymealert.alert({text:"上传失败"});
                        postImg.canclePhoto(imgLocaL);
                    }
                }
            } catch (ex) {
                swfu.debug(ex);
            }
        },

        uploadComplete:function (file) {
            try {
                if (swfu.getStats().files_queued > 0) {
                    swfu.startUpload();
                    return true;
                } else {
                    $("#post_chat_submit").removeAttr("disabled");
                    if ($("#ul_preview li").size() > 0) {
                        $("#post_chat_submit").attr('class','publishon')
                        $("#edit_chat_submit").attr('class','publishon')
                    }
                    window.swfu.customSettings.uploadCallback();
                    return false;
                }
            } catch (ex) {
                alert(ex)
                swfu.debug(ex);
            }
        },

//        fileQueued:function (file) {
//            try {
//                this.customSettings.tdFilesQueued.innerHTML = this.getStats().files_queued;
//            } catch (ex) {
//                this.debug(ex);
//            }
//
//        },

        uploadError:function (file, errorCode, message) {
            try {
                switch (errorCode) {
                    case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
                        break;
                    case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
                        swfu.cancelQueue();
                        break;
                    case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
                        swfu.cancelQueue();
                        break;
                    case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
                        swfu.cancelQueue();
                        break;
                    case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
                        swfu.cancelQueue();
                        break;
                    default:
                        alert(message);
                        break;
                }
            } catch (ex3) {
                swfu.debug(ex3);
            }
        }
    }
    /* ******************************************
     *	FileProgress Object
     *	Control object for displaying file info
     * ****************************************** */
    var FileProgress = function () {
        this.fileProgressID = imgLocaL;
        this.fileProgressWrapper = document.getElementById("preview_photo_" + this.fileProgressID);
        if (!swfu.fileProgressWrapper) {
            this.fileProgressWrapper = document.getElementById("preview_photo_" + this.fileProgressID);
        }

        this.height = this.fileProgressWrapper.offsetHeight;

    }

    var beforePostPhoto = function (imgLinkidx) {
        $("#ul_preview").append('<li id="preview_photo_' + imgLinkidx + '">' +
                '<span class="upload"></span>' +
                '上传中...' +
                '<a href="javascript:void(0)" name="chat_upload_image_loading">取消</a>' +
                '</li>');

        if ($("#rel_preview").is(":hidden")) {
            $("#rel_preview").show();
        }
        $("#relPhotoDialog").css({"top": $("#pic_more").offset().top + 31 + "px"});
        $("#post_chat_submit").attr("disabled", "disabled")
    }
    var checkLocalImageNumber = function (swf) {
        if (!imageUploadLimit(blogContent.image.length)) {
            joymealert.alert({text:"只能上传40张图片"});
            swf.cancelQueue();
            return false;
        }
        return true;
    }
    var imageUploadLimit = function (currImageSize) {
        if (currImageSize >= 40) {
            return false;
        }
        return true;
    }

    return handler;
})
