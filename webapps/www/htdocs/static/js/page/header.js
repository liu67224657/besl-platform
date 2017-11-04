/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-3
 * Time: 下午3:05
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    navscroll();
    var notice = require('./header-notice');
    var search = require('./header-search');
    var loginBiz = require('../biz/login-biz');

    var header = {
        //小纸条 搜索 返回顶部
        noticeSearchReTopInit: function() {
            notice.initNotice();
            search.initSearch();
            bindReturnTop();
            $(window).resize(function() {
                reScrollTopLeft();
            })
            loginBiz.bindMaskLogin();
            //处理回调地址
            loginBiz.regReturn(window.location.href);
            moveUserNav();
            loginTips.initRegTips();
//            bindGameMenu();
        },
        //小纸条 搜索
        noticeSearchInit: function() {
            notice.initNotice();
            search.initSearch();
            loginBiz.bindMaskLogin();
            //处理回调地址
            loginBiz.regReturn(window.location.href);
            moveUserNav();
            loginTips.initRegTips();
            bindGameMenu();
        } ,
        searchReTopInit: function() {
            search.initSearch();
            bindReturnTop();
            $(window).resize(function() {
                reScrollTopLeft();
            })
            loginBiz.bindMaskLogin();
            //处理回调地址
            loginBiz.regReturn(window.location.href);
            moveUserNav();
            loginTips.initRegTips();
            bindGameMenu();
        }
    }

    var timeout = false;

    function navscroll() {
        $(window).scroll(function(event) {
            if (timeout) {
                clearTimeout(timeout);
            }
            timeout = setTimeout(function() {
                scrollMove($(window).scrollTop(), 57);

                if ($(window).scrollTop() > $(window).height()) {
                    $("#linkHome").fadeIn();
                } else {
                    $("#linkHome").fadeOut();
                }
                reScrollTopLeft();
            }, 100);
        })
        scrollMove($(window).scrollTop(), 57);
    }

    function moveUserNav() {
        showHideMethods($('#header_func_link'), 100, 'headerNav', function(dom) {
            if (!$("#memo_message").is(":hidden")) {
                $("#memo_message").hide();
            }
            $('#header_func_area').css({'left': $("#header_func_link").offset().left-10 + 'px','top':'37px'}).slideDown();
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

    var currentTop = 0;

    function scrollMove(scrollTop, headt) {
        var navDom = $("#joyme-head-2013");
//        var logoDom = $('.head_t');

        //想下滚动或者定位不到头部的位置不随这滚动
        if (scrollTop == undefined || scrollTop > currentTop) {
//            logoDom.attr("style", 'margin-bootom:0px');
            navDom.attr('style', 'position:relative;');
            currentTop = scrollTop;
            return;
        }

        //向上滚动时候
        if (scrollTop > headt) {
//            logoDom.attr("style", 'margin-bootom:39px');
            navDom.attr('style', 'position:fixed;');
        } else {
//            logoDom.attr("style", 'margin-bootom:0px');
            navDom.attr('style', 'position:relative;');
        }
        currentTop = scrollTop;
    }

    var loginTips = {
        initRegTips:function() {
            if (joyconfig.joyuserno == '') {
                $(window).scroll(function() {
                    if (Sys.ie == "6.0") {
                        $("#reg_tips").css("top", $(window).scrollTop() + $(window).height() - 90 + 'px');
                        $(".scroll_top").css("top", $(window).scrollTop() + $(window).height() - 130 + 'px');
                        if (($(window).scrollTop() + ($("#previewBox").data('bodyTop') || 0)) > $(window).height()) {
                            $("#reg_tips").show();
                            $(".scroll_top").show();
                        } else {
                            $("#reg_tips").hide();
                            $(".scroll_top").hide();
                        }
                    } else {
                        if (($(window).scrollTop() + ($("#previewBox").data('bodyTop') || 0)) > $(window).height()) {
                            $("#reg_tips").fadeIn();
                            $(".scroll_top").fadeIn();
                        } else {
                            $("#reg_tips").fadeOut();
                            $(".scroll_top").fadeOut();
                        }
                    }
                });
                $("#close_reg_tips").live("click", function() {
                    if (Sys.ie == "6.0") {
                        $("#reg_tips").hide();
                    } else {
                        $("#reg_tips").fadeOut(function() {
                            $(this).remove();
                        })
                    }
                });
            }
        }
    }

    var reScrollTopLeft = function() {
        if ($(window).width() > 940) {
            $(".scroll_top").css("right", ($(window).width() - 940) / 2 - 92 + 'px');
        } else {
            $(".scroll_top").css("right", 0);
        }
    }

    var bindReturnTop = function() {
        $("#linkHome").live('click', function() {
            window.scrollTo($(window).scrollTop(), 0);
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
    return header;
});