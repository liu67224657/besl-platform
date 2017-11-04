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
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>QQ登录</title>
    <style type="text/css">
        a {
            text-decoration: none;
        }

        * {
            -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
            margin: 0;
            padding: 0;
            word-break: break-all;
            word-wrap: break-word;
        }

        #box {
            width: 100%;
            height: 90px;
            position: absolute;
            top: 50%;
            left: 0;
            margin-top: -45px;
        }

        #box a {
            display: block;
            height: 40px;
            line-height: 40px;
            margin-left: 10px;
            margin-right: 10px;
            text-align: center;
            -webkit-border-radius: 4px;
            border-radius: 4px;
        }

        #box a:nth-of-type(1) {
            background: #2492f8;
            font-size: 16px;
            color: #fff;
            margin-bottom: 14px;
        }

        #box a:nth-of-type(1):before {
            content: '';
            display: inline-block;
            width: 21px;
            height: 23px;
            background: url(http://reswiki1.joyme.com/css/lt/mwiki/qqicon.png) no-repeat;
            background-size: 21px 23px;
            vertical-align: middle;
            margin: -6px 4px 0 0;
        }

        #box a:nth-of-type(1):active {
            background: #296eae
        }

        #box a:nth-of-type(2) {
            background: #dcdcdc;
            color: #434343;
            font-size: 18px;
            letter-spacing: 10px
        }

        #box a:nth-of-type(2):active {
            background: #b5b5b5
        }

    </style>

    <script src="${URL_LIB}/static/js/common/seajs.js"></script>
    <script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
    <script>
        seajs.use('${URL_LIB}/static/js/init/login-init.js')
    </script>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
</head>

<body>
<div id="box">
    <a href="http://passport.${DOMAIN}/auth/thirdapi/qq/bind<c:if test="${reurl!=null}">?reurl=${reurl}</c:if>">腾讯QQ登录</a>
    <a href="javascript:window.history.go(-1)">&nbsp;取消</a>
</div>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
</body>
</html>