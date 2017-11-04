<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/jstllibs.jsp" %>
document.write('<style type="text/css">');
document.write('#joyme-logo{float:left; width:120px; height:44px; background:url(${URL_LIB}/static/theme/default/img/joyme-wiki-img.png) no-repeat left 0; margin:4px 26px 0 10px;}')
document.write('#joyme-wiki-top-nav{height:50px; background:url(${URL_LIB}/static/theme/default/img/joyme-wiki-img.png) repeat left -45px; overflow:hidden}')
document.write('#joyme-wiki-top-nav span{float:left;background:url(${URL_LIB}/static/theme/default/img/joyme-wiki-img.png) no-repeat -118px -158px; padding-left:3px; }')
document.write('#joyme-wiki-top-nav span a{display:inline-block; height:50px; line-height:50px; padding:0 16px; color:#fff; font-weight:bold; text-decoration:none; font-size:12px;}')
document.write('#joyme-wiki-top-nav span a:hover{background:url(${URL_LIB}/static/theme/default/img/joyme-wiki-img.png) repeat-x left -101px}')
document.write('</style>');
document.write('<div id="joyme-wiki-top-nav">')
document.write('<a href="http://www.joyme.com" id="joyme-logo"></a>')
document.write('<span><a href="http://www.joyme.com/">首页</a></span>');
document.write('<span><a href="http://www.joyme.com/news/official/">手游资讯</a></span>');
document.write('<span><a href="http://wiki.joyme.com/">WIKI</a></span>');
document.write('<span><a href="http://www.joyme.com/gift">礼包中心</a></span>');
document.write('<span><a href="http://bbs.joyme.com/">论坛</a></span>');
document.write('<span><a href="http://html.joyme.com/mobile/gameguides.html">应用</a></span>');
document.write('</div>');


<%--document.write('<link href="${URL_LIB}/static/theme/default/css/core.css?" rel="stylesheet" type="text/css"/>');--%>
<%--document.write('<link href="${URL_LIB}/static/theme/default/css/global.css?" rel="stylesheet" type="text/css"/>');--%>
<%--document.write('<link href="${URL_LIB}/static/theme/default/css/common.css?" rel="stylesheet" type="text/css"/>');--%>
<%--document.write('<div id="head"><div class="headt">' +--%>
<%--'<div class="head_t clearfix">' +--%>
<%--'<a href="${URL_WWW}" title="着迷" class="logo"></a>'+--%>
<%--'</div>' +--%>
<%--'<div class="headb">' +--%>
<%--'<div class="head_b clearfix">' +--%>
<%--'<ul class="nav_list" id="header_nav_list">' +--%>
    <%--'<li><a href="${URL_WWW}" class="listbold <c:if test="${hdflag=='index'}">on</c:if>"><span>&nbsp;首&nbsp;页&nbsp;</span></a></li>' +--%>
    <%--'<li><a href="${URL_WWW}/game" class="listbold <c:if test="${hdflag=='game'}">on</c:if>"><span>&nbsp;游&nbsp;戏&nbsp;</span></a></li>' +--%>
    <%--'<li><a href="${URL_WWW}/event" class="listbold <c:if test="${hdflag=='activity'}">on</c:if>" ><span>&nbsp;活&nbsp;动&nbsp;</span></a></li>'+--%>
    <%--'<li><a href="http://html.joyme.com/mobile/gameguides.html" class="listbold" ><span>手机看攻略</span></a></li>'+--%>
<%--'</ul>' +--%>
<%--'</div>' +--%>
<%--'</div></div></div>');--%>