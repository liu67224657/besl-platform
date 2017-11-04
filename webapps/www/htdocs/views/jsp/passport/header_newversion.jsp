<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/jstllibs.jsp" %>
<link href="${URL_LIB}/static/theme/default/css/header_simple.css?${version}" rel="stylesheet" type="text/css"/>
<div id="joyme-head-2015">
    <div class="joyme-head-nav">
        <div class="joyme-left-nav">
            <a href="${URL_WWW}">返回着迷首页 >></a>
            <a href="http://www.joyme.com/news/official/">手游资讯</a>
            <a href="http://wiki.joyme.com/">着迷WIKI</a>
            <a href="${URL_WWW}/gift">礼包中心</a>
            <a href="http://bbs.joyme.com">论 坛</a>
            <a href="http://html.joyme.com/mobile/gameguides.html">应用下载</a>
        </div>
        <div class="joyme-right-nav">
            <div class="joyme-loginbar">
                <c:choose>
                    <c:when test="${userSession!=null}">
                        <div class="after">
                        <span>
                        	<em>欢迎你，</em>
                            <a class="joyme-userName" href="javascript:void(0);">${userSession.nick}</a>
                            <cite></cite>
                            <div>
                                <ul>
                                    <li><a href="${URL_WWW}/profile/customize/basic">设置</a></li>
                                    <li><a href="http://passport.${DOMAIN}/auth/logout?reurl=<c:out value="${redr}"/>">退出</a>
                                    </li>
                                </ul>
                            </div>
                        </span>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="not">
                            <a href="http://passport.${DOMAIN}/auth/thirdapi/sinaweibo/bind<c:if test="${fn:indexOf(redr,'index')<0}">?reurl=${redr}</c:if>"
                               class="weibo-login" title="微博登录">微博登录</a>
                            <a href="http://passport.${DOMAIN}/auth/thirdapi/qq/bind<c:if test="${fn:indexOf(redr,'index')<0}">?reurl=${redr}</c:if>"
                               class="qq-login" title="QQ登录">QQ登录</a>
                        <span>
                            <a href="http://passport.${DOMAIN}/auth/loginpage" class="a-login">登录</a><a
                                href="http://passport.${DOMAIN}/auth/registerpage" class="a-regsiter">注册</a>
                        </span>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
