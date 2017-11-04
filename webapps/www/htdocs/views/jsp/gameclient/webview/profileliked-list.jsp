<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1x.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/jstllibs.jsp" %>
<%@ include file="/views/jsp/common/jsconfig.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta content="width=device.width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title><c:choose><c:when
            test="${fn:length(desuid) <= 0}">喜欢我的人${likedsum}</c:when><c:otherwise>喜欢TA的人${likedsum}</c:otherwise></c:choose>
    </title>
    <script type="text/javascript">
        window.browser = {
            versions: function () {
                var u = navigator.userAgent, app = navigator.appVersion;
                return {//移动终端浏览器版本信息
                    trident: u.indexOf('Trident') > -1, //IE内核
                    presto: u.indexOf('Presto') > -1, //opera内核
                    webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
                    gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
                    mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
                    ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
                    android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或uc浏览器
                    iPhone: u.indexOf('iPhone') > -1, //是否为iPhone或者QQHD浏览器
                    iPad: u.indexOf('iPad') > -1, //是否iPad
                    webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
                };
            }(),
            language: (navigator.browserLanguage || navigator.language).toLowerCase()
        }

        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.11.0.min.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/applib.js"></script>
    <link href="${URL_LIB}/static/theme/wap/css/wap_common.css" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/wap/css/wap_style.css" rel="stylesheet" type="text/css">
</head>
<body>
<div id="wrapper">
    <c:choose>
        <c:when test="${priflelist.size() <= 0}">
            <!--喜欢TA的人 无数据-->
            <div class="like_none">
                <p><c:choose><c:when test="${fn:length(desuid) <= 0}">喜欢你的人快来了，多发靓照让更多迷友喜欢你。</c:when><c:otherwise>喜欢TA的人快来了。</c:otherwise></c:choose>
                </p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="look-main wrapper pt-45" id="main-list">
                <c:forEach var="profile" items="${priflelist}">
                    <div class="look-main-block like">
                        <a href="javascript:void(0); " onclick="jump(22,'${profile.uid}');">
                            <cite><img src="${profile.iconurl}" alt=""></cite>

                            <h2><c:choose><c:when test="${fn:length(profile.nick) > 4}"><c:out
                                    value="${fn:substring(profile.nick, 0, 4)}..."/></c:when><c:otherwise>${profile.nick}</c:otherwise></c:choose>
                            </h2>
                        </a>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>
<script type="text/javascript">
    var curPage =${page.curPage};
    var pageSize =${page.pageSize};
    var maxPage =${page.maxPage};
    var uid = '<c:choose><c:when test="${fn:length(desuid) <= 0}">${uid}</c:when><c:otherwise>${desuid}</c:otherwise></c:choose>';

    $(document).ready(function () {
        $('.wrapper').scroll(function () {
            if (curPage >= maxPage) {
                return;
            } else {
                scrollPage();
            }
        });
    });
    var scrollLock = false;
    function scrollPage() {
        var loadTips = "<div id='loadTips'><div class='loadTips txt-c'><div class='load-bar1'></div><div class='load-bar2'></div><div class='load-bar1'></div></div>";
        var sTop = $('.wrapper')[0].scrollTop + 100;
        var sHeight = $('.wrapper')[0].scrollHeight;
        var scrollHeight = $('.wrapper').height();
        var sNum = sHeight - scrollHeight;
        if (sTop >= sNum && !scrollLock) {
            scrollLock = true;
            $('.wrapper').append(loadTips);
            $.ajax({
                url: 'http://api.' + joyconfig.DOMAIN + '/joymeapp/gameclient/api/profile/likedlist?uid=' + uid + '&pnum=' + (curPage + 1) + '&pcount=' + pageSize,
                type: 'GET',
                success: function (req) {
                    var result = eval('(' + req + ')');

                    if (result.rs != '1') {
                        return;
                    }

                    if (result.result == null || result.result === undefined) {
                        return;
                    }
                    $('#loadTips').remove();
                    var list = result.result.priflelist;
                    for (var i = 0; i < list.length; i++) {
                        var nick = list[i].nick;
                        if (nick.length > 4) {
                            nick = nick.substring(0, 4) + "...";
                        }
                        var html = '<div class="look-main-block like">' +
                                '<a href="javascript:void(0); " onclick="jump(22,' + list[i].uid + ');">' +
                                '<cite><img src="' + list[i].iconurl + '" alt=""></cite>' +
                                '<h2>' + nick + '</h2></a></div>';
                        $('#main-list').append(html);
                    }
                    curPage += 1;
                },
                complete: function () {
                    scrollLock = false;
                }
            })
        }
    }

    function supports_html5_storage() {
        try {
            return 'localStorage' in window && window['localStorage'] !== null;
        } catch (e) {
            return false;
        }
    }
</script>
</body>
</html>
