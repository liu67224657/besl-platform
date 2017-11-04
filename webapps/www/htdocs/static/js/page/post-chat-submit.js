/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-11
 * Time: 下午2:35
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var tips = require('../common/tips')
    var postbiz = require('../biz/post-biz');
    var joymealert = require('../common/joymealert');
    var ajaxverify = require('../biz/ajaxverify');
    window.postchatlock = false;
    var chatsubmit = {
        ajaxPostChat:function (homeOption) {
            if (window.postchatlock) {
                return;
            }

            $("#joyme_alert").css("display", "none")
            $(".reldiv").css("display", "none");
            $("#relMusic").css("background-color", "");
            $("#relVideo").css("background-color", "");
            $('#sync_info').slideUp();
            $(".Atwho").hide();
            //验证内容是否为空
            if (checkChatModel(homeOption) && checkTag(homeOption)) {
                checkContextAt($('#chat_content').val(), sendPostChat, homeOption);
            }
        }
    }
    var sendPostChat = function (homeOption) {
        var joymeForm = require('../common/joymeform');

        var formOption = {
            formid:'postchat',
            beforeSend:function() {
                if (window.postchatlock) {
                    return false;
                }

                window.postchatlock = true;
                $('#post_chat_submit').before('<em class="loadings"></em>').attr('class', 'publishloadbtn');
            },
            success:function(req, param) {
                var resultMsg = eval('(' + req + ')');
                if (resultMsg.status_code == "1") {
                    homeOption.postChatCallBack(resultMsg);
                } else if (resultMsg.status_code == '-5') {
                    var alertOption = {text:tipsText.profile.user_ipforbidden_forbidlogin,
                        tipLayer:true,
                        textClass:"tipstext",
                        callbackFunction:function() {
                            if (resultMsg.result != null && resultMsg.result.length > 0) {
                                window.location.href = resultMsg.result[0];
                            }
                        }};
                    joymealert.alert(alertOption);
                } else {
                    var alertOption = {text:resultMsg.msg, tipLayer:true, textClass:"tipstext" };
                    joymealert.alert(alertOption);
                }

            },
            complete:function() {
                window.postchatlock = false;
                $('#post_chat_submit').attr('class', 'publish_btn').prev().remove();
            }
        };

        joymeForm.form(formOption);

    }

    var checkContextAt = function (context, successCallback, homeOption) {
        postbiz.checkAtSize(context, showAtSizeTips, checkAtSizeCallback, successCallback, homeOption);
    }
    var checkAtSizeCallback = function(context, successCallback, homeOption) {
        postbiz.checkAtNicks(context, showAtNicksTips, successCallback, homeOption)
    }
    var checkChatModel = function (homeOption) {
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

    //验证tag是否非法
    var checkTag = function(homeOption) {
        var alertOption = {tipLayer:true,
            textClass:"tipstext"};
        var tags = $("#postchat").find("input[name=tags]");
        var tagStr = '';
        $.each(tags, function(i, val) {
            tagStr = tagStr + val.value;
        });
        if (tagStr.length > 0) {
            //非法词条
            var verify = ajaxverify.verifyPost(tagStr);
            if (!verify) {
                alertOption.text = tipsText.tag.tag_illegl;
                joymealert.alert(alertOption);
                return false;
            }
        }
        return true;
    }
    /*账户提示*/
    var showAtNicksTips = function (resMsg, homeOption) {
        var list = resMsg.result;
        var str = '';
        $.each(list, function(i, val) {
            str = str + '“' + val + '”、';
        });

        var offSet = $('#post_area').offset();
        var tips = '您在文章@到的用户' + str.substring(0, str.length - 1) + '，不存在。';

        var confirmOption = {
            confirmid:"atNicksTips",
            offset:"Custom",
            offsetlocation:[offSet.top,offSet.left + Math.floor(($('#post_area').width() - 229) / 2)],
            title:"确认继续发送",
            text:tips,
            width:229,
            submitButtonText:'继续发送',
            submitFunction:function() {
                sendPostChat(homeOption);
            },
            cancelButtonText:'返回修改',
            cancelFunction:null};
        joymealert.confirm(confirmOption);

    }
    //超过@次数提示层
    var showAtSizeTips = function (context, successCallback, homeOption) {
        var offSet = $('#post_area').offset();
        var tips = '您在一篇文章中使用@的次数超过15次超出的部分不能传达给对方信息。'

        var submitFunction = function() {
            checkAtSizeCallback(context, successCallback, homeOption);
        }

        var confirmOption = {
            confirmid:"atSizeTips",
            offset:"Custom",
            offsetlocation:[offSet.top,offSet.left + Math.floor(($('#post_area').width() - 229) / 2)],
            text:tips,
            width:229,
            submitButtonText:'继续发送',
            submitFunction:submitFunction,
            cancelButtonText:'返回修改',
            cancelFunction:null};
        joymealert.confirm(confirmOption);

    }

    return chatsubmit;
});