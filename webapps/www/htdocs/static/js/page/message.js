define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var common = require('../common/common');
    var pop = require('../common/jmpopup');
    var messageBiz = require('../biz/message-biz');
    var messageCallback = require('./messagecallback');
    var moodBiz = require('../biz/mood-biz');
    var loginBiz = require('../biz/login-biz');
    var ajaxverify = require('../biz/ajaxverify');
    var joymealert = require('../common/joymealert');

    var message = {
        bindSendMsg:function() {
            $("a[name=sendMsgMask]").live("click", function() {
                if (loginBiz.checkLogin($(this))) {
                    initMask($(this).attr("data-nick"));
                }
            });
        },
        initMessageMask:function(recevieName) {
            var privilegeObj = ajaxverify.verifyPrivilege(true);
            if (privilegeObj.status_code == '-1') {
                loginBiz.maskLoginByJsonObj(privilegeObj);
            } else if (privilegeObj.status_code != '1') {
                var alertOption = {text:privilegeObj.msg,tipLayer:true,textClass:"tipstext"};
                joymealert.alert(alertOption);
                return;
            }

            initMask(recevieName);
        },
        bindSaveMsg:function() {
            $("a[name=savemsg]").live("click", function() {
                var receName = $('#receivename').val();
                var messagebody = $('#messagebody').val();
                messageBiz.send(receName, messagebody, $("#message_num"), sendMsgCallback);
            });

        },

        bindRemoveMsg:function() {
            $("a[name=removeMsg]").live("click", function() {
                var linkObj = $(this);
                var submitFunction = function() {
                    messageBiz.removeMsg(linkObj.attr("uno"), removeMsgCallback);
                }

                var removeConfirmOption = {
                    tipLayer:true,
                    width:240,
                    text:'确定要删除该私信么？',
                    submitButtonText:'删 除',
                    submitFunction:submitFunction,
                    cancelButtonText:'取 消'};
                joymealert.confirm(removeConfirmOption);
            });
        },
        bindRemoveMsgReply:function() {
            $("a[name=removeMsgReply]").live("click", function() {
                var linkObj = $(this);
                var submitFunction = function() {
                    messageBiz.removeMsgReply(linkObj.attr("id"), removeMsgReplyCallback);
                }

                var removeConfirmOption = {
                    tipLayer:true,
                    width:240,
                    text:'确定要删除该私信么？',
                    submitButtonText:'删 除',
                    submitFunction:submitFunction,
                    cancelButtonText:'取 消'};
                joymealert.confirm(removeConfirmOption);
            });
        },
        bindRemoveNotice:function() {
            $("a[name=removeNotice]").live("click", function() {
                var linkObj = $(this);
                var removeConfirmOption = {
                    tipLayer:true,
                    width:240,
                    text:'确定要删除该通知么？',
                    submitButtonText:'删 除',
                    submitFunction:function() {
                        messageBiz.removeMsgReply(linkObj.attr("id"), removeNoticeCallback);
                    },
                    cancelButtonText:'取 消'};
                joymealert.confirm(removeConfirmOption);
            });
        },
        bindMood:function() {
            var te = new TextareaEditor(document.getElementById('textarea_messagebody'));
            $('#message_mood').live('click', function() {
                var config = {
                    allowmultiple:true,
                    isremovepop:true,
                    isfocus:false
                };
                moodBiz.docFaceBindOnReply($(this), 'textarea_messagebody', config, te);
            });
        }

    }

    function initMask(recevieName) {

        var config = {
            pointerFlag : false,//是否有指针
            tipLayer : true,//是否遮罩
            containTitle : true,//包含title
            containFoot : true,//包含footer
            forclosed:true,
            addClass:true,
            className:"atpop",
            offset:"",
            popwidth:505,
            popscroll:true,
            allowmultiple:true,
            isremovepop:true,
            hideCallback:function() {
                moodBiz.hideFace();
            }
        };
        var htmlObj = new Object();
        var inputStr = '';
        if (recevieName == '') {
            inputStr = '<input type="text" id="text_receivename" class="pd_text" name="" value="">';
        } else {
            inputStr = '<input type="text" id="text_receivename" class="pd_text" name="" value="' + recevieName + '" readonly="true">';
        }
        htmlObj['id'] = 'message_mask'; //设置层ID
        htmlObj['html'] = '<div class="private_div">' +
                '<ul>' +
                '<li>发私信给：</li>' +
                '<li>' + inputStr + '</li>' +
                '<li class="mt10"><span class="left">私信内容：</span><span id="message_body_count" class="right">还可以输入<b>300</b>字</span></li>' +
                '<li><textarea id="textarea_messagebody" name="" cols="" rows="" class="pd_textarea" style="font-family:Tahoma, \'宋体\';"></textarea></li>' +
                '<li><span class="left"><div id="message_mood" class="commenface"></div></span><div id="error_message"></div></li>' +
                '</ul>' +
                '</div>';
        htmlObj['title'] = "<em>发送私信</em>";//设置标题内容
        htmlObj['input'] = '<a class="submitbtn" id="submit_message"><span>发 送</span></a></a>';//设置按钮内容
        pop.popupInit(config, htmlObj);

        $('#submit_message').die().live('click', function() {
            messageBiz.send($('#text_receivename').val(), $('#textarea_messagebody').val(), $('#error_message'), messageCallback.commonCallback);
        });

        $('#textarea_messagebody').die().live('keyup',
                function() {
                    common.checkInputLength(tipsText.comment.blog_reply_length, 'textarea_messagebody', 'message_body_count');
                }).live('keydown', function(event) {
                    common.joymeCtrlEnter(event, $("#submit_message"));
                });
        var te = new TextareaEditor(document.getElementById('textarea_messagebody'));
        $('#message_mood').die().live('click', function() {
            var config = {
                allowmultiple:false,
                isremovepop:true,
                isfocus:true
            };
            moodBiz.docFaceBindOnReply($(this), 'textarea_messagebody', config, te);
        });

    }

    function removeMsgCallback(sendUno, resultMsg) {
        common.locationLoginByJsonObj(resultMsg);
        if (resultMsg.status_code == '1') {
            $("#msg_l_" + sendUno).remove();
        }
    }

    function removeMsgReplyCallback(msgId, resultMsg) {
        common.locationLoginByJsonObj(resultMsg);
        if (resultMsg.status_code == '1') {
            $("#msg_" + msgId).remove();
        }
    }

    function sendMsgCallback(resultMsg, errorJq) {
        common.locationLoginByJsonObj(resultMsg);

        if (resultMsg.status_code == '1') {
            var message = resultMsg.result[0];
            addNewMessage(message);
            $("#messagebody").val('');
            var alertOption = {text:'发送成功',tipLayer:true};
            joymealert.alert(alertOption);
        } else if (resultMsg.status_code == '-5') {
            var alertOption = {text:tipsText.profile.user_ipforbidden_forbidpost,
                tipLayer:true,
                textClass:"tipstext",
                callbackFunction:function() {
                    if (resultMsg.result != null && resultMsg.result.length > 0) {
                        window.location.href = resultMsg.result[0];
                    }
                }};
            joymealert.alert(alertOption);
        } else {
            errorJq.html(resultMsg.msg);
        }

    }

    function addNewMessage(message) {
        var str = '<div class="messages_list clearfix" style="display:none" id="msg_' + message.msgId + '">' +
                '<dl>' +
                '<dt class="personface">' +
                '<a name="atLink" title="' + joyconfig.joyblogname + '">' +
                '<img width="58" height="58" src="' + common.parseSimg(joyconfig.joyheadimg, joyconfig.DOMAIN, joyconfig.joysex) + '">' +
                '</a>' +
                '</dt>' +
                '<dd class="messages">' +
                '<div class="messages_l"></div>' +
                '<p class="p_text">' +
                '<a href="javascript:void(0)"  name="atLink" title="' + joyconfig.joyblogname + '">' + joyconfig.joyblogname + '</a>：' + message.body +
                '</p>' +
                '<p class="delmycomment">' +
                '<a href="javascript:void(0)" name="removeMsgReply" title="删除" id="' + message.msgId + '">×</a>' +
                '</p>' +
                '<p class="p_tools">' +
                '<span>今天&nbsp;&nbsp;' + message.sendDate + '</span>' +
                '</p>' +
                '</dd>' +
                '</dl>' +
                '</div';
        $("div:[id^=msg_]:first").before(str);
        $('#msg_' + message.msgId).slideDown();
    }

    function removeNoticeCallback(noticeId, resultMsg) {
        common.locationLoginByJsonObj(resultMsg);
        if (resultMsg.status_code == '1') {
            $("#notice_l_" + noticeId).remove();
        }
    }

    return message;

});










