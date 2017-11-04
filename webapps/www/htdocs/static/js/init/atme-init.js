define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../common/jquery.lazyload')($);
    var header = require('../page/header');
    var card = require('../page/card');
    var followCallback = require('../page/followcallback');
    var contentlist = require('../page/contentlist');
    require('../common/atme');
    var atme = require('../page/atme');
    $(document).ready(function() {
        $(".lazy").scrollLoading();
        //名片注册方法
        card.bindCard(followCallback.followCallBack, followCallback.unFollowOnList);
        contentlist.atmeBind();
        //评论的事件

        //删除
        atme.bindRemoveAt();
        header.noticeSearchReTopInit();
        //焦点失去隐藏判断
        $("body").die().live('mousedown', function() {
            if (window.isOut) {
                $(".pop").hide();
                $("#mood").remove();
                isOut = true;
            }
        });
    });
    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});