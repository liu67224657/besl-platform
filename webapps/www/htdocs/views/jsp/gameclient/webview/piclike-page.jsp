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
    <meta http-equiv="Cache-Control" content="no-Cache"/>
    <meta http-equiv="Cache-Control" content="max-age=0"/>
    <title>着迷，才有乐趣</title>
    <link href="${URL_LIB}/static/theme/wap/css/wap_common.css" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/wap/css/wap_miFriend.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/applib.js"></script>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>

    <script type="text/javascript">
        $(function () {
            var storage = null;
            if (!supports_html5_storage()) {
            } else {
                storage = window.localStorage;
                var openDate = storage.getItem("opendate");
                var now = new Date().toLocaleDateString();
                if (openDate != undefined || openDate != null) {
                    if (now != openDate) {
                        for (var i = storage.length - 1; i >= 0; i--) {
                            if (storage.key(i).indexOf('likedpic_') == 0) {
                                storage.removeItem(storage.key(i));
                            } else if (storage.key(i).indexOf('likepic_') == 0) {
                                storage.removeItem(storage.key(i));
                            }
                        }
                        storage.removeItem("opendate");
                        storage.setItem("opendate", now);
                    }
                } else {
                    storage.setItem("opendate", now);
                }
            }

            var touchAllow = true;
            $(document).ready(function () {
                $('#user-center-btn').on("touchstart", function () {
                    if (!touchAllow) {
                        return;
                    }
                    touchAllow = false;
                    var desUid = $('li[name=current]').attr('data-u');
                    var ajaxTimeoutTest = $.ajax({
                        url: "http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/api/profile/getbyuid",
                        data: {uid: desUid},
                        timeout: 5000,
                        dataType: "json",
                        type: "POST",
                        success: function (data) {
                            touchAllow = true;
                            jump(22, desUid);
                            return;
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            $('div.mi_friend_tip span').html('么么哒失败，请重试');
                            $('div.mi_friend_tip').show(100).delay(1800).hide(100);
                            touchAllow = true;
                            return;
                        }
                    });

                });

                $('img[name=usericon]').on("touchstart", function () {
                    if (!touchAllow) {
                        return;
                    }
                    touchAllow = false;
                    var desUid = $('li[name=current]').attr('data-u');
                    var ajaxTimeoutTest = $.ajax({
                        url: "http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/api/profile/getbyuid",
                        data: {uid: desUid},
                        timeout: 5000,
                        dataType: "json",
                        type: "POST",
                        success: function (data) {
                            touchAllow = true;
                            jump(22, desUid);
                            return;
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            $('div.mi_friend_tip span').html('么么哒失败，请重试');
                            $('div.mi_friend_tip').show(100).delay(1800).hide(100);
                            touchAllow = true;
                            return;
                        }
                    });

                });
            });
        });

        function supports_html5_storage() {
            try {
                return 'localStorage' in window && window['localStorage'] !== null;
            } catch (e) {
                return false;
            }
        }
    </script>
</head>
<body>
<div id="wrapper" class="wrapper">
    <div class="mi-friend" id="wrap" style="height: 320px;">
        <div class="mi_friend_tip p hide"><span>今日血条已空，等你明日满血复活！</span></div>
        <ul class="mi-friend-pic">
            <c:choose>
                <c:when test="${list != null && list.size() >0}">
                    <c:forEach items="${list}" var="dto" varStatus="st">
                        <c:if test="${dto != null && dto.picdto != null && dto.profiledto != null && dto.gamedto != null}">
                            <li data-idx="${st.index}" data-u="${dto.profiledto.uid}" data-pic="${dto.picdto.picid}"
                                style="-webkit-transform: translate3d(0px, 0px, 0px); -webkit-transition: -webkit-transform 0.2s
                                        ease-out; transition: -webkit-transform 0.2s ease-out;">
                                <a href="javascript:void(0);">
                                    <img data-image="${dto.picdto.picurl}" src="" alt="" style="width: 100%;"
                                         name="image"/>

                                    <p name="profile-info" style="display: none;">
                                        <cite class="fl">
                                            <img name="usericon" src="" data-usericon="${dto.profiledto.iconurl}"
                                                 alt="${dto.profiledto.iconurl}"
                                                 style="width: 100%;"/>
                                        </cite>
                                                <span class="fl"><b>${dto.profiledto.nick}</b><br>
                                                    <em><c:choose>
                                                        <c:when test="${fn:length(dto.gamedto.name) > 0}">正在玩&nbsp;${dto.gamedto.name}</c:when>
                                                        <c:otherwise>游戏空窗期</c:otherwise>
                                                    </c:choose></em>
                                                </span>
                                    </p>
                                </a>
                            </li>
                        </c:if>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <li class="no-photo">
                        <a href="#">
                            <img src="${URL_LIB}/static/theme/wap/images/no-pic.jpg" alt="">

                            <p><em>迷友靓照正在审核中，请稍候...</em></p>
                        </a>
                    </li>
                </c:otherwise>
            </c:choose>

        </ul>
        <div class="mi-friendi mi-friend-no">
            <img src="${URL_LIB}/static/theme/wap/images/no-like.png" alt="" style="width: 100%;">
        </div>
        <div class="mi-friendi mi-friend-yes">
            <img src="${URL_LIB}/static/theme/wap/images/like.png" alt="" style="width: 100%;">
        </div>
    </div>
    <div class="mi-friend-btn">
        <div class="mi-friend-btn-bg clearfix">
            <a href="javascript:void(0);" class="mi-friend-btn-like mi-friend-btn-yes fr" id="like-btn"></a>
            <a href="javascript:void(0);" class="mi-friend-btn-like mi-friend-btn-no fl" id="no-like-btn"></a>
            <a href="javascript:void(0);" class="mi-friend-btn-i" id="user-center-btn"></a>
        </div>
    </div>
    <input type="hidden" value="${uid}" id="input_hidden_uid"/>
    <input type="hidden" value="${appkey}" id="input_hidden_appkey"/>
    <input type="hidden" value="${logindomain}" id="input_hidden_logindomain"/>
    <input type="hidden" value="${platform}" id="input_hidden_platform"/>
</div>
<script type="text/javascript" src="${URL_LIB}/static/js/wap/slider.js"></script>
<script type="text/javascript">
    new Slider({
        wrapper: document.getElementById('wrap'),
        btn: $('.mi-friend-btn-like')
        //change:document.getElementById('sp')
    });
    $('.mi-friend').height($(window).innerWidth());
    $('.mi-friend').width($(window).innerWidth());
    $('.mi-friend-pic li a>img').height($(window).innerWidth());
    $('.mi-friend-pic li a>img').width($(window).innerWidth());


     var _paq = _paq || [];
    _paq.push(['trackPageView']);
    _paq.push(['enableLinkTracking']);
    (function () {
        var u = "//stat.joyme.com/";
        _paq.push(['setTrackerUrl', u + 'piwik.php']);
        _paq.push(['setSiteId', 222]);
        var d = document, g = d.createElement('script'), s = d.getElementsByTagName('script')[0];
        g.type = 'text/javascript';
        g.async = true;
        g.defer = true;
        g.src = u + 'piwik.js';
        s.parentNode.insertBefore(g, s);
    })();
</script>
</body>
</html>