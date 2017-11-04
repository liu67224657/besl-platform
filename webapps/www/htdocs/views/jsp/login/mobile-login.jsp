<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <link href="http://lib.joyme.com/static/theme/default/css/core.css" rel="stylesheet" type="text/css">
    <link href="css/joymeMobile.css" rel="stylesheet" type="text/css">
    <title>登录</title>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>

    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
</head>
<body>
<div id="wrap-snsLogin">

    <div class="joymeMobile-title-bg">
        <h1 class="joymeMobile-title"><span>登录着迷</span></h1>
    </div>

    <div class="sns-choosePlatform">
        <h2>社交网络登录</h2>

        <div>
            <a href="#" class="sns-qq">QQ</a>
            <a href="#" class="sns-sina">SINA</a>
            <%--<a href="#" class="sns-tengxun">TENTXUN</a>--%>
            <%--<a href="#" class="sns-renren">RENREN</a>--%>
        </div>
    </div>

    <div class="localLogin">
        <h2>老用户邮箱登录</h2>

        <div>
            <p><span></span><input type="email" placeholder="邮箱"></p>

            <p><span></span><input type="password" placeholder="密码"></p>
        </div>
        <div><span>您输入的密码有误!</span><a href="#">忘记密码?</a></div>
    </div>

    <div class="btn-box">
        <a href="javascript:void(0)" class="btn btn-1">登&nbsp;&nbsp;录</a>
    </div>

</div>

</body>
</html>