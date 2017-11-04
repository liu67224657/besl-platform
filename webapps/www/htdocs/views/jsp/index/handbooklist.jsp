<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>网页游戏攻略,单机游戏攻略,手机游戏攻略 - ${jmh_title}</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="Keywords" content="网页游戏攻略,单机游戏攻略,手机游戏攻略">
    <meta name="description" content="着迷网热门攻略频道提供热门网页游戏攻略、单机游戏攻略和手机游戏攻略等最新游戏攻略,让玩家第一时间掌握各类热门游戏秘籍与攻略。"/>
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
			<li class="subnav-menu-1"><a href="${URL_WWW}/category/assessment">游戏那点事</a></li>
			<li class="subnav-menu-2"><a href="javascript:void(0);" class="current">游戏专区</a></li>
			<li class="subnav-menu-3"><a href="${URL_WWW}/category/hot" >着迷热点</a></li>
			<li class="subnav-menu-4"><a href="${URL_WWW}/category/recommend">专题推荐</a></li>
		</ul>
	</div>

	<div class="location">当前位置：<a href="${URL_WWW}/index">首页</a> &gt; 热门攻略</div>
	<!-- 左侧 -->
	<div class="columns-left">
		<h2 class="index-2013-title">游戏专区</h2>
		<ul class="special-article">
           <c:forEach var="element" items="${list}">
			<li>
				<a href="${element.link}" class="game-pic" target="_blank" title="<c:out value="${element.title}"/>">
                    <p>
                        <c:choose>
                            <c:when test="${element.thumbimgSmall!=null && element.thumbimgSmall!=''}"><img width="188" src="${uf:parseOrgImg(element.thumbimgSmall)}" alt="<c:out value="${element.title}"/>"></c:when>
                            <c:otherwise><img width="188" src="${uf:parseOrgImg(element.thumbimg)}" alt="<c:out value="${element.title}"/>"></c:otherwise>
                        </c:choose>
                    </p>
                </a>
				<div>
					<h2><a href="${element.link}" target="_blank" title="<c:out value="${element.title}"/>"><c:out value="${jstr:subStr(element.title,22 ,'…')}"/></a></h2>
                    <p>${dateutil:parseDate(element.createDate)}</p>
                    <span><c:out value="${jstr:subStr(element.desc,105 ,'…')}"/></span>
				</div>
			</li>
            </c:forEach>
		</ul>

		<!-- 分页 -->
        <div class="pagecon clearfix">
            <c:set var="pageurl" value="${URL_WWW}/category/handbook"/>
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

		<!-- 游戏那点事 -->
        <c:if test="${fn:length(assessmentList)>0}">
		<h2 class="index-2013-title">游戏那点事</h2>
		<div class="colmns-right-recommend">
			<ul class="game-measured">
                 <c:forEach var="assessment" items="${assessmentList}" varStatus="st">
				<li  <c:if test="${st.index%2==1}">class="bg"</c:if>>
					<a target="_blank" class="game-pic" href="${URL_WWW}/note/${assessment.elementId}" title="<c:out value="${assessment.title}"/>">
						<p>
							<img src="${uf:parseOrgImg(assessment.thumbimg)}" alt="<c:out value="${assessment.title}"/>">
						</p>
					</a>
					<div>
						<a target="_blank" href="${URL_WWW}/note/${assessment.elementId}" title="${assessment.title}"><c:out value="${jstr:subStr(assessment.title,11 ,'…')}"/></a>
						<p><c:out value="${jstr:subStr(assessment.desc,27 ,'…')}"/></p>
						<span>${dateutil:parseDate(assessment.createDate)}</span>
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
