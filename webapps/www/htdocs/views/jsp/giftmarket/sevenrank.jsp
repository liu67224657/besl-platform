<%--
  Created by IntelliJ IDEA.
  User: zhitaoshi
  Date: 13-11-1
  Time: 下午2:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<c:if test="${ranksList != null && !empty ranksList}">
    <div class="gift-center-title clearfix">
        <h2>7日兑换排行</h2>
    </div>
    <div class="top-bg exchange-ranking">
        <ul>
            <c:forEach items="${ranksList}" var="ranks">
                <c:if test="${ranks.activityId!=1502}">
                    <li>
                        <c:choose>
                            <c:when test="${ranks.activityType.code==0}">
                                <a href="${URL_WWW}/coin/${ranks.goodsId}"><img width="40" height="40"
                                                                                   src="${ranks.pic}"/></a>

                                <div>
                                    <p><a href="${URL_WWW}/coin/${ranks.goodsId}">${ranks.activityName}</a></p>

                                    <p>7天内已有<span>${ranks.exchange_num}</span>人领取</p>
                                </div>
                            </c:when>
                            <c:otherwise>

                                <a href="${URL_WWW}/gift/${ranks.goodsId}"><img width="40" height="40"
                                                                                   src="${ranks.pic}"/></a>

                                <div>
                                    <p><a href="${URL_WWW}/gift/${ranks.goodsId}">${ranks.activityName}</a></p>

                                    <p>7天内已有<span>${ranks.exchange_num}</span>人领取</p>
                                </div>
                            </c:otherwise>
                        </c:choose>

                        <c:choose>
                            <c:when test="${ranks.activityType.code==0}">
                                <a href="${URL_WWW}/coin/${ranks.goodsId}" class="iwant">我也要</a>
                            </c:when>
                            <c:otherwise>
                                <a href="${URL_WWW}/gift/${ranks.goodsId}" class="iwant">我也要</a>
                            </c:otherwise>
                        </c:choose>
                    </li>
                </c:if>
            </c:forEach>
        </ul>
    </div>
</c:if>