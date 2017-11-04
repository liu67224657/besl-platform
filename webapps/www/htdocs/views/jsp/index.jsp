<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>着迷网Joyme.com_移动游戏攻略第一站|iphone游戏|ipad游戏|安卓手机游戏</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta property="qh.webmaster" content="5225f55970236"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="Keywords" content="手机游戏，iphone游戏，ipad游戏，安卓手机游戏">
    <meta name="description" content="游戏攻略找着迷，百万玩家来帮你。囊括iphone游戏，ipad游戏,安卓手机游戏，每日更新过千条资料攻略和最资深精英玩家心得贡献，手机电脑均可浏览。"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <script src="${URL_LIB}/static/js/common/seajs.js"></script>
    <script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
    <script>
        var fMaxPage=${famousPage!=null?famousPage.maxPage:0};
        var fgamePage=${fpage};
        seajs.use("${URL_LIB}/static/js/init/index-init.js")
    </script>
    <style type="text/css">
		body{background:url(${URL_LIB}/static/img/index-ad-1.jpg) no-repeat center 97px #000;}
		#ad-link-left, #ad-link-right{display:block; width:140px; height:1240px; position:absolute; top:36px;}
		#ad-link-left{left:-140px;}
		#ad-link-right{right:-140px;}
	</style>
</head>
<body>
<div id="wraper">
<!--头部-->
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>

<!-- =========广场内容========== -->
<div class="index-2013-box clearfix">
    <%@ include file="/hotdeploy/views/jsp/index/adtop.jsp" %>
    <a href="http://www.joyme.com/wiki/macn/index.shtml" title="百万亚瑟王最权威的中文百科" target="_blank" id="ad-link-left"></a>
    <a href="http://www.joyme.com/game/WindRunner" title="旋风跑跑度假最霸气专区开启" target="_blank" id="ad-link-right"></a>


    <!-- 试试手气 -->
    ${findgame}

    <%@ include file="/hotdeploy/views/jsp/index/banner.jsp" %>

    <!-- 左侧栏 -->
    <div class="index-2013-left">
        <!-- 游戏评测 -->
        ${indexPageDTO.wikiModule}
        <!-- AD -->
        <%@ include file="/hotdeploy/views/jsp/index/ad1.jsp" %>
        <!-- 最新攻略 -->
        ${indexPageDTO.assessment}
        <%--${indexPageDTO.handbook}--%>

    </div>

    <!-- 中间栏 -->
    <div class="index-2013-center">
        <!-- 热门资讯 -->
        ${indexPageDTO.hotNews}

        <!-- AD -->
        <%@ include file="/hotdeploy/views/jsp/index/ad2.jsp" %>

        <!-- 免费游戏 -->
        ${indexPageDTO.handbook}
        <%--${indexPageDTO.indexTopic}--%>

        <!-- 推荐小组 -->
        ${indexPageDTO.group}
    </div>

    <!-- 右侧栏 -->
    <div class="index-2013-right">
          <!-- 热门活动 -->
          <h2 class="index-2013-title">热门活动</h2>
          <%@ include file="/hotdeploy/views/jsp/index/ad3.jsp" %>
          ${indexPageDTO.recommend}

        <!-- 他们在着迷 -->
        <h2 class="index-2013-title">着迷达人堂<a id="reload_talent" class="reload-daren" href="javascript:void(0)">看后排</a>
        </h2>
        <ul id="daren-tj" class="daren-tj daren-tj-2013 clearfix">
            <c:forEach items="${talentList}" var="tal" varStatus="st">
                <li <c:if test="${st.last}">class="noborder"</c:if>>
                    <div class="user_info">
                        <a title="${tal.screenName}" target="_blank" href="${URL_WWW}/people/${tal.domain}" class="user_img">
                            <img width="33" height="33" src="${uf:parseSFace(tal.thumbimg)}">
                        </a>
                        <a title="${tal.screenName}" target="_blank" href="${URL_WWW}/people/${tal.domain}" class="user_name">
                            ${jstr:subStr(tal.screenName,8,'…')}
                        </a>
                        <c:if test="${fn:length(tal.verifyType)>0 &&  tal.verifyType!='n'}">
                            <a href="javascript:void(0);" class="${tal.verifyType}vip"
                               title="<fmt:message key="verify.profile.${tal.verifyType}" bundle="${userProps}"/>"></a>
                        </c:if>
                        <p> ${jstr:subStr(tal.desc,10,'…')}
                        </p>
                    </div>
                    <c:forEach items="${tal.lastContentList}" var="content">
                        <p>• <a href="${URL_WWW}/note/${content.contentId}/"
                                target="_blank">
                            <c:choose>
                                <c:when test="${fn:length(content.subject)>0}">
                                    <c:choose>
                                        <c:when test="${fn:length(content.subject)>18}"><c:out
                                                value="${fn:substring(content.subject,0,18)}"/>…</c:when>
                                        <c:otherwise>${content.subject}</c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${fn:length(content.content)>18}"><c:out
                                                value="${fn:substring(content.content,0,18)}"/>…</c:when>
                                        <c:otherwise>${content.content}</c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                        </a>
                        </p>
                    </c:forEach>
                </li>
            </c:forEach>
        </ul>

        <!-- 官方微博 -->
        <%@ include file="/hotdeploy/views/jsp/index/ad4.jsp" %>
        <h2 class="index-2013-title">官方微博</h2>

        <div class="tab01-hd">
            <ul class="tab01_hd clearfix">
                <%--<li><a name="link_showopen" class="active" data-openid="weixin" href="javascript:void(0);">官方微信</a></li>--%>
                <li><a name="link_showopen" class="active" data-openid="sinaweibo" href="javascript:void(0);">新浪微博</a></li>
            </ul>
        </div>
        <ul id="iframeWeibo">
            <li>
                <%--<div id="frame_show_weixin" class="index-weixin">--%>
                    <%--<a href="http://www.joyme.com/note/2ImZdCCGddbbUe0XkZ_Hh-a" target="_blank">--%>
                        <%--<img src="http://lib.joyme.com/static/theme/default/img/weixin_logo.jpg" width="184">--%>
                    <%--</a>--%>
                <%--</div>--%>
            </li>
        </ul>

        <!-- 着迷在进步 -->
        ${indexPageDTO.progress}
    </div>

</div>
</div>
<!--页尾-->
<%@ include file="/views/jsp/tiles/footer-partner.jsp" %>
</body>
</html>
