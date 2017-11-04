/**
 * Created by IntelliJ IDEA.
 * User: zhaoxin
 * Date: 12-2-1
 * Time: 下午2:24
 */
define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../common/jquery.validate.min');
    var header = require('../page/header');
    require('../common/tips');

    $(document).ready(function () {
        //
        $('#a_send').click(function() {
            $('#fm').submit();
        });


        // 不正确的，未修改好的。
        $("#fm").validate({
                    rules:{
                        userid:{required:true, email:true}
                    },
                    messages:{
                        userid:{required:tipsText.userPwd.user_pwd_forgot_mail_notnull, email:tipsText.userPwd.user_pwd_forgot_mail_error}

                    },
                    showErrors: showValidateTips
                });
        header.noticeSearchReTopInit();
    });
    function showValidateTips() {
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