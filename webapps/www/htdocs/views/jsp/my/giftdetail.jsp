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
    <title>礼包详情</title>

    <link href="${URL_LIB}/static/theme/default/css/wap_common.css?${version}" rel="stylesheet" type="text/css">
    <%--<link href="${URL_LIB}/static/theme/default/css/wap_index.css?${version}" rel="stylesheet" type="text/css">--%>
    <%--<script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>--%>
    <%--<script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>--%>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <c:if test="${empty sdkversion}">
        <script src='${URL_LIB}/static/js/page/WebViewJavascriptBridge.js?${version}'></script>
    </c:if>
    <%--<script src='${URL_LIB}/static/js/page/WebViewJavascriptBridge.js?${version}'></script>--%>
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
            } else {
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
<body>
<div id="wrapper">
    <!-- <header id="header">
   <a href="#" class="return"></a>
   <h1>礼包详情</h1>
   <a href="#" class="close">关闭</a>
   </header> -->
    <div class="details">
        <c:if test="${activityDetailDTO.reserveType==1}">
            <cite class="reserveing-icon"><img src="${URL_LIB}/static/theme/default/images/my/reserveing.png"
                                               alt=""></cite>
        </c:if>


        <div class="details-mark"></div>
        <p><img src="${activityDetailDTO.bgPic}" alt=""></p>

        <div class="details-box">
            <div class="details-tit fl"><img src="${activityDetailDTO.gipic}" alt=""></div>
            <div class="details-text fl"><h1>${activityDetailDTO.title}</h1>
                <c:if test="${activityDetailDTO.reserveType!=1}">
                    <h2>需${activityDetailDTO.point}${wallname}</h2>
                </c:if>
                <c:choose>
                    <c:when test="${empty reserveNum}">
                        <h3>剩余<span>${activityDetailDTO.sn}/${activityDetailDTO.cn}</span></h3>
                    </c:when>
                    <c:otherwise>
                        <h3>${reserveNum}<span>人已预订</span></h3>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
    <div class="details-message">
        <p class="fl"><span style="color:#f00;">*</span>&nbsp;兑换后请尽快使用，以免过期失效</p>

        <p class="fr">有效期:<span>${activityDetailDTO.endTime}</span></p>
    </div>
    <div class="details-main">
        <div class="exclusive">
            <h1><span>独家礼包内容：</span></h1>
            <c:if test="${not empty activityDetailDTO.textJsonItemsList}">
                <c:forEach items="${activityDetailDTO.textJsonItemsList}" var="dto">
                    <c:choose>
                        <c:when test="${dto.type=='1'}">
                            <p>${dto.item}</p>
                        </c:when>
                        <c:otherwise>
                            <p><img src="${dto.item}"/></p>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </c:if>
        </div>
    </div>

    <div class="details-btn">
        <c:choose>
            <c:when test="${activityDetailDTO.reserveType==1}">
                <c:choose>
                    <c:when test="${empty giftReserve}">
                        <a href="javascript:void(0);" class="details-butten a1" id="reserve-btn">马上预定</a>
                    </c:when>
                    <c:otherwise>
                        <a href="javascript:void(0);" class="details-butten a1" id="s-reserve-btn">已预定</a>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${activityDetailDTO.sn!=0}">
                        <a href="javascript:void(0);" class="details-butten a1" id="exchange-btn">马上兑换</a>
                    </c:when>
                    <c:otherwise>
                        <a href="javascript:void(0);" class="details-butten a2">抢光了</a>
                    </c:otherwise>
                </c:choose>

            </c:otherwise>
        </c:choose>


    </div>
    <div class="mark-box"></div>


    <div class="dialog " id="reservediv">
        <h1>您要预订的礼包为：</h1>

        <p class="confirm-text2"><span class="all">${activityDetailDTO.title}</span></p>

        <div class="dialog-btn"><a href="javascript:cancelReserve();" class="cancel">取消</a><a href="javascript:void(0);"
                                                                                              id="reserveGift"
                                                                                              class="gain">确定</a></div>
    </div>

    <div class="dialog " id="cofirmshopdiv">
        <h1>是否兑换：</h1>

        <p class="confirm-text2"><span class="all">${activityDetailDTO.title}</span>，共需<span
                class="all">${activityDetailDTO.point}</span>${wallname}</p>

        <div class="dialog-btn"><a href="javascript:cancelReserve();" class="cancel">取消</a><a href="javascript:void(0);"
                                                                                              id="getcode"
                                                                                              class="gain">确定</a></div>
    </div>
    <div class="dialog " id="gopointwall">
        <h1>可用${wallname}${wallname}不足：</h1>

        <p class="confirm-text2">兑换此礼包，共需<span
                class="all">${activityDetailDTO.point}</span>${wallname}</p>

        <div class="dialog-btn"><a href="javascript:cancelReserve();" class="cancel">取消</a><a
                href="http://api.${DOMAIN}/my/hotapp?appkey=${appkey}&profileid=${profileid}&clientid=${clientid}&platform=${platform}"
                class="gain">马上赚${wallname}</a>
        </div>
    </div>
</div>
<div class="black-dialog " id="reserveSuccess"></div>

<div id="exchange" style="display:none;">
    <div class="guild_box"></div>
    <div class="win_box"></div>
    <div style="clear:both; margin-bottom:4rem">
        <div class="triangle">
            <div class="popup pp"><span></span>您领取到的的激活码为</div>
        </div>
    </div>
    <div class="code">
        <div class="codebox share_code">
            <div><span id="codevalue"></span></div>

        </div>

    </div>

    <div class="done dn">
        <p style="text-align:center">兑换后请尽快使用，以免过期无效，可在“我的礼包”中查询</p>

        <div class="share_btn">
            <a href="javascript:copy();" style="background:#60af22;color:white;">复制</a>
        </div>
    </div>
</div>


<script type="text/javascript">
    var _paq = _paq || [];
    _paq.push(['trackPageView']);
    _paq.push(['enableLinkTracking']);
    (function () {
        // var loading = document.getElementById('loading-btn'),
        // icon = loading.getElementsByTagName('i')[0],
        // txt = loading.getElementsByTagName('b')[0];
        // loading.onclick = function() {
        // icon.style.display = 'inline-block';
        // txt.innerHTML = '加载中';
        // }

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
<input type="hidden" value="1" name="type"/>
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
