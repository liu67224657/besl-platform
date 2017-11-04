<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>精品推荐</title>
    <link href="${URL_LIB}/static/theme/wap/css/wap_common.css" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/wap/css/wap_phb.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
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
    <!-- <header id="header">
         <a href="#" class="return"></a>
         <h1>精选礼包</h1>
         <a href="#" class="close">关闭</a>

     </header> -->
    <c:choose>
        <c:when test="${not empty list}">
            <div class="phb-wrap ">
                <div class="phb-main">
                    <!--phb-bar-list-->
                    <div class="phb-bar-list clearfix">
                        <!--phb-info-->
                        <c:forEach items="${list}" var="item">
                            <div
                            <c:if test="${item.hot==3}">class="phb-info phb-ab"</c:if> <c:if test="${item.hot!=3}">class="phb-info"</c:if>>
                              <%--itemtype为7是广告--%>
                            <c:choose>
                                <c:when test="${item.itemType.code==7}">
                                    <c:choose>
                                        <c:when test="${not empty item.sharePic}">
                                                <a href="${item.sharePic}">
                                                    <img src="${item.bigpic}"/>
                                                    <div>
                                                        <em class="linenamecut">${item.lineName}</em>
                                                        <span class="introcut">${item.line_intro}</span>
                                                    </div>
                                                    <c:if test="${item.hot==1}">
                                                        <span class="hot-tag">HOT</span>
                                                    </c:if>
                                                    <c:if test="${item.hot==2}">
                                                        <span class="new-tag">NEW</span>
                                                    </c:if>
                                                    <c:if test="${item.hot==3}">
                                                        <span class="hot-tag">推广</span>
                                                    </c:if>
                                                </a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="javascript:void(0);">
                                                <img src="${item.bigpic}"/>
                                                <div>
                                                    <em class="linenamecut">${item.lineName}</em>
                                                    <span class="introcut">${item.line_intro}</span>
                                                </div>
                                                <c:if test="${item.hot==1}">
                                                    <span class="hot-tag">HOT</span>
                                                </c:if>
                                                <c:if test="${item.hot==2}">
                                                    <span class="new-tag">NEW</span>
                                                </c:if>
                                                <c:if test="${item.hot==3}">
                                                    <span class="hot-tag">推广</span>
                                                </c:if>
                                            </a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <a href="http://api.${DOMAIN}/joymeapp/gameclient/webview/game/top/itemlist?id=${item.lineId}">
                                        <img src="${item.bigpic}"/>

                                        <div>
                                            <em class="linenamecut">${item.lineName}</em>
                                            <span class="introcut">${item.line_intro}</span>
                                        </div>
                                        <c:if test="${item.hot==1}">
                                            <span class="hot-tag">HOT</span>
                                        </c:if>
                                        <c:if test="${item.hot==2}">
                                            <span class="new-tag">NEW</span>
                                        </c:if>
                                        <c:if test="${item.hot==3}">
                                            <span class="hot-tag">推广</span>
                                        </c:if>
                                    </a>
                                </c:otherwise>
                            </c:choose>

                    </div>
                    </c:forEach>

                    <!--phb-info==end-->
                </div>
                <!--phb-bar-list==end-->
            </div>
            <!--phb-main-->
</div>
</c:when>
<c:otherwise>
    <div class="phb_none" style="display:block;">
        <cite><img src="${URL_LIB}/static/theme/wap/images/bx.png" alt=""></cite>
        <span>小编正在为排行榜补血中...</span>
    </div>
</c:otherwise>
</c:choose>

</div>
<script type="text/javascript">

    (function () {

    })();
    $.each($('.linenamecut'), function() {
        cut_out($(this), 16);
    });
    $.each($('.introcut'), function() {
        cut_out($(this), 22);
    });
    function cut_out($target, length) {
        var text = $target.text();
        var len = text.length;
        if (len > length) {
            return $target.text(text.substr(0, length) + '...');
        } else {
            return $target.text();
        }
    }
</script>


<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/joymeappmy-init.js');
</script>
<%--<script type="text/javascript">--%>
<%--lz_main();--%>
<%--</script>--%>

</body>
</html>
