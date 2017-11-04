function fileDialogComplete(numFilesSelected, numFilesQueued) {
    try {
        this.startUpload();
    } catch (ex) {
        alert("111111");
        this.debug(ex);
    }
}

function fileDialogComplete2(numFilesSelected, numFilesQueued) {
    try {
        this.startUpload();
    } catch (ex) {
        this.debug(ex);
    }
}

function fileDialogComplete3(numFilesSelected, numFilesQueued) {
    try {
        this.startUpload();
    } catch (ex) {
        this.debug(ex);
    }
}


function fileDialogComplete4(numFilesSelected, numFilesQueued) {
    try {
        this.startUpload();
    } catch (ex) {
        this.debug(ex);
    }
}

function uploadError(file, code, message) {
    try {
        if (code == '-110') {
            alert("上传的图片太大");
            return;
        }

    } catch (ex) {

        alert("上传失败");
        alert(message);
    }
}


function uploadStart(file) {
    $('#loading').css('display', '');
}

function uploadStart2(file) {
    $('#loading2').css('display', '');
}

function uploadStart3(file) {
    $('#loading3').css('display', '');
}
function uploadStart4(file) {
    $('#loading4').css('display', '');
}

function uploadSuccess(file, serverData) {

    try {
        var jsonData = eval('(' + serverData + ')');
        if (jsonData.status_code == "1") {
            var largeLogoSrc = genImgDomain(jsonData.result[0], DOMAIN);
            $('#menu_pic').attr('src', largeLogoSrc);
            $('#input_menu_pic').val(largeLogoSrc);
        } else {
            if (jsonData.msg == 'token_faild') {
                alert('登录失败');
            } else {
                if (jsonData.msg == '') {
                    alert('上传失败');
                } else {
                    alert(jsonData.msg);
                }
            }
        }
    } catch (ex) {
        this.debug(ex);
    }
}

function jietuSuccess(file, serverData) {

    try {
        var jsonData = eval('(' + serverData + ')');
        if (jsonData.status_code == "1") {
            var largeLogoSrc = genImgDomain(jsonData.result[0], DOMAIN);
            $('#menu_pic').attr('src', largeLogoSrc);

            var corpObj = this.customSettings.corpObj;
            setTimeout(function () {
                corpObj.setImage(largeLogoSrc);
            }, 200)


            $('#input_menu_pic').val(largeLogoSrc);
        } else {
            if (jsonData.msg == 'token_faild') {
                alert('登录失败');
            } else {
                if (jsonData.msg == '') {
                    alert('上传失败');
                } else {
                    alert(jsonData.msg);
                }
            }
        }
    } catch (ex) {
        this.debug(ex);
    }
}

function uploadSuccess2(file, serverData) {

    try {
        var jsonData = eval('(' + serverData + ')');
        if (jsonData.status_code == "1") {
            var largeLogoSrc = genImgDomain(jsonData.result[0], DOMAIN);
            $('#menu_pic2').attr('src', largeLogoSrc);
            $('#input_menu_pic2').val(largeLogoSrc);
        } else {
            if (jsonData.msg == 'token_faild') {
                alert('登录失败');
            } else {
                if (jsonData.msg == '') {
                    alert('上传失败');
                } else {
                    alert(jsonData.msg);
                }
            }
        }
    } catch (ex) {
        this.debug(ex);
    }
}

function uploadSuccess3(file, serverData) {

    try {
        var jsonData = eval('(' + serverData + ')');
        if (jsonData.status_code == "1") {
            var largeLogoSrc = genImgDomain(jsonData.result[0], DOMAIN);
            $('#menu_pic3').attr('src', largeLogoSrc);
            $('#input_menu_pic3').val(largeLogoSrc);
        } else {
            if (jsonData.msg == 'token_faild') {
                alert('登录失败');
            } else {
                if (jsonData.msg == '') {
                    alert('上传失败');
                } else {
                    alert(jsonData.msg);
                }
            }
        }
    } catch (ex) {
        this.debug(ex);
    }
}


function uploadSuccess4(file, serverData) {

    try {
        var jsonData = eval('(' + serverData + ')');
        if (jsonData.status_code == "1") {
            var largeLogoSrc = genImgDomain(jsonData.result[0], DOMAIN);
            $('#menu_pic4').attr('src', largeLogoSrc);
            $('#input_menu_pic4').val(largeLogoSrc);
        } else {
            if (jsonData.msg == 'token_faild') {
                alert('登录失败');
            } else {
                if (jsonData.msg == '') {
                    alert('上传失败');
                } else {
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
            $('#loading').css('display', 'none');
        }
    } catch (ex) {
        this.debug(ex);
    }
}


function uploadComplete2(file) {
    try {
        if (this.getStats().files_queued <= 0) {
            $('#loading2').css('display', 'none');
        }
    } catch (ex) {
        this.debug(ex);
    }
}

function uploadComplete3(file) {
    try {
        if (this.getStats().files_queued <= 0) {
            $('#loading3').css('display', 'none');
        }
    } catch (ex) {
        this.debug(ex);
    }
}

function uploadComplete4(file) {
    try {
        if (this.getStats().files_queued <= 0) {
            $('#loading4').css('display', 'none');
        }
    } catch (ex) {
        this.debug(ex);
    }
}




function createJCropArea(option) {
    var option = $.extend({}, {}, option);

    for (var ckey in option.corp) {
        $('#' + option.sectionid).append(
            $('<button name="selectbutton" />').append(ckey).click(function () {
                var key = $(this).text();
                option.corpclickHandler(option.corp[key], option.corpObj)
            })
        );
    }

    $('#' + option.areaId).show();
}


function showCoords(c) {
    $('#x1').val(c.x);
    $('#y1').val(c.y);
    $('#x2').val(c.x2);
    $('#y2').val(c.y2);
    $('#w').val(c.w);
    $('#h').val(c.h);
}


function clearCoords() {
    $('#coords input').val('');
}


function createSwfUploadObj(option) {
    var settings = {
        upload_url: urlUpload + "/json/upload/qiniu",
        post_params: {
            "at": "joymeplatform",
            "filetype": "original"
        },

        // File Upload Settings
        file_size_limit: "1 MB",    // 2MB
        file_types: "*.jpg;*.png;*.gif",
        file_types_description: "请选择图片",
        file_queue_limit: 1,

        file_queue_error_handler: option.uploadError,
        file_dialog_complete_handler: option.fileDialogComplete,
        upload_start_handler: option.uploadStart,
        upload_success_handler: option.uploadSuccess,
        upload_complete_handler: option.uploadComplete,

        // Button Settings
        button_image_url: "/static/images/uploadbutton.png",
        button_placeholder_id: option.buttonId,
        button_width: 61,
        button_height: 22,
        moving_average_history_size: 40,

        // Flash Settings
        flash_url: "/static/include/swfupload/swfupload.swf",
        flash9_url: "/static/include/swfupload/swfupload_fp9.swf",

        custom_settings: {imgId: option.imgId, corpObj: option.corpObj},

        // Debug Settings
        debug: false };
    var swfu = new SWFUpload(settings);

    return swfu;
}