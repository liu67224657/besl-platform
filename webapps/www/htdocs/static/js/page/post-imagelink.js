/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-9
 * Time: 上午9:37
 * To change this template use File | Settings | File Templates.
 */


define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var common = require('../common/common');
    var postimagelink = {
        uploadChatImageLink:function (url) {
            var errinfoJquery = $("#error_image_link");
            uploadImageLinkPhoto(url, errinfoJquery, chatBeforeCallBack, chatSuccessCallBack);
        },
        uploadTxtImageLink:function (url) {
            var errinfoJquery = $("#error_txt_img_link");
            uploadImageLinkPhoto(url, errinfoJquery, textBeforeSendCallback, textSuccessCallback);
        },
        resetChatImageLink:function() {
            resetLinkImageInfo();
        },
        resetTextImageLink:function() {
            resetTxtLinkImageInfo();
        }
    }

    var uploadImageLinkPhoto = function (url, errinfoJquery, beforeSendCallback, successCallback) {
        if (url == null || url.length == 0) {
            errinfoJquery.attr("class", "tipstext").html('图片地址不能为空，请在输入框粘贴或输入');
            return false;
        }
        if (!imageUploadLimit(blogContent.image.length)) {
            errinfoJquery.attr("class", "tipstext").html('只能上传40张图片');
            return false;
        }
        if (!regImageExtName(url)) {
            errinfoJquery.attr("class", "tipstext").text('您输入的图片地址不是正确的URL格式，请重新输入');
            return;
        }
        errinfoJquery.html("");
        var linkUrl = encodeURIComponent(url);
//        if (isRImageSrc(url)) {
//            beforeSendCallback();
//            var req = "{\"result\":[{\"s\":\"" + joymeImgPrefix(common.parseSimg(url, joyconfig.DOMAIN)) + "\",\"b\":\"" + joymeImgPrefix(common.parseBimg(url, joyconfig.DOMAIN)) + "\",\"ss\":\"" + joymeImgPrefix(common.parseSSimg(url, joyconfig.DOMAIN)) + "\",\"m\":\"" + joymeImgPrefix(common.parseMimg(url, joyconfig.DOMAIN)) + "\"}],\"msg\":\"IMG\",\"status_code\":\"1\"}";
//            successCallback(req);
//            return;
//        }
        var postbiz = require('../biz/post-biz');
        postbiz.uploadImgLink(linkUrl, beforeSendCallback, successCallback)
    }
    var chatSuccessCallBack = function (req) {
        var resultMsg = eval('(' + req + ')');
        if (resultMsg.status_code == '0') {
            $("#error_image_link").attr("class", "cs_red").text(resultMsg.msg);
            canclePhoto(imgLocaL);
            return;
        } else {
            var imgMap = resultMsg.result[0];
            uploadLinkPhoto(imgLocaL, common.parseSSimg(imgMap['m'], joyconfig.DOMAIN), resultMsg.msg, imgMap);
            $("#post_chat_submit").removeAttr("disabled");
            if ($("#ul_preview li").size() > 0) {
                $("#post_chat_submit").attr('class','publishon')
                $("#edit_chat_submit").attr('class','publishon')
            }
        }
        $('#txt_image_url').bind('focus', function() {
            resetLinkImageInfo();
        });
        resetLinkImageInfo();
    }
    var chatBeforeCallBack = function () {
        $("#error_image_link").attr("class", "tipstext").text('您输入的链接已提交，请等待...');
        $('#txt_image_url').unbind('focus');
        beforePostPhoto(++imgLocaL);
    }
    var beforePostPhoto = function (imgLinkidx) {
        $("#ul_preview").append('<li id="preview_photo_' + imgLinkidx + '">' +
                '<span class="upload"></span>' +
                '上传中...' +
                '<input type="button" id="btnCancel" class="model" value="取消" style="border: none; background: none; width:52px" />' +
                '</li>');
        if ($("#rel_preview").is(":hidden")) {
            $("#rel_preview").show();
        }
        var jmpopup = require('../common/jmpopup');
        var photoId = "relPhotoDialog";
        var offset = $("#pic_more").offset();
        var config = {};
        config.offset = "Custom";
        config.popwidth = 355;
        config.offsetlocation = [offset.top + 31,offset.left];
        jmpopup.resetOffset(config, photoId)
        isOut = false;
    }
    var imageUploadLimit = function (currImageSize) {
        if (currImageSize >= 40) {
            return false;
        }
        return true;
    }
    var regImageExtName = function (url) {
        var extName = url.substring(url.lastIndexOf('.')).toLowerCase();
        if (extName != null && (extName == '.jpeg' || extName == '.jpg' || extName == '.gif' || extName == '.png')) {
            return true;
        }
        return false;
    }

    var canclePhoto = function (imgLinkidx) {
        $('#preview_photo_' + imgLinkidx).remove();
        $.each(blogContent.image, function(i, val) {
            if (val.key == imgLinkidx) {
                blogContent.image = $.grep(blogContent.image, function(n) {
                    return n.key != imgLinkidx;
                });
            }
        });
        hidePreviewDiv();
    }

    var joymeImgPrefix = function (src) {
        var joymeImgPrefix = /http:\/\/r\d\d\d\.joyme\.\w+(\/.*)/i;
        var match = joymeImgPrefix.exec(src);
        return match[1];
    }
    var isRImageSrc = function (url) {
        var imgService = /http:\/\/r\d\d\d\.joyme\.\w+\/r\d\d\d\/image\/.+/gi;
        if (imgService.test(url)) {
            return true;
        }
        return false;
    }

    var uploadLinkPhoto = function (imgLinkidx, imgSrc, name, imgMap) {
        postPhoto(imgLinkidx, imgSrc, name, imgMap);
        bindListener();
    }
    var postPhoto = function (imgLinkidx, imgSrc, name, imgMap) {
        $("#preview_photo_" + imgLinkidx).html('<input type="hidden" name="picurl_s" value="' + imgMap['s'] + '"/>' +
                '<input type="hidden" name="picurl_m" value="' + imgMap['m'] + '"/>' +
                '<input type="hidden" name="picurl_b" value="' + imgMap['b'] + '"/>' +
                '<input type="hidden" name="picurl_ss" value="' + imgMap['ss'] + '"/>' +
                '<input type="hidden" name="picurl" id="picurl" value="' + imgSrc + '"/>' +
                '<input type="hidden" name="w" id="picurl" value="' + imgMap['w'] + '"/>' +
                '<input type="hidden" name="h" id="picurl" value="' + imgMap['h'] + '"/>' +
                '<a href="javascript:void(0)" class="picreview" title="' + name + '">' + subfilename(name, 5) + '</a>' +
                '<a href="javascript:void(0)" title="取消" class="close""></a>');
        blogContent.image.push({key:imgLinkidx,value:{url:imgMap['b'],src:imgSrc,desc:''}});
    }

    var bindListener = function () {
        $("a[name=rmlink]").unbind().click(function() {
            $(this).parent().remove();
            if ($(".rel_preview li").length == 0) {
                $(".rel_preview").toggle();
            }
        });
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
    var resetLinkImageInfo = function () {
        $("#error_image_link").html('');
        $("#txt_image_url").val('');
    }

    var textBeforeSendCallback = function () {
        ++imgLocaL;
        $("#error_txt_img_link").attr("class", "tipstext").text('您输入的链接已提交，请等待...  ');
        $('#input_text_img_link').unbind('focus');
    }

    var textSuccessCallback = function (req) {
        var resultMsg = eval('(' + req + ')');
        if (resultMsg.status_code == '0') {
            $("#error_txt_img_link").attr("class", "cs_red").text(resultMsg.msg);
            return;
        } else {
            var imgMap = resultMsg.result[0];
            var imgSrc = common.parseSSimg(imgMap['m'], joyconfig.DOMAIN);
            blogContent.image.push({key:imgLocaL,value:{url:imgMap['b'],src:imgSrc,desc:''}});

            var imgStr = '<img src="' + imgSrc + '" joymet="img" joymed="" joymeh="' + imgMap['h'] + '" joymeu="' + imgMap['b'] + '" joymew="' + imgMap['w'] + '"/><br />';
            //判断是否加BR
            var contentText = CKEDITOR.instances['text_content'].getData();
            if (contentText != null && contentText != '' && !common.endsWithBr(contentText)) {
                imgStr = '<br />' + imgStr;
            }

            CKEDITOR.instances['text_content'].insertHtml(imgStr);
            CKEDITOR.instances['text_content'].focus();
            $(".rel_linkpic").css("display", "none");
            $("#closePop_oEditorImageLinkDialog").click();
        }
        $('#input_text_img_link').focus('focus', function() {
            resetTxtLinkImageInfo();
        });
        resetTxtLinkImageInfo();
    }
    var resetTxtLinkImageInfo = function () {
        $("#error_txt_img_link").html('');
        $("#input_text_img_link").val('');
    }


    var hidePreviewDiv = function() {
        if ($("#ul_preview li").length == 0) {
            $(".rel_preview").css("display", 'none');
        }
    }
    return postimagelink;
});
