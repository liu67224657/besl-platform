<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/jstllibs.jsp" %>
<!--头部开始-->
<div id="joyme-head-2013">
    <div class="head-2013-box clearfix">
        <a id="joyme-logo" href="${URL_WWW}" alt="着迷网"></a>

        <div class="head-2013-left">
            <span><a href="${URL_WWW}">首页</a></span>
            <span><a href="http://www.joyme.com/news/official/">手游资讯</a></span>
            <span><a href="http://wiki.joyme.com/">WIKI</a></span>
            <span><a href="${URL_WWW}/gift"
                     <c:if test="${hdflag=='giftmarket'}">class="on"</c:if> >礼包中心</a></span>
            <span><a href="http://bbs.joyme.com/">论坛</a></span>
            <span><a href="http://html.joyme.com/mobile/gameguides.html">应用</a></span>
        </div>
        <div class="head-2013-right">
            <!-- 搜索框 -->
            <div class="search-new">
                <form action="">
                    <input id="stype" type="hidden" value="" name="stype">
                    <input id="txt_search" type="text" class="search-new-textInput" value="找找你感兴趣的..."
                           onblur="if(this.value==''){this.value='找找你感兴趣的...'; this.style.color='#989898';}"
                           onfocus="if(this.value=='找找你感兴趣的...'){this.value=''; this.style.color='#666';}"
                           autocomplete="off" name="key">
                    <input id="sericon" class="search-new-submit" type="button" value="">
                </form>
                <div id="search_tips" class="search_sth" style="display:none">
                    <div class="searcht"></div>
                    <div class="searchc">
                        <ul>
                            <h3>请选择搜索范围</h3>
                            <li id="content">含"<b></b>"的文章 >></li>
                            <li id="profile">含"<b></b>"的人 >></li>
                            <li id="group">含"<b></b>"的小组 >></li>
                            <li id="game">含"<b></b>"的游戏 >></li>
                        </ul>
                    </div>
                    <div class="searchb"></div>
                </div>
            </div>

            <c:choose>
                <c:when test="${userSession!=null}">
                    <div class="logined">
                        <a href="#" class="userArea" id="header_func_link">
                            <img src="${icon:parseIcon(userSession.icon, userSession.sex, "")}"/>
                        </a>
                            <%--<a href="${URL_WWW}/invite/invitepage" class="inviteFriend"><span>邀请好友</span></a>--%>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="unlogin">
                        <a href="http://passport.${DOMAIN}/auth/thirdapi/qq/bind<c:if test="${fn:indexOf(redr,'index')<0}">?reurl=${redr}</c:if>"
                           class="qq" title="qq号码登录" rel="nofollow"></a>
                        <a href="http://passport.${DOMAIN}/auth/thirdapi/sinaweibo/bind<c:if test="${fn:indexOf(redr,'index')<0}">?reurl=${redr}</c:if>"
                           class="weibo" title="新浪微博登录" rel="nofollow"></a>
                        <a href="http://passport.${DOMAIN}/auth/registerpage">注册</a>
                        <a href="http://passport.${DOMAIN}/auth/loginpage">登录</a>
                    </div>
                </c:otherwise>
            </c:choose>

        </div>
    </div>
    <c:if test="${userSession!=null}">
        <div class="nav_item" style="display:none" id="header_func_area">
            <div class="itemt"></div>
            <div class="itemc">
                <ul>
                        <%--<li class="first"><a href="${URL_WWW}/people/${userSession.domain}">我的博客</a></li>--%>
                    <li class="first"><a href="${URL_WWW}/profile/customize/basic">设置</a></li>
                    <%--<li class="first"><a href="${URL_WWW}/activity/pay">充值</a></li>--%>
                    <li>
                        <a href="http://passport.${DOMAIN}/auth/logout?reurl=<c:out value="${redr}"/>">退出</a>
                    </li>
                </ul>
            </div>
            <div class="itemb"></div>
        </div>
    </c:if>
    <span id="memo_f"></span>
    <span id="login_f"></span>
</div>