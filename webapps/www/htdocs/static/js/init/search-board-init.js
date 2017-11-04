define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var header = require('../page/header');
    var search = require('../page/search');
    var followCallback = require('../page/followcallback');
    var follow = require('../page/follow');
    var card= require('../page/card');

    $().ready(function() {
        card.bindCardUnClick(followCallback.followCallBack,followCallback.unFollowOnList);
        header.noticeSearchReTopInit();
        search.initHoldSearch();
        follow.indexGroupBind();
    });
    require.async('../common/google-statistics');
    require.async('../common/bdhm');
});