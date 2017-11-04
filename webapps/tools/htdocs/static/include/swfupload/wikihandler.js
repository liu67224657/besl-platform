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
}

function uploadSuccess(file, serverData) {
    try {
        var jsonData = eval('(' + serverData + ')');
        if (jsonData.status_code == "1") {
            var largeLogoSrc = genImgDomain(jsonData.result[0], DOMAIN);
            $('#img_game_logo').attr('src', largeLogoSrc);
            //genju7比例显示
            $('#icon').val(jsonData.result[0].ll);
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

//////////////////////////////////////////////////////
function uploadThumbimgStart(file) {
    $('#loading_thumimg').css('display','');
}

function uploadThumimgSuccess(file, serverData) {
    try {
        var jsonData = eval('(' + serverData + ')');
        if (jsonData.status_code == "1") {
            var largeLogoSrc = genImgDomain(jsonData.result[0], DOMAIN);
            $('#img_thumb_logo').attr('src', largeLogoSrc);
            $('#thumbicon').val(jsonData.result[0]);
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

function uploadThumbimgComplete(file) {
    try {
        if (this.getStats().files_queued <= 0) {
             $('#loading_thumimg').css('display','none');
        }
    } catch (ex) {
        this.debug(ex);
    }
}




