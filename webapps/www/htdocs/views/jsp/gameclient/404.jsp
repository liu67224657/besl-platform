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

    <link href="${URL_LIB}/static/theme/wap/css/wap_common.css?${version}" rel="stylesheet" type="text/css">

    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
</head>
<body>
<div id="wrapper">
	<div class="page_404">
		<cite class="page_404_icon"><img src="${URL_LIB}/static/theme/wap/images/404.png" alt=""></cite>
		<p>出错了，这下没的玩了～</p>
	</div>
	<div class="page_404" style="display:none">
		<cite class="page_sb_icon"><img src="images/game-no.png" alt=""></cite>
		<p>出错了，这下没的玩了～</p>
	</div>
</div>


</body>
</html>
