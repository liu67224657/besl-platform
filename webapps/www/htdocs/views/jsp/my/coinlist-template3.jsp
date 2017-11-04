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
    <meta http-equiv="Expires" CONTENT="0">
    <meta http-equiv="Cache-Control" CONTENT="no-cache">
    <meta http-equiv="Pragma" CONTENT="no-cache">
    <title>实物商城</title>
    <link href="${URL_LIB}/static/theme/default/css/wap_common_template3.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.11.0.min.js"></script>
    <script type="text/javascript">


        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);

        function toOpenUrl(obj) {
            gid = new Date().getTime();
            var stateObj = {
                title: "title",
                url: "/my/giftlist?profileid=${profile.profileId}&appkey=${appkey}&type=${type}&clientid=${clientid}&platform=${platform}&gid="+gid,
                msg: "msg"
            };
            history.replaceState(stateObj, "test", "/my/giftlist?profileid=${profile.profileId}&appkey=${appkey}&type=${type}&clientid=${clientid}&platform=${platform}&gid="+gid);
            window.location.href = $(obj).attr("data-href");

        }

    </script>
</head>
<body>
<div class="black-dialog" id="popup" style="z-index: 11;"></div>

<div id="wrapper">
    <div class="Personal_title">
        <p class="fl"><em>我有<span>${point}</span>${wallname}</em> <c:if
                test="${appkey=='119mpBeIV49bCDFJj5uSZ4'}"><em>客服QQ3135735548</em> </c:if></p>
        <a href="/my/mygift?appkey=${appkey}&profileid=${profile.profileId}&type=2&moneyname=${wallname}&template=${template}"
           class="fr">兑换记录</a>
    </div>
    <div class="shop" id="giftul">
        <c:if test="${not empty giftDto.rows}">
            <c:forEach items="${giftDto.rows}" var="dto">
                <a href="javascript:void(0);" onclick="toOpenUrl(this);"  data-href="http://api.${DOMAIN}/my/giftdetail?appkey=${appkey}&profileid=${profile.profileId}&aid=${dto.gid}&type=2&clientid=${clientid}&platform=${platform}&moneyname=${wallname}">
                    <div class="shop-box">
                        <cite><img src="${dto.gipic}" alt=""></cite>

                        <div class="shop-box-text"><h2 class="cut_out2">${dto.title}</h2>

                            <h3><span>${dto.point}</span>${wallname}</h3></div>
                        <c:choose>
                            <c:when test="${dto.sn==0}">
                                <div class="shop-btn s2">已售罄</div>
                            </c:when>
                            <c:otherwise>
                                <div class="shop-btn s1">奖品有限，速来参加</div>
                            </c:otherwise>
                        </c:choose>

                    </div>
                </a>
            </c:forEach>
        </c:if>

    </div>
    <c:if test="${giftDto.page.maxPage>1}">
    <div class="loading" id="loading-btn"><a href="javascript:;"><i style="display:none"></i><b>点击加载更多</b></a></div>
    </c:if>
    <input type="hidden" value="${giftDto.page.curPage}" id="wapCurPage"/>
    <input type="hidden" value="${giftDto.page.maxPage}" id="wapMaxPage"/>


    <script type="text/javascript">
        var _paq = _paq || [];
        _paq.push(['trackPageView']);
        _paq.push(['enableLinkTracking']);
        (function () {
            var loading = document.getElementById('loading-btn');
            if (loading) {
                var icon = loading.getElementsByTagName('i')[0];
                var txt = loading.getElementsByTagName('b')[0];
                loading.onclick = function () {
                    icon.style.display = 'inline-block';
                    txt.innerHTML = '加载中';
                }

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

    <input type="hidden" value="${profile.profileId}" name="profileId"/>
    <input type="hidden" value="${appkey}" name="appkey"/>
    <input type="hidden" value="${point}" name="point"/>
    <input type="hidden" value="${platform}" name="platform"/>
    <input type="hidden" value="${clientid}" name="clientid"/>
    <input type="hidden" value="${type}" name="type"/>
    <input type="hidden" value="${wallname}" name="wallname"/>
    <input type="hidden" value="${template}" name="template"/>
    <script src="${URL_LIB}/static/js/common/seajs.js"></script>
    <script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
    <script>
        seajs.use('${URL_LIB}/static/js/init/joymeappmy-init.js');
    </script>


</body>
</html>
