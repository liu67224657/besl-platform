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
    <title>商品详情</title>

    <link href="${URL_LIB}/static/theme/wap/css/wap_common.css?${version}" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/wap/css/wap_spxq.css?${version}" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/wap/css/wap_lbxq.css?${version}" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/wap/css/wap_lhcg.css?${version}" rel="stylesheet" type="text/css">

    <%--<link href="${URL_LIB}/static/theme/wap/css/wap_lhcg.css?${version}" rel="stylesheet" type="text/css">--%>

    <%--<script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>--%>
    <%--<script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>--%>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/page/geo.js"></script>

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

        function copy(value) {
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
    </script>
</head>
<body onload="setup();preselect('省份');">
<div id="wrapper" class="w-bg-color ">
    <!-- <header id="header">
         <a href="#" class="return"></a>
         <h1>礼包详情</h1>
         <a href="#" class="close">关闭</a>
     </header> -->

    <div class="padding_box">
        <div id="pic-loop">
            <div class="pic-loop-box swiper-wrapper">
                <div class="swiper-slide"><img src="${activityDetailDTO.bgPic}"></div>

            </div>
            <div class="pagination">
                <span class="swiper-pagination-switch swiper-active-switch"></span>
            </div>
        </div>
        <div class="sp-details">
            <div class="sp-details-top clearfix">
                <div class="sp-details-top-l fl">
                    <p>${activityDetailDTO.title}</p>

                    <p>剩余：<span>${activityDetailDTO.sn}/${activityDetailDTO.cn}</span></p>
                </div>
                <b class="fr">${activityDetailDTO.rn}人已兑换</b>
            </div>
            <div class="sp-details-bottom clearfix">
                <%--<p>参考价：<span>￥40</span></p>--%>
                <p>需迷豆：<span class="color">${activityDetailDTO.point}</span></p>

                <p>数量：<span>1</span></p>

                <%--<p>规格：<span>默认</span></p>--%>
            </div>
        </div>
        <div class="sp-details-main exclusive clearfix">
            <h1><span>商品详情</span>
            </h1>
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
            <h1><span>提示</span>
            </h1>

            <p>新疆、西藏、青海、港澳台以及海外不在邮寄范围内，如有疑问请联系我们</p>

            <h1 class="dj-title"><span>大家都在换</span></h1>

            <div class="shop">
                <c:choose>
                    <c:when test="${not empty giftDto.rows}">
                        <c:forEach items="${giftDto.rows}" var="dto">
                            <a href="http://api.${DOMAIN}/joymeapp/gameclient/webview/giftmarket/giftdetail?appkey=${appkey}&profileid=${profileid}&aid=${dto.gid}&type=2">
                                <div class="shop-box">
                                    <cite><img src="${dto.gipic}"></cite>

                                    <div class="shop-box-text"><h2 class="cut_out2">${dto.title}</h2>

                                        <h3><span>${dto.point}</span>迷豆</h3></div>
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
                    </c:when>
                    <c:otherwise>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
    <div class="details-btn ">
        <c:choose>
            <c:when test="${activityDetailDTO.sn!=0}">
                <c:choose>
                    <c:when test="${isBool}">
                         <a href="javascript:void(0);" class="details-butten a5">商品已下架</a>
                    </c:when>
                    <c:otherwise>
                          <a href="javascript:void(0);" class="details-butten a5" id="syhb-exchange-btn">马上兑换</a>
                    </c:otherwise>
                </c:choose>

            </c:when>
            <c:otherwise>
                <a href="javascript:void(0);" class="details-butten a2">已售罄</a>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="mark-box"></div>
    <div class="dialog " id="cofirmshopdiv">
        <h1>是否兑换：</h1>

        <p style="text-align:left">${activityDetailDTO.title}，共需<em>${activityDetailDTO.point}</em>迷豆</p>

        <div class="dialog-btn"><a href="javascript:cancelReserve();" class="cancel">取消</a><a href="javascript:void(0);"
                                                                                              id="getgoods"
                                                                                              class="gain">确定</a></div>
    </div>
    <div class="dialog " id="gopointwall">
        <h1>可用迷豆不足：</h1>

        <p style="text-align:left">兑换此商品，共需<em>${activityDetailDTO.point}</em>迷豆</p>

        <div class="dialog-btn"><a href="javascript:cancelReserve();" class="cancel" style="border-left:none;">确定</a>
            <%--<a href="javascript:goTask('http://api.${DOMAIN}/joymeapp/gameclient/webview/task/list?appkey=${appkey}&platform=${platform}&uid=${uid}');"--%>
            <%--class="gain">做任务</a>--%>

            <%--<a--%>
            <%--href="${URL_WWW}/my/hotapp?appkey=${appkey}&profileid=${profileid}&clientid=${clientid}&platform=${platform}"--%>
            <%--class="gain">马上赚迷豆</a>--%>
        </div>
    </div>
</div>
<div class="new-address" style="display:none;">
    <ul>
        <li><input type="text" placeholder="收货人姓名" name="name"/></li>
        <li><input type="text" placeholder="手机号码" name="phone"/></li>
        <li><input type="text" placeholder="邮政编码" name="zipcode"/></li>
        <li style="width:100%;">
            <select class="select" name="province" style="height:25px;width:200px;" id="s1">
                <option value=""></option>
            </select>

        </li>
        <li>
            <select class="select" style="height:25px;width:200px;" name="city" id="s2">
                <option value=""></option>
            </select></li>
        <li>
            <select class="select" style="height:25px;width:200px;" name="town" id="s3">
                <option value=""></option>
            </select></li>
        <li><input type="text" placeholder="详细地址" class="textarea" name="address"/></li>
    </ul>
    <div class="dialog-btn"
         style="text-align:center ;width: 100%;box-sizing: border-box;position: static;border-top:none;border-bottom: 1px solid #e5e5e5;">
        <a href="javascript:cancelAddress();" class="cancel">取消</a><a href="javascript:void(0);"
                                                                      class="gain"
                                                                      id="submitAddress">提交</a>
    </div>
</div>

<div id="exchange" class="pt-45" style="text-align:center;display:none;">
    <div class="lh-succeed lh-error">
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

<div class="dialog black-dialog " id="reserveSuccess"></div>


<script type="text/javascript">
    var _paq = _paq || [];
    _paq.push(['trackPageView']);
    _paq.push(['enableLinkTracking']);
    (function () {
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
<input type="hidden" value="${logindomain}" name="logindomain"/>
<input type="hidden" value="${activityDetailDTO.sn}" name="sn"/>
<input type="hidden" value="${activityDetailDTO.point}" name="dtopoint"/>
<input type="hidden" value="${userpoint}" name="userpoint"/>
<input type="hidden" value="${allowExchangeStatus}" name="allowExchangeStatus"/>
<input type="hidden" value="${exchangeStatus}" name="exchangeStatus"/>
<input type="hidden" value="${goodstype}" name="goodstype"/>

<input type="hidden" value="2" name="type"/>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/joymeappmy-init.js');
</script>
</body>
</html>
