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

function uploadStart(file) {
    $('#loading').css('display', '');
}

function uploadStart2(file) {
    $('#loading2').css('display', '');
}

function uploadStart3(file) {
    $('#loading3').css('display', '');
}


function uploadSuccess(file, serverData) {
    try {
        var jsonData = eval('(' + serverData + ')');
        if (jsonData.rs == "1") {
            var largeLogoSrc = genAudioDomain(jsonData.result, DOMAIN);
            $('#div_mp3_src').html('');
            $('#div_mp3_src').html('<audio controls="controls">'+
                                        '<source src="'+largeLogoSrc+'" type="audio/mpeg">'+
                                        '<embed src="'+largeLogoSrc+'" controls="console" align="middle" loop="false" autostart="false" width="300" height="30"></embed>'+
                                    '</audio>');
            $('#input_mp3_src').val(largeLogoSrc);
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
        if (jsonData.rs == "1") {
            var largeLogoSrc = genAudioDomain(jsonData.result, DOMAIN);
            $('#div_wav_src').html('');
            $('#div_wav_src').html('<audio controls="controls">'+
                                        '<source src="'+largeLogoSrc+'" type="audio/mpeg">'+
                                        '<embed src="'+largeLogoSrc+'" controls="console" align="middle" loop="false" autostart="false" width="300" height="30"></embed>'+
                                    '</audio>');
            $('#input_wav_src').val(largeLogoSrc);
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
            $('#img_pic').attr('src', largeLogoSrc);
            $('#input_pic').val(largeLogoSrc);
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