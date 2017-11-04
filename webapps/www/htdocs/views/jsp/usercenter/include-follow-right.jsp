<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<%
    String path = request.getRequestURL().toString();
    boolean isFollowPage = path.contains("follow");
    String followsClass = isFollowPage? "followed on":"fans";
    String fansClass = isFollowPage? "fans":"followed on";
%>
<!-- 右侧区域 开始 -->
<div class="col-md-3 web-hide ">
    <div id="sidebar">
        <div class="user-mess-box">
            <div class="user-int-mess">
                <a href="${URL_UC}/usercenter/page?pid=${profileId}" class="user-head-img">
                    <c:if   test="${profile.vtype > 0}">
                        <span class="user-vip" title="${profile.vdesc}"></span>
                    </c:if>
                    <img src="${profile.icon}">
                    <span class="luojiaoye-def luojiaoye-dec-0${profile.headskin}"></span>
                </a>
                <font class="nickname">${profile.nick}</font>
                <i class="user-sex man"></i>
            </div>
            <c:choose>
                <c:when test="${profileId eq userSession.profileId}">
                    <div class="user-messing-situ ">
                        <a href="/usercenter/follow/mylist" class="<%=followsClass%>">
                            <i id="follow_count">${profile.follows}</i></br>关注</a>
                        <a href="/usercenter/fans/mylist" class="<%=fansClass%>">
                            <i id="fans_count">${profile.fans}</i></br>粉丝</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="user-messing-situ ">
                        <a href="/usercenter/follow/list?profileid=${profile.profileId}" class="<%=followsClass%>">
                            <i id="follow_count">${profile.follows}</i></br>关注</a>
                        <a href="/usercenter/fans/list?profileid=${profile.profileId}" class="<%=fansClass%>">
                            <i id="fans_count">${profile.fans}</i></br>粉丝</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        <!-- 广告位  开始 -->
        <%--<div class="ad-con">--%>
            <%--<cite><img--%>
                    <%--src="${URL_STATIC}/pc/userEncourageSys/img/ad-img.jpg"><i>活动</i></cite>--%>
        <%--</div>--%>
        <!-- 广告位  结束 -->
    </div>
</div>
<!-- 右侧区域 开始 -->
</div>
</div>
