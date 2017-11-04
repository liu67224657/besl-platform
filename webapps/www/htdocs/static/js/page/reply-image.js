define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var common = require('../common/common');
    var joymealert = require('../common/joymealert');
    var pop = require('../common/jmpopup');
    require('../../third/swfupload/swfupload');
    require('../../third/swfupload/swfupload.queue');
    require('../../third/swfupload/fileprogress');
    var replyUploadImageDialog = "replyUploadImageDialog";
    var url = joyconfig.urlUpload + "/json/upload/single";

    var replyImage = {
        replyImageInit:function () {
            $("div[name=reply_image_icon]").die().live("mouseenter", function () {
                $(this).next().css({"top":$(this).position().top + 'px',"left":$(this).position().left + 2 + 'px'});
                $(this).next().show();
                $(this).hide();
            });
            $("div[name=reply_image_icon_more]").die().live("mouseleave", function () {
                $(this).prev().show();
                $(this).hide();

            });
            $("a[name=reply_blog_upload_img]").die().live("click", function () {
                loadReplyUploadImageDialog($(this).parent(), false, $(this).attr("data-cid"),false);
            });
            $("a[name=reply_blog_upload_img_link]").die().live("click", function () {
                loadReplyUploadImageDialog($(this).parent(), true, $(this).attr("data-cid"),false);
            });
            $("a[name=reply_upload_img]").die().live("click", function () {
                loadReplyUploadImageDialog($(this).parent(), false, $(this).attr("data-cid"),true);
            });
            $("a[name=reply_upload_img_link]").die().live("click", function () {
                loadReplyUploadImageDialog($(this).parent(), true, $(this).attr("data-cid"),true);
            });
            $("#replypicuploadbtn").die().live("click", function () {
                if ($("#replyPhotoUploadBtn").is(":hidden")) {
                    $("#replyPhotoLinkBox").hide();
                    $("#replyPhotoUploadBtn").show();
                    $("#replypicuploadbtn").addClass("tabon");
                    $("#replypiclinkbtn").removeClass("tabon");
                }
            });
            $("#replypiclinkbtn").die().live("click", function () {
                if ($("#replyPhotoLinkBox").is(":hidden")) {
                    $("#replyPhotoUploadBtn").hide();
                    $("#replyPhotoLinkBox").show();
                    $("#replypiclinkbtn").addClass("tabon");
                    $("#replypicuploadbtn").removeClass("tabon");
                }
            });
            $("a[name=closeReplyUploadImagePreview]").die().live("click", function() {
                $("#" + $(this).parent().attr("data-id")).find(".t_pic").show();
                $(this).parent().parent().parent().remove();
            });

            $("a[name=reply_upload_image_loading]").die().live("click", function() {
                window.replyswfu.cancelQueue();
                $(this).parent().parent().parent().remove();
                $("#" + $(this).attr("data-id")).find(".t_pic").show();
            });

            $("#reply_image_url").die().live('focus',
                    function() {
                        if ($(this).val() == "请输入图片链接地址") {
                            $(this).css("color", "#5b5b5b").val("");
                        }
                    }).live('blur', function() {
                        if ($(this).val() == "") {
                            $(this).css("color", "#ccc").val("请输入图片链接地址");
                        }
                    });
            $("#reply_image_url_btn").die().live('click', function() {
                var uploadid = $(this).attr("data-uploadid")
                window['imageUploadFlag'] = true;
                $("#subreply_" + uploadid.substr('reply_image_'.length, uploadid.length)).attr('class', 'loadbtn fr');
                uploadReplyImageLink($("#reply_image_url").val(), uploadid);
            });
            $("ul[name=replyUploadImagePreview]").die().live('mouseenter',
                    function(event) {
                        clearTimeout(window['outprevfocusTimeout']);
                        var prevId = $(this).children('li').attr("data-id");
                        previewPhoto($(this), prevId);
                    }).live('mouseleave', function() {
                        var prevId = $(this).children('li').attr("data-id");
                        window['outprevfocusTimeout'] = setTimeout(function() {
                            $("#prev_" + prevId).fadeOut();
                        }, 1000)
                    });
            togglePicOnReply();
        },
        checkUploadNum:function(objId) {
            if ($("#" + objId).find(".preview").length > 0) {
                replyImage.showUploadErrorTips('只能上传1张图片');
                return false;
            } else {
                return true;
            }
        },
        replyUploadImageStart:function(objId) {
            window['imageUploadFlag'] = true;
            $("#subreply_" + objId.substr('reply_image_'.length, objId.length)).attr('class', 'loadbtn fr');
            $("#" + objId).find(".t_pic").hide();
            $("#" + objId).find(".t_pic_more").hide();
            $("#" + objId).append('<div class="preview clearfix"><ul><li><span class="upload"></span>上传中...' +
                    '<a href="javascript:void(0)" name="reply_upload_image_loading" data-id="' + objId + '">取消</a></li></ul></div>');
        },
        replyUploadSuccess:function(name, imgMap, uploadId) {
            var imgPreview = common.genImgDomain(imgMap['ss'], joyconfig.DOMAIN);
            $("#" + uploadId).find(".preview").html('<ul name="replyUploadImagePreview" id="replyUploadImagePreview"><li data-id="' + uploadId + '">' + common.subfilename(name, 5) +
                    '<a href="javascript:void(0)" title="取消" class="close" name="closeReplyUploadImagePreview"></a>' +
                    '<input type="hidden" name="picurl_m" value="' + imgMap['m'] + '"/>' +
                    '<input type="hidden" name="picurl_b" value="' + imgMap['b'] + '"/>' +
                    '<input type="hidden" name="picurl_ss" value="' + imgMap['ss'] + '"/>' +
                    '<input type="hidden" name="picurl_s" value="' + imgMap['s'] + '"/>' +
                    '<input type="hidden" name="w" id="w" value="' + imgMap['w'] + '"/>' +
                    '<input type="hidden" name="h" id="h" value="' + imgMap['h'] + '"/>' +
                    '</li></ul>');
        },
        showUploadErrorTips:function(tips) {
            if ($("#" + replyUploadImageDialog).find(".piccontext").find(".tipstext").length > 0) {
                $("#" + replyUploadImageDialog).find(".piccontext").find(".tipstext").html(tips);
            } else {
                $("#" + replyUploadImageDialog).find(".piccontext").append('<p class="tipstext" id="">' + tips + '</p>');
            }
        }

    }
    //加载图片层
    var loadReplyUploadImageDialog = function (photoDom, flag, uploadId,arrowUp) {
        var offset = photoDom.offset();
        var photoId = replyUploadImageDialog;
        var preTop = 28;
//        if ($("#previewBox").length > 0) {
//            preTop = $("#previewBox").data('bodyTop') + preTop;
//        }
        var imageConfig = {
            pointerFlag:true, //是否有指针
            pointdir:'up', //指针方向
            tipLayer:false, //是否遮罩
            containTitle:true, //包含title
            containFoot:false, //包含footer
            className:"",
            forclosed:true,
            allowmultiple:true,
            isfocus:false,
            popwidth:355,
            offset:"Custom",
            offsetlocation:[photoDom.position().top + preTop, photoDom.position().left],
            hideCallback:function () {
                window.replyswfu.cancelQueue();
                $("#" + photoId).remove();
            },
            showFunction:function() {
                window.replyswfu = null;
                replyUploadSWFInit(uploadId);
            },
            afterDom:photoDom
        };
        if (!arrowUp) {
           imageConfig.pointdir="down";
           imageConfig.offsetlocation=[photoDom.position().top-110, photoDom.position().left];
        }

        if ($("#" + photoId).length > 0) {
            $("#" + photoId).remove();
        }

        var htmlObj = new Object();
        htmlObj['id'] = photoId;
        htmlObj['title'] = '<div class="pichd"><ul>' +
                '<li id="replypicuploadbtn"><a href="javascript:void(0)">上传图片</a></li> ' +
                '<li id="replypiclinkbtn"><a href="javascript:void(0)">图片链接</a></li>' +
                '</ul></div>'
        htmlObj['html'] = '<div class="picbd" id="replyPhotoUploadBtn">' +
                '<div class="piccontext">' +
                '<p><a href="javascript:void(0);" id="replyUploadImgSpanButton">从电脑选择图片</a></p>' +
                '<p class="pt10">仅支持JPG、GIF、PNG图片文件，且单个文件小于3M</p>' +
                '</div>' +
                '</div>' +
                '<div id="replyPhotoLinkBox" class="picbd">' +
                '<div>' +
                '<p>粘贴网上图片地址</p>' +
                '<p><input type="text" class="linktext" id="reply_image_url" name="" value="请输入图片链接地址" style="color:#ccc"><a class="submitbtn" id="reply_image_url_btn" data-uploadid="' + uploadId + '"><span>确 定</span></a></p>' +
                '<p class="tipstext" id="error_reply_image_link"></p>' +
                '</div>' +
                '</div>';
        if (flag) {
            pop.popupInit(imageConfig, htmlObj);
            $("#" + photoId + " .hd").css({padding:0});
            $("#replyPhotoUploadBtn").hide();
            $("#replypiclinkbtn").addClass("tabon");
        } else {
            pop.popupInit(imageConfig, htmlObj);
            $("#" + photoId + " .hd").css({padding:0});
            $("#replyPhotoLinkBox").hide();
            $("#replypicuploadbtn").addClass("tabon");
        }
    }
    var replyUploadSWFInit = function(uploadId) {
        var handler = require('../../third/swfupload/handlers_reply');
        var replySettings = {
            upload_url :url,
            post_params : {
                "at" : joyconfig.token
            },
            // File Upload Settings
            file_size_limit : "3 MB",
            file_types : "*.jpg;*.png;*.gif",
            file_types_description : "请选择图片",
            file_upload_limit : 0,
            file_queue_limit : 0,
            swfupload_preload_handler : handler.preLoad,
            swfupload_load_failed_handler : handler.loadFailed,
            file_dialog_complete_handler: handler.fileDialogComplete,
            upload_start_handler : handler.uploadStart,
            upload_progress_handler : handler.uploadProgress,
            upload_success_handler : handler.uploadSuccess,
            upload_complete_handler : handler.uploadComplete,
            file_queue_error_handler : handler.fileQueueError,

            // Button Settings
            button_image_url : joyconfig.URL_LIB + "/static/theme/default/img/choose.jpg",
            button_placeholder_id : "replyUploadImgSpanButton",
            button_width: 148,
            button_height: 26,
            button_text : '',
            button_text_style : '',
            button_text_top_padding: 0,
            button_text_left_padding: 12,
            button_window_mode: SWFUpload.WINDOW_MODE.OPAQUE,
            button_cursor: SWFUpload.CURSOR.HAND,
            button_action:SWFUpload.BUTTON_ACTION.SELECT_FILE,//控制选择文件窗口.选择文件个数 -100选择一个文件 -110选择多个文件
            // Flash Settings
            flash_url : joyconfig.URL_LIB + "/static/third/swfupload/swfupload.swf",
            flash9_url : joyconfig.URL_LIB + "/static/third/js/swfupload/swfupload_fp9.swf",

            custom_settings : {
                cancelButtonId : "btnCancel",
                uploadCallback: function() {
                    replyUploadComplete(this.uploadId);
                    $("#subreply_" + this.uploadId.substr('reply_image_'.length, this.uploadId.length)).attr('class', 'submitbtn fr');
                    window['imageUploadFlag'] = false;
                },
                tdFilesUploaded : document.getElementById("tdFilesUploaded"),
                resource_path :  joyconfig.DOMAIN,
                uploadId : uploadId
            }}
        window.replyswfu = new SWFUpload(replySettings);
    }

    var replyUploadComplete = function(uploadId) {
//        window.replyswfu.destroy();
        if ($("#" + uploadId).find("input[type=hidden]").length > 0) {
            $("#" + replyUploadImageDialog).remove();
        }
    }

    var uploadReplyImageLink = function(url, uploadId) {
        if (url == null || url.length == 0) {
            $("#error_reply_image_link").attr("class", "tipstext").html('图片地址不能为空，请在输入框粘贴或输入');
            $("#subreply_" + uploadId.substr('reply_image_'.length, uploadId.length)).attr('class', 'submitbtn fr');
            window['imageUploadFlag'] = false;
            return false;
        }
        if ($("#" + uploadId).find(".preview").length > 0) {
            $("#error_reply_image_link").attr("class", "tipstext").html('只能上传1张图片');
            $("#subreply_" + uploadId.substr('reply_image_'.length, uploadId.length)).attr('class', 'submitbtn fr');
            window['imageUploadFlag'] = false;
            return false;
        }
        if (!regImageExtName(url)) {
            $("#error_reply_image_link").attr("class", "tipstext").text('您输入的图片地址不是正确的URL格式，请重新输入');
            $("#subreply_" + uploadId.substr('reply_image_'.length, uploadId.length)).attr('class', 'submitbtn fr');
            window['imageUploadFlag'] = false;
            return;
        }
        $("#error_reply_image_link").html("");
        var linkUrl = encodeURIComponent(url);
        var postbiz = require('../biz/post-biz');
        postbiz.uploadReplyImgLink(linkUrl, uploadId, beforeReplyImageLinkCallback, successReplyImageLinkCallback);
    }

    var regImageExtName = function (url) {
        var extName = url.substring(url.lastIndexOf('.')).toLowerCase();
        if (extName != null && (extName == '.jpeg' || extName == '.jpg' || extName == '.gif' || extName == '.png')) {
            return true;
        }
        return false;
    }

    var beforeReplyImageLinkCallback = function (uploadId) {
        $("#error_reply_image_link").attr("class", "tipstext").text('您输入的链接已提交，请等待...');
        $('#reply_image_url').unbind('focus');
        replyImage.replyUploadImageStart(uploadId);
    }

    var successReplyImageLinkCallback = function (req, uploadId) {
        var resultMsg = eval('(' + req + ')');
        if (resultMsg.status_code == '0') {
            $("#error_reply_image_link").attr("class", "tipstext").text(resultMsg.msg);
            $("#" + uploadId).find(".preview").remove();
            $("#" + uploadId).find(".t_pic").show();
            return;
        } else {
            var imgMap = resultMsg.result[0];
            replyImage.replyUploadSuccess(resultMsg.msg, imgMap, uploadId);
            $("#" + replyUploadImageDialog).remove();
        }
        $("#subreply_" + uploadId.substr('reply_image_'.length, uploadId.length)).attr('class', 'submitbtn fr');
        window['imageUploadFlag'] = false;
        $('#txt_image_url').bind('focus', function() {
            resetReplyImageLinkInfo();
        });
        resetReplyImageLinkInfo();
    }

    var resetReplyImageLinkInfo = function () {
        $("#error_reply_image_link").html('');
        $("#reply_image_url").val('');
        replyUploadComplete();
    }
    var previewPhoto = function(prevDom, prevId) {
        var prevPhotoId = "prev_" + prevId;
        if ($("#" + prevPhotoId).length > 0) {
            $("#" + prevPhotoId).remove();
        }

        prevDom.parent().append('<div style="position: absolute; width: 230px; z-index:19999; display: none;" id="' + prevPhotoId + '" class="pop previewlist">' +
                '<div class="corner"></div>' +
                '<div class="bd">' +
                '<div class="uploadpic">' +
                '<div><img src="' + common.genImgDomain(prevDom.children('li').find("input[name=picurl_ss]").val(), joyconfig.DOMAIN) + '"></div>' +
                '</div></div></div>');
        $("#" + prevPhotoId).css({top:26 + "px",left:0}).show();

        $("#" + prevPhotoId).live("mouseenter mouseleave", function(event) {
            showpreviewInit($(this), event);
        });

    }
    var showpreviewInit = function(dom, event) {
        if (event.type == "mouseenter") {
            clearTimeout(window['outprevfocusTimeout']);
            clearTimeout(window['cleartimeoutpreview']);
        } else if (event.type == "mouseleave") {
            var _this = dom;
            window['cleartimeoutpreview'] = setTimeout(function() {
                _this.fadeOut();
            }, 500)
        }
    }
    var togglePicOnReply = function() {
        $('.search_piclist>li>a>img').die().live('click', function() {
            var imgUl = $(this).parent().parent().parent();
            imgUl.children().hide();
            imgUl.attr('class', 'search_single pic_limit clearfix');
            var domain = imgUl.attr("data-domain");
            var cid = imgUl.attr("data-cid");
            var imgss = $(this).attr("src");
            imgUl.append('<li><p><a href="' + common.parseBimg(imgss, joyconfig.DOMAIN) + '" target="_blank" class="look_over">查看原图</a></p><img src="' + common.parseMimg(imgss, joyconfig.DOMAIN) + '"></li>');
        });

        $('.search_single>li>img').die().live('click', function() {
            var imgUl = $(this).parent().parent();
            $(this).parent().remove();
            imgUl.children().show();
            imgUl.attr('class', 'search_piclist clearfix');
            var docTop = imgUl.parent().offset().top - 50;
            window.scrollTo(0, docTop);
        });
    }

    return replyImage;
});
