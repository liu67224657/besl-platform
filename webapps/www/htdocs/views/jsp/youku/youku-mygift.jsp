<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/youkutaglibs.jsp" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>个人中心</title>

    <!--手机style-->
    <link rel="stylesheet" media="screen and (max-width:768px)" href="${URL_LIB}/static/theme/youku/css/club.css">
    <!--平板style-->
    <link rel="stylesheet" media="screen and (min-width:768px)" href="${URL_LIB}/static/theme/youku/css/ipad_common.css">
    <link rel="stylesheet" media="screen and (min-width:768px)" href="${URL_LIB}/static/theme/youku/css/ipad_club.css">


    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.11.0.min.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/theme/youku/js/swiper.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/theme/youku/js/action.js"></script>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true);
        }, true);
    </script>
    <script type="text/javascript" src="http://static.joyme.com/app/youku/js/youkubtn.js"></script>
    <script type="text/javascript">
        $(function () {
            var signComplete = '${signcomplete}';
            if (signComplete == 'true') {
                $("#signdays").html("已签到<b>${signnum}</b>天");
                $('#a_tosign').html('已签到');
            } else {
                $("#signdays").html("尚未签到");
                $("#signdays").addClass("unsign");
                $('#a_tosign').html('立即签到');
            }
        });

    </script>
</head>
<body>
<!-- wrapper Start -->
<div id="wrapper">
    <!-- header Start -->
    <div class="my">
        <dl>
            <dd>
                <c:choose>
                    <c:when test="${point >0 && fn:length(user_nickname) > 0}">
                        <span class="noSign"><b>${point}</b>酷豆</span>
                        <span class="line"></span>
                        <span class="toSign" id="signdays">已签到<b>0</b>天</span>
                    </c:when>
                    <c:otherwise>
                        <span class="noSign"><b>空空如也</b></span>
                        <span class="line"></span>
                        <span>你还没有签到过</span>
                    </c:otherwise>
                </c:choose>
            </dd>
            <a href="/youku/webview/task/signpage?appkey=${appkey}&platform=${platform}&profileid=${profile.profileId}&uid=${profile.uid}" class="sign" id="a_tosign">立即签到</a>
        </dl>
    </div>
    <!-- header-box-begin -->
    <div class="page-box1 padd10">
        <h2 class="title"><span>我的宝贝</span></h2>
        <c:choose>
            <c:when test="${not empty dto}">
                <c:forEach items="${dto}" var="dto">
                    <dl class="border-one">
                        <a href="/youku/webview/giftmarket/mygiftdetail?aid=${dto.gid}&consumeorder=${dto.consumeOrder}"
                           class="list-box">
                            <dt>
                                <img src=" ${dto.gipic}" alt=""/>
                            </dt>
                            <dd>
                                <p>${dto.title}</p>
                                <em>消耗${dto.point}酷豆</em>
                            </dd>
                            <p class="msg">已兑换</p>
                        </a>
                    </dl>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p class="no-commodity">您还没有兑换过商品哦～</p>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<input type="hidden" value="${page.curPage}" id="midouWapCurPage"/>
<input type="hidden" value="${page.maxPage}" id="midouWapMaxPage"/>

<input type="hidden" value="${profile.profileId}" name="profileId"/>
<input type="hidden" value="${appkey}" name="appkey"/>
<input type="hidden" value="${point}" name="point"/>
<input type="hidden" value="${profile.uno}" name="uno"/>
<input type="hidden" value="2" name="type"/>
<input type="hidden" value="酷豆" name="wallname"/>
<%--<script src="${URL_LIB}/static/js/common/seajs.js"></script>--%>
<%--<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>--%>
<%--<script>--%>
<%--seajs.use('${URL_LIB}/static/js/init/joymeappmy-init.js');--%>
<%--</script>--%>

<%@ include file="/views/jsp/youku_pwiki.jsp" %>
</body>
</html>