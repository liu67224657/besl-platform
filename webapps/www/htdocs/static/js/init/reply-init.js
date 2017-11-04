/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-3
 * Time: 下午1:56
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var header = require('../page/header');
    var common = require('../common/common');
    var card = require('../page/card');
    var followCallback = require('../page/followcallback');
    var mood = require('../page/mood');
    var contentOper=require('../page/content-operate');
    var comment = require('../page/comment');
    require('../common/atme');
    require('../common/tips');

    (function() {
        card.bindCard(followCallback.followCallBack, followCallback.unFollowOnList);
        contentOper.receiveReplyList();
        header.noticeSearchReTopInit();

        //焦点失去隐藏判断
        $("body").die().live('mousedown', function() {
            if (window.isOut) {
                $(".pop").hide();
                $("#mood").remove();
                hideFocusBtn();
                isOut = true;
            }
        });


    })();
    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});