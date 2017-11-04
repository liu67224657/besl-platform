/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-2
 * Time: 上午11:12
 * To change this template use File | Settings | File Templates.
 */
define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var jmpopup = require('./jmpopup');

    var defaultAlertOption = {alertid: '',
        title: '提示信息',
        offset: '',
        offsetlocation: [0, 0],
        alertFooter: false,
        alertButtonText: '确 定',
        tipLayer: false,
        width: 240,
        text: '',
        textClass: '',
        callbackFunction: null,
        timeOutMills: 2000,
        forclosed: false,
        popzindex: 100
    };

    var defaultConfirmOption = {confirmid: '',
        title: '提示信息',
        offset: '',
        offsetlocation: [0, 0],
        tipLayer: false,
        width: 240,
        text: '',
        textClass: '',
        submitButtonText: '确 定',
        submitFunction: null,
        cancelButtonText: '取 消',
        cancelFunction: null,
        showFunction: null,
        popzindex: 100
    };
    var defaultPromptOption = {confirmid: '',
        title: '提示信息',
        offset: '',
        offsetlocation: [0, 0],
        tipLayer: false,
        width: 275,
        html: '<div class="bk_tc"><div class="bk_p clearfix"><span></span><input type="text" class="bk_txt"></div></div>',
        text: '',
        textClass: '',
        submitButtonText: '确 定',
        submitFunction: null,
        cancelButtonText: '取 消',
        cancelFunction: null,
        fromLiveFunction: null
    };
    var alertTimoutid;
    var joymeAlert = {

        alert: function (option) {
            option = $.extend({}, defaultAlertOption, option);
            var alertId = 'joymealert_' + option.alertid;
            if ($('#' + alertId).size() > 0) {
                $('#p_alert_text').text(option.text).attr('class', option.textClass);
            }

            var jmpopupConfig = {
                pointerFlag: false,
                pointdir: 'up',
                tipLayer: option.tipLayer,
                containTitle: true,
                containFoot: option.alertFooter,
                offset: option.offset,
                offsetlocation: option.offsetlocation,
                forclosed: option.forclosed,
                popwidth: option.width,
                addClass: false,
                className: "",
                popscroll: false,
                allowmultiple: true,
                isremovepop: true,
                isfocus: false,
                popzindex: option.popzindex
            }
            if (option.callbackFunction != null) {
                jmpopupConfig.hideCallback = option.callbackFunction;
            }
            var htmlObj = new Object();
            htmlObj['id'] = alertId;
            htmlObj['html'] = '<div class="publicuse"><p class="' + option.textClass + '" id="p_alert_text">' + option.text + '</p></div>';
            htmlObj['title'] = '<em>' + option.title + '</em>';
            htmlObj['input'] = '<a id="but_alert" class="submitbtn"><span>' + option.alertButtonText + '</span></a>';
            if (alertTimoutid != null) {
                clearTimeout(alertTimoutid);
            }
            jmpopup.popupInit(jmpopupConfig, htmlObj);
            if (option.timeOutMills != null && !isNaN(option.timeOutMills) && option.timeOutMills > 0) {
                alertTimoutid = setTimeout(function () {
                    $('#' + alertId).fadeOut(function () {
                        jmpopup.hidepop(htmlObj, jmpopupConfig);
                    })
                }, 2000);
            }

            $('#but_alert').die().live('click', function () {
                jmpopup.hidepop(htmlObj, jmpopupConfig);
            });
        },
        confirm: function (option) {
            option = $.extend({}, defaultConfirmOption, option);
            var alertId = 'joymeconfirm_' + option.confirmid;

            var jmpopupConfig = {
                pointerFlag: false,
                pointdir: 'up',
                tipLayer: option.tipLayer,
                containTitle: true,
                containFoot: true,
                offset: option.offset,
                offsetlocation: option.offsetlocation,
                forclosed: true,
                popwidth: option.width,
                addClass: false,
                className: "",
                popscroll: false,
                allowmultiple: false,
                isremovepop: true,
                isfocus: false,
                popzindex: option.popzindex
            }

            var htmlObj = new Object();
            htmlObj['id'] = alertId;
            htmlObj['html'] = '<div class="publicuse"><p class="' + option.textClass + '" id="p_alert_text">' + option.text + '</p></div>';
            htmlObj['title'] = '<em>' + option.title + '</em>';
            htmlObj['input'] = '<div class="ftr"><a class="submitbtn" id="confirm_submit_buutton" href="javascript:void(0)"><span>' + option.submitButtonText + '</span></a>' +
                '<a class="graybtn" id="cancel_submit_buutton" href="javascript:void(0)"><span>' + option.cancelButtonText + '</span></a></div>';
            jmpopup.popupInit(jmpopupConfig, htmlObj);

            $('#confirm_submit_buutton').die().live('click', function () {
                if (option.submitFunction != null) {
                    option.submitFunction.call();
                }
                jmpopup.hidepop(htmlObj, jmpopupConfig);
            });
            $('#cancel_submit_buutton').die().live('click', function () {
                if (option.cancelFunction != null) {
                    option.cancelFunction.call();
                }
                jmpopup.hidepop(htmlObj, jmpopupConfig);
            });
        },
        prompt: function (option) {
            option = $.extend({}, defaultPromptOption, option);
            var alertId = 'joymeconfirm_' + option.confirmid;
            var jmpopupConfig = {
                pointerFlag: false,
                pointdir: 'up',
                tipLayer: option.tipLayer,
                containTitle: true,
                containFoot: true,
                offset: option.offset,
                offsetlocation: option.offsetlocation,
                forclosed: true,
                popwidth: option.width,
                addClass: false,
                className: "",
                popscroll: false,
                allowmultiple: false,
                isremovepop: true,
                isfocus: false,
                showFunction: option.showFunction
            }
            var htmlObj = new Object();
            htmlObj['id'] = alertId;
            htmlObj['html'] = option.html;
            htmlObj['title'] = '<em>' + option.title + '</em>';
            htmlObj['input'] = '<div class="ftr"><a class="submitbtn" id="confirm_submit_buutton"><span>' + option.submitButtonText + '</span></a>' +
                '<a class="graybtn" id="cancel_submit_buutton"><span>' + option.cancelButtonText + '</span></a></div>';
            jmpopup.popupInit(jmpopupConfig, htmlObj);
            $('#confirm_submit_buutton').die().live('click', function () {
                if (option.submitFunction != null) {
                    if (option.submitFunction.call()) {
                        return;
                    }
                }
                jmpopup.hidepop(htmlObj, jmpopupConfig);
            });
            $('#cancel_submit_buutton').die().live('click', function () {
                option.cancelFunction;
                jmpopup.hidepop(htmlObj, jmpopupConfig);
            });
            if (option.fromLiveFunction != null) {
                option.fromLiveFunction.call();
            }
        },
        discoveryAlertConfirm: function () {//调整弹出层在随便看看页
            if ($("#joymealert_").length > 0) {
                var styleStr = $("#joymealert_").attr('style');
                $("#joymealert_").attr('style', styleStr.replace('absolute', 'fixed'));
                $("#joymealert_").css({top: $(window).height() / 2 - $("#joymealert_").height() / 2});
            }
            if ($("#joymeconfirm_atNicksTips").length > 0) {
                var styleStr = $("#joymeconfirm_atNicksTips").attr('style');
                $("#joymeconfirm_atNicksTips").attr('style', styleStr.replace('absolute', 'fixed'));
                $("#joymeconfirm_atNicksTips").css({top: $(window).height() / 2 - $("#joymeconfirm_atNicksTips").height() / 2});
            }
        }


    }

    return joymeAlert;
});

