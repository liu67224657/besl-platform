<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/jslibs.jsp" %>

<%
  response.setHeader("Pragma","No-cache");
  response.setHeader("Cache-Control","no-cache");
  response.setDateHeader("Expires", 0);
%>

document.write('<script src="${URL_LIB}/static/js/common/seajs.js" data-main="${URL_LIB}/static/js/init/jsheader-init"></script>');
document.write('<link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css">');
document.write('<link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css">');
document.write('<div id="joyme-head-2013">');
document.write('<div class="head-2013-box clearfix">');
document.write('<a id="joyme-logo" href="${URL_WWW}" alt="着迷网"></a> ');
document.write('<div class="head-2013-left"> ');
document.write('<span><a href="${URL_WWW}" <c:if test="${hdflag=='index'}">class="on"</c:if> >首页</a></span>');
document.write('<span><a href="http://www.joyme.com/news/official/">手游资讯</a></span>');
document.write('<span><a href="http://wiki.joyme.com/">WIKI</a></span>');
document.write('<span><a href="${URL_WWW}/gift" <c:if test="${hdflag=='giftmarket'}">class="on"</c:if> >礼包中心</a></span>');
document.write('<span><a href="http://bbs.joyme.com/">论坛</a></span>');
document.write('<span><a href="http://html.joyme.com/mobile/gameguides.html">应用</a></span>');
document.write('</div>');
document.write('<div class="head-2013-right">');
			<!-- 搜索框 -->
<%--document.write('<div class="search-new">');--%>
<%--document.write('<input id="stype" type="hidden"  value="" name="stype">');--%>
<%--document.write('<input id="txt_search" type="text" class="search-new-textInput" value="找找你感兴趣的..." onblur="if(this.value==\'\'){this.value=\'找找你感兴趣的...\'; this.style.color=\'#989898\';}" onfocus="if(this.value==\'找找你感兴趣的...\'){this.value=\'\'; this.style.color=\'#666\';}"autocomplete="off" name="key"><input id="sericon" class="search-new-submit" type="button" value="">');--%>
<%--document.write('<div id="search_tips" class="search_sth"  style="display:none">');--%>
<%--document.write('<div class="searcht"></div>');--%>
<%--document.write('<div class="searchc">');--%>
<%--document.write('<ul>');--%>
<%--document.write('<h3>请选择搜索范围</h3>');--%>
<%--document.write('<li id="content">含"<b></b>"的文章 >></li>');--%>
<%--document.write('<li id="profile">含"<b></b>"的人 >></li>');--%>
<%--document.write('<li id="group">含"<b></b>"的小组 >></li> ');--%>
<%--document.write('<li id="game">含"<b></b>"的游戏 >></li>');--%>
<%--document.write('</ul>');--%>
<%--document.write('</div>');--%>
<%--document.write('<div class="searchb"></div>');--%>
<%--document.write('</div>');--%>
<%--document.write('</div>');--%>

            <c:choose>
                <c:when test="${userSession!=null}">
document.write('<div class="logined" >');
document.write('<a href="#" class="userArea" id="header_func_link">');
document.write('<img src="${uf:parseFacesInclude(userSession.blogwebsite.headIconSet,userSession.userDetailinfo.sex,"m" , true,0,1)[0]}"/>');
document.write('</a>');
document.write('<a href="${URL_WWW}/invite/invitepage" class="inviteFriend"><span>邀请好友</span></a>');
document.write('</div>');
                </c:when>
                <c:otherwise>
document.write('<div class="unlogin">');
document.write('<a href="${URL_WWW}/profile/sync/qq/bind?redr=${redr}" class="qq" title="qq号码登录"></a>');
document.write('<a href="${URL_WWW}/profile/sync/sinaweibo/bind?redr=${redr}" class="weibo" title="新浪微博登录"></a>');
<%--document.write('<a href="${URL_WWW}/profile/sync/renren/bind?redr=${redr}" class="renren" title="人人登录"></a>');--%>
document.write('<a href="${URL_WWW}/registerpage?redr=${redr}">注册</a>');
document.write('<a href="${URL_WWW}/loginpage?redr=${redr}">登录</a>');
document.write('</div>');
                </c:otherwise>
            </c:choose>
document.write('</div>');
document.write('</div>');

 <c:if test="${userSession!=null}">
document.write('<div class="nav_item" style="display:none" id="header_func_area">');
document.write('<div class="itemt"></div>');
document.write('<div class="itemc">');
document.write('<ul>');
document.write('<li class="first"><a href="${URL_WWW}/people/${userSession.blogwebsite.domain}">我的博客</a></li>');
document.write('<li><a href="${URL_WWW}/profile/customize">设置</a></li>');
document.write('<li>');
document.write('<a href="http://passport.${DOMAIN}/auth/logout<c:if test="${redr ne null}">?reurl=<c:out value="${redr}"/></c:if>">退出</a>');
document.write('</li>');
document.write('</ul>');
document.write('</div>');
document.write('<div class="itemb"></div>');
document.write('</div>');
 </c:if>
document.write('<span id="memo_f"></span>');
document.write('<span id="login_f"></span>');
document.write('</div>');