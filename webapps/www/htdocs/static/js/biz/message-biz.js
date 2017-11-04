define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var login = require('./login-biz');
    var ajaxverify = require('./ajaxverify');
    var common = require('../common/common');

    var message = {
        send:function (receName, messagebody, errorJq, callback) {
            if (receName == null || receName.length == 0) {
                errorJq.html(tipsText.joyMessage.message_receiver_empty);
                return false;
            }

            if (!ajaxverify.verifyNickNameExists(receName)) {
                errorJq.html(tipsText.joyMessage.message_receiver_notexists);
                return  false;
            }
            if (messagebody == null || messagebody.length == 0) {
                errorJq.html(tipsText.joyMessage.message_content_empty);
                return false;
            }
            var messBodyLen = common.strLen(messagebody);

            if (messBodyLen > 300) {
                errorJq.html(tipsText.joyMessage.message_content_length);
                return false;
            }
            if (!ajaxverify.verifyPost(messagebody)) {
                errorJq.html(tipsText.joyMessage.message_body_illegl);
                return  false;
            }
//            $(subDom).attr("disabled", "disabled");
            $.post('/json/message/private/send', {receivename:receName,messagebody:messagebody}, function(req) {
                var resultMsg = eval('(' + req + ')');
                login.locationLoginByJsonObj(resultMsg);
                callback(resultMsg, errorJq);
            });
//            setTimeout(function() {
//                $(subDom).removeAttr("disabled");
//            }, 2000)
        },
        removeMsg:function(sendUno, callback) {
            $.post("/json/message/private/deletegroup", {sendUno:sendUno}, function(req) {
                var resultMsg = eval('(' + req + ')');
                callback(sendUno, resultMsg);
            });
        },
        removeMsgReply:function(msgId, callback) {
            $.post("/json/message/private/delete", {messageid:msgId}, function(req) {
                var resultMsg = eval('(' + req + ')');
                callback(msgId, resultMsg);
            });
        },
        removeNotice:function(noticeId,callback) {
            $.post("/json/message/notice/delete", {noticeid:noticeId}, function(req) {
                var resultMsg = eval('(' + req + ')');
                callback(noticeId,callback);
            });
        }

    }

    return message;
});






