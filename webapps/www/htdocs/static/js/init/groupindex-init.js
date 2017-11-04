/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-3
 * Time: 下午1:56
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var header = require('../page/header');
    var follow = require('../page/follow');
    $(document).ready(function() {
        header.noticeSearchReTopInit();
        follow.indexGroupBind();

        var gamePage = 1;
        var gameUl = $('[id^=game_ul_]');
        var maxGamePage = gameUl.length;
        $("a[name=game_page]").bind('click', function() {
            var curPage = gamePage;

            var type = $(this).attr('data-ptype');
            switch (type) {
                case 'num':
                    gamePage = $(this).attr('data-pno');
                    break;
                case 'last':
                    if ($(this).hasClass('pagebtn_left')) {
                        return;
                    }
                    gamePage = gamePage - 1 < 0 ? 1 : gamePage - 1;
                    break;
                case 'next':
                    if ($(this).hasClass('pagebtn_right')) {
                        return;
                    }
                    gamePage = gamePage + 1 > maxGamePage ? maxGamePage : gamePage + 1;
                    break;
            }

            $('#page_area').animate({left:(-620 * (gamePage - 1)) + 'px'});

            //页码效果
            $('a[name=game_page][data-ptype=num]').attr('class', 'pagebtn_small');
            $('a[name=game_page][data-pno=' + gamePage + ']').attr('class', 'pagebtn_small_current');
            if (gamePage == 1) {
                $('a[name=game_page][data-ptype=last]').attr('class', 'pagebtn_left');
                $('a[name=game_page][data-ptype=next]').attr('class', 'pagebtn_right_current');
            } else if (gamePage == maxGamePage) {
                $('a[name=game_page][data-ptype=last]').attr('class', 'pagebtn_left_current');
                $('a[name=game_page][data-ptype=next]').attr('class', 'pagebtn_right');
            } else {
                $('a[name=game_page][data-ptype=last]').attr('class', 'pagebtn_left_current');
                $('a[name=game_page][data-ptype=next]').attr('class', 'pagebtn_right_current');
            }
        })
    })
    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});