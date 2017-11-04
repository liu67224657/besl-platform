define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../common/jquery.lazyload')($);
    var card = require('../page/card');
    var followCallback = require('../page/followcallback');
    var header = require('../page/header');
    var contentEffective = require('../page/content-effective');
    var contentOper = require('../page/content-operate');
    var delBlog = require('../page/delblog');
    require('../common/atme');
    $(document).ready(function() {
        $(".lazy").scrollLoading();
        //名片注册方法
        card.bindCard(followCallback.followCallBack, followCallback.unFollowOnList);
        delBlog.bindDel()
        contentEffective.togglePreviewVideo();
        contentEffective.togglePreviewPic();
        contentEffective.toggleAllContent();
        contentOper.favlistLive();
        header.noticeSearchReTopInit();
    });

    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});