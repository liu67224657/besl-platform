/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-3
 * Time: 下午1:56
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../common/jquery.lazyload')($);
    var card = require('../page/card');
    var header = require('../page/header');
    var followCallback = require('../page/followcallback');
    var contentlist = require('../page/contentlist');
    $(document).ready(function() {
        $(".lazy").scrollLoading();
        card.bindCard(followCallback.followCallBack, followCallback.unFollowOnList);
        contentlist.otherBind();
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
    })
    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});