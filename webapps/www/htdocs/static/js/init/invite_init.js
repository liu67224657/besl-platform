define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../common/jquery.validate.min');
    require('../common/jquery.form');
    var common = require('../common/common');
    var header = require('../page/header');
    var verify = require('../biz/ajaxverify');
    var login = require('../biz/login-biz');
    var invite = require('../page/invite');
    var tips = require('../common/tips');
    var joymealert = require('../common/joymealert');

    $().ready(function() {
        $("#copy_invite_link").click(function() {
            invite.clicpInviteLink();
        });

        $('#ipt_invite_link').focus(function() {
            invite.recoverInviteLink();
        });

        $('#priviewMail').click(function() {
            $('#div_invite_mail').slideDown();
        });

        $('#hideMail').click(function() {
            $('#div_invite_mail').slideUp();
        });

        $("#form_mail").validate({
                    rules: {
                        inviteMail:{required:true,email:true},
                        mailTitle:{checkNickNameLength:'',checkNickName:''},
                        mailBody:{maxlength:140}
                    },
                    messages: {
                        inviteMail: {required:tips.invite.invite_mail_not_null,email:tips.invite.invite_mail_illegl},
                        mailTitle:{},
                        mailBody:{maxlength:tips.invite.invite_mailBody_out_length}
                    },
                    showErrors: showMailInviteError
                });
        $("#form_mail").ajaxForm(function(data) {
            $('#error_mail_invite').text('');
            var jsonObj = eval('(' + data + ')');
//            login.locationLoginByJsonObj(jsonObj);

            if (jsonObj.status_code == "1") {
                //弹出层提示
                $('#inviteMail').val('');
                var alertOption = {text:jsonObj.msg,tipLayer:true};
                joymealert.alert(alertOption);
                $('#div_invite_mail').slideUp();
            } else {
                $("#error_mail_invite").text(jsonObj.msg);
            }
        });
        $('#send_invite_mail').click(function() {
            $('#form_mail').submit();
        });

        $('#link_import_submit').click(function() {
            var improtPwd = $('#importPwd').val();
            var importMail = $('#importMail').val();
            var mailProvider = $('#mailProvider').val();
            var jqError = $('#import_error');
            jqError.text('');
            if (importMail == '' || importMail.length == 0) {
                jqError.text(tips.invite.invite_mail_not_null);
                return false;
            }
            if (/[.|@]/.test(importMail)) {
                jqError.text(tips.invite.invite_mail_illegl);
                return false;
            }
            if (improtPwd == '' || improtPwd.length == 0) {
                jqError.text(tips.invite.invite_pwd_not_null);
                return false;
            }
            if (mailProvider == '' || mailProvider.length == 0) {
                jqError.text(tips.invite.invite_provider_not_null);
                return false;
            }
            $('#import_mail_tips').css('display', '');

            $('#form_import_mail').submit();
        });
        header.noticeSearchReTopInit();
    });

    function showMailInviteError() {
        for (var i = 0; this.errorList[i]; i++) {
            var error = this.errorList[i];
//            var elename = this.idOrName(error.element);

            $("#error_mail_invite").text(error.message);

        }
    }

    $.validator.addMethod('checkNickNameLength', function(value, element, params) {
        var inputNum = common.getInputLength(value);
        var result = false;
        inputNum = !inputNum ? 0 : inputNum;
        if (inputNum >= 2 && inputNum <= 16) {
            result = true;
        }
        return result;
    }, tips.invite.invite_mailtitle_out_length);

    $.validator.addMethod('checkNickName', function(value, element, params) {
        return verify.verifyInviteTiltie(value)
    }, tips.userSet.user_word_illegl);
    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});