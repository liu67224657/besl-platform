define(function(require, exports, module) {
    var common = require('../common/common');
    var pop = require('../common/jmpopup');
    var joymealert = require('../common/joymealert');
    var moodBiz = require('../biz/mood-biz');

    var messageCallback = {
        commonCallback:function(resultMsg, errorJq) {
            pop.hidepopById('message_mask', true, true, moodBiz.hideFace);
            if (resultMsg.status_code == '1') {
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
                var alertOption = {text:resultMsg.msg,tipLayer:true,textClass:"tipstext"};
                joymealert.alert(alertOption);
            }
        }
    }

    return messageCallback;

});










