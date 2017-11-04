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
    <meta name="description" content="${clientLine.line_desc}"/>
    <title>${clientLine.lineName}</title>

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
        <c:when test="${not empty lists}">
            <div id="_sharebtn_status" style="display: none;">yes</div>
            <div id="_title" style="display: none;">${clientLine.lineName}</div>
            <div id="_desc" style="display: none;">${clientLine.line_desc}</div>
            <div id="_clientpic" style="display: none;"><c:choose><c:when
                    test="${not empty clientLine.sharePic}">${clientLine.sharePic}</c:when><c:otherwise>${URL_LIB}/static/theme/wap/images/syhbcon.png</c:otherwise></c:choose></div>
            <div id="_share_task" style="display: none;">shareranking</div>
            <div id="_share_url" style="display: none;">${share_url}</div>
            <div class="phb-wrap">
                <div class="phb-main">
                    <div class="phb-head clearfix">
                            <%--<div class="phb-head-tit">${clientLine.lineName}</div>--%>
                            <%--<div class="php-head-amt">--%>
                            <%--<c:forEach items="${lists}" var="item" varStatus="status" begin="0" end="4">--%>
                            <%--<span class="pha-icon${status.index+1}"><img src="${item.gamePic}" alt=""--%>
                            <%--title=""/></span>--%>
                            <%--</c:forEach>--%>
                            <%--</div>--%>
                        <div class="phb-head-bg">
                            <img src="${clientLine.smallpic}" alt="" title=""/>

                            <h2 id="linedesc">${clientLine.line_desc}</h2>
                        </div>
                    </div>
                    <!--phb-bar-list-->
                    <div class="phb-con">
                        <!--phb-tm-->
                        <div class="phb-tm hb-list box-sizing">
                            <c:forEach items="${lists}" var="item" varStatus="status">
                                <a href="javascript:jumpcove('http://api.${DOMAIN}/joymeapp/gameclient/webview/game/cover?gameid=${item.gamedbId}')"
                                   class="display clearfix">
                                    <i><img src="${item.gamePic}" alt="" title=""/></i>

                                    <div>
                                        <em class="gamenamecut">${item.title}</em>

                                        <p>
                            	<span class="xingxing">
                                <cite style="width:${item.rate*10}%"></cite>
                                	<%--<cite></cite>--%>
                            	</span>
                                            <font>${item.rate}</font>
                                        </p>

                                        <p class="explain">${item.reason}</p>
                                    </div>
                                    <code
                                            <c:if test="${status.index+1==1}">class="No-1"</c:if>
                                            <c:if test="${status.index+1==2}">class="No-2"</c:if>
                                            <c:if test="${status.index+1==3}">class="No-3"</c:if>
                                            <c:if test="${status.index+1!=1&&status.index+1!=2&&status.index+1!=3}">class="No-4"</c:if>>${status.index+1}</code>
                                </a>

                            </c:forEach>

                        </div>
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
        cut_out($("#linedesc"), 40);
        $.each($('.explain'), function() {
            cut_out($(this), 12);
        });
        $.each($('.gamenamecut'), function() {
            cut_out($(this), 15);
        });


    })();
    function cut_out($target, length) {
        var text = $target.text();
        var len = text.length;
        if (len > length) {
            return $target.text(text.substr(0, length) + '...');
        } else {
            return $target.text();
        }
    }

    function jumpcove(url) {
        _jclient.jump("jt=23&ji=" + encodeURIComponent(url));
    }
</script>


<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/joymeappmy-init.js');
</script>
</body>
</html>
