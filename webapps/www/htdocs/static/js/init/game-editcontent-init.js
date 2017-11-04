define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var header = require('../page/header');
    var loginBiz = require('../biz/login-biz');
    var gameEditObj = require('../page/game-editcontent');

    $().ready(function () {
        header.noticeSearchReTopInit();

        $('#add_content').click(function() {
            gameEditObj.addContent();
        });

        $('.editor-btn-remove').click(function() {
            gameEditObj.delContent($(this));
        });

        $('.editor-btn-edit').click(function() {
            gameEditObj.editContent($(this));
        });

    });

    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});