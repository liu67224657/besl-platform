/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-11
 * Time: 下午2:35
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var postbiz = require('../biz/post-biz');
    var joymealert = require('../common/joymealert');
    var ajaxverify = require('../biz/ajaxverify');
    var common = require('../common/common');
    window.posttextlock = false;

    var posttextsubmit = {
        ajaxPostText:function (homeOption) {
            var alertOption = {timeOutMills:3000,callbackFunction:function() {
                $("#blogSubject").focus();
            }};

            var subject=$("#blogSubject").val();
            if (subject==null|| subject.length==0 ||  subject == "给你的文章加个标题吧") {
                $("#blogSubject").val("");
                alertOption.text = '没有标题的文章是得不到幸福的~~!';
                joymealert.alert(alertOption);
                return;
            }else if(subject.length>140){
                alertOption.text = tipsText.blogNew.blog_new_text_subject_maxlength;
                joymealert.alert(alertOption);
                return;
            }

            var blogContent = CKEDITOR.instances['text_content'].getData();
            if (homeOption.formula != null && homeOption.formula.length > 0) {
                for (var fidx = 0; fidx < homeOption.formula.length; fidx++) {
                    var formulaObj = homeOption.formula[fidx];
                    if (formulaObj.code == 'image' && !common.contentValidate.textHasImage(blogContent)) {
                        alertOption.text = formulaObj.text;
                        joymealert.alert(alertOption);
                        return;
                    }

                    if (formulaObj.code == 'video' && !common.contentValidate.textHasVideo(blogContent)) {
                        alertOption.text = formulaObj.text;
                        joymealert.alert(alertOption);
                        return;
                    }
                }
            }

            if (window.posttextlock) {
                return;
            }

            if (checkTextModel(homeOption, this) && checkTag(homeOption, this)) {
                checkContextAt(blogContent, sendPostText, homeOption);
            }

        }
    }

    var checkTextModel = function (homeOption, post) {
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

    //验证tag是否非法
    var checkTag = function(homeOption, post) {
        var tags = $("#posttext").find("input[name=tags]");
        var tagStr = '';
        $.each(tags, function(i, val) {
            tagStr = tagStr + val.value;
        });
        if (tagStr.length > 0) {
            //非法词条
            var verify = ajaxverify.verifyPost(tagStr);
            if (!verify) {
                var alertOption = {text:tipsText.tag.tag_illegl,tipLayer:true,textClass:"tipstext"};
                joymealert.alert(alertOption);
                return false;
            }
        }
        return true;
    }
    var sendPostText = function (homeOption) {
        var joymeForm = require('../common/joymeform');
        var str = CKEDITOR.instances['text_content'].getData();
        str = str.replace(/<span style="font-weight: bold;">/g, '<strong>').replace(/<\/span>/g, '</strong>').
                replace(/<b>/g, '<strong>').replace(/<\/b>/g, '</strong>');
        $('#text_content').val(str);
        var formOption = {
            formid:'posttext',
            beforeSend:function() {
                if (window.posttextlock) {
                    return false;
                }
                CKEDITOR.instances['text_content'].focusManager.forceBlur();
                window.posttextlock = true;
                $('#post_text_submit').before('<em class="loadings"></em>').attr('class', 'publishloadbtn');
            },
            success:function(data, param) {
                blogContent = {audio:null,video:null,image:new Array()};
                $(".reldiv").css("display", "none");
                var resultMsg = eval('(' + data + ')');
                if (resultMsg.status_code == "1") {
                    var resultMsg = eval('(' + data + ')');
                    homeOption.postTextCallBack(resultMsg, homeOption);
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
                    joymealert.alert({text:resultMsg.msg,textClass:"tipstext",tipLayer:true,width:320});
                }
            },
            complete:function(resultMsg, param) {
                window.posttextlock = false;
                var content=$('#text_content').val();
                var subject= $('#blogSubject').val()
                if(content.length >0 || (subject.length>0 && subject!='给你的文章加个标题吧')){
                    $('#post_text_submit').attr('class', 'publishon').prev().remove();
                }else{
                    $('#post_text_submit').attr('class', 'publish_btn').prev().remove();
                }
            }
        };

        joymeForm.form(formOption);
    }
    //验证@相关信息
    var checkContextAt = function (context, successCallback, homeOption) {
        postbiz.checkAtSize(context, showAtSizeTips, checkAtSizeCallback, successCallback, homeOption);
    }
    var checkAtSizeCallback = function(context, successCallback, homeOption) {
        postbiz.checkAtNicks(context, showAtNicksTips, successCallback, homeOption)
    }

    //未@到昵称提示层
    var showAtNicksTips = function (resMsg, homeOption) {
        var list = resMsg.result;
        var str = '';
        $.each(list, function(i, val) {
            str = str + '“' + val + '”、';
        });

        var offSet = $('#post_area').offset();
        var tips = '您在文章@到的用户' + str.substring(0, str.length - 1) + '，不存在。';

        var submitFunction = function() {
            sendPostText(homeOption);
        }

        var confirmOption = {
            confirmid:"atNicksTips",
            offset:"Custom",
            offsetlocation:[offSet.top + 60,offSet.left + Math.floor(($('#post_area').width() - 229) / 2)],
            title:"确认继续发送",
            text:tips,
            width:229,
            submitButtonText:'继续发送',
            submitFunction:submitFunction,
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
            offsetlocation:[offSet.top + 60,offSet.left + Math.floor(($('#post_area').width() - 229) / 2)],
            text:tips,
            width:229,
            submitButtonText:'继续发送',
            submitFunction:submitFunction,
            cancelButtonText:'返回修改',
            cancelFunction:null};
        joymealert.confirm(confirmOption);

    }

    return posttextsubmit;
});