/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-3
 * Time: 下午1:56
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var header = require('../page/header');
    var postChat = require('../page/post-chat');
    var postText = require('../page/post-text');
    var postbiz = require('../biz/post-biz');
    var postOption = require('../page/post-option');
    var joymealert = require('../common/joymealert');
    var common = require('../common/common');
    window.blogContent = {content:'',audio:null,video:null,image:new Array()};//设置全局对象
    window.imgLocaL = 0;
    (function() {
        blogContent.audio = editAudio;
        blogContent.video = editVideo;
        blogContent.image = editImages
        imgLocaL = editImages.length;
        var option = postOption.editOption;
        option.initTag = editTags;
        postChat.postChatInit(option);
        postText.posttextinit(option);
        if ($("#chat_content").val() == "") {
            $("#edit_chat_submit").attr('class','publish_btn');
        } else {
            $("#edit_chat_submit").attr('class','publishon')
        }
        var loadEditEditorTimes = setInterval(function() {
            if ("undefined" != typeof(CKEDITOR)) {
                if (CKEDITOR.instances['text_content'].getData().length > 0) {
                    $("#edit_text_submit").attr('class','publishon')
                } else {
                    $("#edit_text_submit").attr('class','publish_btn')
                }
                clearInterval(loadEditEditorTimes);
            }
        }, 500)
        $('#edit_chat_submit').click(function() {
            if (checkChatModel()) {
                var alertOption = {text:'编辑成功',tipLayer:true};
                joymealert.alert(alertOption);
                $('#postchat').submit();
            }

        });

        $('#edit_text_submit').click(function() {
            if ($("#blogSubject").val() == "给你的文章加个标题吧") {
                $("#blogSubject").val("");
                var alertOption = {
                    text:'没有标题的文章是得不到幸福的~~!',
                    timeOutMills:3000,
                    callbackFunction:function() {
                        $("#blogSubject").focus();
                    }
                };
                joymealert.alert(alertOption);
                return;
            }
            if (checkTextModel()) {
                var str = CKEDITOR.instances['text_content'].getData();
                str = str.replace(/<span style="font-weight: bold;">/g, '<strong>').replace(/<\/span>/g, '</strong>').
                        replace(/<b>/g, '<strong>').replace(/<\/b>/g, '</strong>');
                CKEDITOR.instances['text_content'].setData(str, function() {
                    $('#posttext').submit();
                })
            }
        });
        header.noticeSearchReTopInit();
    })();

    var checkChatModel = function () {
        var chatVal = $.trim($("#chat_content").val());

        var alertOption = {tipLayer:true,
            textClass:"tipstext"};

        if (chatVal.length > 0) {

            if (!postbiz.verifyPost($("#chat_content").val())) {
                alertOption.text = tips.blogNew.blog_new_content_illegl;
                joymealert.alert(alertOption);
                return false;
            }
            return true;
        }

        if ($("#ul_preview li").length > 0) {
            return true;
        }
        alertOption.text = '发布内容不能为空';
        joymealert.alert(alertOption);
        $("#chat_content").val('');
        return false;
    }

    var checkTextModel = function () {
        var str = CKEDITOR.instances['text_content'].getData();
        str = common.getBlogContent(str);
        var subject = $.trim($("#blogSubject").val());
        var verify = require('../biz/ajaxverify')
        if (str.length == 0 && subject.length == 0) {
            var alertOption = {text:'发布内容不能为空',tipLayer:true};
            joymealert.alert(alertOption);
            return false;
        }

        if (common.getInputLength(subject) > 140) {
            var alertOption = {text:require('../common/tips').blogNew.blog_new_text_subject_maxlength,tipLayer:true};
            joymealert.alert(alertOption);
            return false;
        }

        if (str.length > 0 && !verify.verifyPost(str)) {
            var alertOption = {text:require('../common/tips').blogNew.blog_new_content_illegl,tipLayer:true};
            joymealert.alert(alertOption);
            return false;
        }


        if (subject.length > 0 && !verify.verifyPost(subject)) {
            var alertOption = {text:'标题含有不适当内容',tipLayer:true};
            joymealert.alert(alertOption);
            return false;
        }
        return true;
    }
    require.async('../../third/swfupload/swfupload');
    require.async('../../third/swfupload/swfupload.queue');
    require.async('../../third/swfupload/fileprogress');
    require.async('../../third/ckeditor/ckeditor');
    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});