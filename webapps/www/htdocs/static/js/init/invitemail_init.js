define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../common/jquery.form');
    var common = require('../common/common');
    var header = require('../page/header');
    var login = require('../biz/login-biz');
    var invite = require('../page/invite');
    var joymealert = require('../common/joymealert');

    $().ready(function() {
        $('#check_all').click(function() {
            if ($(this).attr('checked') == true) {
                $('input[id^=inviteMails_]').attr('checked', true);
            } else {
                $('input[id^=inviteMails_]').attr('checked', false);
            }
        });

        $('input[type=checkbox][id^=inviteMails_]').click(function() {
            if (!$(this).attr('checked')) {
                $('#check_all').attr('checked', false);
            }
        });

        $('#uncheck_all').click(function() {
            var inviteMails = $('input[id^=inviteMails_]');
            inviteMails.each(function(i, val) {
                var jqMailCheckBox = $(val);
                jqMailCheckBox.attr('checked', !jqMailCheckBox.attr('checked'));
            });
        });
        $("#form_invitemails").ajaxForm(function(data) {
            var jsonObj = eval('(' + data + ')');
            login.locationLoginByJsonObj(jsonObj);
            if (jsonObj.status_code == "1") {
                //弹出层提示
                var alertOption = {text:'发送成功',tipLayer:true,callbackFunction:function() {
                    locationHref();
                }};
                joymealert.alert(alertOption);
            } else {
                var alertOption = {text:'发送成功',tipLayer:true};
                joymealert.alert(alertOption);
            }
        });

        $('#link_import_submit').click(function() {
            var jqError = $('#form_error');
            if ($('input[id^=inviteMails]:checked').length == 0) {
                jqError.text(tipsText.invite.invite_mails_not_null);
                return false;
            }

            if ($('#mailTitle').val().length >= 16) {
                jqError.text(tipsText.invite.invite_mailtitle_out_length);
                return false;
            }

            if ($('#mailBody').val().length >= 140) {
                jqError.text(tipsText.invite.invite_mailBody_out_length);
                return false;
            }


            $('#form_invitemails').submit();
        });

        $('#priviewMail').click(function() {
            $('#div_invite_mail').slideDown();
        });

        $('#hideMail').click(function() {
            $('#div_invite_mail').slideUp();
        });
        header.noticeSearchReTopInit();
    });
    function locationHref() {
        window.location.href = joyconfig.URL_WWW + "/invite/invitepage";
    }

    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});