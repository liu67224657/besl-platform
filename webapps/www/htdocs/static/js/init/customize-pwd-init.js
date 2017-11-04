define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../common/jquery.validate.min');
    var common = require('../common/common');
    var customize = require('../page/customize');
    var header = require('../page/header');
    require('../common/tips');

    $(document).ready(function() {
        //重新设置密码form验证
        $("#form_resetpwd").validate({
                    rules: {
                        oldpwd: {required:true, rangelength: [6,18]},
                        newpwd: {required: true, rangelength: [6,18]},
                        repeatpwd: {required: true, rangelength: [6,18], equalTo:"#newpwd"}//使用自定义验证方法
                    },
                    messages: {
                        oldpwd: {required:tipsText.userSet.userset_password_notnull, rangelength:tipsText.userSet.user_userpwd_length},
                        newpwd: {required:tipsText.userSet.userset_newpassword_notnull, rangelength:tipsText.userSet.user_userpwd_length},
                        repeatpwd: {required:tipsText.userSet.userset_repetpassword_notnull, rangelength:tipsText.userSet.user_userpwd_length,equalTo:tipsText.userSet.userset_userpwd_notequals}
                    },
                    showErrors: customize.showValidateTips//使用自定义的提示方法
                });

        $(".submitbtn").bind("click", function() {
            customize.resetPwd();
        });

        header.noticeSearchReTopInit();
    });
    require.async('../common/google-statistics');
    require.async('../common/bdhm');
});