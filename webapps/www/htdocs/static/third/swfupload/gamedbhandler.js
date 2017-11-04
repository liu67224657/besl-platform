define(function(require, exports, module) {
    var $ = require('../../js/common/jquery-1.5.2');
    var common = require('../../js/common/common');
    var joymealert = require('../../js/common/joymealert');
    var alertOption = {tipLayer: true, alertFooter: true, alertButtonText: '确 定', timeOutMills: -1,
        width: 400
    };

    var handler = {
        uploadError :function (file, errorCode, message) {
            if (errorCode == SWFUpload.QUEUE_ERROR.INVALID_FILETYPE) {
                alertOption.text = '请确定您上传图片类型是否正确（只支持jpg、png）。';
                alertOption.title = '上传失败';
                joymealert.alert(alertOption);
                return;
            }
            if (errorCode == SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT) {
                alertOption.text = '上传的文件过大，请上传小于2MB的文件。';
                alertOption.title = '上传失败';
                joymealert.alert(alertOption);
                return;
            }
        },
        fileDialogComplete:function (numFilesSelected, numFilesQueued) {
            var piclength = $("img[name='gamepicurl']").size();
            if (piclength > 4) {
                alertOption.text = '最多只能上传5张图片';
                alertOption.title = '上传失败';
                joymealert.alert(alertOption);
                return;
            } else {
                if (numFilesQueued > 0) {
                    this.startUpload();
                } else {
                    alertOption.text = '不要太着急哦，图片正在努力上传';
                    alertOption.title = '上传失败';
                    joymealert.alert(alertOption);
                    return;
                }

            }

        },

        uploadStart:function (file) {
            $("#loading1").css("display", "inline");
        },

        uploadProgress:function (file, bytesLoaded, bytesTotal) {
            try {
                this.customSettings.progressCount++;
                updateDisplay.call(this, file);
            } catch (ex) {
                this.debug(ex);
            }

        },
        uploadSuccess:function (file, serverData) {
            try {
                var jsonData = eval('(' + serverData + ')');
                if (jsonData.status_code == '1') {
                    var imgSrc = common.genImgDomain(jsonData.result[0].b, joyconfig.DOMAIN);
                    if (imgLocaL > 5) {

                    } else {
                        var img = $('<li id="gamepic' + imgLocaL + '"><div><img src="' + imgSrc + '" name="gamepicurl"/><span></span></div><a class="close" href="javascript:void(0)" onclick="deletePic(' + imgLocaL + ')"></a></li>');
                        //$("#gamepic").attr("src", imgSrc);
                        //$("#picurl").val(imgSrc);
                        $("#picdef").css("display", "none");
                        $("#upload").append(img);
                        $("#loading1").css("display", "none");
                    }
                    imgLocaL++;
                } else {
                    alertOption.text = '请确定您上传图片类型是否正确（只支持jpg、png）。';
                    alertOption.title = '上传失败';
                    joymealert.alert(alertOption);
                    $("#loading").css("display", "none");
                    return;
                }
            } catch (ex) {
                this.debug(ex);
            }
        }
    }
    return handler;
})
function deletePic(id) {
    $("#gamepic" + id).remove();
    var piclength = $("img[name='gamepicurl']").size();
    if (piclength < 1) {
        $("#picdef").css("display", "block");
    }
}
