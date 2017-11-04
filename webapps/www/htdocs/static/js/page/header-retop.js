//todo @Deprecated
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');

    var search = {
        initReTop:function() {
            $(window).scroll(function() {
                if ($(window).scrollTop() > $(window).height()) {
                    $("#linkHome").fadeIn();
                } else {
                    $("#linkHome").fadeOut();
                }
                reScrollTopLeft();
            });
            $(window).resize(function() {
                reScrollTopLeft();
            })
            //滚动条返回顶部
            $("#linkHome").live('click', function() {
                window.scrollTo($(window).scrollTop(), 0);
            });
        }
    }
    var reScrollTopLeft = function() {
        if ($(window).width() > 940) {
            $(".scroll_top").css("right", ($(window).width() - 940) / 2 - 92 + 'px');
        } else {
            $(".scroll_top").css("right", 0);
        }
    }
    return search;
});