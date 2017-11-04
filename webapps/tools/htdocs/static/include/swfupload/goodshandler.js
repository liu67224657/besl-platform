
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

function uploadSuccess(file, serverData) {
    try {
        var jsonData = eval('(' + serverData + ')');
        if (jsonData.status_code == "1") {
            var largeLogoSrc = genImgDomain(jsonData.result[0], DOMAIN);
            $('#img_pic').attr('src', largeLogoSrc);

            $('#input_hid_goodsPic').val(largeLogoSrc);
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

