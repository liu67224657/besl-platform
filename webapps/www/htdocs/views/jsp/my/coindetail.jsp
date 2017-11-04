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
    <title>商品详情</title>

    <link href="${URL_LIB}/static/theme/default/css/wap_common.css?${version}" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/wap/css/wap_lhcg.css?${version}" rel="stylesheet" type="text/css">

    <%--<script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>--%>
    <%--<script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>--%>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/page/geo.js"></script>
    <c:if test="${empty sdkversion}">
        <script src='${URL_LIB}/static/js/page/WebViewJavascriptBridge.js?${version}'></script>
    </c:if>


    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);

        var browser = {
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
        <c:choose>
        <c:when test="${empty sdkversion}">
        if (browser.versions.ios) {
            var _jclient;
        }
        function copy(value) {
            var stringval = 'type=text&content=' + value;
            if (browser.versions.ios) {
                _jclient.callHandler('copy', stringval, function (response) {
                    if (response) {
                        $("#reserveSuccess").text("复制成功");
                        $("#reserveSuccess").addClass("close");
                        var t = setInterval(function () {
                            $("#reserveSuccess").removeClass("close");
                            clearTimeout(t);
                        }, 3000);
                    }
                })
            }
            if (browser.versions.android) {
                _jclient.copy(stringval);
            }
        }
        </c:when>
        <c:otherwise>
        function copy(value) {
            var stringval = 'type=text&content=' + value;

            if (browser.versions.android) {
                _jclient.copy(stringval);
            }else{
                 _jclient.copy(stringval, function (response) {
                    if (response) {
                        $("#reserveSuccess").text("复制成功");
                        $("#reserveSuccess").addClass("close");
                        var t = setInterval(function () {
                            $("#reserveSuccess").removeClass("close");
                            clearTimeout(t);
                        }, 3000);
                    }
                })
            }
        }
        </c:otherwise>
        </c:choose>

    </script>
</head>
<body onload="setup();preselect('省份');">
<div id="wrapper" style="display:block;">
    <!-- <header id="header">
         <a href="#" class="return"></a>
         <h1>礼包详情</h1>
         <a href="#" class="close">关闭</a>
     </header> -->
    <div id="pic-loop">
        <div class="pic-loop-box swiper-wrapper">
            <div class="swiper-slide"><img src="${activityDetailDTO.bgPic}"></div>

        </div>
        <div class="pagination">
            <%--<span class="swiper-pagination-switch swiper-active-switch"></span>--%>
        </div>
    </div>
    <div class="sp-details">
        <div class="sp-details-top">
            <b class="fl">${activityDetailDTO.title}</b>

            <div class="sp-details-top-r fr">
                <p>剩余：<span>${activityDetailDTO.sn}/${activityDetailDTO.cn}</span></p>

                <p>${activityDetailDTO.rn}人已兑换</p>
            </div>
        </div>
        <div class="sp-details-bottom">
            <%--<p>参考价：<span>￥40</span></p>--%>
            <p>需${wallname}：<span>${activityDetailDTO.point}</span></p>

            <p>数量：<span>1</span></p>

            <p>规格：<span>默认</span></p>
        </div>
    </div>
    <div class="sp-details-main">
        <c:if test="${not empty activityDetailDTO.textJsonItemsList}">
            <c:forEach items="${activityDetailDTO.textJsonItemsList}" var="dto">
                <c:choose>
                    <c:when test="${dto.type=='1'}">
                        <p>${dto.item}</p>
                    </c:when>
                    <c:otherwise>
                        <p><img src="${dto.item}" style="width:100%"/></p>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </c:if>
        </p>
    </div>
    <div class="shop djdzh">
        <h4>大家都在换</h4>
        <c:choose>
            <c:when test="${not empty giftDto.rows}">
                <c:forEach items="${giftDto.rows}" var="dto">
                    <a href="http://api.${DOMAIN}/my/giftdetail?appkey=${appkey}&profileid=${profileid}&aid=${dto.gid}&type=2">
                        <div class="shop-box">
                            <cite><img src="${dto.gipic}"></cite>

                            <div class="shop-box-text"><h2 class="cut_out2">${dto.title}</h2>

                                <h3><span>${dto.point}</span>${wallname}</h3></div>
                        </div>
                    </a>
                </c:forEach>
            </c:when>
            <c:otherwise>
            </c:otherwise>
        </c:choose>

    </div>
    <div class="details-btn">
        <c:choose>
            <c:when test="${activityDetailDTO.sn!=0}">
                <a href="javascript:void(0);" class="details-butten a1" id="exchange-btn">马上兑换</a>
            </c:when>
            <c:otherwise>
                <a href="javascript:void(0);" class="details-butten a2">已售罄</a>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="mark-box"></div>
    <div class="dialog " id="cofirmshopdiv">
        <h1>是否兑换：</h1>

        <p class="confirm-text2"><span class="all">${activityDetailDTO.title}</span>，共需<span
                class="all">${activityDetailDTO.point}</span>${wallname}</p>

        <div class="dialog-btn"><a href="javascript:cancelReserve();" class="cancel">取消</a><a href="javascript:void(0);"
                                                                                              id="getcode"
                                                                                              class="gain">确定</a></div>
    </div>
    <div class="dialog " id="gopointwall">
        <h1>可用${wallname}不足：</h1>

        <p class="confirm-text2">兑换此商品，共需<span
                class="all">${activityDetailDTO.point}</span>${wallname}</p>

        <div class="dialog-btn"><a href="javascript:cancelReserve();" class="cancel">取消</a><a
                href="${URL_WWW}/my/hotapp?appkey=${appkey}&profileid=${profileid}&clientid=${clientid}&platform=${platform}"
                class="gain">马上赚${wallname}</a>
        </div>
    </div>
</div>
<div class="new-address" style="display:none;">
    <ul>
        <li><input type="text" placeholder="收货人姓名" name="name"></li>
        <li><input type="text" placeholder="手机号码" name="phone"></li>
        <li><input type="text" placeholder="邮政编码" name="zipcode"></li>
        <li><select class="select" name="province" style="height:25px;" id="s1">
            <option></option>
        </select>
            <select class="select" style="height:25px;" name="city" id="s2">
                <option></option>
            </select>
            <select class="select" style="height:25px;" name="town" id="s3">
                <option></option>
            </select></li>
        <li><input type="text" placeholder="详细地址" class="textarea" name="address"></li>
    </ul>
    <div class="dialog-btn" style="text-align:center ;width: 100%;box-sizing: border-box;"><a
            href="javascript:cancelAddress();" class="cancel">取消</a><a href="javascript:void(0);"
                                                                       class="gain"
                                                                       id="submitAddress">提交</a>
    </div>
</div>
<div id="exchange" style="text-align:center;display:none;">
    <div class="lh-succeed">
        <span><img src="${URL_LIB}/static/theme/wap/images/succee.png" alt=""></span>

        <p>领号成功</p>
    </div>

    <div class="code">
        <p id="kahao" style="display:none;">卡号：</p>

        <div class="codebox">
            <div><span class="fl cut_out5"></span><a href="javascript:void(0);" class="copyBtn fr">复制</a></div>
        </div>
    </div>
    <div class="code" id="password" style="display:none;">
        卡密：
        <div class="codebox">
            <div><span class="fl cut_out5"></span><a href="javascript:void(0);" class="copyBtn fr">复制</a></div>
        </div>
    </div>
    <div class="done dn">
        <p>已领取的礼包请在<span class="color">1小时</span>内尽快使用，否则将<br/>会进入淘号库被其他用户使用！</p>
    </div>
</div>
<div class="black-dialog " id="reserveSuccess"></div>


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

    function changeBox() {
        $("#exchange").css("display", "none");
        $("#wrapper").css("display", "block");
    }


</script>
<noscript><p><img src="//stat.joyme.com/piwik.php?idsite=106" style="border:0;" alt=""/></p></noscript>

<input type="hidden" value="${profileid}" name="profileId"/>
<input type="hidden" value="${appkey}" name="appkey"/>
<input type="hidden" value="${activityDetailDTO.aid}" name="aid"/>
<input type="hidden" value="${activityDetailDTO.gid}" name="gid"/>
<input type="hidden" value="${type}" name="type"/>
<input type="hidden" value="${template}" name="template"/>

<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/joymeappmy-init.js');
</script>


</body>
</html>
