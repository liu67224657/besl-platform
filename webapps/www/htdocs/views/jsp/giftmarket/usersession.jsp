<%--
  Created by IntelliJ IDEA.
  User: zhitaoshi
  Date: 13-11-1
  Time: 下午2:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="user">
    <h2>HI 欢迎回来</h2>
    <dl>
        <dt>
            <%--<a href="${URL_WWW}/people/${userSession.domain}">--%>
                <img width="58" height="58"
                     src="${icon:parseIcon(userSession.icon,userSession.sex, "")}"/>
            <%--</a>--%>
        </dt>
        <dd>
            <em style="overflow: hidden;">
                <%--<a href="${URL_WWW}/people/${userSession.domain}">--%>
                <a href="javascript:void(0);">
                ${userSession.nick}</a>
            </em>
            <c:choose>
                <c:when test="${pointAmount > 999999}"><span>我的积分：<br/><i>${pointAmount}</i></span><br/></c:when>
                <c:otherwise><span>我的积分：<i>${pointAmount}</i></span></c:otherwise>
            </c:choose>
            <p>[ 今天已赚<i>${dayPoint}</i>积分 ]</p>
            <a class="mygift" href="${URL_WWW}/mygift">我的礼包</a>
        </dd>
    </dl>
    <div class="user-gift-link"><a href="${URL_WWW}/giftmarket/creditdetail">积分明细记录</a></div>
    <%----%>
    <%--<a--%>
            <%--href="${URL_WWW}/giftmarket/affordable">查看可兑换的</a></div>--%>
</div>