define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../common/jquery.validate.min');
    var header = require('../page/header');
    require('../common/tips');

    $(document).ready(function () {

        $('#resetSubmit').click(function() {
            $('#resetPwdForm').submit();
        });

        //重新设置密码form验证
        $("#resetPwdForm").validate({
                    rules: {
                        password: {required:true, rangelength: [6,18]},
                        repassword: {required: true, rangelength: [6,18], equalTo:"#password"}//使用自定义验证方法
                    },
                    messages: {
                        password: {required:tipsText.userSet.userset_password_notnull, rangelength:tipsText.userSet.user_userpwd_length},
                        repassword: {required:tipsText.userSet.userset_repetpassword_notnull, rangelength:tipsText.userSet.user_userpwd_length,equalTo:tipsText.userSet.userset_userpwd_notequals}
                    },
                    showErrors: showReSetErrors//使用自定义的提示方法
                });
        header.noticeSearchReTopInit();
    });

    function showReSetErrors() {
        //验证失败处理
        for (var i = 0; this.errorList[i]; i++) {
            var error = this.errorList[i];
            var elename = this.idOrName(error.element);
            $("#" + elename + "tips").html(error.message);
            onsubmit: false;
        }
        //验证成功处理
        for (var i = 0; this.successList[i]; i++) {
            var suc = this.successList[i];
            $("#" + suc.id + "tips").html('');
        }
    }

    require.async('../common/google-statistics');
    require.async('../common/bdhm')
})
        ;