<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>好玩的网络游戏,好玩的单机游戏,好玩的手机游戏 - ${jmh_title}</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="Keywords" content="好玩的网络游戏,好玩的单机游戏,好玩的手机游戏">
    <meta name="description" content="着迷网游戏那点事频道提供推荐好玩的网络游戏,好玩的单机游戏和好玩的手机游戏等详细游戏那点事信息,是最真实的游戏那点事平台。"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <script src="${URL_LIB}/static/js/common/seajs.js"></script>
    <script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
    <script>
        seajs.use("${URL_LIB}/static/js/init/common-init.js")
    </script>
</head>
<body>
<div id="wraper">
<!--头部-->
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>

<!-- =========广场内容========== -->
<div class="index-2013-box clearfix">
	<!-- 导航 -->
	<div class="subnav">
		<ul>
			<li class="subnav-menu-1"><a href="javascript:void(0);" class="current">游戏那点事</a></li>
			<li class="subnav-menu-2"><a href="${URL_WWW}/category/handbook">游戏专区</a></li>
			<li class="subnav-menu-3"><a href="${URL_WWW}/category/hot" >着迷热点</a></li>
			<li class="subnav-menu-4"><a href="${URL_WWW}/category/recommend">专题推荐</a></li>
		</ul>
	</div>

	<div class="location">当前位置：<a href="${URL_WWW}/index">首页</a> &gt; 游戏那点事</div>
	<!-- 左侧 -->
	<div class="columns-left">
		<h2 class="index-2013-title">游戏那点事</h2>
		<ul class="special-article special-article-type2">
              <c:forEach var="element" items="${list}">
			<li>
				<a target="_blank" href="${URL_WWW}/note/${element.elementId}" class="game-pic" title="<c:out value="${element.title}"/>">
                    <p>
                        <c:choose>
                            <c:when test="${element.thumbimgSmall!=null && element.thumbimgSmall!=''}"><img width="188" src="${uf:parseOrgImg(element.thumbimgSmall)}" alt="<c:out value="${element.title}"/>"></c:when>
                            <c:otherwise><img width="188" src="${uf:parseOrgImg(element.thumbimg)}" alt="<c:out value="${element.title}"/>"></c:otherwise>
                        </c:choose>
                    </p>
                </a>
				<div>
					<h2><a target="_blank" href="${URL_WWW}/note/${element.elementId}" title="<c:out value="${element.title}"/>"><c:out value="${jstr:subStr(element.title,39 ,'…')}"/></a></h2>
					<p><a target="_blank" href="${URL_WWW}/people/${element.domain}">${element.screenName}</a>&nbsp;&nbsp;发表于&nbsp;&nbsp;${dateutil:parseDate(element.createDate)}&nbsp;&nbsp;<a target="_blank" href="${URL_WWW}/note/${element.elementId}" class="comment-num">评论(${element.replyTimes})</a></p>
					<span><c:out value="${jstr:subStr(element.desc,89 ,'…')}"/></span>
				</div>
                <%--<c:if test="${fn:length(element.extField2)>0}">--%>
				<%--<span class="level-${element.extField2}"></span>--%>
                <%--</c:if>--%>
			</li>
            </c:forEach>
		</ul>

		<!-- 分页 -->
        <div class="pagecon clearfix">
            <c:set var="pageurl" value="${URL_WWW}/category/assessment"/>
            <%@ include file="/views/jsp/page/page.jsp" %>
        </div>
	</div>

	<!-- 右侧 -->
	<div class="columns-right">

		<!-- 着迷热点 -->
        <c:if test="${fn:length(hotList)>0}">
		<h2 class="index-2013-title">着迷热点</h2>
		<ul class="index-2013-hot-news">
            <c:forEach var="hot" items="${hotList}">
			<li><a target="_blank" href="${hot.link}" title="<c:out value="${hot.title}"/>">${hot.extField1}<c:out value="${jstr:subStr(hot.title,15 ,'…')}"/></a></li>
            </c:forEach>
		</ul>
        </c:if>

        <c:if test="${fn:length(handbookList)>0}">
        <h2 class="index-2013-title">游戏专区</h2>
        <div class="colmns-right-recommend-type2">
			<ul class="game-measured">
                <c:forEach var="handbook" items="${handbookList}" varStatus="st">
				<li <c:if test="${st.index%2==1}">class="bg"</c:if>>
					<a href="${handbook.link}" class="game-pic" target="_blank" title="<c:out value="${handbook.title}"/>">
						<p>
							<img src="${uf:parseOrgImg(handbook.thumbimg)}" alt="<c:out value="${handbook.title}"/>">
						</p>
					</a>
					<div>
						<a href="${handbook.link}" target="_blank"  title="<c:out value="${handbook.title}"/>"><c:out value="${jstr:subStr(handbook.title,13 ,'…')}"/></a>
						<p><c:out value="${jstr:subStr(handbook.desc,31 ,'…')}"/></p>
						<span>${dateutil:parseDate(handbook.createDate)}</span>
					</div>
				</li>
                </c:forEach>
			</ul>
		</div>
        </c:if>

		<!-- 攻略资料wiki -->
        <%--<c:if test="${wiki!=null}">--%>
		<%--<h2 class="index-2013-title">攻略资料wiki</h2>--%>
		<%--<ul class="recommendwiki">--%>
			<%--<li><a target="_blank" href="${wiki.link}" title="<c:out value="${wiki.title}"/>"><img width="284" height="140" alt="<c:out value="${wiki.title}"/>" src="${uf:parseOrgImg(wiki.thumbimg)}"><span></span></a></li>--%>
		<%--</ul>--%>
        <%--</c:if>--%>

        <c:if test="${fn:length(topicList)>0}">
        <h2 class="index-2013-title">专题推荐</h2>
        <div class="colmns-right-recommend-type2">
			<ul class="game-measured">
                <c:forEach var="recommend" items="${topicList}" varStatus="st">
				<li <c:if test="${st.index%2==1}">class="bg"</c:if>>
					<a href="${recommend.link}" class="game-pic" target="_blank" title="<c:out value="${recommend.title}"/>">
						<p>
							<img src="${uf:parseOrgImg(recommend.thumbimg)}" alt="<c:out value="${recommend.title}"/>">
						</p>
					</a>
					<div>
						<a href="${recommend.link}" target="_blank" title="<c:out value="${recommend.title}"/>"><c:out value="${jstr:subStr(recommend.title,13 ,'…')}"/></a>
						<p><c:out value="${jstr:subStr(recommend.desc,31 ,'…')}"/></p>
						<span>${dateutil:parseDate(recommend.createDate)}</span>
					</div>
				</li>
                </c:forEach>
			</ul>
		</div>
        </c:if>
	</div>
</div>
</div>
<!--页尾-->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
</body>
</html>
