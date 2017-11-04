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
    <title>我的商品</title>
    <link href="${URL_LIB}/static/theme/default/css/wap_common.css?${version}" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.11.0.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function (e) {
            window.addEventListener('DOMContentLoaded', function () {
                document.addEventListener('touchstart', function () {
                    return false
                }, true)
            }, true);
            $("#wapCurPage").val("1");
            window.loadFlag = {};
            loadFlag.loadTime = null;
            loadFlag.isLoading = false;

            function toLoadMore() {
                var curNum = $("#wapCurPage").val();
                var maxNum = $("#wapMaxPage").val();
                if (parseInt(maxNum) <= parseInt(curNum)) {
                    return;
                }

                var sTop = $(window).scrollTop();
                var sHeight = $(document).height();
                var sMainHeight = $(window).height();
                var sNum = sHeight - sMainHeight;

                //console.log("sTop-->" + sTop + "sHeight->" + sHeight + "sMainHeight-->" + sMainHeight + "sNum-->" + (sHeight - sMainHeight));

                if (sTop + 50 >= sNum && !loadFlag.isLoading) {
                    loadFlag.isLoading = true;

                    $("#mygift-loading-btn").click();
                    loadFlag.loadTime = setTimeout(function () {
                        loadFlag.isLoading = false;
                    }, 1000);
                }
            }


            $(window).scroll(toLoadMore);

        });


    </script>
</head>
<body>
 <div class="black-dialog" id="popup" style="z-index: 11;"></div>
<div id="wrapper">
    <c:choose>
        <c:when test="${not empty dto}">
            <c:forEach items="${dto}" var="dto">
                <a href="http://api.${DOMAIN}/my/mygiftdetail?aid=${dto.gid}&appkey=${appkey}&profileid=${profileid}&type=2&consumeorder=${dto.consumeOrder}&moneyname=${wallname}"
                   style="overflow:hidden;">
                    <div class="order clearfix">
                        <h2><em class="fr">已兑换</em>

                            <p>订单号：<span>${dto.consumeOrder}</span></p></h2>
                        <dl class="clearfix">
                            <dt class="fl"><img src="${dto.gipic}" alt=""></dt>
                            <dd class="fl">
                                <h3>${dto.title}</h3>

                                <p>消耗<span>${dto.point}</span>${wallname}</p>
                            </dd>
                        </dl>
                    </div>
                </a>
            </c:forEach>
        </c:when>
        <c:otherwise>
            您还没有兑换过商品哦～
        </c:otherwise>
    </c:choose>
</div>
<c:if test="${page.maxPage>1}">
    <div class="loading" id="mygift-loading-btn"><a href="javascript:;"><i style="display:none"></i><b>点击加载更多</b></a>
    </div>
</c:if>
<input type="hidden" value="${page.curPage}" id="wapCurPage"/>
<input type="hidden" value="${page.maxPage}" id="wapMaxPage"/>
<script type="text/javascript">
    var _paq = _paq || [];
    _paq.push(['trackPageView']);
    _paq.push(['enableLinkTracking']);
    (function () {
        var loading = document.getElementById('mygift-loading-btn'),
                icon = loading.getElementsByTagName('i')[0],
                txt = loading.getElementsByTagName('b')[0];
        loading.onclick = function () {
            icon.style.display = 'inline-block';
            txt.innerHTML = '加载中';
        }
        var u = "//stat.joyme.com/";
        _paq.push(['setTrackerUrl', u + 'piwik.php']);
        _paq.push(['setSiteId', 106]);
        var d = document, g = d.createElement('script'), s = d.getElementsByTagName('script')[0];
        g.type = 'text/javascript';
        g.async = true;
        g.defer = true;
        g.src = u + 'piwik.js';
        s.parentNode.insertBefore(g, s);
    })();
</script>
<noscript><p><img src="//stat.joyme.com/piwik.php?idsite=106" style="border:0;" alt=""/></p></noscript>

<input type="hidden" value="${profileid}" name="profileId"/>
<input type="hidden" value="${appkey}" name="appkey"/>
<input type="hidden" value="${point}" name="point"/>
<input type="hidden" value="${uno}" name="uno"/>
<input type="hidden" value="${type}" name="type"/>
<input type="hidden" value="${wallname}" name="wallname"/>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/joymeappmy-init.js');
</script>
<script type="text/javascript">
    lz_main();
</script>

</body>
</html>
