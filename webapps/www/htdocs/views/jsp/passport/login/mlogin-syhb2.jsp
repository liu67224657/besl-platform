<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta content="width=device.width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>登录页</title>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <link href="${URL_LIB}/static/theme/default/css/wap_login.css" rel="stylesheet" type="text/css">
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
</head>

<body>
<div id="wap_login">
    <div class="wap_loginTitle">请选择登录方式</div>
    <div class="wap_loginList">
        <%--<a href="#">--%>
        <%--<span class="wx_icon"></span>--%>
        <%--<b>微信</b>--%>
        <%--</a>--%>
        <a href="http://passport.${DOMAIN}/auth/thirdapi/sinaweibo/bind<c:if test="${reurl!=null}">?reurl=${reurl}</c:if>">
            <span class="wb_icon"></span>
            <b>微博</b>
        </a>
        <a href="http://passport.${DOMAIN}/auth/thirdapi/qq/bind<c:if test="${reurl!=null}">?reurl=${reurl}</c:if>">
            <span class="qq_icon"></span>
            <b>QQ</b>
        </a>
    </div>
    <div class="cancel_box">
        <div class="cancel_btn"><a href="javascript:window.history.go(-1)">取消</a></div>
    </div>

</div>
</body>
</html>