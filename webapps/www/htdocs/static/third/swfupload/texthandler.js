define(function(require, exports, module) {
    var $ = require('../../js/common/jquery-1.5.2');
    var joymealert = require('../../js/common/joymealert');
    var common = require('../../js/common/common');
    var texthandler = {
        fileDialogComplete:function (numFilesSelected, numFilesQueued) {
            try {
                if (numFilesSelected == 0) {

                } else if (numFilesSelected > 40) {
                    joymealert.alert({text:"您的图片超过40张，请重新选择",popzindex:16002});
                } else {
                    if (numFilesQueued != 0) {
                        swftext.startUpload();
                    }
                }
            } catch (ex) {
                swftext.debug(ex);
            }
        },
        uploadStart:function (file) {
            $("#link_insert_photo").attr("disabled", 'true');
            if (checkLocalImageNumber(swftext)) {
                textPostPhotoLi(file.name, imgLocaL);
            }
        },
        uploadProgress: function (file, bytesLoaded) {
            try {
                var percent = Math.ceil((bytesLoaded / file.size) * 100);

                //loading\
                uploadLoading(blogContent.image.length, percent);
                if (percent >= 100) {
                    $("#upload_txt_img_" + imgLocaL).attr('class', 'chulion').html('<p> 处理中...</p>' +
                            '<a class="close" id="delPhotoLi_' + imgLocaL + '"></a>');
                }
            } catch (ex) {
                swftext.debug(ex);
            }
        },
        uploadSuccess: function (file, serverData) {
            if (!imageUploadLimit(blogContent.image.length)) {
                return false;
            }

            try {
                var jsonData = eval('(' + serverData + ')');
                if (jsonData.status_code == "1") {
                    var ssimg = common.genImgDomain(jsonData.result[0].ss, swftext.customSettings.resource_path);
                    uploadsuc(imgLocaL, ssimg, jsonData.result[0]);
                } else {
                    if (jsonData.msg == 'token_faild') {
                        window.location.href = "/loginpage?reurl=" + encodeURIComponent(window.location.href);
                    } else {
                        $("#upload_txt_img_" + imgLocaL).attr('class', '').html('<p class="tipstext">上传失败!</p><a class="close" id="delPhotoLi_' + imgLocaL + '"></a>');
                    }
                }
            } catch (ex) {
                swftext.debug(ex);
            }
        },
        fileQueueError:function (file, errorCode, message) {
            try {
                switch (errorCode) {
                    case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
                        this.cancelQueue();
                        joymealert.alert({text:"您的图片超过8M，请重新选择",popzindex:16002});
                        break;
                    default:
                        // joymealert.alert({text:"超出一次最多上传图片张数"});
                        break;
                }


            } catch (ex) {
                swfu.debug(ex);
            }
        },

        uploadComplete: function (file) {
            imgLocaL++;
            try {
                if (swftext.getStats().files_queued > 0) {
                    swftext.startUpload();
                } else {
                    $("#link_insert_photo").removeAttr("disabled")
                    $("#post_text_submit").removeAttr("disabled")
                }
            } catch (ex) {
                swftext.debug(ex);
            }
        },

        //插入富文本框
        insertPhotoStr :function () {
            var str = "";
            $("#photo_upload_progress li[id^=upload_txt_img_]").each(function(i, val) {
                var fileid = val.id;
                var uploadid = fileid.substring(fileid.lastIndexOf('_') + 1, fileid.length)
                var description = $("#des_" + uploadid).val();
                if (description == "添加简要描述(22个字)") {
                    description = "";
                } else {
                    description = $("#des_" + uploadid).val();
                }
                var imgurl = $("#src_" + uploadid).val();
                str += '<img joymew="' + $("#img_" + uploadid).attr("joymew") + '" joymeh="' + $("#img_" + uploadid).attr("joymeh") + '" joymet="img" joymed="' + description + '" joymeu="' + imgurl + '" src="' + $("#img_" + uploadid).attr("src") + '"/><span>&nbsp;</span><br/>';
                if (description.length > 0) {
                    str += description + '<br/>';
                }
            });

            //判断是否加BR
            var contentText = CKEDITOR.instances['text_content'].getData();
            if (contentText != null && contentText != '' && !common.endsWithBr(contentText)) {
                str = '<br />' + str;
            }

            if (str != "") {
                CKEDITOR.instances['text_content'].insertHtml(str);
            }
            CKEDITOR.instances['text_content'].focus();
        },
        delPhotoLi:function (fileid) {
            $.each(blogContent.image, function(i, val) {
                if (val.key == fileid) {
                    blogContent.image = $.grep(blogContent.image, function(n) {
                        return n.key != fileid;
                    });
                }
            });
            window.swftext.cancelQueue();
            $("#upload_txt_img_" + fileid).remove();
        }
    }


    /* ******************************************
     *	FileProgress Object
     *	Control object for displaying file info
     * ****************************************** */

    var FileProgress = function () {
        swftext.fileProgressID = 'upload_txt_img_' + blogContent.image.length;
        swftext.fileProgressWrapper = document.getElementById(swftext.fileProgressID);
        if (!swftext.fileProgressWrapper) {
            swftext.fileProgressWrapper = document.getElementById(swftext.fileProgressID);
        } else {

        }
        swftext.height = swftext.fileProgressWrapper.offsetHeight;
    }

    //加载进度层
    var textPostPhotoLi = function (filename, fileid) {
        var listr = '<li id="upload_txt_img_' + fileid + '">' +
                '<div class="ep_list">' +
                ' ' + subfilename(filename, 5) + ' ' +
                '<div class="loadtc"><div id="' + fileid + '_loading" class="loading_on">0%</div>' +
                '</div>' +
                '<span><a href="javascript:void(0);" class="close" id="delPhotoLi_' + fileid + '"></a></span>' +
                '</div>' +
                '</li>';
        $("#photo_upload_progress .add").before(listr);
        $("#post_text_submit").attr("disabled", "disabled")
    }

    var checkLocalImageNumber = function (swf) {
        if (!imageUploadLimit(blogContent.image.length)) {
            joymealert.alert({text:"您的图片超过40张，请重新选择",popzindex:16002});
            swftext.cancelQueue();
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
    var subfilename = function (imgname, l) {
        var splitarr = imgname.split(".");
        var typestr = "." + splitarr[splitarr.length - 1];
        var ps = imgname.indexOf(typestr);
        var namestr = imgname.substr(0, ps);
        if (namestr.length > l) {
            return namestr.substr(0, l - 1) + "..." + namestr.charAt(namestr.length - 1) + typestr;
        }
        return imgname;
    }

    //加载完成后展示图层
    var uploadsuc = function (fileid, imgSrc, imgMap) {
        blogContent.image.push({key:fileid,value:{url:imgMap['b'],src:imgSrc,desc:''}});
        var str = '<input type="hidden" name="picurl_b" id="src_' + fileid + '" value="' + imgMap['b'] + '"/>' +
                '<p>' +
                '<img id="img_' + fileid + '" src="' + imgSrc + '" joymet="img" joymed="" joymew="' + imgMap['w'] + '" joymeh="' + imgMap['h'] + '" width="150" height="114"/>' +
                '</p>' +
                '<p><textarea name="des" cols="11" rows="2" class="description" id="des_' + fileid + '">' +
                '添加简要描述(22个字)</textarea></p>' +
                '<a href="javascript:void(0);" class="close" id="delPhotoLi_' + fileid + '"></a>';

        $("#upload_txt_img_" + fileid).attr('class', '').html(str);
        $("#des_" + fileid).css("color", "#ccc");
        $('#des_' + fileid).bind('focusin focusout paste cut keydown keyup focus blur', function(event) {
            if (event.type == "focusin") {
                if ($(this).val() == '添加简要描述(22个字)') {
                    $(this).val('');
                    $(this).css("color", "")
                }
            } else if (event.type == "focusout") {
                if ($(this).val() == '') {
                    $(this).val('添加简要描述(22个字)');
                    $(this).css("color", "#ccc");
                }
            } else {
                if ($(this).val().length > 22) {
                    $(this).val($(this).val().substr(0, 22));
                }
            }
        })
    }

    var uploadLoading = function (fileid, gress) {
        var loadingImg = $("#" + fileid + "_loading");
        loadingImg.text(gress + '%');
        loadingImg.css("width", 0.98 * gress + '%');
    }

    return texthandler;
});