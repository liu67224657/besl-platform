<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1x.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/jstllibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>下载玩霸 立得Q币</title>
    <link href="${URL_LIB}/static/theme/wap/css/activity/wap_common20150304.css" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/wap/css/activity/wap_styles20140304.css" rel="stylesheet" type="text/css">
    <%--<link href="${URL_LIB}/static/theme/wap/css/wap_common.css?${version}" rel="stylesheet" type="text/css">--%>

    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.11.0.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            window.addEventListener('DOMContentLoaded', function () {
                document.addEventListener('touchstart', function () {
                    return false
                }, true)
            }, true);

            $('#downloadjoyme').on('touchstart', function () {
                if (is_weixn()) {
                    $('.mark_box').show();
                    $('.popup_box').show();
                } else {
                    location.href = "http://www.joyme.com/appclick/sgyqq9ac";
                }

            });
            $('.popup_box').on('touchstart', function () {
                if (is_weixn()) {
                    $('.mark_box').hide();
                    $('.popup_box').hide();
                }
            })
        });
        function is_weixn() {
            var ua = navigator.userAgent.toLowerCase();
            if (ua.match(/MicroMessenger/i) == "micromessenger") {
                return true;
            } else {
                return false;
            }
        }

    </script>
<body>
<div id="wrapper">
    <div class="bg-box">
		<span class="pr">
			<div class="title"><p>下载着迷玩霸&nbsp;&nbsp;立得Q币</p></div>
			<img src="${URL_LIB}/static/theme/wap/images/activity/bg-1.jpg" alt="">
		</span>
        <span><img src="${URL_LIB}/static/theme/wap/images/activity/bg-2.jpg" alt=""></span>
        <span><img src="${URL_LIB}/static/theme/wap/images/activity/bg-3.jpg" alt=""></span>
        <span><img src="${URL_LIB}/static/theme/wap/images/activity/bg-4.jpg" alt=""></span>
        <span><img src="${URL_LIB}/static/theme/wap/images/activity/bg-5.jpg" alt=""></span>
        <span><img src="${URL_LIB}/static/theme/wap/images/activity/bg-6.jpg" alt=""></span>
        <span><img src="${URL_LIB}/static/theme/wap/images/activity/bg-7.jpg" alt=""></span>
        <span><img src="${URL_LIB}/static/theme/wap/images/activity/bg-8.jpg" alt=""></span>
		<span class="pr">
			<img src="${URL_LIB}/static/theme/wap/images/activity/bg-9.jpg" alt="">
			<div class="btn">
                <a href="javascript:void(0);" id="downloadjoyme" class="button">立即下载 着迷玩霸</a>

                <p>着迷最新APP“着迷玩霸”现已面世</p>

                <p>推广期间，<b>只需下载“着迷玩霸”</b></p>

                <p>即可在APP的相应入口</p>
                <%--http://lnk8.cn/cogwJh--%>
                <a href="javascript:void(0);" id="downloadjoyme" class="button-bottom">立即获得Q币</a>
            </div>
		</span>
        <span><img src="${URL_LIB}/static/theme/wap/images/activity/bg-10.jpg" alt=""></span>
        <span><img src="${URL_LIB}/static/theme/wap/images/activity/bg-11.jpg" alt=""></span>
        <span><img src="${URL_LIB}/static/theme/wap/images/activity/bg-12.jpg" alt=""></span>
        <span><img src="${URL_LIB}/static/theme/wap/images/activity/bg-13.jpg" alt=""></span>
    </div>
    <footer><span>本活动最终解释权归属着迷网所有</span></footer>
    <div class="popup_box" style="display:none;">
        <img src="${URL_LIB}/static/theme/wap/images/popup.jpg" alt="">
    </div>
    <div class="mark_box" id="mark_box" style="display:none;"></div>
</div>
</body>
</html>