define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../common/jquery.validate.min');
    var common = require('../common/common');
    var ajaxverify = require('../biz/ajaxverify');
    require('../common/tips');

    $(document).ready(function() {
        $("#maskLoginForm").validate({
                    rules: {
                        loginkey: {required:true,email:true},
                        password: {required: true,rangelength: [6,18]}
                    },
                    messages: {
                        loginkey: {required:tipsText.userLogin.user_email_notnull, email:tipsText.userLogin.user_email_wrong},
                        password:{required:tipsText.userSet.user_userpwd_notnull,rangelength:tipsText.userSet.user_userpwd_length}
                    },
                    showErrors: showErrors//自定义错误提示
                });
        $("#log_in_dl").bind("click", function() {
            $("#log_in_dl").attr('class', 'loadbtn').html('<span>登录中…</span>');
            $("#maskLoginForm").submit();
        });

        //回车事件
        $("#password").keyup(function(e) {
            if (e.keyCode == 13) {
                $("#log_in_dl").attr('class', 'loadbtn').html('<span>登录中…</span>');
                $("#maskLoginForm").submit();
            }
        });
        //小纸条 搜索 返回
    });

    function showErrors() {
        //验证失败处理
        for (var i = 0; this.errorList[i]; i++) {
            var error = this.errorList[i];
            var elename = this.idOrName(error.element);
            if (elename == "loginkey") {
                $("#useridTips").text(error.message);
                $("#log_in_dl").attr('class', 'graybtn').html('<span>登录</span>');
            }
            if (elename == "password") {
                $("#passwordTips").text(error.message);
                $("#log_in_dl").attr('class', 'graybtn').html('<span>登录</span>');
            }
        }
        //验证成功处理
        for (var i = 0; this.successList[i]; i++) {
            var suc = this.successList[i];
            if (suc.id == "loginkey") {
                $("#useridTips").text();
            }
            if (suc.id == "password") {
                $("#passwordTips").text('');
            }
        }
    }

    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});