<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
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
    <meta name="Keywords" content="手机游戏礼包领取,手游兑换码,手游激活码">
    <meta name="description"
          content="着迷网礼包中心为手游玩家提供各种手游礼包,其中包括手机游戏激活码,特权码,测试礼包,新手礼包,兑换码，节假日礼包等,还有多种着迷专属礼包等你拿,多种手游礼包尽在着迷网"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>手机游戏礼包领取,手游兑换码,手游激活码_着迷网</title>

    <link rel="stylesheet" type="text/css" href="http://static.joyme.com/mobile/cms/jmsy/css/common-beta1.css">
    <link rel="stylesheet" type="text/css"
          href="${URL_STATIC}/mobile/cms/jmsy/css/newlibao.css?v=${version}">
    <%--<link rel="stylesheet" type="text/css" href="${URL_STATIC}/mobile/cms/jmsy/logincont/oldLoginbar.css?${version}"/>--%>

    <script type="text/javascript" src="${URL_STATIC}/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
        function buildexclusive() {
            $("a[name='exclusiveDisplayNone']").each(function () {
                $(this).attr("style", "");
            });
            $("#clickExclusive").remove();
        }
        function checkSearch() {
            var searchText = $("#search_text").val().trim();
            if (searchText == "") {
                alert("请输入礼包名称");
                return false;
            }
        }
    </script>
</head>
<body>

<div class="container">
    <%@ include file="/views/jsp/passport/m/gift-header-m.jsp" %>

    <div id="wrapper">
        <div id="main">
            <div id="center">
                <c:if test="${not empty  menupic}">
                    <!-- 图片轮播 -->
                    <div id="pic-loop">
                        <div class="pic-loop-box swiper-wrapper">
                            <c:forEach items="${menupic}" var="item" end="3">
                                <div class="swiper-slide">
                                    <a href="${item.url}" target="_blank">
                                        <img class="lazy" src="${item.picUrl1}"
                                             data-url="${item.picUrl1}"
                                             alt="${item.menuName}"
                                             title="${item.menuName}"
                                             target="_blank"/>
                                    </a></div>
                            </c:forEach>
                        </div>
                        <div class="pagination"></div>
                    </div>
                </c:if>
                <!-- 图片轮播 -->
                <!-- 搜索 开始 -->
                <div class="search-box fn-clear border-b">
                    <form action="${URL_M}/gift/searchpage" method="post" onsubmit="return checkSearch();">
                        <input type="text" class="search-text" name="searchtext" id="search_text" placeholder="请输入礼包名称">
                        <input type="submit" class="search-resu" value="搜索" 　/>
                    </form>
                </div>
                <!-- 搜索 结束 -->
                <!-- 独家礼包 开始 -->
                <c:if test="${not empty exclusiveList}">
                    <a name="exclusive"></a>
                    <div class="gift-list">
                        <div class="gift-list-tit">独家礼包</div>
                        <div class="gift-list-box">
                            <c:forEach var="item" items="${exclusiveList}" varStatus="index">
                                <a href="${URL_M}/gift/${item.activityGoodsId}" class="border-b" <c:if
                                        test="${index.index>4}">name='exclusiveDisplayNone'
                                   style="display:none;"</c:if>>
                                    <cite>
                                        <img src="${item.activityPicUrl}" title="${item.activitySubject}"
                                             alt="${item.activitySubject}"/>
                                    </cite>
                                    <span>
                                        <font>${item.activitySubject}</font>
                                        <em>有效期：${dateutil:parseCustomDate(item.endTime, "yyyy-MM-dd HH:mm")}</em>
                                        <b>剩余: ${item.goodsResetAmount}</b>
                                    </span>
                                </a>
                            </c:forEach>
                        </div>
                        <c:if test="${exclusiveList.size()>5}">
                            <div class="gift-more" id="clickExclusive">
                                <a href="javascript:buildexclusive();">点击查看更多</a>
                            </div>
                        </c:if>
                    </div>

                </c:if>
                <!-- 独家礼包 结束 -->
                <!-- 最新礼包 开始 -->
                <a name="new"></a>
                <div class="gift-list">
                    <div class="gift-list-tit">最新礼包</div>
                    <div class="gift-list-box">
                        <c:forEach var="item" items="${list}">
                            <a href="${URL_M}/gift/${item.activityGoodsId}" class="border-b">
                                <cite>
                                    <img src="${item.activityPicUrl}" title="${item.activitySubject}"
                                         alt="${item.activitySubject}"/>
                                </cite>
                                <span>
                                          <font>${item.activitySubject}</font>
                                          <em>有效期：${dateutil:parseCustomDate(item.endTime, "yyyy-MM-dd HH:mm")}</em>
                                          <b>剩余: ${item.goodsResetAmount}</b>
                                      </span>
                            </a>
                        </c:forEach>

                        <div class="gift-more more-icon">
                            <a href="javascript:;" id="span-gift-more">查看全部礼包</a>
                        </div>
                    </div>

                    <!-- 最新礼包 结束 -->
                </div>


            </div>
            <%@ include file="/hotdeploy/views/jsp/giftmarket/giftmarket-m-main-ad.jsp" %>

            <!-- 页脚 开始 -->
            <script>
                document.write('<script src="http://passport.${DOMAIN}/auth/footer/m?v=' + Math.random() + '"><\/script>');
            </script>
            <!-- 页脚 结束 -->
        </div>
        <!-- 导航 开始 -->
    </div>
    <%@ include file="/views/jsp/passport/m/nav.jsp" %>

    <%--<section class="wrapper">--%>
    <%--<div class="tab-box">--%>
    <%--<div class="tab-menu">--%>
    <%--<span class="border-r li-xin active"><b><i></i>最新礼包</b></span>--%>
    <%--<span class="li-my"><b><i></i>我的礼包</b></span>--%>
    <%--</div>--%>
    <%--<div class="fn-stripe border-t"></div>--%>
    <%--<div class="tab-cont">--%>
    <%--<div id="div-li-xin" class="active">--%>
    <%--<c:choose>--%>
    <%--<c:when test="${list != null && list.size() > 0}">--%>
    <%--<c:forEach items="${list}" var="gift">--%>
    <%--<a href="${URL_M}/gift/${gift.activityGoodsId}"--%>
    <%--<c:choose>--%>
    <%--<c:when test="${gift.channelType.code == 1}">class="border-b dj-icon" </c:when>--%>
    <%--<c:otherwise>class="border-b"</c:otherwise>--%>
    <%--</c:choose>>--%>
    <%--<cite><img src="${gift.activityPicUrl}" alt="${gift.activitySubject}"--%>
    <%--title="${gift.activitySubject}"></cite>--%>

    <%--<p>--%>
    <%--<b>${gift.activitySubject}</b><em>有效期：${dateutil:parseCustomDate(gift.endTime, "yyyy-MM-dd HH:mm")}</em><em>剩余: ${gift.goodsResetAmount}</em>--%>
    <%--</p>--%>
    <%--</a>--%>
    <%--</c:forEach>--%>
    <%--</c:when>--%>
    <%--</c:choose>--%>
    <%--<p class="load-bottom"><span id="span-gift-more">查看全部礼包</span></p>--%>
    <%--</div>--%>
    <%--<div id="div-li-my">--%>
    <%--<p class="load-bottom">您还没有领取过礼包o(∩_∩)o </p>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--</section>--%>
    <script type="text/javascript" src="http://static.joyme.com/js/swiper.js"></script>
    <script type="text/javascript" src="${URL_STATIC}/mobile/cms/jmsy/js/common.js"></script>


    <script type="text/javascript">
        var maxPage = 0;
        var curPage = 0;
        var myGiftLock = false;
        $(document).ready(function () {
            if (is_weixn()) {
                $('.login-wb').remove();
                $('.login-qq').remove();
                $('.login-wx').attr('href', 'http://passport.${DOMAIN}/auth/loginwappage?channel=wx&reurl=' + encodeURIComponent(window.location.href));
            } else {
                $('.login-wx').remove();
                $('.login-wb').attr('href', 'http://passport.${DOMAIN}/auth/thirdapi/sinaweibo/bind?reurl=' + encodeURIComponent(window.location.href));
                $('.login-qq').attr('href', 'http://passport.${DOMAIN}/auth/thirdapi/qq/bind?reurl=' + encodeURIComponent(window.location.href));
            }
            //登录
            $('#span-gift-more').on('touchstart', function () {
                window.location.href = '${URL_M}/gift/more';
            });
            $('#login-btn').on('touchstart', function (e) {
                e.stopPropagation();
                e.preventDefault();
                $('.login-mask').click();
                return false;
            });

//        $('.load-bottom').on('touchstart', function(e) {
//            if ($(this).find('span').attr('id') == 'span-my-gift-more') {
//                getMyGiftList(e);
//            }
//        })

            $('.tab-menu>span').on('touchstart', function (e) {
                if ($(this).hasClass("li-xin")) {
                    //最新礼包 页面已有数据
                    $(this).addClass('active').siblings().removeClass('active');
                    $('#div-li-xin').addClass('active').siblings().removeClass('active');
                } else if ($(this).hasClass("li-my")) {
                    $(this).addClass('active').siblings().removeClass('active');
                    $('#div-li-my').addClass('active').siblings().removeClass('active');
                    getMyGiftList(e, 0);
                }
            });
        });

        function is_weixn() {
            var ua = navigator.userAgent.toLowerCase();
            if (ua.match(/MicroMessenger/i) == "micromessenger") {
                return true;
            } else {
                return false;
            }
        }

        function getMyGiftList(e, tab) {
            var uno = getCookie('jmuc_uno');
            var uid = getCookie('jmuc_u');
            var token = getCookie('jmuc_token');
            var t = getCookie('jmuc_t');
            var lgdomain = getCookie('jmuc_lgdomain');
            var s = getCookie('jmuc_s');
            var pid = getCookie('jmuc_pid');
            if (uno == null || uid == null || token == null || t == null || lgdomain == null || s == null || pid == null || lgdomain == 'client') {
                e.stopPropagation();
                e.preventDefault();
                $('.login-mask').click();
                return;
            }
            if (myGiftLock) {
                return;
            }
            var p = 1;
            if (tab != 0) {
                p = curPage + 1;
            }
            myGiftLock = true;
            $.ajax({
                url: "http://api." + joyconfig.DOMAIN + "/json/gift/mygift",
                data: {p: p},
                timeout: 5000,
                dataType: "jsonp",
                type: "POST",
                jsonpCallback: "mygiftlist",

                success: function (req) {
                    var res = req[0];
                    if (res.rs == '0') {
                        alert('系统错误!');
                        myGiftLock = false;
                        return;
                    } else if (res.rs == '-1') {
                        e.stopPropagation();
                        e.preventDefault();
                        $('.login-mask').click();
                        $('.wrapper').hide();
                        myGiftLock = false;
                        return;
                    } else if (res.rs == '1') {
                        var result = res.result;
                        var cur = result.page.curPage;
                        if (cur == 1) {
                            $("#div-li-my").empty();
                            $('#div-li-my').html(' <p class="load-bottom" >您还没有领取过礼包o(∩_∩)o </p>');
                        }
                        if (result != null && result != undefined) {
                            var rows = result.rows;
                            var html = '';
                            if (rows != null && rows.length > 0) {
                                for (var i = 0; i < rows.length; i++) {
                                    var dto = rows[i];
                                    if (dto != null && dto != undefined) {
                                        html += '<a href="${URL_M}/gift/' + dto.aid + '?logid=' + dto.lid + '&reurl=${URL_M}/gift" class="border-b">' +
                                                '<cite><img src="' + dto.gipic + '" alt="' + dto.title + '" title="' + dto.title + '"></cite>' +
                                                '<p><b>' + dto.title + '</b><em>有效期：' + new Date(dto.endTime.time).format("yyyy-MM-dd hh:mm") + '</em></p></a>';
                                    }
                                }
                            }
                            $('#div-li-my .load-bottom').before(html);
                            if (result.page.curPage < result.page.maxPage) {
                                maxPage = result.page.maxPage;
                                curPage = result.page.curPage;
                                $('#div-li-my .load-bottom').html('<span id="span-my-gift-more" onclick="getMyGiftList(this,1);">点击加载更多</span>');
                            } else {
                                $('#div-li-my .load-bottom').remove();
                            }

                        }

                        myGiftLock = false;
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert('获取失败，请重试');
                    myGiftLock = false;
                    return;
                }
            });
        }


        Date.prototype.format = function (format) {
            var o = {
                "M+": this.getMonth() + 1, //month
                "d+": this.getDate(),    //day
                "h+": this.getHours(),   //hour
                "m+": this.getMinutes(), //minute
                "s+": this.getSeconds(), //second
                "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter
                "S": this.getMilliseconds() //millisecond
            }
            if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
                    (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)if (new RegExp("(" + k + ")").test(format))
                format = format.replace(RegExp.$1,
                        RegExp.$1.length == 1 ? o[k] :
                                ("00" + o[k]).substr(("" + o[k]).length));
            return format;
        }

        function getCookie(objName) {
            var arrStr = document.cookie.split("; ");
            for (var i = 0; i < arrStr.length; i++) {
                var temp = arrStr[i].split("=");
                if (temp[0] == objName && temp[1] != '\'\'' && temp[1] != "\"\"") {
                    return unescape(temp[1]);
                }
            }
            return null;
        }
    </script>
    <script type="text/javascript">
        var _paq = _paq || [];
        _paq.push(["setDocumentTitle", document.domain + "/" + document.title]);
        _paq.push(['trackPageView']);
        _paq.push(['enableLinkTracking']);
        (function () {
            var u = "//stat.joyme.com/";
            _paq.push(['setTrackerUrl', u + 'piwik.php']);
            _paq.push(['setSiteId', 198]);
            var d = document, g = d.createElement('script'), s = d.getElementsByTagName('script')[0];
            g.type = 'text/javascript';
            g.async = true;
            g.defer = true;
            g.src = u + 'piwik.js';
            s.parentNode.insertBefore(g, s);

            if ($('#pic-loop .swiper-slide').length > 2) {
                var mySwiper = new Swiper('#pic-loop', {
                    loop: true,
                    pagination: '.pagination',
                    paginationClickable: true,
                    spaceBetween: 30,
                    centeredSlides: true,
                    autoplay: 5000,
                    autoplayDisableOnInteraction: false

                });
            }
            function adClose() {
                var adCon = $('.pos-advert'),
                        adIcon = adCon.find('.advert-close');
                adIcon.click(function () {
                    adCon.hide();
                });
            }

            adClose();
        })();
        document.write('<noscript><p><img src="//stat.joyme.com/piwik.php?idsite=198" style="border:0;" alt="" /></p></noscript>');
    </script>
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
    <script type="text/javascript" src="${URL_LIB}/static/js/common/giftmarket-tj.js"></script>

</body>
</html>
