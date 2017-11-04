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
    <meta name="Keywords" content="积分兑换,游戏周边,手机壳,鼠标垫,扑克">
    <meta name="description"
          content="着迷网积分兑换区包含的礼品十分丰富,涵盖了手机壳、鼠标垫、扑克、游戏符石等游戏周边产品,着迷网玩家可凭账号积分去兑换区免费领取."/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>积分兑换,游戏周边,手机壳,鼠标垫,扑克_${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/giftcenter.css?${version}" rel="stylesheet" type="text/css"/>
</head>
<body style="background:#fff">
<!--头部开始-->
<c:import url="/views/jsp/passport/header.jsp"/>
<!-- 内容开始 -->

<!-- 当前位置 -->
<div class="location-2th">
    <div>当前位置：<a href="${URL_WWW}">着迷网</a> &gt; <a href="${URL_WWW}/giftmarket/">礼包中心</a> &gt; 积分明细
    </div>
</div>

<div class="gift-box clearfix">

    <!-- 左侧 -->
    <div class="columns-left">
        <div class="cloumns-box">
            <div class="gift-center-title clearfix">
                <h2>积分明细</h2>
            </div>

            <!-- 积分明细 -->
            <div class="top-bg points-detail">
                <div class="points-detail-box">
                    <!-- 标题 -->
                    <div class="points-detail-title">
                        <ul>
                            <li>我都做了些什么</li>
                            <li>时间</li>
                            <li>积分收支</li>
                        </ul>
                    </div>

                    <ul class="points-detail-list">
                        <c:forEach items="${creditDetailList}" var="dto" varStatus="st">
                            <li <c:if test="${st.index % 2 == 1}">class="bg"</c:if>>
                                <span>${dto.description}</span>
                                <span><fmt:formatDate value="${dto.date}" pattern="yyyy-MM-dd"/></span>
                                <span><c:if test="${dto.point>=0}">+</c:if>${dto.point}</span>
                            </li>
                        </c:forEach>
                    </ul>

                    <!-- 分页 -->
                    <div class="pagecon clearfix">
                        <c:set var="pageurl" value="${URL_WWW}/giftmarket/creditdetail"/>
                        <%@ include file="/views/jsp/page/page.jsp" %>
                    </div>
                </div>
            </div>


        </div>
    </div>

    <!-- 右侧 -->
    <div class="columns-right">
    <!-- 用户信息 -->
    <c:if test="${userSession!=null}">
        <%@ include file="/views/jsp/giftmarket/usersession.jsp" %>
    </c:if>

    <!-- 礼包上架日志 -->
    <c:if test="${logHtml!=null}">
        ${logHtml}
    </c:if>
    <!-- 7日兑换排行 -->
    <%@ include file="/views/jsp/giftmarket/sevenrank.jsp" %>
    <!-- 大家正在领 -->
    <%@ include file="/views/jsp/giftmarket/recentlog.jsp" %>
    <!-- 积分消费排行榜 -->
    <%@ include file="/views/jsp/giftmarket/consumerank.jsp" %>
    <!-- 积分攻略 -->
    <%@ include file="/hotdeploy/views/jsp/giftmarket/giftmarket-wiki_new.jsp" %>
</div>

</div>

<!-- footer -->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/common-init.js');
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
