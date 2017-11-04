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

    <title>搜索礼包</title>
    <link href="${URL_LIB}/static/theme/default/css/wapstyle.css?${version}" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
        function onBridgeReady() {
            WeixinJSBridge.call('hideOptionMenu');
        }

        if (typeof WeixinJSBridge == "undefined") {
            if (document.addEventListener) {
                document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
            } else if (document.attachEvent) {
                document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
                document.attachEvent('sonWeixinJSBridgeReady', onBridgeReady);
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
<c:choose>
    <c:when test="${empty list}">
        <div class="report no">抱歉！ 着迷暂时没有“<span>${cond}</span>”的礼包发送</div>
        <div class="tips_imgs"></div>
        <div class="tips_txt">
            向着迷许愿想要“<span>${cond}</span>”的相关礼包，我们会联系厂家发送礼包，许愿很可能会实现呀
        </div>
        <div class="make"><a href="/giftmarket/wap/reserve" class="xuyuan-btn">礼包许愿</a>
            <a href="/giftmarket/wap?openid=${openid}&token=${token}" class="chakan-btn">查看其他礼包</a></div>

    </c:when>
    <c:otherwise>
        <div class="report no">查找到与“<span>${cond}</span>”相关的礼包(${list.size()})</div>
        <div class="list" id="wapgiftmarketlist">
            <!-- 已经领取的 加 class="received" -->
            <c:forEach items="${list}" var="dto">
                <a href="/giftmarket/wap/giftdetail?aid=${dto.gid}&openid=${openid}&token=${token}">
                <img src="${dto.gipic}">

                    <div>
                        <p>${dto.title}</p>

                        <p>
                            <span>有效期：${dto.exDate}</span>
                            <span>剩余：${dto.sn}/${dto.cn}</span>
                        </p>
                    </div>
                </a>
            </c:forEach>
        </div>
        <!--加载更多-->
        <c:if test="${page.maxPage>1}">
            <div class="loading" id="loading-btn"><a href="javascript:;" id="logdingSearch"><i style="display:none"></i><b>点击加载更多</b></a>
            </div>
        </c:if>
        <script type="text/javascript">
            (function() {
                var loading = document.getElementById('loading-btn'),
                        icon = loading.getElementsByTagName('i')[0],
                        txt = loading.getElementsByTagName('b')[0];
                loading.onclick = function() {
                    icon.style.display = 'inline-block';
                    txt.innerHTML = '加载中';
                }
            })();
        </script>
        <input type="hidden" value="${page.curPage}" id="wapCurPage"/>
        <input type="hidden" value="${page.maxPage}" id="wapMaxPage"/>
        <input type="hidden" value="${token}" id="token"/>
        <input type="hidden" value="${openid}" id="openid"/>
        <input type="hidden" value="${cond}" id="cond"/>
    </c:otherwise>
</c:choose>
<script type="text/javascript">
  var _paq = _paq || [];
  _paq.push(['trackPageView']);
  _paq.push(['enableLinkTracking']);
  (function() {
    var u="//stat.joyme.com/";
    _paq.push(['setTrackerUrl', u+'piwik.php']);
    _paq.push(['setSiteId', 113]);
    var d=document, g=d.createElement('script'), s=d.getElementsByTagName('script')[0];
    g.type='text/javascript'; g.async=true; g.defer=true; g.src=u+'piwik.js'; s.parentNode.insertBefore(g,s);
  })();
</script>
<noscript><p><img src="//stat.joyme.com/piwik.php?idsite=113" style="border:0;" alt="" /></p></noscript>


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
