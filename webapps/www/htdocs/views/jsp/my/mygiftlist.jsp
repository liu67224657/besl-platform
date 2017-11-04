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
    <title>我的礼包</title>

    <link href="${URL_LIB}/static/theme/default/css/wap_common.css?${version}" rel="stylesheet" type="text/css">
    <%--<script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>--%>
    <%--<script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>--%>
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
    <%--<div class="usable">--%>
    <%--<div class="usable-midou fl">--%>
    <%--<img src="${URL_LIB}/static/theme/default/images/my/midou.png" alt=""><span>可用迷豆</span><b class="midou_cut">${point}</b>--%>
    <%--<input type="hidden" value="${appkey}" name="appkey"/>--%>
    <%--<input type="hidden" value="${profileid}" name="profileId"/>--%>
    <%--<input type="hidden" value="1" name="type"/>--%>
    <%--</div>--%>
    <%--<a href="#" class="usable-mission">做任务，赚迷豆></a>--%>
    <%--</div>--%>
    <div class="lately">
        <p>最近一周领取记录<b>
            <c:choose>
                <c:when test="${not empty page.totalRows}">
                    ${page.totalRows}
                </c:when>
                <c:otherwise>
                    0
                </c:otherwise>

            </c:choose></b></p>
    </div>
    <div class="main jxlb">
        <ul id="giftul">
            <c:choose>
                <c:when test="${not empty dto}">
                    <c:forEach items="${dto}" var="dto">
                        <li>
                            <a href="http://api.${DOMAIN}/my/mygiftdetail?aid=${dto.gid}&appkey=${appkey}&profileid=${profileid}&type=1&moneyname=${wallname}">
                                <div class="main-list">
                                    <cite class="title-pic"><img src="${dto.gipic}" alt=""></cite>

                                    <div class="title-text">
                                        <h1>${dto.title}</h1>

                                        <p>有效期&nbsp;&nbsp;<span class="color">${dto.exDate}</span></p>
                                    </div>
                                    <span class="mian-btn reserveing">查号</span>
                                </div>

                            </a>
                        </li>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                </c:otherwise>
            </c:choose>
        </ul>
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
    <input type="hidden" value="1" name="type"/>
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
