<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/youkutaglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.11.0.min.js"></script>

    <title>酷豆俱乐部</title>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
    <!--手机style-->
    <link rel="stylesheet" media="screen and (max-width:768px)" href="${URL_LIB}/static/theme/youku/css/club.css">
    <!--平板style-->
    <link rel="stylesheet" media="screen and (min-width:768px)" href="${URL_LIB}/static/theme/youku/css/ipad_common.css">
    <link rel="stylesheet" media="screen and (min-width:768px)" href="${URL_LIB}/static/theme/youku/css/ipad_club.css">

</head>
<body>
<!-- wrapper Start -->
<div id="wrapper">
    <!-- header Start -->
    <div id="header">
        <!-- 图片轮播 -->
        <div id="pic-loop">
            <div class="pic-loop-box swiper-wrapper">
                <c:forEach items="${menulist}" var="item">
                    <div class="swiper-slide">
                        <c:choose>
                            <c:when test="${not empty item.url}">
                                <a href="${item.url}" <c:if test="${fn:length(item.gameId) > 0}">id="idfa_downloadlink" data-aid="${item.gameId}"</c:if>>
                                <img src="${item.picUrl1}"></a>
                            </c:when>
                            <c:otherwise>
                                <a href="javascript:void(0);" <c:if test="${fn:length(item.gameId) > 0}">id="idfa_downloadlink" data-aid="${item.gameId}"</c:if>>
                                <img src="${item.picUrl1}"></a>
                            </c:otherwise>
                        </c:choose>

                    </div>
                </c:forEach>
            </div>
            <div class="pagination"></div>
        </div>
        <!-- 图片轮播 -->
    </div>
    <!-- header-box-begin -->
    <%--<dl>--%>
    <%--<a href="${ykt:ykLogin(pageContext.request,'/youku/webview/giftmarket/mygift','/youku/webview/giftmarket/list')}">--%>
    <%--<dt><img src="${URL_LIB}/static/theme/wap/images/youku/club-1.png" alt=""></dt>--%>
    <%--<dd>--%>
    <%--<p>个人中心</p>--%>
    <%--<span>--%>
    <%--<c:choose>--%>
    <%--<c:when test="${not empty userPoint && isLogin}">--%>
    <%--${userPoint.userPoint}酷豆--%>
    <%--</c:when>--%>
    <%--<c:otherwise>--%>
    <%--请先登录--%>
    <%--</c:otherwise>--%>
    <%--</c:choose>--%>

    <%--</span>--%>
    <%--</dd>--%>
    <%--</a>--%>
    <%--</dl>--%>
    <%--<dl>--%>
    <%--<c:set var="url"--%>
    <%--value="/youku/webview/task/signpage?appkey=${appkey}&platform=${platform}&profileid=${profile.profileId}&uid=${profile.uid}"/>--%>
    <%--&lt;%&ndash;<a href="${ykt:ykLogin(pageContext.request,url,'')}">&ndash;%&gt;--%>
    <%--<a href="/youku/webview/task/signpage?appkey=${appkey}&platform=${platform}&profileid=${profile.profileId}&uid=${profile.uid}">--%>
    <%--<dt><img src="${URL_LIB}/static/theme/wap/images/youku/club-2.png" alt=""></dt>--%>
    <%--<dd>--%>
    <%--<p>每日签到</p>--%>
    <%--<span id="signdays"></span>--%>
    <%--</dd>--%>
    <%--</a>--%>
    <%--</dl>--%>
    <%--<dl>--%>
    <%--<a href="/youku/webview/giftmarket/novice">--%>
    <%--<dt><img src="${URL_LIB}/static/theme/wap/images/youku/club-3.png" alt=""></dt>--%>
    <%--<dd>--%>
    <%--<p>新手指南</p>--%>
    <%--<span>玩转俱乐部</span>--%>
    <%--</dd>--%>
    <%--</a>--%>
    <%--</dl>--%>


</div>
<script>
    $(document).ready(function () {
        loadUserInfo();
        var number=0;
        var cookieInterval = setInterval(function () {
            var reqUid = $('#input_hidden_uid').val();
            var uid = -1;
            number++;
            var cookieUid = getCookie("jmuc_u");
            if(cookieUid != null){
                uid = cookieUid;
            }
            if(parseInt(reqUid) != uid || number >= 10){
                clearInterval(cookieInterval);
                if(parseInt(reqUid) != uid){
                    loadUserInfo();
                }
            }
        }, 1000);
    });

    function loadUserInfo() {
        $.ajax({
            type: "POST",
            cache: false,
            url: "/youku/ajax/userdata?v=" + Math.random(),
//            url: jsconfig.YK_DOMAIN + "/youku/ajax/userdata?v=" + Math.random(),
            success: function (req) {
                $('#wrapper').append(req);
            }
        });
    }

    function getCookie(name) {
        var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
        if(arr=document.cookie.match(reg))
            return unescape(arr[2]);
        else
            return null;
    }
</script>
<script type="text/javascript" src="http://static.joyme.com/app/youku/js/youkubtn.js"></script>
<!-- wrapper End -->
<script type="text/javascript" src="${URL_LIB}/static/theme/youku/js/swiper.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/theme/youku/js/action.js"></script>
<%@ include file="/views/jsp/youku_pwiki.jsp" %>

<script type="text/javascript" src="http://stat.joyme.com/clickheat/js/clickheat.js"></script>
<script type="text/javascript">
    <!--
    clickHeatSite = 'Club';
    clickHeatGroup = 'youku';
    clickHeatServer = 'http://stat.joyme.com/clickheat/click.php';
    initClickHeat();
    //-->
</script>

</body>
</html>