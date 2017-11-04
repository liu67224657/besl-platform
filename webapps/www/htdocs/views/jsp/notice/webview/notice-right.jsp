<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
</head>

<body>
<!-- 右侧区域  开始 -->
<div class="col-md-3 web-hide ">
    <div id="sidebar">
        <div class="user-mess-box">
            <div class="user-int-mess">
                <a href="${URL_UC}/usercenter/home" class="user-head-img" data-id="${profileid}"><img src="${profileMap[profileid].icon}">
                    <c:if test="${profileMap[profileid].vtype>0}">
                        <span class="user-vip" title="${profileMap[profileid].vdesc}"></span>
                    </c:if>
                    <c:if test="${not empty profileMap[profileid].headskin}">
                        <span class="luojiaoye-def luojiaoye-dec-0${profileMap[profileid].headskin}"></span>
                    </c:if>
                </a>
                <font class="nickname">${profileMap[profileid].nick}</font>
                    <c:if test="${profileMap[profileid].sex eq 0}">
                        <i class="user-sex female"></i>
                    </c:if>
                    <c:if test="${profileMap[profileid].sex eq 1}">
                        <i class="user-sex man"></i>
                    </c:if>
            </div>
            <div class="user-messing">
                <a href="${URL_UC}/usercenter/notice/at"
                   class="ding <c:if test="${noticeType eq 'at'}">on</c:if>">@我的
                    <c:if test="${not empty noticeSumMap && not empty noticeSumMap['at']}">
                        <i>${noticeSumMap['at'].value}</i>
                    </c:if>
                </a>

                <a href="${URL_UC}/usercenter/notice/reply"
                   class="discuss <c:if test="${noticeType eq 'reply'}">on</c:if>">评论及回复
                    <c:if test="${not empty noticeSumMap && not empty noticeSumMap['reply']}">
                        <i>${noticeSumMap['reply'].value}</i>
                    </c:if>
                </a>
                <a href="${URL_UC}/usercenter/notice/agree"
                   class="zan <c:if test="${noticeType eq 'agree'}">on</c:if>">点赞
                    <c:if test="${not empty noticeSumMap && not empty noticeSumMap['agree']}">
                        <i>${noticeSumMap['agree'].value}</i>
                    </c:if></a>
                <a href="${URL_UC}/usercenter/notice/follow"
                   class="follow <c:if test="${noticeType eq 'follow'}">on</c:if>">关注
                    <c:if test="${not empty noticeSumMap && not empty noticeSumMap['follow']}">
                        <i>${noticeSumMap['follow'].value}</i>
                    </c:if>
                </a>
                <a href="${URL_UC}/usercenter/notice/sys"
                   class="notice <c:if test="${noticeType eq 'sys'}">on</c:if>">系统通知
                    <c:if test="${not empty noticeSumMap && not empty noticeSumMap['sys']}">
                        <i>${noticeSumMap['sys'].value}</i>
                    </c:if>
                </a>
            </div>
        </div>
        <!-- 广告位  开始 -->
        <%--<div class="ad-con">--%>
        <%--<cite><img src="img/ad-img.jpg"><i>活动</i></cite>--%>
        <%--</div>--%>
        <!-- 广告位  结束 -->
    </div>
</div>
<!-- 右侧区域  开始 -->


</body>

</html>