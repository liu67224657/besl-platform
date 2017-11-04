/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-3
 * Time: 下午1:56
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var search = require('../page/header-search');
    $(document).ready(function() {
        search.initSearch();
        moveUserNav();
        navscroll();
        bindGameMenu();
    })

    var currentTop = 0;
    var timeout = false;

    function navscroll() {
        $(window).scroll(function(event) {
            if (timeout) {
                clearTimeout(timeout);
            }
            timeout = setTimeout(function() {
                scrollMove($(window).scrollTop(), 57);
            }, 100);
        })
        scrollMove($(window).scrollTop(), 57);
    }

    function scrollMove(scrollTop, headt) {
        var navDom = $("#joyme-head-2013");

        //想下滚动或者定位不到头部的位置不随这滚动
        if (scrollTop == undefined || scrollTop > currentTop) {
            navDom.attr('style', 'position:relative;');
            $('body').css('padding-top', '0px');
            currentTop = scrollTop;
            return;
        }

        //向上滚动时候
        if (scrollTop > headt) {
            navDom.attr('style', 'position:fixed;');
            $('body').css('padding-top', '47px');
        } else {
            navDom.attr('style', 'position:relative;');
            $('body').css('padding-top', '0px');
        }
        currentTop = scrollTop;
    }

    function moveUserNav() {
        showHideMethods($('#header_func_link'), 100, 'headerNav', function(dom) {
            if (!$("#memo_message").is(":hidden")) {
                $("#memo_message").hide();
            }
            $('#header_func_area').css({'left':$("#header_func_link").offset().left - 10 + 'px','top':'37px'}).slideDown();
        }, $('#header_func_area'), function(dom) {
            if ($("#memo_message>div.tipsc>p").size() > 0) {
                $("#memo_message").slideDown();
            }
        }, 100, 'headertimer');//showin
        showHideMethods($("#header_func_area"), 100, 'headerNav', null, $('#header_func_area'), function() {
            if ($("#memo_message>div.tipsc>p").size() > 0) {
                $("#memo_message").slideDown();
            }
        }, 100, 'headertimer'); //hideout
    }

    function showHideMethods(dom, responseTime, shwoTimer, showFun, hidedom, hideFun, showTime, timer) {//触发dom,响应时间,响应用timer,显示方法,隐藏dom,隐藏方法,显示时间,控制用timer
        dom.live("mouseenter mouseleave", function(event) {
            var _this = $(this)
            if (event.type == 'mouseenter') {
                clearTimeout(window[timer]);
                if (showFun != null) {
                    try {
                        window[shwoTimer] = setTimeout(function() {
                            showFun(_this);
                        }, responseTime)
                    } catch(ex) {
                        console.log("message:" + ex.message + " line:" + ex.lineNumber);
                    }
                }
            } else if (event.type == 'mouseleave') {
                clearTimeout(window[shwoTimer])
                window[timer] = setTimeout(function() {
                    hidedom.hide();
                    if (hideFun != null) {
                        try {
                            hideFun(_this);
                        } catch(ex) {
                            console.log("message:" + ex.message + " line:" + ex.lineNumber);
                        }
                    }
                }, showTime);
            }
        });
    }

    var hoverMenuTimeOutId = 0;
    var bindGameMenu = function() {
        $('#link_game_menu,#hoverMenu').bind('mouseenter',
                function() {
                    clearTimeout(hoverMenuTimeOutId);
                    var moreNavObj = $("#hoverMenu");
                    var gameMenuObj = $('#link_game_menu');

                    if (!gameMenuObj.hasClass('on')) {
                        gameMenuObj.attr('class', 'hoverOn');
                    }

                    moreNavObj.slideDown();
                }).bind('mouseleave', function() {
                    hoverMenuTimeOutId = setTimeout(function() {
                        var moreNavObj = $("#hoverMenu");
                        var gameMenuObj = $('#link_game_menu');

                        if (!gameMenuObj.hasClass('on')) {
                            gameMenuObj.attr('class', '');
                        }

                        moreNavObj.slideUp();
                    }, 500)
                });
    }

});