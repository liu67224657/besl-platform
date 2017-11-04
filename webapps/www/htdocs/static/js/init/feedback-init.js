define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../common/jquery.validate.min');
    require('../common/jquery.form');
    var common = require('../common/common');
    var ajaxverify = require('../biz/ajaxverify');
    var joymealert = require('../common/joymealert');

    $(document).ready(function() {
        //jquery.validate 进行form表单验证
        $("#feedbackForm").validate({
                    rules:{
                        feedbackBody:{required:true, maxlength:500}
                    },
                    messages:{
                        feedbackBody:{required:"请输入你的反馈意见。", maxlength:"请不要超过500字，更多内容请您分多次提交。"}
                    },
                    showErrors:showErrors//自定义错误提示
                });

        $(".submitbtn").bind("click", function() {
            sendFeedBack();
        });

        var urlStr = window.location.href;
        var text = urlStr.split('/');
        var hrefText = text[text.length - 1];
        if (hrefText) {
            if (hrefText == "aboutus") {
                $("#aboutus").html('<b>' + $("#aboutus").html() + '</b>');
                $("#aboutus").addClass("about-nav-active");
            }
            if (hrefText == "service") {
                $("#service").html('<b>' + $("#service").html() + '</b>');
                $("#service").addClass("about-nav-active");
            }
            if (hrefText == "zhaopin") {
                $("#zhaopin").html('<b>' + $("#zhaopin").html() + '</b>');
                $("#zhaopin").addClass("about-nav-active");
            }
            if (hrefText == "feedback") {
                $("#feedback").html('<b>' + $("#feedback").html() + '</b>');
                $("#feedback").addClass("about-nav-active");
            }
            if (hrefText == "help") {
                $("#help").html('<b>' + $("#help").html() + '</b>');
                $("#help").addClass("about-nav-active");
            }
            if (hrefText == "apply") {
                $("#apply").html('<b>' + $("#apply").html() + '</b>');
                $("#apply").addClass("about-nav-active");
            }
        }
    });

    var showErrors = function() {
        //验证失败处理
        for (var i = 0; this.errorList[i]; i++) {
            var error = this.errorList[i];
            $("#tips").html(error.message);
        }
        //验证成功处理
        for (var i = 0; this.successList[i]; i++) {
            $("#tips").html('');
        }
    }

    var sendFeedBack = function() {
        $("#feedbackForm").ajaxForm(function(data) {
            var jsonObj = eval('(' + data + ')');
            common.locationLoginByJsonObj(jsonObj);
            var alertOption = {};
            if (jsonObj.status_code == "1") {
                alertOption = {text:"发送成功，感谢您对我们工作的支持！",tipLayer:true};
                $("#feedbackBody").val('');
            } else {
                alertOption = {text:jsonObj.msg,tipLayer:true,textClass:"tipstext"};
            }
            joymealert.alert(alertOption);
        });

        $("#feedbackForm").submit();
    }
    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});