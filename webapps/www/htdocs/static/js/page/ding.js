/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-5
 * Time: 下午5:21
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var dingbiz = require('../biz/ding-biz');
    var common = require('../common/common');
    var login = require('../biz/login-biz');
    var dingObj = {
        dingInit:function() {
            $("a[id^=link_ding_]").live('click', function() {
                if (login.checkLogin($(this))) {
                    var id = $(this).attr("data-blogid");
                    var uno = $(this).attr("data-uno");
                    dingbiz.ajaxDing(id, uno, $(this), setDingAddShow);
                }
            })
        }

    }
    var setDingAddShow = function (jqDing, text, contentId, billingStatus, billingMsg) {
        if (billingStatus) {
//            var joymealert = require('../common/joymealert');
//            var alertOption = {text:billingMsg,tipLayer:false,textClass:"tipstext"};
//            joymealert.alert(alertOption);
            $("body").append("<div id='dingAddShow_" + contentId + "' style='position: absolute;color:#FF8773; display:none; z-index:10000;font-size:12px;'>" + text + "</div>");
            $("#dingAddShow_" + contentId).css({left:jqDing.offset().left + "px",top:jqDing.offset().top - 20 + "px"}).show().animate({
                        top:'-=7',
                        opacity:'0'
                    }, 1000, function() {
                $(this).remove();
            });
        }
        var num = common.getTimes(jqDing) + 1;
        $('a[id^=link_ding_][data-blogid^=' + contentId + ']').text("(" + num + ")").attr('class', 'praise_on');
    }
    return dingObj;
});