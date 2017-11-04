define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var joymealert = require('../common/joymealert');
    var common = require('../common/common');
    var header = require('../page/header');
    var confirmOption = {
        confirmid: 'confirm_form',
        width: 229,
        submitButtonText: '这就是我',
        cancelButtonText: '再考虑下',
        submitFunction: function () {
            $('form').submit();
        },
        cancelFunction: function () {
        }};
    $(document).ready(function () {
        header.noticeSearchReTopInit();
        $('#savenick').click(function () {
            var nick = $('#nick').val();
            if (nick == null || nick.length == 0) {
                $('#message').html('用户昵称不能为空').attr('style', 'display:inline');
                return false;
            }

            var reg = /^[\u4e00-\u9fa5a-zA-Z0-9]+$/gi;
            if (!reg.test(nick)) {
                $('#message').html('昵称不能含有特殊字符').attr('style', 'display:inline');
                return false;
            }

            var oValLength = 0;
            nick.replace(/n*s*/, '') == '' ? oValLength = 0 : oValLength = nick.match(/[^ -~]/g) == null ? nick.length : nick.length + nick.match(/[^ -~]/g).length;
            if (oValLength < 4) {
                $('#message').html('您输入的昵称过短,2-10汉字或4-20字母').attr('style', 'display:inline');
                return false;
            } else if (oValLength > 20) {
                $('#message').html('您输入的昵称过长,2-10汉字或4-20字母').attr('style', 'display:inline');
                return false;
            }


            if (nick != null && nick.length > 0) {
                confirmOption.text = '您的昵称将定为：' + nick + '<br>今后将不能修改，您确定吗？';
                joymealert.confirm(confirmOption);
            }
        });


        $('#skip_link').click(function (event) {
            event.preventDefault();
            skipOne();
        });

        $('#nick').focus(function () {
            $('#message').hide();
        });
    });

    function skipOne() {
        var nick = $('#nick').val();
        var reg = /^[\u4e00-\u9fa5a-zA-Z0-9]+$/gi;
        if (!reg.test(nick)) {
            $('#message').html('昵称不能含有特殊字符').attr('style', 'display:inline');
            return;
        }
        var skipConfirmOption = {
            confirmid: 'confirm_skip_form',
            width: 229,
            submitButtonText: '还是改了吧',
            cancelButtonText: '回头再说',
            text: '即使跳过设置昵称步骤，在后面遇到部分功能，还是会需要你确认昵称哦~',
            submitFunction: function () {

            },
            cancelFunction: function () {

                window.location.href = $('#skip_link').attr("href");
            }
        };

        joymealert.confirm(skipConfirmOption);
    }
});