define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var card = require('../page/card');
    var header = require('../page/header');
    var contentlist = require('../page/contentlist');
    var followCallback = require('../page/followcallback');
    var game = require('../page/game');
    var postText = require('../page/post-text');
    var postOption = require('../page/post-option');
    var invite = require('../page/invite');
    var common = require('../common/common');
    window.blogContent = {content:'',audio:null,video:null,image:new Array()};//设置全局对象
    window.imgLocaL = 0;
    $().ready(function () {
        card.bindCardUnClick(followCallback.followCallBack, followCallback.unFollowOnList);
        contentlist.talkBoardListBind();
        header.noticeSearchReTopInit();
        game.followGroup();
        game.userLoginSub();
        game.handleOther();
        postText.posttextinit(postOption.talkOption);
        invite.gameTalkLink();
       common.jmPostConfirm(null);
    });
    require.async('../../third/swfupload/swfupload');
    require.async('../../third/swfupload/swfupload.queue');
    require.async('../../third/swfupload/fileprogress');
    require.async('../../third/ckeditor/ckeditor');
    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});