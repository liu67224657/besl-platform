define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var follow = require('../page/follow');
    var followBiz = require('../biz/follow-biz');
    var followCallback = require('../page/followcallback');
    var header = require('../page/header');
    var card = require('../page/card');

    $().ready(function() {
        card.bindCard(followCallback.fansfollowCallBack, followCallback.unFollowOnFansList);

        //焦点失去隐藏判断
        $("body").die().live('mousedown', function() {
            if (window.isOut) {
                $(".pop").hide();
                hideFocusBtn();
                isOut = true;
            }
        });

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


        $('a[id^=remove_fans_]').live('click', function() {
            var id = $(this).attr('id');
            var uno = id.substr('remove_fans_'.length, id.length - 'remove_fans_'.length);

            var nickName = $('#hid_nickname_' + uno).val();
            follow.removeFans($(this), nickName, uno, followCallback.unFansOnList);
        });

        $('.add_attention_ok,.add_attention').live('click', function() {
            var id = $(this).attr('id');
            var focusUno = id.substr('follow_'.length, id.length - 'follow_'.length);
            followBiz.ajaxFocus(joyconfig.joyuserno, focusUno, followCallback.fansfollowCallBack);
        });
        header.noticeSearchReTopInit();
    });
    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});