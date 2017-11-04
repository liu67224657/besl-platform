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
        if (jsonData.rs == "1") {
            var pic = genImgDomain(jsonData.result.pic, DOMAIN);
            var pics = genImgDomain(jsonData.result.pic_s, DOMAIN);
            $('#pic').attr('src', pic);
            $('#pics').attr('src', pics);
            $('#input_pic').val(pic);
            $('#input_pics').val(pics);
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
            var amr = genAudioDomain(jsonData.result.amr, DOMAIN);
            var mp3 = genAudioDomain(jsonData.result.mp3, DOMAIN);
            $('#div_audio').html('');
            $('#div_audio').html('<audio controls="controls">'+
                                        '<source src="'+amr+'" type="audio/mpeg">'+
                                        '<embed src="'+amr+'" controls="console" align="middle" loop="false" autostart="false" width="300" height="30"></embed>'+
                                    '</audio>'+
                                    '<audio controls="controls">'+
                                        '<source src="'+mp3+'" type="audio/mpeg">'+
                                        '<embed src="'+mp3+'" controls="console" align="middle" loop="false" autostart="false" width="300" height="30"></embed>'+
                                    '</audio>');
            $('#input_amr').val(amr);
            $('#input_mp3').val(mp3);
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