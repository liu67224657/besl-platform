<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>正在登录中...请稍候</title>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/applib.js"></script>
    <script type="text/javascript">
        var profile = {token: '${token}', uid: '${uid}', icon: '${icon}', uno: '${uno}', nick: '${nick}', description: '${description}'};
        var callBack = function () {
        }
        setTimeout(function(){
            saveLogin(profile, callBack)
        },100) ;
    </script>
</head>
<body>
正在登录中...请稍候
</body>
</html>