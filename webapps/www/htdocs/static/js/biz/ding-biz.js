/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-5
 * Time: 下午5:17
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var login = require('../biz/login-biz');
    require('../common/tips');
    var joymealert = require('../common/joymealert');
    var ding = {
        ajaxDing:function(contentId, uno, jqDing, callback, dingOption) {
            $.post('/json/content/ding', {contentId:contentId,uno:uno}, function(req) {
                var resultMsg = eval('(' + req + ')');
                login.maskLoginByJsonObj(resultMsg);
                if (resultMsg.status_code == '1') {
                    callback(jqDing, "积分+1", contentId, resultMsg.billingStatus,resultMsg.billingMsg);
                } else if (resultMsg.status_code == '0') {
                    var alertOption = {text:resultMsg.msg,tipLayer:true,textClass:"tipstext"};
                    alertOption = $.extend(alertOption, dingOption)
                    joymealert.alert(alertOption);
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
                }
            });
        }
    }
    var maskLoginByJsonObj = function(jsonObj) {
        if (jsonObj == null || jsonObj.status_code == '-1') {
            //maskLogin();
            alert("请先登录");
            return;
        }
    }
    return ding;
});