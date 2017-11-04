define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var header = require('../page/header');
    var follow = require('../page/follow');
    var followBiz = require('../biz/follow-biz');
    var followCallback = require('../page/followcallback');
    var card = require('../page/card');

    $().ready(function() {
        card.bindCard(followCallback.followCallBack, followCallback.unFollowOnBlog);

        $('.friend-box').live('mouseenter',
                function() {
                    var id = $(this).attr('id');
                    var uno = id.substr('socialprofile_'.length, id.length - 'socialprofile_'.length);
                    $('#cancel_' + uno).children('a').css('display', '');
                }).live('mouseleave', function() {
                    var id = $(this).attr('id');
                    var uno = id.substr('socialprofile_'.length, id.length - 'socialprofile_'.length);
                    $('#cancel_' + uno).children('a').css('display', 'none');
                });

        $('a[id^=remove_follow_]').live('click', function() {
            var id = $(this).attr('id');
            var uno = id.substr('remove_follow_'.length, id.length - 'remove_follow_'.length);

            var nickName = $('#hid_nickname_' + uno).val();
            follow.unfollow($(this), nickName, uno, followCallback.unFollowOtherOnList);
        });

        $('.add_attention_ok,.add_attention').live('click', function() {
            var id = $(this).attr('id');
            var focusUno = id.substr('follow_'.length, id.length - 'follow_'.length);
            followBiz.ajaxFocus(joyconfig.joyuserno, focusUno, followCallback.followOtherCallBack);
        });
        header.noticeSearchReTopInit();
    });
    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});