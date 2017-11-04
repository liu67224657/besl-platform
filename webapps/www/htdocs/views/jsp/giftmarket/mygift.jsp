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
    <meta name="Keywords" content="手机游戏礼包,手机游戏激活码,兑换码,礼包领取">
    <meta name="description" content="着迷网礼包中心为手游玩家提供各种手机游戏领号,淘号,激活码礼包,特权码,测试礼包,新手礼包,兑换码等领取,还有多种手游的着迷专属礼包等你拿,多种手游礼包尽在着迷网."/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>我的礼包_手游礼包放送区_${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/giftcenter.css?${version}" rel="stylesheet" type="text/css"/>
</head>
<body style="background:#fff">
<c:import url="/views/jsp/passport/header.jsp"/>

<div class="location-2th">
    <div>当前位置：<a href="${URL_WWW}">着迷网</a> &gt; <a href="${URL_WWW}/giftmarket/">礼包中心</a> &gt; 我的礼包</div>
</div>

<div class="gift-box clearfix">
    <!-- 左侧 -->
    <div class="columns-left">
        <div class="cloumns-box">
            <!-- 礼包放送列表 -->
            <div class="my-credits" style="border:none">
                <ul>
                    <c:choose>
                        <c:when test="${list==null || empty list}">
                            <li>
                                <p align="center">亲，您暂时没有礼包，赶快去领取吧</p>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${list}" var="dto" varStatus="st">
                                <li>
                                    <p class="p-item-1"><c:if test="${dto.remove}">${dto.title}</c:if><c:if test="${!dto.remove}"><a href="${URL_WWW}/gift/${dto.aid}">${dto.title}</a></c:if></p>

                                    <p class="p-item-2"><c:choose><c:when test="${dto.expire}">已于<fmt:formatDate
                                            value="${dto.endTime}" pattern="yyyy-MM-dd"/>过期</c:when>
                                        <c:otherwise>有效期：<fmt:formatDate value="${dto.endTime}"
                                                                         pattern="yyyy-MM-dd"/></c:otherwise></c:choose></p>

                                    <p class="p-item-3">${dto.itemName1}：</p>

                                    <p class="p-item-4">
							        <span class="copy-num">
								        <em id="em_code_${st.index}">${dto.itemValue1}</em>
								        <a href="javascript:void(0)" id="a_copy_${st.index}" class="copycode" onclick="copyCode();">复制</a>
                                        <c:if test="${!dto.expire}"><a href="javascript:void(0)" data-id="${st.index}" class="send-to-mobile" data-aid="${dto.aid}" data-gid="${dto.gid}" data-lid="${dto.lid}">免费发送到手机</a></c:if>
							        </span>
                                    </p>
                                </li>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>

            <!-- 分页 -->
            <div class="pagecon clearfix">
                <c:set var="pageurl" value="${URL_WWW}/mygift"/>
                <%@ include file="/views/jsp/page/page.jsp" %>
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
    var userPoint =${userSession.pointAmount==null?0:userSession.pointAmount};
    seajs.use('${URL_LIB}/static/js/init/mygift-init.js')
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
