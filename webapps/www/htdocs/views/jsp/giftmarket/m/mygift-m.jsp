<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta name="applicable-device" content="mobile">
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="Keywords" content="手机游戏礼包领取,手游兑换码,手游激活码">
    <meta name="description"
          content="着迷网礼包中心为手游玩家提供各种手游礼包,其中包括手机游戏激活码,特权码,测试礼包,新手礼包,兑换码，节假日礼包等,还有多种着迷专属礼包等你拿,多种手游礼包尽在着迷网"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>手机游戏礼包领取,手游兑换码,手游激活码_着迷网</title>

    <link rel="stylesheet" type="text/css" href="http://static.joyme.com/mobile/cms/jmsy/css/common-beta1.css">
    <link rel="stylesheet" type="text/css"
          href="${URL_STATIC}/mobile/cms/jmsy/css/newlibao.css?v=${version}">
    <%--<link rel="stylesheet" type="text/css" href="${URL_STATIC}/mobile/cms/jmsy/logincont/oldLoginbar.css?${version}"/>--%>

    <script type="text/javascript" src="${URL_STATIC}/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
        function buildexclusive() {
            $("a[name='exclusiveDisplayNone']").each(function () {
                $(this).attr("style", "");
            });
            $("#clickExclusive").remove();
        }
    </script>
</head>
<body>

<div class="container">
    <c:set var="mygiftdisplay" value="none"/>
    <%@ include file="/views/jsp/passport/m/gift-header-m.jsp" %>

    <div id="wrapper">
        <div id="main" class="no-pag">
            <div id="center">
                <!-- 我的礼包列表 开始 -->
                <div class="gift-list list-item">
                    <div class="gift-list-tit">我的礼包</div>
                    <c:choose>
                        <c:when test="${empty userSession}">
                            <p class="expire-gift">
                                您还没有登录，点击<a href="javascript:loginDiv();">登录</a>查看礼包
                            </p>
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${empty list}">
                                    <p class="expire-gift">
                                        当前没有礼包，快去领取礼包吧
                                    </p>
                                </c:when>
                                <c:otherwise>
                                    <div class="gift-list-box" id="div-li-my">
                                        <c:forEach items="${list}" var="item">
                                            <a href="${URL_M}/gift/${item.aid}?logid=${item.lid}" class="border-b">
                                                <cite>
                                                    <img src="${item.gipic}">
                                                </cite>
                                                <span>
                                                    <font>${item.title}</font>
                                                    <em>有效期：${dateutil:parseCustomDate(item.endTime, "yyyy-MM-dd HH:mm")}</em>
                                                    <b>剩余: ${item.sn}</b>
                                                </span>
                                            </a>
                                        </c:forEach>
                                        <input type="hidden" value="${page.curPage}" id="curpage"/>
                                        <input type="hidden" value="${page.maxPage}" id="maxpage"/>

                                    </div>
                                </c:otherwise>
                            </c:choose>

                        </c:otherwise>
                    </c:choose>
                </div>
                <!-- 我的礼包列表 开始 -->
            </div>
        </div>
    </div>
    <!-- 导航 开始 -->
    <%@ include file="/views/jsp/passport/m/nav.jsp" %>
    <!-- 导航 结束 -->
</div>
<script type="text/javascript" src="http://static.joyme.com/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${URL_STATIC}/mobile/cms/jmsy/js/common.js"></script>
<!-- <script src="js/common.js"></script> -->
<script type="text/javascript">
    var loadToole = {
        'isLoading': false,
        'loadTime': null,
        'p': 1,
        'loadComment': function (config) {
            var uno = getCookie('jmuc_uno');
            var uid = getCookie('jmuc_u');
            var token = getCookie('jmuc_token');
            var t = getCookie('jmuc_t');
            var lgdomain = getCookie('jmuc_lgdomain');
            var s = getCookie('jmuc_s');
            var pid = getCookie('jmuc_pid');
            if (uno == null || uid == null || token == null || t == null || lgdomain == null || s == null || pid == null || lgdomain == 'client') {
                e.stopPropagation();
                e.preventDefault();
                $('.login-mask').click();
                return;
            }
            var curNum = loadToole.p += 1;
            $.ajax({
                url: "http://api." + joyconfig.DOMAIN + "/json/gift/mygift",
                data: {p: curNum},
                timeout: 5000,
                dataType: "jsonp",
                type: "POST",
                jsonpCallback: "mygiftlist",
                success: function (req) {
                    var res = req[0];
                    if (res.rs == '0') {
                        alert('系统错误!');
                        loadToole.isLoading = false;
                        return;
                    } else if (res.rs == '-1') {
                        e.stopPropagation();
                        e.preventDefault();
                        $('.login-mask').click();
                        $('.wrapper').hide();
                        loadToole.isLoading = false;
                        return;
                    } else if (res.rs == '1') {
                        var result = res.result;
                        var cur = result.page.curPage;
                        if (cur == 1) {
                            $("#div-li-my").empty();
                            $('#div-li-my').html(' <p class="expire-gift" >您还没有领取过礼包o(∩_∩)o </p>');
                        }
                        if (result != null && result != undefined) {
                            var rows = result.rows;
                            var html = '';
                            if (rows != null && rows.length > 0) {
                                for (var i = 0; i < rows.length; i++) {
                                    var dto = rows[i];
                                    if (dto != null && dto != undefined) {
                                        html += '<a href="${URL_M}/gift/' + dto.aid + '?logid=' + dto.lid + '&reurl=${URL_M}/gift" class="border-b">' +
                                                '<cite><img src="' + dto.gipic + '" alt="' + dto.title + '" title="' + dto.title + '"></cite>' +
                                                '<span><font>' + dto.title + '</font><em>有效期：' + new Date(dto.endTime.time).format("yyyy-MM-dd hh:mm") + '</em><b>剩余：' + dto.sn + '</b></span></a>';
                                    }
                                }
                                $('#div-li-my').append(html);
                            }
                            loadToole.p = result.page.curPage;
                            $("#maxpage").val(result.page.maxPage);
                            $('.loading').remove();

                        }

                        loadToole.isLoading = false;
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert('获取失败，请重试');
                    loadToole.isLoading = false;
                    return;
                }
            });
//                $('.loading').remove();
//                $('.' + config).append(txt);
            // loadToole.isLoading = false;
            /*新增-没有更多加载*/
            // $('.moreNo').show().delay(3000);
            /*新增end*/
        },
        //滚动加载
        'scroll_load': function (parentBox, className) {
            var className = className, parentBox = parentBox;
            $("#" + parentBox).scroll(function (ev) {
                ev.stopPropagation();
                ev.preventDefault();
                var sTop = $("#" + parentBox)[0].scrollTop + 5;
                var sHeight = $("#" + parentBox)[0].scrollHeight;
                var sMainHeight = $("#" + parentBox).height();
                var sNum = sHeight - sMainHeight;
                console.log(sHeight)
                var loadTips = '<div class="loading"><span>正在加载...</span></div>'

                if (sTop >= sNum && !loadToole.isLoading) {
                    var curNum = loadToole.p;
                    var maxNum = $("#maxpage").val();
                    if (parseInt(maxNum) <= parseInt(curNum)) {
                        return;
                    }
                    loadToole.isLoading = true;
                    $('.' + className).append(loadTips);
//                    loadToole.loadTime = setTimeout(function () {
                    loadToole.loadComment(className);
//                    }, 3000);
                }
                ;
            });
        }
    };
    window.onload = function () {
        loadToole.scroll_load('wrapper', 'list-item');
    }
    Date.prototype.format = function (format) {
        var o = {
            "M+": this.getMonth() + 1, //month
            "d+": this.getDate(),    //day
            "h+": this.getHours(),   //hour
            "m+": this.getMinutes(), //minute
            "s+": this.getSeconds(), //second
            "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter
            "S": this.getMilliseconds() //millisecond
        }
        if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
                (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)if (new RegExp("(" + k + ")").test(format))
            format = format.replace(RegExp.$1,
                    RegExp.$1.length == 1 ? o[k] :
                            ("00" + o[k]).substr(("" + o[k]).length));
        return format;
    }
</script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/giftmarket-tj.js"></script>

</body>
</html>
