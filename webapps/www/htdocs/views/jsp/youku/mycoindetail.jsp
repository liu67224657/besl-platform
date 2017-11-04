<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/youkutaglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>订单详情</title>
    <!--手机style-->
    <link rel="stylesheet" media="screen and (max-width:768px)" href="${URL_LIB}/static/theme/youku/css/club.css">
    <!--平板style-->
    <link rel="stylesheet" media="screen and (min-width:768px)" href="${URL_LIB}/static/theme/youku/css/ipad_common.css">
    <link rel="stylesheet" media="screen and (min-width:768px)" href="${URL_LIB}/static/theme/youku/css/ipad_club.css">

    <script type="text/javascript" src="http://static.joyme.com/app/youku/js/youkubtn.js"></script>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
</head>
<body>
<div id="wrapper">
    <div class="win pt-45">
        <a href="/youku/webview/giftmarket/list" class="sign Details-sign">前往俱乐部</a>
        <h1>已兑换</h1>

        <p>订单号：${dto.consumeOrder}</p>

        <p>下单时间：${dto.endTime}</p>

        <p>消耗酷豆：<span>${dto.point}</span></p>
    </div>

    <div class="order">
        <h1><span>订单详情</span></h1>
        <dl>
            <dt class="fl"><img src="${dto.gipic}" width="105" height="60" alt=""></dt>
            <dd class="fl">
                <h3>${dto.title}</h3>
                <c:choose>
                    <c:when test="${dto.goodsType eq 1}">
                        <b>虚拟商品</b>
                    </c:when>
                    <c:otherwise>
                        <b>实物商品</b>
                    </c:otherwise>
                </c:choose>
                <b>数量x<span class="num">1</span></b>&nbsp;&nbsp;&nbsp;
                <%--<b>规格：<span class="color">默认</span></b>--%>
            </dd>
        </dl>
    </div>
    <div class="win-message">
        <c:choose>
            <c:when test="${dto.goodsType eq 1}">
                <c:if test="${not empty goodsItem}">
                    <c:forEach items="${goodsItem}" var="dto">
                        <c:choose>
                            <c:when test="${not empty dto.snValue2}">
                                <h2>卡号： <p><span>${dto.snValue1}</span></p></h2>
                                <h2>密码：<p><span>${dto.snValue2}</span></p></h2>
                            </c:when>
                            <c:otherwise>
                                <h2>卡号：<p><span>${dto.snValue1}</span></p></h2>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </c:if>
            </c:when>
            <c:otherwise>
                <h1>收货人：<span>${address.contact}</span></h1>
                <em>电话：${address.phone}</em>
                <cite>收货地址：${address.province}${address.city}${address.county}${address.address}</cite>
            </c:otherwise>
        </c:choose>
    </div>
    <c:if test="${dto.goodsType eq 1}">
        <div class="sp-details-main exclusive clearfix">
            <h1 class="no-xian order-bt"><span>商品详情＆使用方法</span></h1>

            <c:if test="${not empty dto.textJsonItemsList}">
                <c:forEach items="${dto.textJsonItemsList}" var="goods">
                    <c:choose>
                        <c:when test="${goods.type=='1'}">
                            <p>${goods.item}</p>
                        </c:when>
                        <c:otherwise>
                            <p><img src="${goods.item}"/></p>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </c:if>
        </div>
    </c:if>
</div>
<%@ include file="/views/jsp/youku_pwiki.jsp" %>

<input type="hidden" value="${profileid}" name="profileId"/>
<input type="hidden" value="${appkey}" name="appkey"/>
<input type="hidden" value="${activityDetailDTO.aid}" name="aid"/>
<input type="hidden" value="${activityDetailDTO.gid}" name="gid"/>
</body>
</html>
