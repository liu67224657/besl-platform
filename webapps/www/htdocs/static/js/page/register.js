define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../common/jquery.validate.min');
    var common = require('../common/common');
    var ajaxverify = require('../biz/ajaxverify');
    require('../common/tips');
    var joymealert = require('../common/joymealert');

    var register = {
        turnDefaultTips:function(jqObj) {
            if (jqObj.attr("name") == "userid") {
                $("#useridTips").removeClass().addClass("rs_7");
                $("#useridTips").html('请输入您的常用邮箱，这将作为您的登录账号');
                $("#useridTips").css("display", "inline");
            }
            if (jqObj.attr("name") == "userpwd") {
                $("#userpwdTips").removeClass().addClass("rs_7");
                $("#userpwdTips").html('密码可以是6-18位半角字符（数字、<br/>字母、符号）区分大小写');
                $("#userpwdTips").css("display", "inline");
            }
            if (jqObj.attr("name") == "nickname") {
                $("#nicknameTips").removeClass().addClass("rs_7");
                $("#nicknameTips").html('这是您在着迷的名字');
                $("#nicknameTips").css("display", "inline");
            }
            if (jqObj.attr("name") == "blogdomain") {
                $("#blogdomainTips").removeClass().addClass("rs_7");
                $("#blogdomainTips").html('您可以命名一个属于自己的域<br/>名（5-20位英文），让你的着<br/>迷博客从网址开始就与众不同');
                $("#blogdomainTips").css("display", "inline");
            }
        },
        turnOffDefaultTips:function(jqObj) {
            if ($('#' + jqObj.attr("name") + 'Tips').length > 0 && $('#' + jqObj.attr("name") + 'Tips').attr("class") == "rs_7") {
                $('#' + jqObj.attr("name") + 'Tips').css("display", "none");
            }
        },
        turnErrorTips:function(elementId, msg) {
            $('#' + elementId + "Tips").removeClass().addClass("rs_8");
            $('#' + elementId + "Tips").html(msg);
            $('#' + elementId + "Tips").css("display", "inline");
        },
        turnSucTips:function(elementId) {
            $('#' + elementId + "Tips").css("display", "none");
            $('#' + elementId + "Tips").html('');
        },
        saveInfo:function() {
            if ($("#headIconSetDiv").is(":hidden")) {
                $('#form1').submit();

            } else {
                //头像未保存，是否保存？
                var confirmOption = {
                    text:'您的头像还没有保存，是否保存？',
                    width:229,
                    submitButtonText:'确 定',
                    submitFunction:register.saveContinue,
                    cancelButtonText:'取 消',
                    cancelFunction:null};
                joymealert.confirm(confirmOption);

            }
        },
        saveContinue:function() {
            $('#form1').submit();
        }

    }

    function checkGames() {
        //验证输入的最近玩的游戏
        var result = true;
        $("input[name=gamename]").each(function(i, val) {
            if (val.value != "最近在玩的游戏" && result) {
                result = ajaxverify.verifySysWord(val.value);
                if (!result) {
                    var alertOption = {text:tipsText.userSet.user_word_illegl,tipLayer:true,textClass:"tipstext"};
                    joymealert.alert(alertOption);
                    return result;
                }
            } else {
                val.value = '';
            }
        });
        return result;
    }

    return register;
});