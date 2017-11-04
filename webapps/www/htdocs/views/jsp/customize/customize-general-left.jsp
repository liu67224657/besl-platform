<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
</head>
<body>

<div class="col-md-3 bg-ebeffa">
    <div class="setting-sidebar">
        <div class="setting-name web-hide">
            <a href="${URL_WWW}/usercenter/home" class="user-login user-head-img" data-id="${userinfoDTO.profileId}">
                <img id="profileIcon"
                     src="${icon:parseIcon( profile.getIcon(),  profile.getSex(),"m")}" alt="">
                <c:if test="${not empty userinfoDTO}"><c:if test="${userinfoDTO.vtype>0}"><cite class="vip" title="${userinfoDTO.vdesc}"></cite></c:if></c:if>
                <span class="info-dec-def <c:if test="${not empty userinfoDTO.headskin}">info-dec-0${userinfoDTO.headskin}</c:if>"></span>
                <%--info-dec-01 info-dec-02 info-dec-03 info-dec-04 info-dec-05 info-dec-06 info-dec-07--%>
            </a>
            <div class="user-intro-con">
                <cite class="user-des fn-clear">
                    <font class="nickname">${profile.nick}</font>
                    <c:if test="${not empty profile.sex}">
                            <c:if test="${profile.sex eq 0}">
                                <i class="user-sex female"></i>
                            </c:if>
                            <c:if test="${profile.sex eq 1}">
                                <i class="user-sex man"></i>
                            </c:if>
                    </c:if>
                </cite>
                <a href="javascript:;" class="user-intr">${profile.description}</a>
            </div>
        </div>
        <ul class="setting-list">
            <li><a href="${URL_UC}/usercenter/account/safe" class="account <c:if test="${type eq 'account'}">on</c:if>"><i></i>账号安全</a></li>
            <li><a href="${URL_UC}/usercenter/customize/personinfo" class="message <c:if test="${type eq 'message'}">on</c:if>"><i></i>我的信息</a></li>
            <li><a href="${URL_UC}/usercenter/customize/modifyhead"
                   class="portrait <c:if test="${type eq 'portrait'}">on</c:if>"><i></i>修改头像</a></li>
            <%--<li><a href="${URL_UC}/usercenter/customize/skin"--%>
                   <%--class="cloth <c:if test="${type eq 'cloth'}">on</c:if>"><i></i>个性换装</a></li> //todo 用户激励体系删除--%>
            <li><a href="${URL_UC}/usercenter/customize/privacy" class="secret <c:if test="${type eq 'secret'}">on</c:if>"><i></i>隐私</a></li>
        </ul>
    </div>
</div>
</body>
</html>