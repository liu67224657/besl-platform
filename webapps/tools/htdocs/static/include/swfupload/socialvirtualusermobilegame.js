function fileDialogComplete(numFilesSelected, numFilesQueued) {
    try {
        this.startUpload();
    } catch (ex) {
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

function uploadStart(file) {
    $('#loading').css('display', '');
}

function uploadStart2(file) {
    $('#loading2').css('display', '');
}

function uploadSuccess(file, serverData) {
    try {
        var jsonData = eval('(' + serverData + ')');
        if (jsonData.status_code == "1") {
            var largeLogoSrc = genImgDomain(jsonData.result[0].b, DOMAIN);
            $('#picurl1_src').attr('src', largeLogoSrc);
            $('#picurl1').val(largeLogoSrc);
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
            var largeLogoSrc = genImgDomain(jsonData.result[0].b, DOMAIN);
            $('#picurl2_src').attr('src', largeLogoSrc);
            $('#picurl2').val(largeLogoSrc);
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