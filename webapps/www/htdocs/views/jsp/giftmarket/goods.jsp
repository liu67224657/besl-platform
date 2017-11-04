<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta name="applicable-device"content="pc">
    <meta name="mobile-agent"content="format=xhtml;url=http://m.joyme.com/">
    <meta name="mobile-agent" content="format=html5;url=http://m.joyme.com">

    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="Keywords" content="着迷、着迷网、游戏社区、游戏攻略、游戏交友">
    <meta name="description" content="来着迷小组寻找推荐游戏、实用攻略、向游戏达人提问、结交志同道合的朋友。"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>积分专区_${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>

    <script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>
</head>

<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>

<div class="articlepage-white-bg">
    <div class="index-2013-box">
        <div class="pointsShop-banner"></div>

        <!-- 左侧栏 -->
        <div class="pointsShop-left">
            <h2 class="pointsShop-title">积分直兑区<span class="pointsShop-title-icon-1"></span></h2>

            <div class="pointsShop-item">
                <ul>
                    <c:forEach items="${list}" var="goods" varStatus="st">
                    <li>
                        <div><a href="${goods.detailUrl}" target="_blank"><img src="${goods.goodsPic}"></a></div>
                        <h2><a href="${goods.detailUrl}" target="_blank">${goods.goodsName}</a></h2>

                        <p>${goods.goodsDesc}</p>

                        <div class="clearfix"><span>${goods.goodsConsumePoint}积分/个</span>剩余${goods.goodsResetAmount}个
                        </div>
                        <c:choose>
                            <c:when test="${goods.goodsResetAmount<=0}">
                                <a href="javascript:void(0)" class="exchange-end">兑换结束</a>
                            </c:when>
                            <c:when test="${userSession==null || (userSession!=null && goods.goodsResetAmount>0)}">
                                <a href="javascript:void(0);" class="exchange" data-gsid="${goods.goodsId}"
                                   data-cpoint="${goods.goodsConsumePoint}">马上兑换</a>
                            </c:when>
                            <%--<c:when test="${userSession!=null  && goods.goodsResetAmount>0 && userSession.pointAmount<goods.goodsConsumePoint}">
                                <a href="javascript:void(0)" class="exchange-disable">积分不足</a>
                            </c:when>--%>
                        </c:choose>
                    </li>

                    <c:if test="${st.index%3==2}">
                </ul>
                <c:if test="${!st.last}"> <span class="dotted-bg"></span></c:if>
            </div>
            <div class="pointsShop-item">
                <ul>
                    </c:if>
                    </c:forEach>
                </ul>
            </div>
        </div>

        <!-- 右侧栏 -->
        <div class="pointsShop-right">
            <c:if test="${userSession!=null}">
                <h2 class="pointsShop-title">Hi,欢迎回来!<span class="pointsShop-title-icon-2"></span></h2>

                <div class="pointsShop-user">
                    <a href="${URL_WWW}/people/${userSession.blogwebsite.domain}" class="u"><img
                            src="${uf:parseFacesInclude(userSession.blogwebsite.headIconSet,userSession.userDetailinfo.sex,"m" , true,0,1)[0]}"/></a>

                    <div>
                        <h2>
                            <c:choose>
                                <c:when test="${socialProfile.profile.detail.sex=='0'}"><em class="boy"></em></c:when>
                                <c:when test="${socialProfile.profile.detail.sex=='1'}"><em class="girl"></em></c:when>
                            </c:choose>
                            <a href="${URL_WWW}/people/${userSession.blogwebsite.domain}">${userSession.blogwebsite.screenName}</a>
                            <c:if test="${userSession.userDetailinfo.verifyType !=null && userSession.userDetailinfo.verifyType.code!= 'n'}">
                                <a href="${URL_WWW}/people/${userSession.blogwebsite.domain}"
                                   class="${userSession.userDetailinfo.verifyType.code}vip"
                                   title="<fmt:message key="verify.profile.${userSession.userDetailinfo.verifyType.code}" bundle="${userProps}"/>">
                                </a>
                            </c:if>
                        </h2>

                        <p>我的积分：${userSession.pointAmount}</p>
                    </div>
                </div>
            </c:if>

            <h2 class="pointsShop-title">积分攻略<span class="pointsShop-title-icon-3"></span></h2>
            <c:if test="${userSession!=null}">
                <div class="pointsTips">
                    <em>今天已赚：<c:if test="${userSession!=null}">${dayPoint}</c:if>积分</em>
                    <span class="dotted-bg"></span>
                </div>
            </c:if>

            <h3 class="pointsShop-title-2">分享WIKI和活动到其他社交网络<span class="pointsShop-title-icon-4"></span></h3>

            <div class="points-strategy">
                <%@ include file="/hotdeploy/views/jsp/giftmarket/recommend-wiki.jsp" %>
                <span class="dotted-bg"></span>
            </div>
        </div>

        <div class="clear"></div>
    </div>
</div>

<!--页尾开始-->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    var userPoint =${userSession.pointAmount==null?0:userSession.pointAmount};
    seajs.use('${URL_LIB}/static/js/init/goods-init.js')
</script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/giftmarket-tj.js"></script>
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
