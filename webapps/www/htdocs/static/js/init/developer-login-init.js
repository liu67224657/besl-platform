define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../common/jquery.validate.min');
    var common = require('../common/common');
    var ajaxverify = require('../biz/ajaxverify');
    var header = require('../page/header');
    var joymealert = require('../common/joymealert');
    var alertOption = {tipLayer: true, alertFooter: true, alertButtonText: '确 定', timeOutMills: -1,
        width: 400
    };
    require('../common/tips');

    $(document).ready(function() {
        $("#loginForm").validate({
                    rules: {
                        userid: {required:true,email:true},
                        password: {required: true,rangelength: [6,18]}
                    },
                    messages: {
                        userid: {required:tipsText.userLogin.user_email_notnull, email:tipsText.userLogin.user_email_wrong},
                        password:{required:tipsText.userSet.user_userpwd_notnull,rangelength:tipsText.userSet.user_userpwd_length}
                    },
                    showErrors: showErrors//自定义错误提示
                });
        $("#submit_login").bind("click", function() {
            $("#loginForm").submit();
        });

        //回车事件
        $("#password").keyup(function(e) {
            if (e.keyCode == 13) {
                $("#loginForm").submit();
            }
        });
        //小纸条 搜索 返回
        header.noticeSearchReTopInit();

        //鲜游退出登录
        $("#confirmloginout").click(function() {
            alertOption.text = '您是否要退出登录？（是 / 否）';
            alertOption.submitButtonText = "是";
            alertOption.cancelButtonText = "否";
            alertOption.title = '退出';
            alertOption.submitFunction = function newGameLoginOut() {
                $("#newgameloginout").click();
            };
            joymealert.confirm(alertOption);
        });

    });

    function showErrors() {
        //验证失败处理
        for (var i = 0; this.errorList[i]; i++) {
            var error = this.errorList[i];
            var elename = this.idOrName(error.element);
            if (elename == "userid") {
                $("#error_userid").text(error.message);
            }
            if (elename == "password") {
                $("#error_password").text(error.message);
            }
        }
        //验证成功处理
        for (var i = 0; this.successList[i]; i++) {
            var suc = this.successList[i];
            if (suc.id == "userid") {
                $("#error_userid").text();
            }
            if (suc.id == "password") {
                $("#error_password").text('');
            }
        }
    }

    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});