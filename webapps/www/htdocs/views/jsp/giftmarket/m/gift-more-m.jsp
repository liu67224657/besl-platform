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
    <meta name="Keywords" content="手机游戏礼包,激活码,兑换码">
    <meta name="description"
          content="着迷网礼包中心为手游玩家提供各种手游礼包,其中包括手机游戏激活码,特权码,测试礼包,新手礼包,兑换码，节假日礼包等,还有多种着迷专属礼包等你拿,多种手游礼包尽在着迷网!"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>最新热门手机游戏礼包,激活码,兑换码_着迷网移动版</title>
    <link rel="stylesheet" type="text/css" href="http://static.joyme.com/mobile/cms/jmsy/css/common-beta1.css">
    <link rel="stylesheet" type="text/css"
          href="${URL_STATIC}/mobile/cms/jmsy/css/newquanbulibao.css?v=${version}">

    <script type="text/javascript" src="${URL_STATIC}/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
</head>
<body>
<div class="container">
    <%@ include file="/views/jsp/passport/m/gift-header-m.jsp" %>

    <div id="wrapper">
        <div id="main">
            <div id="center">
                <div class="gift-all-box">
                    <div class="gift-list-tit">全部礼包</div>
                    <div class="gift-tab-box fn-clear">
                        <div class="scro-tit-hide fn-left">
                            <div class="gift-tab-tit">
                                <div class="gift-tit-box">
                                    <c:forEach items="${letterGroupList}" var="groupLetter" varStatus="st">
                                                       <span id="span_letter_${groupLetter}"
                                                             <c:if test="${firstLetter eq groupLetter}">class="active"</c:if>>${groupLetter}</span>
                                    </c:forEach>
                                    <span id="span_letter_others">其他</span>
                                </div>
                            </div>
                        </div>
                        <div class="srco-con-hide fn-left">
                            <div class="gift-tab-con">
                                <div class="gift-con-box">
                                    <c:forEach items="${letterGroupList}" var="groupLetter" varStatus="st">
                                        <c:choose>
                                            <c:when test="${firstLetter eq groupLetter}">
                                                <div class="active fn-clear" id="div_letter_${groupLetter}" data-cp="0"
                                                     data-mp="1">
                                                    <c:forEach items="${list}" var="gift" varStatus="st">

                                                        <a href="${URL_M}/gift/${gift.activityGoodsId}">
                                                            <cite>
                                                                <img class="lazy" src=""
                                                                     data-url="${gift.activityPicUrl}"/>
                                                            </cite>
                                                            <font>${gift.activitySubject}</font>
                                                            <em>
                                                                <c:choose>
                                                                    <c:when test="${gift.platform.code == 4}">苹果&安卓</c:when>
                                                                    <c:when test="${gift.platform.code == 0}">苹果</c:when>
                                                                    <c:when test="${gift.platform.code == 1}">安卓</c:when>
                                                                </c:choose>
                                                            </em>
                                                            <i
                                                                    <c:if test="${gift.goodsResetAmount < 10}">class="i-num"</c:if>>剩余${gift.goodsResetAmount}</i>
                                                        </a>
                                                    </c:forEach>
                                                </div>

                                            </c:when>
                                            <c:otherwise>
                                                <div class="fn-clear" id="div_letter_${groupLetter}" data-cp="0"
                                                     data-mp="1">

                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                    <div class="fn-clear" id="div_letter_others" data-cp="0" data-mp="1">

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>
        <script>
            document.write('<script src="http://passport.${DOMAIN}/auth/footer/m?v=' + Math.random() + '"><\/script>');
        </script>
    </div>
    <%@ include file="/views/jsp/passport/m/nav.jsp" %>

</div>

<%--<div class="wrapper">--%>
<%--<div class="tab-box gifts-box">--%>
<%--<div class="tab-item-box border-r">--%>
<%--<div class="tab-item-over">--%>
<%--<div class="tab-item">--%>
<%--<c:forEach items="${letterGroupList}" var="groupLetter" varStatus="st">--%>
<%--<span id="span_letter_${groupLetter}"--%>
<%--<c:if test="${firstLetter eq groupLetter}">class="active"</c:if>>${groupLetter}</span>--%>
<%--</c:forEach>--%>
<%--<span id="span_letter_others">其他</span>--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>
<%--<div class="tab-con-box">--%>
<%--<div class="tab-con-over">--%>
<%--<div class="tab-con gifts-con">--%>
<%--<c:forEach items="${letterGroupList}" var="groupLetter" varStatus="st">--%>
<%--<c:choose>--%>
<%--<c:when test="${firstLetter eq groupLetter}">--%>
<%--<ul class="active" id="div_letter_${groupLetter}" data-cp="0" data-mp="1">--%>
<%--<c:choose>--%>
<%--<c:when test="${list != null && list.size() > 0}">--%>
<%--<c:forEach items="${list}" var="gift" varStatus="st">--%>
<%--<c:if test="${st.index== 0}">--%>
<%--<li>--%>
<%--</c:if>--%>
<%--<a href="${URL_M}/gift/${gift.activityGoodsId}">--%>
<%--<cite>--%>
<%--<img class="lazy" src="" data-url="${gift.activityPicUrl}"--%>
<%--alt="${gift.activitySubject}"--%>
<%--title="${gift.activitySubject}">--%>
<%--</cite>--%>
<%--<font>${gift.activitySubject}</font>--%>
<%--<em><c:choose><c:when--%>
<%--test="${gift.platform.code == 4}">苹果&安卓</c:when><c:when--%>
<%--test="${gift.platform.code == 0}">苹果</c:when><c:when--%>
<%--test="${gift.platform.code == 1}">安卓</c:when></c:choose></em>--%>
<%--<i <c:if test="${gift.goodsResetAmount < 10}">class="i-num"</c:if>>剩余${gift.goodsResetAmount}</i>--%>
<%--</a>--%>
<%--<c:if test="${st.index % 3 == 2 && st.index!=0 && !st.last}">--%>
<%--</li><li>--%>
<%--</c:if>--%>
<%--<c:if test="${st.last}">--%>
<%--</li>--%>
<%--</c:if>--%>
<%--</c:forEach>--%>
<%--</c:when>--%>
<%--<c:otherwise>--%>
<%--<li class="no-content" style="display: block"></li>--%>
<%--</c:otherwise>--%>
<%--</c:choose>--%>
<%--</ul>--%>
<%--</c:when>--%>
<%--<c:otherwise>--%>
<%--<ul id="div_letter_${groupLetter}" data-cp="0" data-mp="1">--%>
<%--</ul>--%>
<%--</c:otherwise>--%>
<%--</c:choose>--%>
<%--</c:forEach>--%>
<%--<ul id="div_letter_others" data-cp="0" data-mp="1">--%>
<%--</ul>--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>

<script type="text/javascript" src="${URL_STATIC}/mobile/cms/jmsy/js/lazyimg.js"></script>
<script type="text/javascript" src="${URL_STATIC}/mobile/cms/jmsy/js/common.js"></script>
<script>
    $(function () {
        $('.lazy').lazyImg({
            container: $('.srco-con-hide')
        });
    });

    var getGiftLock = false;
    $(document).ready(function () {
        $('span[id^=span_letter_]').on('touchstart', function (e) {
            $(this).addClass('active').siblings().removeClass('active');
            var letter = $(this).attr('id').replace('span_letter_', '');
            if ($('#div_letter_' + letter).find('a').length > 0) {
                $('#div_letter_' + letter).addClass('active').siblings().removeClass('active');
                return;
            } else {
                var cp = parseInt($('#div_letter_' + letter).attr('data-cp'));
                var mp = parseInt($('#div_letter_' + letter).attr('data-mp'))
                getGiftList(letter, cp, mp, e);
                $('.lazy').lazyImg();
            }
        });
    });

    function getGiftList(letter, cp, mp, e) {
        if (getGiftLock) {
            return;
        }
        getGiftLock = true;
        $.ajax({
            url: "http://api." + joyconfig.DOMAIN + "/json/gift/more?v=" + Math.random(),
            data: {p: cp + 1, firstletter: letter},
            timeout: 5000,
            cache: false,
            dataType: "jsonp",
            type: "POST",
            jsonpCallback: "lettergiftlist",
            success: function (req) {
                var res = req[0];
                if (res.rs == '-99999') {
                    alert('系统维护中!');
                    getGiftLock = false;
                    return;
                } else if (res.rs == '0') {
                    alert('系统错误!');
                    getGiftLock = false;
                    return;
                } else if (res.rs == '1') {
                    var result = res.result;
                    var html = '';
                    if (result == null || result == undefined) {
                        html = '<li class="no-content" style="display: block"></li>';
                    } else {
                        var rows = result.rows;
                        if (rows != null && rows.length > 0) {
                            for (var i = 0; i < rows.length; i++) {
                                var gift = rows[i];
                                if (gift != null && gift != undefined) {

                                    var platStr = '';
                                    if (gift.platform.code == 4) {
                                        platStr = '苹果&安卓';
                                    } else if (gift.platform.code == 0) {
                                        platStr = '苹果';
                                    } else if (gift.platform.code == 1) {
                                        platStr = '安卓';
                                    }
                                    html += '<a href="${URL_M}/gift/' + gift.activityGoodsId + '">' +
                                            '<cite><img class="lazy" src="' + gift.activityPicUrl + '" data-url="" alt="' + gift.activitySubject + '" title="' + gift.activitySubject + '"></cite>' +
                                            '<font>' + gift.activitySubject + '</font>' +
                                            '<em>' + platStr + '</em>' +
                                            '<i class="' + (gift.goodsResetAmount < 10 ? 'i-num' : '') + '">剩余' + gift.goodsResetAmount + '</i>' +
                                            '</a>';
                                }
                            }
                            $('#div_letter_' + letter).attr('data-cp', result.page.curPage);
                            $('#div_letter_' + letter).attr('data-mp', result.page.maxPage);
                        } else {
                            html = '<li class="no-content" style="display: block"></li>';
                        }
                    }
                    $('#div_letter_' + letter).append(html);
                    $('#div_letter_' + letter).addClass('active').siblings().removeClass('active');
                    getGiftLock = false;
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert('获取失败，请重试');
                getGiftLock = false;
                return;
            }
        });
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
