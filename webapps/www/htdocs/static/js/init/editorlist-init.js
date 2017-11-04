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
    var header = require('../page/header');
    var postChat = require('../page/post-chat');
    var postText = require('../page//post-text');
    var postOption = require('../page/post-option');
    var contentlist = require('../page/contentlist');
    var common = require('../common/common');
    window.blogContent = {content:'',audio:null,video:null,image:new Array()};//设置全局对象
    window.imgLocaL = 0;
    window.memoContent = true;
    (function() {
        header.noticeSearchReTopInit();
        $(".lazy").scrollLoading();
        postChat.postChatInit(postOption.editorOption);
        postText.posttextinit(postOption.editorOption);
        contentlist.initBind();

        $("a").live('click', function() {
            $(this).blur();
        });
        common.jmPostConfirm(null);
    })();
    require.async('../../third/swfupload/swfupload');
    require.async('../../third/swfupload/swfupload.queue');
    require.async('../../third/swfupload/fileprogress');
    require.async('../../third/ckeditor/ckeditor');
    require.async('../common/google-statistics');
    require.async('../common/bdhm');

});