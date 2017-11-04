define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var common = require('../common/common');
    var card = require('../page/card');
    var followCallback = require('../page/followcallback');
    var header = require('../page/header');
    var msg = require('../page/message');
    var mood = require('../page/mood');
    require('../common/tips');

    $(document).ready(function() {
        card.bindCard(followCallback.followCallBack, followCallback.unFollowOnList);
        msg.bindSaveMsg();
        msg.bindRemoveMsg();
        msg.bindRemoveMsgReply();
        msg.bindRemoveNotice();
        mood.bindMood("moodFace", "messagebody");
        mood.outCloseMood();

        $("a[name=sendMsgMask]").live("click", function() {
            msg.initMessageMask('');
        });
        common.checkInputLength(tipsText.comment.blog_reply_length, 'messagebody', 'message_num');
        $('#messagebody').live('keyup keydown', function(event) {
            common.checkInputLength(tipsText.comment.blog_reply_length, 'messagebody', 'message_num');
        });
        $('#messagebody').live('keydown', function(event) {
            common.joymeCtrlEnter(event, $("a[name=savemsg]"));
        });
        header.noticeSearchReTopInit();
    });
    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});