<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<form action="${ctx}/guide/focus" id="guideForm" name="guideForm">
    <div class="commenduser">
        <div class="ch">
            <p>欢迎你来到着迷，在这里，大家一起分享一切有意思的事，比如游戏。</p>
            <p>您可以先看看这里有哪些你感兴趣的人，例如如下这些：</p>
            <a class="close" href="javascript:void(0)" id="closeRecommend"></a>
        </div>
        <div class="cm">
            <c:forEach items="${recommendUserMap}" var="recommendUser" varStatus="st">
                <c:if test="${st.index%3==0}"><div class="commendlist"></c:if>
                <c:choose>
                    <c:when test="${st.index%3==2}">
                        <div class="comuserbox_th">
                            <div class="comface">
                                <img width="58" height="58"
                                     src="<c:out value="${uf:parseSFace(recommendUser.value.headIcon)}"/>"/>
                                <input type="checkbox" checked="checked" class="comcheck" name="userno"
                                       value="${recommendUser.value.uno}">
                            </div>
                            <div class="com-box">
                                <a href="javascript:void(0)" class="comname" title="${recommendUser.value.screenName}">
                                    <c:choose>
                                        <c:when test="${recommendUser.value.screenName!=null && fn:length(recommendUser.value.screenName)>9}">
                                            <c:out value="${fn:substring(recommendUser.value.screenName, 0, 9)}"/>...
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${recommendUser.value.screenName}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </a>
                                <div class="cominfo">
                                    <c:choose>
                                        <c:when test="${recommendUser.value.description!=null && fn:length(recommendUser.value.description)>15}">
                                            <c:out value="${fn:substring(recommendUser.value.description, 0, 15)}"/>...
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${recommendUser.value.description}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="comreason">${recommendUser.value.tips}</div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="comuserbox">
                            <div class="comface">
                                <img width="58" height="58"
                                     src="<c:out value="${uf:parseSFace(recommendUser.value.headIcon)}"/>"/>
                                <input type="checkbox" checked="checked" class="comcheck" name="userno"
                                       value="${recommendUser.value.uno}">
                            </div>
                            <div class="com-box">
                                <a href="javascript:void(0)" class="comname" title="${recommendUser.value.screenName}">
                                    <c:choose>
                                        <c:when test="${recommendUser.value.screenName!=null && fn:length(recommendUser.value.screenName)>9}">
                                            <c:out value="${fn:substring(recommendUser.value.screenName, 0, 9)}"/>...
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${recommendUser.value.screenName}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </a>
                                <div class="cominfo">
                                    <c:choose>
                                        <c:when test="${recommendUser.value.description!=null && fn:length(recommendUser.value.description)>15}">
                                            <c:out value="${fn:substring(recommendUser.value.description, 0, 15)}"/>...
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${recommendUser.value.description}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="comreason">${recommendUser.value.tips}</div>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
                <c:if test="${st.index%3==2 || st.last}"></div></c:if>
            </c:forEach>
        </div>
        <div class="cb">
            <a class="submitbtn" href="javascript:void(0)" id="guideFocus"><span>关注所选</span></a>
        </div>
    </div>
</form>
<div class="com-title"></div>
<div class="commentline" id="guideline"></div>