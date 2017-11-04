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
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>404</title>

    <%--<link href="${URL_LIB}/static/theme/default/css/wap_common.css?${version}" rel="stylesheet" type="text/css">--%>
    <style>
        .find-404 {
            position: absolute;
            top: 50%;
            left: 50%;
            margin-top: -99px;
            margin-left: -68px;
            width: 137px;
            height: 199px;
        }

        .find-404 p {
            width: 100%;
            font-size: 0;
        }

        .find-404 p img {
            width: 100%;
        }
    </style>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
        function getMoreInfo () {
            return {"show":"no"};
        }
    </script>
</head>
<body>
<div id="wrapper">
    <div class="find-404">
        <p><img src="${URL_LIB}/static/theme/default/images/my/404.png" alt=""></p>
    </div>
</div>
</body>
</html>
