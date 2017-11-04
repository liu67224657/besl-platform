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
    <title>礼包详情</title>

    <link href="${URL_LIB}/static/theme/wap/css/wap_common.css?${version}" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/wap/css/wap_lbxq.css?${version}" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/wap/css/wap_lhcg.css?${version}" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/wap/css/wap_style.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function() {
            <%--var times_type = '${times_type}';--%>
            <%--if (times_type != '') {--%>
            <%--if (times_type == 2) {--%>
            <%--$("#codemsg").html("该礼包每天只能领取一次");--%>
            <%--}--%>
            <%--$(".mark-box").css("display", "none");--%>
            <%--$("#exchange").css("display", "block");--%>
            <%--$("#wrapper").css("display", "none");--%>
            <%--$("#codemsg").css("display", "block");--%>
            <%--$(".codebox").children("div").children("span").text('${code}');--%>

            <%--}--%>
        });

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
        function copy() {
            var value = $("#codevalue").text();
            var stringval = 'type=text&content=' + value;
            if (browser.versions.ios) {
                _jclient.copy(stringval, function (response) {
                    if (response) {
                        $("#reserveSuccess").text("复制成功");
                        $("#reserveSuccess").addClass("close");
                        var t = setInterval(function () {
                            $("#reserveSuccess").removeClass("close");
                            clearTimeout(t);
                        }, 3000);
                    }
                });
            }
            if (browser.versions.android) {
                _jclient.copy(stringval);
            }

        }


        function taocodecopy(value) {
            var stringval = 'type=text&content=' + value;
            if (browser.versions.ios) {
                _jclient.copy(stringval, function (response) {
                    if (response) {
                        $("#reserveSuccess").html("<div style='padding-top: 20px;'>复制成功！</div>");
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
    </script>
    <style>
        .Threebox {
            width: 60%;
            display: none;
            position: fixed;
            top: 50%;
            left: 22%;
            position: fixed;
            z-index: 1000;
        }

        .Threebox span {
            color: #fff;
            line-height: 18px;
            font-size: 14px;
            padding: 10px;
            display: block;
            text-align: center;
            background: rgba(0, 0, 0, 0.8);
            border-radius: 5px;
            -moz-border-radius: 5px;
            -webkit-border-radius: 5px;
            -o-border-radius: 5px;
            -ms-border-radius: 5px;
        }
    </style>
</head>
<body>
<div class="Threebox"><span></span></div>

<div id="wrapper" style="display:block;">
    <!-- <header id="header">
   <a href="#" class="return"></a>
   <h1>礼包详情</h1>
   <a href="#" class="close">关闭</a>
   </header> -->
    <%--${uno}--%>
    <div class="details">
        <c:if test="${activityDetailDTO.reserveType==1}">
            <cite class="reserveing-icon"><img src="${URL_LIB}/static/theme/wap/images/reserveing.png"
                                               alt=""></cite>
        </c:if>


        <div class="details-mark"></div>
        <p><img src="${activityDetailDTO.gipic}" alt=""></p>

        <div class="details-box">
            <div class="details-tit fl"><img src="${activityDetailDTO.gipic}" alt=""></div>
            <div class="details-text fl"><h1>${activityDetailDTO.title}</h1>

                <c:choose>
                    <c:when test="${empty reserveNum}">
                        <h3>剩余<span>${activityDetailDTO.rn}/${activityDetailDTO.cn}</span></h3>
                        <c:if test="${activityDetailDTO.point ne 0}">
                            <h3 class="details-md">迷豆<span>${activityDetailDTO.point}</span></h3>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <h3>${reserveNum}<span>人已预约</span></h3>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
    <div class="details-message">
        <c:choose>
            <c:when test="${activityDetailDTO.reserveType==1}">
                <p class="fl"><span style="color:#f00;">*</span>&nbsp;礼包上架后，我们会第一时间通知您</p>

            </c:when>
            <c:otherwise>
                <p class="fl"><span style="color:#f00;">*</span>&nbsp;兑换后请尽快使用，以免过期失效</p>

                <p class="fr">有效期:<span>${activityDetailDTO.endTime}</span></p>
            </c:otherwise>
        </c:choose>

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
                        <a href="javascript:void(0);" class="details-butten a1" id="reserve-btn">马上预约</a>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${logindomain=='client'||empty logindomain}">
                                <a href="javascript:void(0);" class="details-butten a1" id="reserve-btn">马上预约</a>
                            </c:when>
                            <c:otherwise>
                                <a href="javascript:void(0);" class="details-butten a1" id="s-reserve-btn">已预约</a>
                            </c:otherwise>
                        </c:choose>

                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${activityDetailDTO.rn!=0}">
                        <div class="fl"><a href="javascript:void(0);" class="butten a3" id="reserve"><span>领号</span></a>
                        </div>
                        <div class="fl"><a href="javascript:void(0);" class="butten a4" id="Forno"><span>淘号</span></a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <a href="javascript:void(0);" class="details-butten a4" id="Forno"><span>淘号</span></a>
                    </c:otherwise>
                </c:choose>

            </c:otherwise>
        </c:choose>


    </div>
    <div class="mark-box"></div>


    <div class="dialog " id="reservediv">
        <h1>您要预约的礼包为：</h1>

        <p class="confirm-text2"><span class="all">${activityDetailDTO.title}</span></p>

        <div class="dialog-btn"><a href="javascript:cancelReserve();" class="cancel">取消</a><a href="javascript:void(0);"
                                                                                              id="reserveGift"
                                                                                              class="gain"
                                                                                              style="border-left:none;">确定</a>
        </div>
    </div>
    <div class="dialog " id="gopointwall">
        <h1>迷豆不足：</h1>

        <p style="text-align:left">您可以在个人中心签到或做任务领取迷豆</p>

        <div class="dialog-btn"><a href="javascript:cancelReserve();" class="cancel" style="border-left:none;">知道了</a>

        </div>
    </div>
</div>
<div class="dialog black-dialog " id="reserveSuccess"></div>

<div id="exchange" class="pt-45" style="text-align:center;display:none;">
    <div class="lh-succeed lh-error">
        <span><img src="${URL_LIB}/static/theme/wap/images/succee.png" alt=""></span>

        <p>领号成功</p>

        <p style="display:none;" id="codemsg">您已领取过此礼包，每用户仅限领取一次</p>
    </div>
    <div class="code">
        <div class="codebox">
            <div><span class="fl" id="codevalue"></span><a href="javascript:copy();" class="copyBtn fr">复制</a></div>
        </div>
    </div>
    <div class="done dn">
        <p>已领取的礼包请在<span class="color">1小时</span>内尽快使用，否则将<br/>会进入淘号库被其他用户使用！</p>
    </div>
</div>


<div class="th-result" style="display:none;">
    <h1>${activityDetailDTO.title}号码表</h1>

    <p>淘号库中的礼包是其他用户已领取过的号码，有可能已经被使用，多试几个碰碰运气吧～</p>
</div>
<div id="taocodevalue">

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
        _paq.push(['setSiteId', 222]);
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
<noscript><p><img src="//stat.joyme.com/piwik.php?idsite=222" style="border:0;" alt=""/></p></noscript>

<input type="hidden" value="${profileid}" name="profileId"/>
<input type="hidden" value="${appkey}" name="appkey"/>
<input type="hidden" value="${activityDetailDTO.aid}" name="aid"/>
<input type="hidden" value="${activityDetailDTO.gid}" name="gid"/>
<input type="hidden" value="${uno}" name="uno"/>
<input type="hidden" value="1" name="type"/>
<input type="hidden" value="${gameid}" name="gameid"/>
<input type="hidden" value="${uid}" name="uid"/>
<input type="hidden" value="${token}" name="token"/>
<input type="hidden" value="${logindomain}" name="logindomain"/>
<input type="hidden" value="${activityDetailDTO.rn}" name="rn"/>
<input type="hidden" value="${platform}" name="platform"/>
<input type="hidden" value="${times_type}" name="times_type"/>
<input type="hidden" value="${code}" name="code"/>
<input type="hidden" value="${activityDetailDTO.point}" name="dtopoint"/>
<input type="hidden" value="${otherid}" name="otherid"/>
<input type="hidden" value="${channelid}" name="channelid"/>
<input type="hidden" value="${clientid}" name="clientid"/>
<input type="hidden" id="timeOut" value="${activityDetailDTO.endTime}">
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/joymeappmy-init.js');
</script>
</body>
</html>
