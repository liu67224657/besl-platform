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
    <title>热门礼包</title>

    <link href="${URL_LIB}/static/theme/default/css/wap_common.css?${version}" rel="stylesheet" type="text/css">
    <%--<script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>--%>
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
    <div class="usable">
        <div class="usable-midou fl">
            <img src="${URL_LIB}/static/theme/default/images/my/midou.png" alt=""><span>可用${wallname}</span><b
                class="midou_cut">${point}</b>
            <input type="hidden" value="${appkey}" name="appkey"/>
            <input type="hidden" value="${profile.profileId}" name="profileId"/>
        </div>
        <a href="http://api.${DOMAIN}/my/totask?profileid=${profile.profileId}&appkey=${appkey}&clientid=${clientid}&platform=${platform}&moneyname=${wallname}"
           class="usable-mission">做任务，赚${wallname}></a>
        <a href="$http://api.${DOMAIN}/my/mygift?appkey=${appkey}&profileid=${profile.profileId}&type=1&moneyname=${wallname}"
           class="usable-my fr">我的礼包</a>
    </div>
    <div class="main jxlb">
        <ul id="giftul">
            <c:choose>
                <c:when test="${not empty giftDto.rows}">
                    <c:forEach items="${giftDto.rows}" var="dto">
                        <li>
                            <c:if test="${dto.reserveType==1}">
                                <cite class="hot"><img src="${URL_LIB}/static/theme/default/images/my/reserve.png"
                                                       alt=""></cite>
                            </c:if>
                            <a href="http://api.${DOMAIN}/my/giftdetail?appkey=${appkey}&profileid=${profile.profileId}&aid=${dto.gid}&type=1&clientid=${clientid}&platform=${platform}&moneyname=${wallname}">
                                <div class="main-list">
                                    <cite class="title-pic"><img src="${dto.gipic}" alt=""></cite>

                                    <div class="title-text">
                                        <h1 class="cut_out2">${dto.title}</h1>

                                        <p class="cut_out1">${dto.desc}</p>
                                        <c:if test="${dto.reserveType!=1}">
                                            <p>所需${wallname}：<span>${dto.point}</span></p>
                                        </c:if>
                                    </div>
                                </div>
                                <c:if test="${dto.reserveType==0}">
                                    <c:choose>
                                        <c:when test="${dto.sn==0}">
                                            <span class="mian-btn sack">抢光了</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="mian-btn exchange">兑换</span>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                                <c:if test="${dto.reserveType==1}">
                                    <c:if test="${empty reservelist}">
                                        <span class="mian-btn reserveing">预定</span>
                                    </c:if>
                                    <c:forEach items="${reservelist}" var="res">
                                        <c:if test="${res.aid==dto.gid}">
                                            <c:set value="true" var="bool" scope="page"/>
                                            <span class="reserve">已预定</span>
                                        </c:if>
                                    </c:forEach>
                                    <c:if test="${bool ne true}">
                                        <span class="mian-btn reserveing">预定</span>
                                    </c:if>
                                    <c:set value="false" var="bool" scope="page"/>
                                </c:if>
                            </a>
                        </li>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                </c:otherwise>
            </c:choose>
        </ul>
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
            var loading = document.getElementById('loading-btn'),
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

    <input type="hidden" value="${profile.profileId}" name="profileId"/>
    <input type="hidden" value="${appkey}" name="appkey"/>
    <input type="hidden" value="${point}" name="point"/>
    <input type="hidden" value="${platform}" name="platform"/>
    <input type="hidden" value="${clientid}" name="clientid"/>
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
