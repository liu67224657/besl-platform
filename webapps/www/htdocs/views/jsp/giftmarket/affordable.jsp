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
    <title>我能兑换的礼包_积分兑换区_礼包中心_${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/giftcenter.css?${version}" rel="stylesheet" type="text/css"/>
</head>
<body style="background:#fff">
<c:import url="/views/jsp/passport/header.jsp"/>

<div class="location-2th">
    <div>当前位置：<a href="${URL_WWW}">着迷网</a> &gt; <a href="${URL_WWW}/giftmarket/">礼包中心</a> &gt; <a
            href="${URL_WWW}/coin/">积分兑换区</a> &gt; 我能兑换的商品
    </div>
</div>

<div class="gift-box clearfix">
    <!-- 左侧 -->
    <div class="columns-left">
        <div class="cloumns-box">
            <!-- 积分兑换列表 -->
            <div class="gift-center-title clearfix">
                <h2 class="">我能兑换的商品</h2>
            </div>
            <c:choose>
                <c:when test="${pointEnoughPage==null || empty pointEnoughPage}">
                    <div class="result-null-2">
                        <div class="top-bg"></div>
                        <a href="http://www.joyme.com/note/09DRsdCd90RXYOKHKhRda7" class="more"
                           target="_blank">学习拿积分全攻略&gt;&gt;</a>
                    </div>
                    <h3 class="gift-center-title-2"><span>0</span>积分可领取...</h3>

                    <div class="gift-intro">
                        <ul class="clearfix">
                            <c:forEach items="${exchangeActivityPage.rows}" var="dto">
                                <li>

                                    <a href="${URL_WWW}/gift/${dto.gid}"><img src="${dto.gipic}"></a>

                                    <p><a href="${URL_WWW}/gift/${dto.gid}">${dto.title}</a></p>

                                    <div><span class="fl"><strong
                                            class="text-color-red">${dto.rn}</strong>人领取</span><span
                                            class="fr">剩余：${dto.sn}</span>
                                    </div>
                                    <c:choose>
                                        <c:when test="${dto.sn>0}">
                                            <a href="${URL_WWW}/gift/${dto.gid}" class="getNum">领号</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${URL_WWW}/gift/${dto.gid}"
                                               class="chooseNum">淘号</a>
                                        </c:otherwise>
                                    </c:choose>
                                </li>
                            </c:forEach>

                        </ul>
                    </div>
                </c:when>
                <c:otherwise>
                    <div style="display:block; margin-top:8px;" class="top-bg exchange-credits">
                        <ul class="clearfix">


                            <c:forEach items="${pointEnoughPage.rows}" var="dto" varStatus="st">
                                <li>
                                    <div><a href="${URL_WWW}/coin/${dto.gid}"><img width="160" height="160"
                                                                                   src="${dto.gipic}"></a>
                                    </div>
                                    <h2><a href="${URL_WWW}/coin/${dto.gid}">${dto.title}</a></h2>

                                    <p>${dto.desc}</p>

                                    <p>
                                        <span class="fl">${dto.point}积分/次</span>
                                        <span class="fr">剩余${dto.sn}个</span>
                                    </p>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:otherwise>
            </c:choose>
            <!-- 分页 -->
            <div class="pagecon clearfix">
                <c:set var="pageurl" value="${URL_WWW}/giftmarket/affordable"/>
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
