<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta name="applicable-device" content="mobile">
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="Keywords" content="${detailDTO.title}">
    <meta name="description"
          content="着迷网礼包中心为手游玩家提供各种手游礼包,其中包括手机游戏激活码,特权码,测试礼包,新手礼包,兑换码，节假日礼包等,还有多种着迷专属礼包等你拿,多种手游礼包尽在着迷网!"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>${detailDTO.title}_兑换码_领取地址_着迷网移动版</title>
    <link rel="stylesheet" type="text/css" href="http://static.joyme.com/mobile/cms/jmsy/css/common-beta1.css">
    <link rel="stylesheet" type="text/css"
          href="${URL_STATIC}/mobile/cms/jmsy/css/newlibao.css?v=${version}">

    <%--<link rel="stylesheet" type="text/css" href="${URL_STATIC}/mobile/cms/jmsy/logincont/loginbar.css?${version}"/>--%>

    <script type="text/javascript" src="${URL_STATIC}/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
</head>
<body>
<div class="container">
    <%--<div class="topbar-mod border-b fn-t-c">--%>
    <%--<h1></h1>--%>
    <%--<a href="javascript:void(0);">${detailDTO.title}</a>--%>
    <input type="hidden" id="profileId" value="${userSession.profileId}"/>
    <%--</div>--%>
    <%@ include file="/views/jsp/passport/m/gift-header-m.jsp" %>
    <c:choose>
        <c:when test="${exchangeLog != null}">
            <div id="wrapper">
                <div id="main">
                    <div id="center">
                        <!-- 详情页之单独礼包 开始 -->
                        <div class="li-box border-b">
                            <div class="li-box-top">
                                <cite>
                                    <img src="${detailDTO.gipic}">
                                </cite>
                                <span>
        	                   		<p class="fn-clear"><font>${detailDTO.title}</font>
                                        <c:if test="${detailDTO.platform eq 0 || detailDTO.platform eq 4}">
                                            <i class="mobile-ios"></i>
                                        </c:if>
                                        <c:if test="${detailDTO.platform eq 1  || detailDTO.platform eq 4}">
                                            <i class="mobile-and"></i>
                                        </c:if>
                                    </p>
        							<em>有效期：${dateutil:parseCustomDate(detailDTO.endTime, "yyyy-MM-dd HH:mm")}</em>
        							<b>剩余: ${detailDTO.rn}</b>
        						</span>
                            </div>
                            <div class="li-lin">
                                <h3>*请手动复制号码</h3>

                                <div class="li-lin-block san">
                                    <span>${exchangeLog.snValue1}</span>
                                </div>
                                <p>已领取<span>礼包请在1小时内</span>尽快使用否则将会入淘号库被其他同学使用哦~</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>

            <div id="wrapper">
                <div id="main">
                    <div id="center">
                        <!-- 详情页之单独礼包 开始 -->
                        <div class="li-box border-b">
                            <div class="li-box-top">
                                <cite>
                                    <img src="${detailDTO.gipic}">
                                </cite>
                                <span>
        	                   		<p class="fn-clear"><font>${detailDTO.title}</font>
                                        <c:if test="${detailDTO.platform eq 0 || detailDTO.platform eq 4}">
                                            <i class="mobile-ios"></i>
                                        </c:if>
                                        <c:if test="${detailDTO.platform eq 1  || detailDTO.platform eq 4}">
                                            <i class="mobile-and"></i>
                                        </c:if>
                                    </p>
        							<em>有效期：${dateutil:parseCustomDate(detailDTO.endTime, "yyyy-MM-dd HH:mm")}</em>
        							<b>剩余: ${detailDTO.rn}</b>
        						</span>
                            </div>
                            <div class="li-box-btn fn-clear">
                                <c:if test="${detailDTO.weixinExclusive!=1&&detailDTO.weixinExclusive!=2}">
                                    <a href="javascript:mgetcode('http://m.${DOMAIN}/gift/getcode?aid=${detailDTO.aid}');"
                                       class="li-box-lh">领号</a>
                                    <a href="javascript:mgetcode('http://m.${DOMAIN}/gift/taocode?aid=${detailDTO.aid}');"
                                       class="li-box-th">淘号</a>
                                </c:if>
                            </div>
                        </div>
                        <!-- 详情页之单独礼包 结束 -->
                        <!-- 详情页之单独礼包描述 开始 -->
                        <div class="li-text">
                            <c:if test="${detailDTO.weixinExclusive==1}">
                                <h3 class="li-zs">该礼包是微信独家礼包，请关注微信号【着迷网络】后点击“领礼包”或在对话框中输入游戏名称领取。
                                </h3>
                                <img src="http://lib.joyme.com/static/theme/default/images/joyme-special4-m.jpg">
                            </c:if>
                            <c:if test="${detailDTO.weixinExclusive==2}">
                                <h3 class="li-zs">本礼包是着迷玩霸专属礼包，安装客户端后才能领取！
                                </h3>
                                <img src="http://joymepic.joyme.com/qiniu/original/2016/09/26/1df5f2550ebd204b9208c840d4d1abbeb989.jpg"
                                />
                            </c:if>
                            <div class="li-text-cont">
                                <c:if test="${not empty detailDTO.textJsonItemsList}">
                                    <c:forEach items="${detailDTO.textJsonItemsList}" var="dto">
                                        <c:if test="${dto.type=='1'}">
                                            <p>${dto.item}</p>
                                        </c:if>
                                        <c:if test="${dto.type=='2'}">
                                            <script type="text/javascript">
                                                var img = '${dto.item}';
                                                if (img.indexOf('http://joymepic.joyme.com') > 0) {
                                                    img += ('?imageView2/1/w/' + $(window).width());
                                                }
                                                var html = '<p><img src="' + img + '" style="display:block;margin:0 auto;" /></p>';
                                                document.write(html);
                                            </script>
                                        </c:if>
                                    </c:forEach>
                                </c:if>
                            </div>
                            <script>
                                document.write('<script src="http://passport.${DOMAIN}/auth/footer/m?v=' + Math.random() + '"><\/script>');
                            </script>
                            <!-- 详情页之单独礼包描述 结束 -->
                        </div>

                    </div>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
    <%@ include file="/views/jsp/passport/m/nav.jsp" %>

</div>
<%--<%@ include file="/views/jsp/passport/new-login-page.jsp" %>--%>


<script type="text/javascript" src="${URL_STATIC}/mobile/cms/jmsy/js/lazyimg.js"></script>
<script type="text/javascript" src="${URL_STATIC}/mobile/cms/jmsy/js/common.js"></script>
<script type="text/javascript">
    $(function () {
        $('.lazy').lazyImg();
    });
    $(document).ready(function () {
        $('.tab-menu>span').on('touchstart', function () {
            var indexs = $(this).index();
            $(this).addClass('active').siblings().removeClass('active');
            $('.tab-cont>div').eq(indexs).addClass('active').siblings().removeClass('active');
        });

    });
    function mgetcode(url) {
        var profileid = $('#profileId').val();
        if (profileid == '') {
            type = "";
            $('.login-mask').click();
        } else {
            window.location.href = url;
        }
    }
</script>
<%--<script type="text/javascript" src="${URL_LIB}/static/js/init/login_common.js"></script>--%>
<script type="text/javascript">
    var _paq = _paq || [];
    _paq.push(["setDocumentTitle", document.domain + "/" + document.title]);
    _paq.push(['trackPageView']);
    _paq.push(['enableLinkTracking']);
    (function () {
        var u = "//stat.joyme.com/";
        _paq.push(['setTrackerUrl', u + 'piwik.php']);
        _paq.push(['setSiteId', 198]);
        var d = document, g = d.createElement('script'), s = d.getElementsByTagName('script')[0];
        g.type = 'text/javascript';
        g.async = true;
        g.defer = true;
        g.src = u + 'piwik.js';
        s.parentNode.insertBefore(g, s);
    })();
    document.write('<noscript><p><img src="//stat.joyme.com/piwik.php?idsite=198" style="border:0;" alt="" /></p></noscript>');
</script>
<script>
    (function (G, D, s, c, p) {
        c = {//监测配置
            UA: "UA-joyme-000001", //客户项目编号,由系统生成
            NO_FLS: 0,
            WITH_REF: 1,
            URL: 'http://lib.joyme.com/static/js/iwt/iwt-min.js'
        };
        G._iwt ? G._iwt.track(c, p) : (G._iwtTQ = G._iwtTQ || []).push([c, p]), !G._iwtLoading && lo();
        function lo(t) {
            G._iwtLoading = 1;
            s = D.createElement("script");
            s.src = c.URL;
            t = D.getElementsByTagName("script");
            t = t[t.length - 1];
            t.parentNode.insertBefore(s, t);
        }
    })(this, document);
</script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/giftmarket-tj.js"></script>

</body>
</html>
