define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var header = require('../page/header');
    var card = require('../page/card');
    var contentlist = require('../page/contentlist');
    var followCallback = require('../page/followcallback');
    $(document).ready(function() {
        card.bindCardUnClick(followCallback.followCallBack, followCallback.unFollowOnList);
        contentlist.talkBoardListBind();
        header.noticeSearchReTopInit();
    })
    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});