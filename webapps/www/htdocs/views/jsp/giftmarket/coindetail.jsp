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
    <title>${detailDTO.title}_着迷积分兑换_${jmh_title}</title>
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
            href="${URL_WWW}/coin/">积分兑换区</a> &gt; 兑换详情页
    </div>
</div>

<div class="gift-box clearfix">
    <!-- 左侧 -->
    <div class="columns-left">
        <div class="cloumns-box">

            <div class="gift-detail">
                <dl>
                    <dd style="width:660px;">
                        <h2>${detailDTO.title}</h2>

                        <p class="p1">剩余：${detailDTO.sn}/${detailDTO.cn}</p>

                        <div class="processbar">
                            <div><span style="width:${(detailDTO.sn / detailDTO.cn)*100}%"></span></div>
                        </div>
                        <p class="p2">有效期：<c:choose><c:when test="${detailDTO.expire}">已过期</c:when><c:when
                                test="${!detailDTO.expire && detailDTO.endTime == null}">永久</c:when><c:otherwise>${detailDTO.endTime}</c:otherwise></c:choose><span
                                style="margin-left:90px;">兑换价格：<em
                                style="color:#0087f8">${detailDTO.point}积分</em></span></p>

                        <p class="p3">
                            <a href="javascript:void(0);" class="exchange-now" data-aid="${detailDTO.aid}"
                               data-gsid="${detailDTO.gid}" data-cpoint="${detailDTO.point}" data-pointamount="${pointAmount}">立即兑换</a></p>

                        <p class="p4"><c:if test="${detailDTO.shareId>0}">分享本页得积分：
                            <a class="share-sina"
                               href="${URL_WWW}/share/content/sinaweibo/bind?sid=${detailDTO.shareId}"
                               target="_blank"></a>
                            <a class="share-tengxun"
                               href="${URL_WWW}/share/content/qweibo/bind?sid=${detailDTO.shareId}" target="_blank"></a>
                            <a class="share-qq" href="${URL_WWW}/share/content/qq/bind?sid=${detailDTO.shareId}"
                               target="_blank"></a>
                        </c:if>
                        </p>
                    </dd>
                </dl>
                <c:if test="${!empty detailDTO.wikiUrl}"><a href="${detailDTO.wikiUrl}"
                                                            class="goto">进入游戏专区&gt;&gt;</a></c:if>
            </div>

            <!-- 礼包说明 -->
            <div class="gift-discription">
                <h2>独家礼包内容：</h2>

                <p>${detailDTO.desc}</p>
            </div>

            <!-- 礼包下载 -->
            <c:if test="${detailDTO.gameIcon!=null && detailDTO.gameTitie!=null && detailDTO.gameType!=null && detailDTO.gameType.getValue()>0}">
                <div class="gift-center-title clearfix"><h2>相关游戏</h2></div>

                <div class="gift-download">
                    <dl>
                        <dt><c:if test="${detaiDTO.gameIcon!=''}"><img height="96" width="96"
                                                                       src="${detailDTO.gameIcon}"></c:if></dt>
                        <dd class="dd-1">
                            <h2>${detailDTO.gameTitie}</h2>

                            <p>
                                <c:if test="${detailDTO.gameType.getValue()!=0}">游戏类型： </c:if>
                                <c:if test="${detailDTO.gameType.hasKaiPai()}">卡牌&nbsp;&nbsp;</c:if>

                                <c:if test="${detailDTO.gameType.hasRpg()}">RPG&nbsp;&nbsp;</c:if>

                                <c:if test="${detailDTO.gameType.hasCasual()}">休闲&nbsp;&nbsp;</c:if>

                                <c:if test="${detailDTO.gameType.hasPuzzle()}">益智&nbsp;&nbsp;</c:if>

                                <c:if test="${detailDTO.gameType.hasMotion()}">动作&nbsp;&nbsp;</c:if>
                                <c:if test="${detailDTO.gameType.hasShoot()}">射击&nbsp;&nbsp;</c:if>
                                <c:if test="${detailDTO.gameType.hasTactics()}">策略&nbsp;&nbsp;</c:if>
                                <c:if test="${detailDTO.gameType.hasRole()}">角色扮演&nbsp;&nbsp;</c:if>
                                <c:if test="${detailDTO.gameType.hasOperate()}">模拟经营&nbsp;&nbsp;</c:if>
                                <c:if test="${detailDTO.gameType.hasPhysical()}">体育&nbsp;&nbsp;</c:if>
                                <c:if test="${detailDTO.gameType.hasSpeed()}">竞速&nbsp;&nbsp;</c:if>
                                <c:if test="${detailDTO.gameType.hasRelax()}">休闲&nbsp;&nbsp;</c:if>
                                <c:if test="${detailDTO.gameType.hasMusic()}">音乐&nbsp;&nbsp;</c:if>
                                <c:if test="${detailDTO.gameType.hasAdopt()}">养成&nbsp;&nbsp;</c:if>
                                <c:if test="${detailDTO.gameType.hasChess()}">棋牌&nbsp;&nbsp;</c:if>
                                <c:if test="${detailDTO.gameType.hasTriple()}">三消&nbsp;&nbsp;</c:if>
                                <c:if test="${detailDTO.gameType.hasDefence()}">塔防&nbsp;&nbsp;</c:if>
                                <c:if test="${detailDTO.gameType.hasRiddle()}">解谜&nbsp;&nbsp;</c:if>
                                <c:if test="${detailDTO.gameType.hasLove()}">恋爱&nbsp;&nbsp;</c:if>

                            <p><c:if test="${detailDTO.gameDeveloper!=''}">发行厂商：${detailDTO.gameDeveloper}</c:if></p>
                        </dd>
                        <dd class="dd-2">
                            <div class="fl">

                                <c:if test="${detailDTO.iosUrl!=''}"> <a href="${detailDTO.iosUrl}" target="_blank"
                                                                         class="d-iphone">苹果版下载</a> </c:if>
                                <c:if test="${detailDTO.androidUrl!=''}"> <a href="${detailDTO.androidUrl}"
                                                                             target="_blank"
                                                                             class="d-android">安卓版下载</a> </c:if>
                            </div>
                            <div class="fr">
                                <c:if test="${fn:length(detailDTO.iosUrl)>0 || fn:length(detailDTO.androidUrl)>0}">
                                    <img src="${URL_WWW}/acitivty/qrcode/generator?url=${URL_WWW}/appclick/${detailDTO.qrUrl}"
                                         width="96" height="96"/></c:if>
                            </div>
                        </dd>
                    </dl>
                </div>
            </c:if>
            <h3 class="result-title" style="padding-left:0px">你可能还感兴趣的礼包</h3>

            <div class="top-bg exchange-credits" style="display:block; margin-top:8px;" id="exchange-credits">
                <ul class="clearfix">
                    <c:choose>
                        <c:when test="${recommendList==null || empty recommendList}">
                            <li><p align="center">暂时没有活动推荐</p></li>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${recommendList}" var="dto">
                                <li>
                                    <div><a href="${URL_WWW}/coin/${dto.gid}">
                                        <%--<span class="fold-6">6折</span>--%>
                                        <img width="160" height="160" src="${dto.gipic}"></a>
                                    </div>
                                    <h2><a href="${URL_WWW}/coin/${dto.gid}">${dto.title}</a></h2>

                                    <p>${dto.desc}</p>

                                    <p>
                                        <span class="fl">${dto.point}积分/次</span>
                                        <span class="fr">剩余${dto.sn}个</span>
                                    </p>
                                </li>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </ul>
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
    seajs.use('${URL_LIB}/static/js/init/coin-init.js')
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
