define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../common/jquery.lazyload')($);
    var search = require('../page/search');
    var followCallback = require('../page/followcallback');
    var card = require('../page/card');
    var header = require('../page/header');
    var contentlist = require('../page/contentlist');
    $().ready(function () {
        $(".lazy").scrollLoading();
        card.bindCard(followCallback.followCallBack, followCallback.unFollowOnList);
        contentlist.searchListBind();
        header.noticeSearchReTopInit();
        search.initHoldSearch();
    });
    require.async('../../third/swfupload/swfupload');
    require.async('../../third/swfupload/swfupload.queue');
    require.async('../../third/swfupload/fileprogress');
    require.async('../../third/ckeditor/ckeditor');
    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});