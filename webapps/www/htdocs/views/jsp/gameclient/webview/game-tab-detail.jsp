<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1x.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/jstllibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta content="width=device.width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>游戏详情</title>
    <link href="${URL_LIB}/static/theme/wap/css/wap_common.css" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/wap/css/yxxq.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.11.0.min.js"></script>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);

        window.onload = function () {
            getWidth();
        }

        function getWidth() {
            var sum = 0;
            $('.move-pics span').each(function () {
                sum = sum + ($(this).width() + 15);
            });
            $('.move-pics>div').width(sum);
        }

    </script>
<body>
<div id="wrapper">
    <div class="yxxq_tab_main">
        <div class="yxxq_xq_box" style="display:block">
            <div>
                <p>${game.gameProfile}</p>

                <div class="move-pics">
                    <div style="width: 678px;">
                        <c:forEach var="pic" items="${gamepics}">
                            <span><img src="${pic}" alt=""></span>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>