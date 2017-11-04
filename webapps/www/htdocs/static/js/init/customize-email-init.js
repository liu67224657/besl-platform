define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../common/jquery.validate.min');
    var common = require('../common/common');
    var ajaxverify = require('../biz/ajaxverify');
    var customize = require('../page/customize');
    var header = require('../page/header');
    var mail = require('../page/getmail');

    require.async('../common/tips');
    $(document).ready(function() {
        $("#form_resetuserid").validate({
                    rules: {
                        password:{required:true,rangelength:[6,18]},
                        newuserid:{required:true, email:true, maxlength:64, checkUserid:""}
                    },
                    messages: {
                        password: {required:tipsText.userSet.user_userpwd_notnull, rangelength:tipsText.userSet.user_userpwd_length},
                        newuserid:{required:tipsText.userSet.user_email_notnull, email:tipsText.userSet.user_email_wrong, maxlength:tipsText.userSet.user_email_length}
                    },
                    showErrors: customize.showValidateTips//使用自定义的提示方法
                });

        $("#modifyemail").bind("click", function() {
            customize.resetuserid();
        });

        $('#goto_email').bind('click', function() {
            mail.hrefMail($(this).attr("data-email"));
        });

        header.noticeSearchReTopInit();
    });

    $.validator.addMethod('checkUserid', function(value, element, params) {
        return ajaxverify.verifyUseridExists(value);
    }, tipsText.userSet.user_email_exists);
    require.async('../common/google-statistics');
    require.async('../common/bdhm');
});