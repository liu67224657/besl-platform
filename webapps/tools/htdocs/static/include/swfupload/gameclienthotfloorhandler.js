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