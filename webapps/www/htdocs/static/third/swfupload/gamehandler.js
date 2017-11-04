define(function(require, exports, module) {
    var $ = require('../../js/common/jquery-1.5.2');
    var joymealert = require('../../js/common/joymealert');
    var common = require('../../js/common/common');
    var handler = {
        fileQueueError:function (file, errorCode, message) {
            try {
                var errorName = "";
                if (errorCode === SWFUpload.QUEUE_LIMIT_EXCEEDED) {
                    errorName = "只能上传一张图片";
                }

                if (errorName !== "") {
                    joymealert.alert({text:errorName + "fileQueueError"});
                    return;
                }

                switch (errorCode) {
                    case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
                        break;
                    case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
                        this.cancelQueue();
                        $("#uploadErrorText").text("您的图片超过3M，请重新选择").show();
                        break;
                    case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
                    case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
                    default:
                        break;
                }
            } catch (ex) {
                window.swfheadImage.debug(ex);
            }
        },

        fileDialogComplete:function (numFilesSelected, numFilesQueued) {
            try {
                if (numFilesSelected > 1) {
                    $("#uploadErrorText").text("您的图片超过40张，请重新选择").show();
                     window.swfheadImage.cancelQueue();
                } else {
                    if (numFilesQueued != 0) {
                        window.swfheadImage.startUpload();
                    }
                }
            } catch (ex) {
                window.swfheadImage.debug(ex);
            }
        },

        uploadStart:function () {
            $('#image_area').show();
        },

        uploadSuccess:function (file, serverData) {
            try {
                var jsonData = eval('(' + serverData + ')');
                if (jsonData.status_code == "1") {
                    var imgSrc = common.genImgDomain(jsonData.result[0], window.swfheadImage.customSettings.resource_path);
                    var html='<input type="hidden" id="headSrc" name="src" value="'+jsonData.result[0]+'" />' +
                            '<em style="color:#55779E">'+window.swfheadImage.customSettings.subfilenameFunction(imgSrc,5)+'</em>' +
                            '<i id="remove_headSrc" style="cursor:pointer; margin-left:4px;">X</i>'
                    $('#image_area').html(html);
                    $('object[id^=SWFUpload_]').hide();
                } else {
                    if (jsonData.msg == 'token_faild') {
                        window.location.href = "/loginpage?reurl=" + encodeURIComponent(window.location.href);
                    } else {
                        joymealert.alert({text:"上传失败"});
                    }
                }
            } catch (ex) {
                window.swfheadImage.debug(ex);
            }
        },

        uploadComplete:function (file) {
//            try {
//                $('object[id^=SWFUpload_]').hide();
//            } catch (ex) {
//                alert(ex)
//                window.swfheadImage.debug(ex);
//            }
        },

        uploadError:function (file, errorCode, message) {
            try {
                switch (errorCode) {
                    case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
                        break;
                    case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
                        window.swfheadImage.cancelQueue();
                        break;
                    case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
                        window.swfheadImage.cancelQueue();
                        break;
                    case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
                        window.swfheadImage.cancelQueue();
                        break;
                    case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
                        window.swfheadImage.cancelQueue();
                        break;
                    default:
                        alert(message);
                        break;
                }
            } catch (ex3) {
                window.swfheadImage.debug(ex3);
            }
        }
    }

    return handler;
})
