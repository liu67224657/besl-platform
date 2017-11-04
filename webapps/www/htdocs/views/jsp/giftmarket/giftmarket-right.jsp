<%--
  Created by IntelliJ IDEA.
  User: zhitaoshi
  Date: 13-11-1
  Time: 下午2:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="joyme-lb-r fn-clear">

    <c:if test="${not empty  giftlist}">
        <div class="joyme-lbzx">
            <h2 class="joyme-title fn-clear"><span class="fn-left">兑换<b>排行</b></span>
                <%--<a href="${URL_WWW}/gift" class="joyme-title-more fn-right">更多</a>--%>
            </h2>
            <ul class="joyme-lbzx-box">
                <c:forEach items="${giftlist}" var="item" varStatus="index">
                    <li class="<c:if test='${index.index==0}'>first-lb show</c:if>" >
                        <a href="${URL_WWW}/gift/${item.goodsId}" target="_blank">
                            <dl>
                                <dt><cite></cite><span>${item.activityName}</span><b></b></dt>
                                <dd>
                                    <cite><img class="lazy" data-url="${item.pic}" width="70px" height="70px"
                                               title="${item.activityName}" alt="${item.activityName}"
                                               src="${URL_LIB}/static/theme/default/images/data-bg.gif"/></cite>

                                    <p>
                                        <span>${item.activityName}</span>
                                            <%--<b>${item.subDesc}</b>--%>
                                    </p>
                                    <em>领取</em>
                                </dd>
                            </dl>
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </div>

    </c:if>
    <!--joyme-lbzx-->

    <!--joyme-lbzx==end-->
    <!--joyme-lb-ban-->
    <%@ include file="/hotdeploy/views/jsp/giftmarket/giftmarket-advert-new.jsp" %>

    <!--joyme-lb-ban-->
    <c:if test="${not empty taolist}">
        <div class="joyme-lbzx-th">
            <h3 class="joyme-title fn-clear"><span class="fn-left">快来<b>淘号</b></span></h3>

            <div class="th-list fn-clear">
                <ul>
                    <c:forEach items="${taolist}" var="item" varStatus="index">
                        <li><a href="${URL_WWW}/gift/${item.activityGoodsId}" target="_blank"><cite
                                class="lb-list-icon"></cite><span>${item.activitySubject}</span><font
                                class="th-btn">淘号</font></a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </c:if>
</div>