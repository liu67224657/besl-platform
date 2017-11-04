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
    <meta name="Keywords" content="手机游戏礼包,手游礼包领取,手游礼包中心,兑换码,激活码,手机游戏礼包领取">
    <meta name="description"
          content="着迷网礼包中心为手游玩家提供各种手游礼包,其中包括手机游戏激活码,特权码,测试礼包,新手礼包,兑换码等领取,
还有多种手游的着迷专属礼包等你拿,多种手游礼包尽在着迷网手游礼包中心.
"/>

    <title>${detail.title}</title>
    <link href="${URL_LIB}/static/theme/default/css/wapstyle.css?${version}" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <script type="text/javascript">
        function changeBox(m) {
            if (m === 'open') {
                document.getElementById('hidebox').style.display = 'none';
                document.getElementById('showbox').style.display = 'block';
                document.documentElement.scrollTop = 0;
                document.body.scrollTop = 0;
            } else {
                document.getElementById('hidebox').style.display = 'block';
                document.getElementById('showbox').style.display = 'none';
            }

        }
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
        function onBridgeReady() {
            WeixinJSBridge.call('showOptionMenu');
        }

        if (typeof WeixinJSBridge == "undefined") {
            if (document.addEventListener) {
                document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
            } else if (document.attachEvent) {
                document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
                document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
            }
        } else {
            onBridgeReady();
        }
        $(document).ready(function() {
            $("#searchText").focus();
        });
    </script>

</head>
<body>
<div id="hidebox">
    <div class="img-place">
        <img src="http://joymepic.joyme.com/qiniu/original/2016/05/58/5d71e225088590472e0b54d0d40a46e82b2f.jpg" width="150">
    </div>
    <div class="gift-info">
        <img src="${detail.gipic}">

        <div>
            <h2>${detail.title}</h2>

            <p>剩余：<span>${detail.rn}</span>/${detail.cn}</p>

            <p>有效期：${detail.endTime}</p>
        </div>
    </div>

    <c:choose>
        <c:when test="${detail.weixinExclusive==2}">
            <div class="dw-wb">
                 <a href="javascript:;" onclick="window.location.href='http://huabao.joyme.com/wap.html'">下载着迷玩霸领礼包</a>
             </div>
        </c:when>
        <c:otherwise>
            <!-- 领号等按钮 -->
            <div class="get">
                <c:choose>
                    <c:when test="${detail.rn<=0}">
                        <a class="get-type2" href="javascript:void(0)" id="taocode">淘号</a>
                        <a class="get-type3" href="javascript:void(0)" id="giftReserveSubmit">预定</a>
                    </c:when>
                    <c:otherwise>
                        <a class="get-type1" href="javascript:void(0);" id="getcode">领号</a>
                        <a class="get-type2" href="javascript:void(0);" id="taocode">淘号</a>
                    </c:otherwise>
                </c:choose>

            </div>
        </c:otherwise>
    </c:choose>

    <!-- 详情 -->
    <div class="gift-detail">
        <h2>独家礼包内容：</h2>
        <%--${detail.desc}--%>
        <c:if test="${not empty detail.textJsonItemsList}">
            <c:forEach items="${detail.textJsonItemsList}" var="dto">
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
<div id="showbox" style="display:none">
    <div class="guild_box"></div>
    <div class="win_box"></div>
    <div style="clear:both; margin-bottom:4rem">
        <div class="triangle">
            <div class="popup pp"><span></span>麻烦您点击号码手动复制</div>
        </div>
    </div>
    <div class="code">
        <div class="codebox share_code">
            <div><span id="codevalue"></span></div>
        </div>
    </div>
    <div class="done dn">
        <p>已领取的礼包请在<span>1小时</span>内尽快使用，否则将会进入淘号库被其他用户使用哦！</p>
    </div>
    <div class="share_box">
        <span><i class="joymeIcon"></i>分享礼包，分享你的爱</span>
    </div>
    <div class="share_btn">
        <a href="javascript:;" onclick="changeBox('close')">下次再说!</a>
    </div>
</div>
<input type="hidden" value="${detail.aid}" id="activityid" name="aid"/>
<input type="hidden" value="${detail.gid}" id="gid" name="gid"/>
<input type="hidden" value="${detail.title}" id="reserve"/>
<input type="hidden" value="${detail.endTime}" id="timeOut" name="timeOut"/>
<input type="hidden" value="${openid}" id="openid" name="openid"/>
<input type="hidden" value="${token}" id="token" name="token"/>
<input type="hidden" id="resetNum" value="${detail.rn}"/>
<script type="text/javascript">
    var _paq = _paq || [];
    _paq.push(['trackPageView']);
    _paq.push(['enableLinkTracking']);
    (function() {
        var u = "//stat.joyme.com/";
        _paq.push(['setTrackerUrl', u + 'piwik.php']);
        _paq.push(['setSiteId', 113]);
        var d = document, g = d.createElement('script'), s = d.getElementsByTagName('script')[0];
        g.type = 'text/javascript';
        g.async = true;
        g.defer = true;
        g.src = u + 'piwik.js';
        s.parentNode.insertBefore(g, s);
    })();
</script>
<noscript><p><img src="//stat.joyme.com/piwik.php?idsite=113" style="border:0;" alt=""/></p></noscript>

<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/giftmarketwap-init.js');
</script>
<script>
    (function (G,D,s,c,p) {
        c={//监测配置
            UA:"UA-joyme-000001", //客户项目编号,由系统生成
            NO_FLS:0,
            WITH_REF:1,
            URL:'http://lib.joyme.com/static/js/iwt/iwt-min.js'
        };
        G._iwt?G._iwt.track(c,p):(G._iwtTQ=G._iwtTQ || []).push([c,p]),!G._iwtLoading && lo();
        function lo(t) {
            G._iwtLoading=1;s=D.createElement("script");s.src=c.URL;
            t=D.getElementsByTagName("script");t=t[t.length-1];
            t.parentNode.insertBefore(s,t);
        }
    })(this,document);
</script>
</body>
</html>
