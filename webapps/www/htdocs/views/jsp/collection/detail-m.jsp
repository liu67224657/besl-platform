<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:wml="http://www.wapforum.org/2001/wml">
<head>
    <meta name="applicable-device" content="mobile">
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="Keywords" content="手机游戏礼包,激活码,兑换码">
    <meta name="description"
          content="着迷网礼包中心为手游玩家提供各种手游礼包,其中包括手机游戏激活码,特权码,测试礼包,新手礼包,兑换码，节假日礼包等,还有多种着迷专属礼包等你拿,多种手游礼包尽在着迷网!"/>
    <title>最新热门手机游戏礼包,激活码,兑换码_着迷网移动版</title>
    <link href="${URL_STATIC}/mobile/cms/jmsy/yxk/css/newyxk_common.css?${version}" rel="stylesheet" type="text/css">
    <link href="${URL_STATIC}/mobile/cms/jmsy/yxk/css/newpage.css?${version}" rel="stylesheet" type="text/css">

    <%--  <link href="${URL_STATIC}/mobile/cms/jmsy/css/common.css?${version}" rel="stylesheet" type="text/css"> --%>
    <link href="${URL_STATIC}/css/swiper-3.3.1.min.css?${version}" rel="stylesheet" type="text/css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/mobile/cms/jmsy/logincont/loginbar.css?${version}">



    <script type="text/javascript">
        ;
        (function (doc, win) {
            var size = 750;
            var int = 100;
            var docEle = doc.documentElement,
                    evt = 'onorientationchange' in window ? 'orientationchange' : 'resize',
                    fn = function () {
                        var width = docEle.clientWidth;
                        if (width > size) {
                            docEle.style.fontSize = int + 'px';
                        } else {
                            width && (docEle.style.fontSize = width / (size / int) + 'px');
                        }
                        doc.addEventListener('touchstart', function () {
                            return false
                        }, true);
                    };
            win.addEventListener(evt, fn, false);
            doc.addEventListener('DOMContentLoaded', fn, false);
        }(document, window));
    </script>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
    <script src="http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion=407121"></script>
    <link rel="stylesheet" href="http://bdimg.share.baidu.com/static/api/css/share_style0_16.css?v=6aba13f0.css">
</head>
<body>
<div class="container">
<div id="wrapper">
<header>
    <div class="topbar-mod border-b fn-clear topbar">
        <h1 class="logo-img">
            <a href="<c:out value="${URL_M}/collection"/>">
                <i><img src="${URL_STATIC}/mobile/cms/jmsy/images/logo-icon2.png" alt="游戏" title="游戏"/></i>游戏
            </a>
        </h1>

        <div class="search-box hide" id="search-box">
            <span><input type="text" value="" placeholder="搜索" class="search" id="search-text"></span>
        </div>
        <div class="search-bar">
            <span class="search-btn" id="search-btn"></span>
        </div>
    </div>
</header>
<div id="main">
<div class="game-box border-b fn-ovf">
    <div class="gameico fn-left"><i><img src="${game.gameIcon}" data-url="<c:out value="${game.gameIcon}"/>"
                                         class="lazy" alt="<c:out value="${game.gameName}"/>"
                                         title="<c:out value="${game.gameName}"/>"></i></div>
    <div class="game-describe fn-left">
        <h2><c:out value="${game.gameName}"/></h2>

        <p>开发商：<c:out value="${game.gameDeveloper}"/></p>

        <p>类型：<c:forEach items="${game.categoryTypeSet}" var="category" varStatus="st"><c:out
                value="${category.value}"/>&nbsp;</c:forEach></p>

        <p>平台：<c:forEach items="${game.platformMap.keySet()}" var="key">
            <c:forEach items="${game.platformMap[key]}" var="platform"><c:out
                    value="${platform.desc}"/>&nbsp;</c:forEach>
        </c:forEach>
        </p>
    </div>
    <c:if test="${gamePlatformType.code == 1}">
        <c:if test="${fn:length(game.wikiUrl) > 0}">
            <a target="_blank" href="<c:out value="${game.wikiUrl}"/>" class="gamewiki border-radius">进入wiki</a>
        </c:if>
    </c:if>
    <c:if test="${gamePlatformType.code != 1}">
        <c:if test="${fn:length(game.officialWebsite) > 0}">
            <a target="_blank" href="<c:out value="${game.officialWebsite}"/>" class="gamewiki border-radius">进入官网</a>
        </c:if>
    </c:if>
</div>
<div class="fn-stripe"></div>
<c:if test="${gamePlatformType.code == 1}">
    <div class="game-dw">
        <div class="m-tit">游戏下载</div>
        <div class="dw-list fn-clear">
            <a target="_blank"
                    <c:choose>
                        <c:when test="${fn:length(game.iosDownload) > 0}">href="<c:out value="${game.iosDownload}"/>"
                            class="dw-ios" target="_blank"
                        </c:when>
                        <c:otherwise>href="javascript:void(0)" class="dw-ios disabled"</c:otherwise>
                    </c:choose>>ios版下载</a>
            <a target="_blank"
                    <c:choose>
                        <c:when test="${fn:length(game.androidDownload) > 0}">href="<c:out
                                value="${game.androidDownload}"/>"
                            class="dw-android" target="_blank"
                        </c:when>
                        <c:otherwise>href="javascript:void(0)" class="dw-android disabled"</c:otherwise>
                    </c:choose>>android版下载</a>
        </div>
    </div>
</c:if>

<div class="game-info">
    <div class="m-tit">游戏介绍</div>
    <div class="gm-details">
        <p>${game.gameProfile}</p>
    </div>
    <span class="gm-more-btn"></span>
    <c:if test="${fn:length(game.gameVideoPic) > 0 || game.gamePicSet.size() > 0}">
        <div class="gm-slideWrap border-b">
            <div class="swiper-container">
                <div class="swiper-wrapper">
                    <c:if test="${fn:length(game.gameVideoPic) > 0}">
                        <div class="swiper-slide" data-video="<c:out value="${game.gameVideo}"/>"
                             id="img-video">
                            <img src="<c:out value="${game.gameVideoPic}"/>"
                                 data-url="<c:out value="${game.gameVideoPic}"/>" class="lazy"/>
                        </div>
                    </c:if>
                    <c:forEach items="${game.gamePicSet}" var="pic" varStatus="st">
                        <div class="swiper-slide"><img src="<c:out value="${pic}"/>"
                                                       data-url="<c:out value="${pic}"/>" class="lazy"/></div>
                    </c:forEach>
                </div>
                <div class="swiper-scrollbar"></div>
            </div>

        </div>
    </c:if>
</div>
<c:if test="${gamePlatformType.code == 1}">
    <c:if test="${giftList != null && giftList.size() >0}">
        <div class="fn-stripe"></div>
        <div class="game-gift border-b">
            <div class="m-tit">游戏礼包</div>
            <ul class="game-gift-list">
                <c:forEach items="${giftList}" var="gift" end="2">
                    <c:if test="${gift.sn > 0}">
                        <li class="border-b">
                            <a href="<c:out value="${URL_M}/gift/${gift.gid}"/>">
                                <i class="fn-left"><img src="${gift.gipic}"
                                                        data-url="<c:out value="${gift.gipic}"/>"
                                                        alt="<c:out value="${gift.title}"/>"
                                                        title="<c:out value="${gift.title}"/>" class="lazy"></i>

                                <div class="fn-left">
                                    <h4><c:out value="${gift.title}"/></h4>

                                    <p><c:if test="${fn:length(gift.desc) > 10}"><c:out
                                            value="${fn:substring(gift.desc, 0, 10)}"/></c:if><c:if
                                            test="${fn:length(gift.desc) <= 10}"><c:out
                                            value="${gift.desc}"/></c:if></p>

                                    <p>剩余：<span><c:out value="${gift.sn}"/></span>/<c:out value="${gift.cn}"/>
                                    </p>
                                </div>
                                <cite class="gift-btn lq border-radius">领取</cite>
                            </a>
                        </li>
                    </c:if>
                </c:forEach>
            </ul>
        </div>
    </c:if>
</c:if>

<div class="fn-stripe"></div>
<div class="game-strategy border-b">
    <div class="m-tit">游戏攻略</div>
    <div class="game-strategy-list">
        <ul class="gm-list">
            <c:if test="${guideList != null && guideList.size() > 0}">
                <c:forEach items="${guideList}" var="archive" end="3">
                    <li class="border-b"><a
                            href="<c:out value="${archive.dede_archives_url}"/>">${archive.dede_archives_title}</a>
                    </li>
                </c:forEach>
            </c:if>
            <c:if test="${guideList == null || guideList.size() == 0}">
                <div class="none-cont">暂无游戏攻略</div>
            </c:if>
        </ul>
    </div>
    <c:if test="${guideList.size() > 3}">
        <div class="more-btn">
            <a target="_blank" href="${URL_WWW}/collection/${game.gameDbId}/guides">查看更多</a>
        </div>
    </c:if>
</div>

<div class="fn-stripe"></div>
<div class="game-news border-b">
    <div class="m-tit">游戏资讯</div>
    <div class="game-strategy-list">
        <ul class="gm-list">
            <c:if test="${newsList != null && newsList.size() > 0}">
                <c:forEach items="${newsList}" var="archive" end="3">
                    <li class="border-b"><a
                            href="<c:out value="${archive.dede_archives_url}"/>">${archive.dede_archives_title}</a>
                    </li>
                </c:forEach>
            </c:if>
            <c:if test="${newsList == null || newsList.size() == 0}">
                <div class="none-cont">暂无游戏资讯</div>
            </c:if>
        </ul>
    </div>
    <c:if test="${newsList.size() > 3}">
        <div class="more-btn">
            <a target="_blank" href="${URL_WWW}/collection/${game.gameDbId}/news">查看更多</a>
        </div>
    </c:if>
</div>

<div class="fn-stripe"></div>
<div class="game-video border-b">
    <div class="m-tit">游戏视频</div>
    <div class="gv-box">
        <c:if test="${videoList != null && videoList.size() > 0}">
            <c:forEach items="${videoList}" var="archive" end="1">
                <a href="<c:out value="${archive.dede_archives_url}"/>">
                    <i><img src="${archive.dede_archives_litpic}" class="lazy"
                            data-url="<c:out value="${archive.dede_archives_litpic}"/>"
                            alt="<c:out value="${archive.dede_archives_title}"/>"
                            title="<c:out value="${archive.dede_archives_title}"/>"></i>
                    <span class="video-icon"><b>${archive.dede_archives_title}</b></span>
                </a>
            </c:forEach>
        </c:if>
        <c:if test="${videoList == null || videoList.size() == 0}">
            <div class="none-cont">暂无游戏视频</div>
        </c:if>
    </div>
    <c:if test="${videoList.size() > 2}">
        <div class="more-btn">
            <a target="_blank" href="${URL_WWW}/collection/${game.gameDbId}/videos">查看更多</a>
        </div>
    </c:if>
</div>
<div id="_pinglun"></div>
<div class="fn-stripe" id="comment_before"></div>
<div id="comment_area"></div>
<c:if test="${categoryList != null && categoryList.size() > 0}">
    <div class="fn-stripe"></div>
    <div class="game-recommend">
        <div class="m-tit">猜你喜欢</div>
        <div class="gr-box fn-clear">
            <c:forEach items="${categoryList}" var="categoryGame" begin="0" end="2">
                <a href="<c:out value="${URL_M}/collection/${categoryGame.gameDbId}"/>">
                    <i><img src="${categoryGame.gameIcon}" class="lazy"
                            data-url="<c:out value="${categoryGame.gameIcon}"/>"
                            title="<c:out value="${categoryGame.gameName}"/>"
                            alt="<c:out value="${categoryGame.gameName}"/>"></i>
                    <span>${categoryGame.gameName}</span>
                </a>
            </c:forEach>
        </div>
    </div>
</c:if>

<script>
    document.write('<script src="http://passport.${DOMAIN}/auth/footer/m?v=' + Math.random() + '"><\/script>');
</script>
</div>

</div>
<!-- 吸底icon 开始-->
<div class="cont-bottom">
    <span class="share icon" id="share_sum"><i>分享</i>分享</span>
    <span class="comment icon " id="comment_sum"><i>评论</i>评论</span>
    <span class="assist icon" id="assist_sum"><i>赞</i><c:if test="${game.favorSum==0}">赞</c:if><c:if
            test="${game.favorSum>0}">${game.favorSum}</c:if></span>
    <input type="hidden" value="${game.gameDbId}" id="span_gameid"/>

    <div class="login_box_comment"></div>
</div>
<!-- 吸底icon 结束-->
<!-- 分享 开始 -->
<div class="share-box">
    <div class="mask"></div>
    <div class="share-con">
        <p>分享到：</p>

        <div class="bdsharebuttonbox">
            <cite>
                <a href="#" class="popup_sqq" data-cmd="sqq" title="分享到QQ"></a>
                <font>QQ好友</font>
            </cite>
            <cite>
                <a href="#" class="bds_tsina" data-cmd="tsina" title="分享到新浪微博"></a>
                <font>新浪微博</font>
            </cite>
            <cite>
                <a href="#" class="bds_qzone" data-cmd="qzone" title="分享到QQ空间"></a>
                <font>QQ空间</font>
            </cite>
            <cite>
                <a class="bds_tieba" data-cmd="tieba" title="分享到百度贴吧"></a>
                <font>百度贴吧</font>
            </cite>
        </div>
        <div class="cancle-btn">取消</div>
        <script>
            window._bd_share_config = {"common": {"bdSnsKey": {}, "bdText": "", "bdMini": "2", "bdPic": "", "bdStyle": "0", "bdSize": "16"}, "share": {}};
            with (document)0[(getElementsByTagName('head')[0] || body).appendChild(createElement('script')).src = 'http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion=' + ~(-new Date() / 36e5)];
        </script>
    </div>
</div>
<!-- 分享 结束 -->
<!-- 评论弹框 -->
<div class="pl-box">
    <div class="mask"></div>
    <div class="cmnt-wrap">
        <div class="cmnt-area">
            <textarea class="new-area"></textarea>
        </div>
        <div class="cmnt-bar">
            <span class="cmnt-btn fabu fn-right">回复</span>
            <span class="cmnt-btn cancel fn-right">取消</span>
            <span class="cmnt-num">还可以输入<b class="inp-num">140</b>个字</span>
        </div>
        <div class="cmnt-beyond"><span>请输入至少2个字</span></div>
        <div class="cmnt-success"></div>
    </div>
</div>
<!-- 评论弹框 结束-->
<div id="wp_comment_alert" class="wp_comment_alert">
    <div id="wp_comment_tips" class="wp_comment_tips"></div>
    <div class="wp_comment_btn">
        <span class="wp_cancel" id="wp_cancel">取消</span>
        <span class="wp_confirm" id="wp_confirm">确定</span>
    </div>
</div>
<div id="wp_comment_alert_no_button" class="wp_comment_alert">
    <div id="wp_comment_tips_no_button" class="wp_comment_tips"></div>
</div>
</div>


<%@ include file="/views/jsp/passport/new-login-page.jsp" %>
<script type="text/javascript" src="${URL_STATIC}/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${URL_STATIC}/js/swiper-3.3.1.jquery.min.js?${version}"></script>
<script src="${URL_STATIC}/mobile/cms/jmsy/js/lazyimg.js"></script>
<script type="text/javascript" src="${URL_STATIC}/mobile/cms/jmsy/yxk/js/action.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/init/game-collection-detail-m-init.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/comment/comment_game.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/init/login_common.js"></script>

<script>
    ;
    (function () {
        var mod = {
            slideFn: function () {
                var swiper = new Swiper('.swiper-container', {
                    scrollbar: '.swiper-scrollbar',
                    scrollbarHide: true,
                    slidesPerView: 'auto',
                    spaceBetween: 10,
                });
            },
            /*             setHeight:function(){
             var moreBtn=$('.gm-more-btn');
             var gdList=$('.gm-details');
             var h = $('.gm-details').height();
             var top=$('.game-info').offset().top;
             var wh=$(window).height();
             var hh=$('header').innerHeight();
             var sH=wh-hh;
             $('#wrapper').css('min-height',sH);
             if(h>100){
             gdList.addClass('hide');

             }else{
             moreBtn.hide();
             }
             moreBtn.on('click',function(){
             $('body,html').scrollTop(top);
             if(!gdList.hasClass('hide')){
             gdList.addClass('hide');
             $(this).removeClass('hide');
             }else{
             gdList.removeClass('hide');
             $(this).addClass('hide');
             }
             });
             }, */
            setHeight: function () {
                var moreBtn = $('.gm-more-btn');
                var gdList = $('.gm-details');
                var h = gdList.height();
                var top = $('.game-info').offset().top;
                var wh = $(window).height();
                var hh = $('header').innerHeight();
                var sH = wh - hh;
                $('#wrapper').css('min-height', sH);
                if (h > 100) {
                    gdList.addClass('hide');
                    moreBtn.css({'display': 'block'});
                } else {
                    moreBtn.hide();
                }
                moreBtn.on('click', function () {
                    $('body,html').scrollTop(top);
                    if (!gdList.hasClass('hide')) {
                        gdList.addClass('hide');
                        $(this).removeClass('hide');
                    } else {
                        gdList.removeClass('hide');
                        $(this).addClass('hide');
                    }
                });
            },
            searchFunc: function () {
                var searchBtn = $('.search-btn');
                var search = $('.search');
                searchBtn.on('click', function () {
                    $('.search-box').removeClass('hide');
                    search.focus();
                });
                search.on('blur', function () {
                    var vals = $(this).val();
                    if (vals != '') {
                        $('.search-box').removeClass('hide');
                    } else {
                        $('.search-box').addClass('hide');
                    }
                })
            },
            int: function () {
                this.slideFn();
                this.setHeight();
            }
        }
        window.onload = mod.int();
        mod.searchFunc();
        $('.lazy').lazyImg();
    })();

    var host = window.location.host.substr(window.location.host.indexOf('.'));
    var mHost = 'http://m' + host + '/';
    var apiHost = 'http://api' + host + '/';
    var passportHost = 'http://passport' + host + "/";

    $(document).ready(function () {
        $('#search-btn').on('click', function () {
            if (!$('#search-box').hasClass('hide')) {
                var name = $('#search-text').val();
                if (name != undefined && name != '') {
                    window.location.href = mHost + 'collection/genre/name:' + name;
                }
            }
        });

        $('#search-text').keydown(function (e) {
            if (e.keyCode == 13) {
                if (!$('#search-box').hasClass('hide')) {
                    var name = $(this).val();
                    if (name != undefined && name != '') {
                        window.location.href = mHost + 'collection/genre/name:' + name;
                    }
                }
            }
        });
        var imgVideo = $('#img-video');
        imgVideo.wrap('<i>');
        imgVideo.on('click', function () {
            var url = $(this).attr('data-video');
            window.location.href = url;
        })
    });
    $(document).ready(function () {
        $('#assist_sum').on('click', function () {
            likeGame($('#span_gameid').val());
        });
        $('#wp_cancel').on('click', function () {
            $('#wp_comment_alert').attr('class', 'wp_comment_alert');
        });
        $('#wp_confirm').on('click', function () {
            $('#wp_comment_alert').attr('class', 'wp_comment_alert');
        });
        $(".login_box_comment").click();
//        $('#comment_sum').on('click',function(){
//        	if (uno == null || uid == null){
//                $('.new-area').blur();
//                $('.pl-box').hide();
//                $('.mask').hide();
//        	}
//        });
    });
</script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/collection-tj.js"></script>

<script>
    (function (G, D, s, c, p) {
        c = {//监测配置
            UA: "UA-joyme-000001", //客户项目编号,由系统生成
            NO_FLS: 0,
            WITH_REF: 1,
            URL: 'http://lib.joyme.com/static/js/iwt/iwt-min.js'
        };
        G._iwt ? G._iwt.track(c, p) : (G._iwtTQ = G._iwtTQ || []).push([c, p]), !G._iwtLoading && lo();
        function lo(t) {
            G._iwtLoading = 1;
            s = D.createElement("script");
            s.src = c.URL;
            t = D.getElementsByTagName("script");
            t = t[t.length - 1];
            t.parentNode.insertBefore(s, t);
        }
    })(this, document);
</script>
</body>
</html>
