define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var header = require('../page/header');
    var joymealert = require('../common/joymealert');
//    var loginBiz = require('../biz/login-biz');
    var contentReg = /[^\u4e00-\u9fa5A-Za-z0-9\-_]+/;
    $().ready(function () {
        header.noticeSearchReTopInit();
        $('#form_modifyrecent').submit(function() {
            var desc = $('#txtarea_recent').val();
            if (desc.length > 200) {
                $("#recent_error").text('游戏描述的文字过长，只能输入200个字！').show();
                return false;
            }
            return true;
        });
    });

    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});