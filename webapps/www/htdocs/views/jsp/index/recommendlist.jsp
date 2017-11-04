<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>网络游戏大全,单机游戏大全,手机游戏下载 - ${jmh_title}</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="Keywords" content="网络游戏大全,单机游戏大全,手机游戏下载">
    <meta name="description" content="着迷网专题推荐频道汇集各类网络游戏,单机游戏和手机游戏,并提供各类游戏的新闻、攻略、下载、秘籍、补丁等相关资料,是网络游戏，单机游戏和手机游戏大全。"/>
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
			<li class="subnav-menu-1"><a href="${URL_WWW}/category/assessment" >游戏那点事</a></li>
			<li class="subnav-menu-2"><a href="${URL_WWW}/category/handbook">游戏专区</a></li>
			<li class="subnav-menu-3"><a href="${URL_WWW}/category/hot" >着迷热点</a></li>
			<li class="subnav-menu-4"><a href="javascript:void(0);" class="current">专题推荐</a></li>
		</ul>
	</div>

	<div class="location">当前位置：<a href="${URL_WWW}/index">首页</a> &gt; 专题推荐</div>
	<!-- 左侧 -->
	<div class="columns-left">
		<h2 class="index-2013-title">专题推荐</h2>
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
            <c:set var="pageurl" value="${URL_WWW}/category/recommend"/>
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
	</div>

</div>
</div>
<!--页尾-->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
</body>
</html>
