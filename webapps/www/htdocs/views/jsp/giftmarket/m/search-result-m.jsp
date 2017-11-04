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
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <script>
        function checkSearch() {
            var searchText = $("#search_text").val().trim();
            if (searchText == "") {
                alert("请输入礼包名称");
                return false;
            }
        }
    </script>
</head>
<body>
<div class="container">
    <%@ include file="/views/jsp/passport/m/gift-header-m.jsp" %>

    <div id="wrapper">
        <div id="main" class="no-pag">
            <div id="center">
                <!-- 搜索 开始 -->
                <div class="search-box fn-clear border-b">
                    <form action="${URL_M}/gift/searchpage" method="post" onsubmit="return checkSearch();">
                        <input type="text" class="search-text" name="searchtext" value="" id="search_text"
                               placeholder="请输入礼包名称">
                        <input type="hidden" id="searchText" value="${searchtext}"/>
                        <input type="submit" class="search-resu" value="搜索"/>
                    </form>
                </div>
                <!-- 搜索 结束 -->
                <div class="bg-f3"></div>
                <!-- 搜索列表 开始 -->
                <div class="gift-list list-item">
                    <c:choose>
                        <c:when test="${not empty list}">
                            <h1 class="search-key">关键词<font>“${searchtext}”</font>的礼包列表</h1>
                            <div class="gift-list-box" id="div-li-my">
                                <c:forEach items="${list}" var="item" varStatus="index">
                                    <a href="${URL_M}/gift/${item.gid}" class="border-b">
                                        <cite>
                                            <img src="${item.gipic}">
                                        </cite>
                                        <span>
                                            <font>${item.title}</font>
                                            <em>有效期：${dateutil:dateToString(item.exDate, "yyyy-MM-dd HH:mm")}</em>
                                            <b>剩余: ${item.sn}</b>
                                        </span>
                                    </a>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <h1 class="search-key">没有找到关键词<font>“${searchtext}”</font>的礼包列表,为您推荐</h1>
                            <div class="gift-list-box">
                                <c:forEach items="${exclusiveList}" var="item" varStatus="index">
                                    <a href="${URL_M}/gift/${item.activityGoodsId}" class="border-b">
                                        <cite>
                                            <img src="${item.activityPicUrl}">
                                        </cite>
                                        <span>
                                        <font>${item.activitySubject}</font>
                                        <em>有效期：${dateutil:parseCustomDate(item.endTime, "yyyy-MM-dd HH:mm")}</em>
                                        <b>剩余: ${item.goodsResetAmount}</b>
                                    </span>
                                    </a>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${empty page}">
                            <input type="hidden" value="1" id="curpage"/>
                            <input type="hidden" value="1" id="maxpage"/>
                        </c:when>
                        <c:otherwise>
                            <input type="hidden" value="${page.curPage}" id="curpage"/>
                            <input type="hidden" value="${page.maxPage}" id="maxpage"/>
                        </c:otherwise>
                    </c:choose>

                </div>
                <!-- 搜索列表 开始 -->
            </div>
        </div>
    </div>
    <%@ include file="/views/jsp/passport/m/nav.jsp" %>
</div>
<script type="text/javascript" src="${URL_STATIC}/mobile/cms/jmsy/js/common.js"></script>
<script type="text/javascript">

    var loadToole = {
        'isLoading': false,
        'loadTime': null,
        'loadComment': function (config) {

            var curNum = $("#curpage").val();
            curNum = parseInt(curNum) + 1;
            var searchtext = $("#searchText").val();
            $.ajax({
                url: "http://api." + joyconfig.DOMAIN + "/json/gift/searchpage",
                data: {p: curNum, psize: 10, searchtext: searchtext},
                timeout: 5000,
                dataType: "jsonp",
                type: "POST",
                jsonpCallback: "searchlist",
                success: function (req) {
                    var res = req[0];
                    if (res.rs == '0') {
                        alert('系统错误!');
                        loadToole.isLoading = false;
                        return;
                    } else if (res.rs == '1') {
                        var result = res.result;

                        if (result != null && result != undefined) {
                            var rows = result.list;
                            var html = '';
                            if (rows != null && rows.length > 0) {
                                for (var i = 0; i < rows.length; i++) {
                                    var dto = rows[i];
                                    if (dto != null && dto != undefined) {
                                        html += '<a href="${URL_M}/gift/' + dto.gid + '"class="border-b">' +
                                                '<cite><img src="' + dto.gipic + '" alt="' + dto.title + '" title="' + dto.title + '"></cite>' +
                                                '<span><font>' + dto.title + '</font><em>有效期：' + dto.exDate.time + '</em><b>剩余：' + dto.sn + '</b></span></a>';
                                    }
                                }
                                $('#div-li-my').append(html);
                            }
                            $("#curpage").val(result.page.curPage);
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
                    var curNum = $("#curpage").val();
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
        $("#curpage").val("1");
        loadToole.scroll_load('wrapper', 'list-item');
    }
</script>
</body>
</html>
