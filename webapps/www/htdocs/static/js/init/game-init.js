define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var card = require('../page/card');
    var header = require('../page/header');
    var followCallback = require('../page/followcallback');
    var loginBiz = require('../biz/login-biz');
    var rollanimate = require('../page/rollanimate');
    var contentlist = require('../page/contentlist');
    var joymealert = require('../common/joymealert');
    $().ready(function () {
        card.bindCardUnClick(followCallback.followCallBack, followCallback.unFollowOnList);
        header.noticeSearchReTopInit();




        if ($('[id^=roll_img_]').length > 1) {
            rollanimate.rollanimate();
        }

        contentlist.talkBoardListBind();

        $('a[name=post_link]').click(function() {
            if (joyconfig.joyuserno == '') {
                loginBiz.maskLoginByJsonObj();
                return false;
            }
        });

        $('a[name=delmoudle_link]').click(function() {
            if (joyconfig.joyuserno == '') {
                loginBiz.maskLoginByJsonObj();
                return false;
            }
            var mid=$(this).attr('data-mid');
            joymealert.confirm({confirmid:'delmoudle_confirm',
                        width:240,
                        text:'确定要删除该模块么？',
                        textClass:'',
                        submitFunction:function(){
                           window.location.href=joyconfig.URL_WWW+'/game/'+gameCode+'/edit/delmodule?mid='+mid;
                        },
                        popzindex:16002
                    });
        });

        $('a[name=menulink]').click(function() {
            $('#ul_game_menu a').removeAttr('class');
            $(this).attr('class', 'current');
        });

        $('#game_desc_toggle').click(function() {
            var linkClass = $(this).attr('class');
            if (linkClass == 'c-open') {
                $(this).attr('class', 'c-close').text('收起');
            } else {
                $(this).attr('class', 'c-open').text('查看全部');
            }
            var showObj = $('#game_desc');
            var hideObj = $('#game_desc_hide')
            var hideDesc = hideObj.html();
            var showDesc = showObj.html();
            showObj.html(hideDesc);
            hideObj.html(showDesc);
        });

        $('a[name=link_setlayout]').click(function() {
            var option = $(this).next();
            $('.options').attr('style', '');
            if (option.is(':hidden')) {
                $('.options-box').hide();
                option.show();
                $(this).parent().css('z-index', 10)
            } else {
                $('.options-box').hide();
            }
        })
    });

    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});