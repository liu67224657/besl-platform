define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var discovery = require('../page/discovery');
    var card = require('../page/card');
    var header = require('../page/header');
    var common = require('../common/common');
    var fav = require('../page/fav');
    var cardBiz = require('../biz/card-biz');
    var contentOper = require('../page/content-operate');
    var rollanimate = require('../page/rollanimate')
    window.offSetTop = [];
    window.offSetIndex = 0;
    window.loadKey = false;
    window.loadDebug = false;
    $().ready(function () {
        if ($(".scan_con").length > 0) {
            loadKey = true;
        }
        $(".scan_con").each(function (i) {
            if (offSetIndex == 0) {
                window.offSetTop[window.offSetIndex] = $(this).offset().top;
            } else {
                window.offSetTop[window.offSetIndex] = $(this).offset().top - 50;
            }
            window.offSetIndex++;
        });
    });
    (function () {
        if (window.location.href.indexOf("#debug") != -1) {
            loadDebug = true;
        }
        document.body.parentNode.style.overflowY = "scroll";
        document.body.parentNode.style.overflowX = "visible";
        $("body").css({"position":"static"});
//        discovery.initDisplay(seeIndex,magazineCode);
        fav.initFavoritePreview();

        contentOper.walllistLive();
        $("body").die().live('mousedown', function () {
            if (window.isOut) {
                $(".pop").hide();
                isOut = true;
            }
        });

        $('#check_forwardroot').die().live('click', function () {
            if ($(this).attr('checked')) {
                if (joyconfig.syncFlag) {
                    $('#sync_forward').removeAttr('disabled').attr('checked', true);
                }
            } else {
                $('#sync_forward').attr('disabled', 'disabled').attr('checked', false);
            }
        });
        var rightPosition = parseInt(($(document.body).width() - 944) / 2);
        if ($(document.body).width() > 944) {
            $("#box").css('right', rightPosition - 50 + 'px');
        } else {
            $("#box").css('right', 0 + 'px');
        }

        $("a[name=preview]").live('click', function () {
            var cuno = $(this).attr("cuno");
            var cid = $(this).attr("cid");
            var contentIdx = '';
            if ($(this).attr("idx")) {
                contentIdx = $(this).attr("idx");
            }
            discovery.alertPreviewBox(cuno, cid, contentIdx);
        });

        $("#precontent").live('click', function() {
            var contentIdx = $(this).attr("data-idx");
            discovery.preContent(magazineCode, contentIdx);
        });

        $("#nextcontent").live('click', function() {
            var contentIdx = $(this).attr("data-idx");
            discovery.nextContent(magazineCode, contentIdx);
        });

        $("#upPage").live('click', function () {
            var dividend, oneHeight;
            if (window.navigator.userAgent.indexOf('MSIE 7.0') > 0) {
                dividend = 657;
                oneHeight = 626
            } else {
                oneHeight = 616
                dividend = 647;
            }
            var wallScrollTop = document.documentElement.scrollTop + document.body.scrollTop;
            var index = parseInt((wallScrollTop - oneHeight) / dividend) + 1;
            if (wallScrollTop > offSetTop[index]) {
                $("html,body").animate({scrollTop:offSetTop[index]}, 450);
            } else {
                $("html,body").animate({scrollTop:offSetTop[index - 1]}, 450);
            }
        });
        $("#downPage").live('click', function () {
            var index , dividend, oneHeight;
            if (window.navigator.userAgent.indexOf('MSIE 7.0') > 0) {
                dividend = 657;
                oneHeight = 626
            } else {
                oneHeight = 616
                dividend = 647;
            }
            var wallScrollTop = document.documentElement.scrollTop + document.body.scrollTop;
            if ((wallScrollTop - oneHeight) > 0) {
                index = parseInt((wallScrollTop - oneHeight) / dividend) + 1;
            } else {
                index = parseInt((wallScrollTop - oneHeight) / dividend)
            }
            if (index + 1 == offSetTop.length) {
                if (loadKey) {
                    discovery.initDisplay(seeIndex, magazineCode);
                }
                var _this = $(this)
                _this.seetimeout = setInterval(function () {
                    if (!(index + 1 == offSetTop.length)) {
                        var ot = index + 1;
                        clearInterval(_this.seetimeout);
                        $("html,body").animate({scrollTop:offSetTop[ot]});
                    }
                }, 100)
            } else {
                var ot = index + 1;
                $("html,body").animate({scrollTop:offSetTop[ot]});
            }
//            $("#memo_message").text(index);
//            for (var i in offSetTop) {
//                $("#memo_message").html($("#memo_message").text() + "键:" + i + " 值:" + offSetTop[i] + '<br />');
//            }
        });
        $("#toppage").live('click', function () {
//            $("html,body").animate({scrollTop:0}, 450);
            window.scrollTo(0, 0);
        });
        $(".music_btn").live('click', function () {
            window.open($(this).attr("url"), "newwindow", "height=300, width=400, top=200, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no")
        });

        $("a[name=atLink]").live("click", function () {
            cardBiz.getAtLink($(this).attr("title"), openAtLinkCallback);
        });

        header.noticeSearchInit();

        var isIE = !!window.ActiveXObject;
        if (isIE && !!document.documentMode && !(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.split(";")[1].replace(/[ ]/g, "") == "MSIE9.0")) {
            $(window).scroll(function () {
                scrollOnWall();
            })
        } else {
            $(document).scroll(function () {
                scrollOnWall();
            })
        }
        $(window).resize(function () {
            reScrollTopLeft();
        })

    })();
    var scrollOnWall = function () {
        if ($(window).scrollTop() > 657) {
            $("#downPage").removeClass("roll_down").addClass("roll_bottom");
            $("#upPage").fadeIn();
            $("#toppage").fadeIn();
        } else {
            $("#downPage").removeClass("roll_bottom").addClass("roll_down");
            $("#upPage").fadeOut();
            $("#toppage").fadeOut();
        }
        var isFoot = ($(window).scrollTop() + $(window).height() + 200) >= $(document).height() ? true : false;
        if (offSetTop.length >= 100) {
            isFoot = false;
        }
        if (isFoot && loadKey && $(".scan_con").length <= 100) {
            discovery.initDisplay(seeIndex, magazineCode);
        }
    }
    var setDingAddShow = function (jqDing, text, contentId, billingStatus) {
        if (billingStatus) {
            $("body").append("<div id='dingAddShow_" + contentId + "' style='position: fixed;color:#FF8773; display:none; z-index:10001;font-size:12px;'>" + text +
                    "</div>");
            $("#dingAddShow_" + contentId).css({left:jqDing.offset().left + "px", top:jqDing.offset().top - 20 + "px"}).show().animate({
                        top:'-=7',
                        opacity:'0'
                    }, 1000, function () {
                $(this).remove();
            });
        }
        var num = common.getTimes(jqDing) + 1;
        jqDing.removeClass('see_praise').addClass('see_praise_on')
        $('a[id^=link_ding_][data-blogid^=' + contentId + ']').text("(" + num + ")");
    }

    var openAtLinkCallback = function (jsonObj) {
        if (jsonObj.status_code == "1") {
            var result = jsonObj.result[0];
            //跳转到博客页
            window.open(joyconfig.URL_WWW + '/people/' + result.domain , '_blank', 'status=yes,toolbar=yes,menubar=yes,scrollbars=yes');
        }
    }
    var reScrollTopLeft = function () {
        if ($(window).width() > 940) {
            $("#box").css("right", ($(window).width() - 940) / 2 - 62 + 'px');
        } else {
            $("#box").css("right", 0);
        }
    }
    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});