<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script type="text/javascript" src="${URL_LIB}/static/js/header.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/jmdialog/jmdialog.js"></script>
<div id="head">

    <div class="header"><a href="<c:out value='${URL_WWW}'/>" title="着迷" class="logo"></a></div>
    <div class="head_function">
        <c:choose>
            <c:when test="${userSession!=null}">
                <a class="userLink" id="userLink"
                   href="${URL_WWW}/people/<c:out value="${userSession.blogwebsite.domain}"/>/>"><span
                        id="bnameid">${userSession.blogwebsite.screenName}</span></a>，欢迎你
                &nbsp;|&nbsp;<a href="${URL_WWW}/profile/customize">设置</a>
                &nbsp;|&nbsp;<a href="${URL_WWW}/invite/invitepage">邀请好友</a>
                &nbsp;|&nbsp;<a href="javascript:void(0)" onclick="ajaxLogout();">退出</a>
            </c:when>
            <c:otherwise>
                <a href="${URL_WWW}/registerpage">注册</a>
                &nbsp;|&nbsp;<a href="#" onclick="maskLogin();this.blur()">登录</a>&nbsp;|&nbsp;
                <a href="${URL_WWW}/discovery">随便看看</a>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="head_nav">
        <c:if test="${userSession!=null}">
            <li><a href="<c:out value='${URL_WWW}'/>/home">我的首页</a></li>
            <c:choose>
                <c:when test="${userSession.blogwebsite.uno eq profile.blog.uno}">
                    <li class="hover"><a href="${URL_WWW}/people/<c:out value="${userSession.blogwebsite.domain}"/>" id="blogLink">我的博客</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="${URL_WWW}/people/<c:out value="${userSession.blogwebsite.domain}"/>" id="blogLink">我的博客</a></li>
                </c:otherwise>
            </c:choose>
        </c:if>
        <li><a href="<c:out value="${URL_WWW}"/>/discovery/hot" title="看热门">看热门</a></li>
        <li><a href="<c:out value="${URL_WWW}"/>/talent" title="看达人"><span>看达人</span></a></li>
        <li><a href="<c:out value="${URL_WWW}"/>/discovery" title="看看大家都在发生什么">随便看看</a></li>
        <%--<c:if test="${userSession!=null}">--%>
        <%--<li class="li4<c:if test="${hdflag!='game'}">d</c:if>"><a href="${ctx}/game">游戏打分</a></li>--%>
        <%--</c:if>--%>
    </div>
    <div class="head_search">
        <form id="searchForm" method="post" action="">
            <input type="hidden" name="stype" value="" id="stype"/>
            <c:choose>
                <c:when test="${tagtitle!=null}">
                    <c:set var="slt" value="${tagtitle}"/>
                </c:when>
                <c:otherwise>
                    <c:set var="slt" value="搜索您感兴趣的内容或人"/>
                </c:otherwise>
            </c:choose>
            <input autocomplete="off" id="txt_search" name="key" type="text"
                   onblur="if(this.value==''){this.value='搜索您感兴趣的内容或人'; this.style.color='#989898';}"
                   onfocus="if(this.value=='搜索您感兴趣的内容或人'){this.value=''; this.style.color='#dedede';}  searchTips($(this));"
                   value="${slt}" class="search_text"/>
            <input name="" type="submit" class="search_but" value=""/>
        </form>
        <div id="serach_tips" class="serach_tips" style="display:none;">
            <ul>
                <p>请选择搜素范围</p>
                <li id="sText">含“<b></b>”的博文</li>
                <li id="sBlog">含“<b></b>”的博主</li>
                <li id="sGame">含“<b></b>”的游戏</li>
            </ul>
        </div>
    </div>
    <div id="message_div" class="message_box" style="top:-32px">
        <div class="message_wrap">
            <div class="message" id="memo_message" style="display:none">
                <div class="message_x"><a title="我知道了" href="javascript:void(0);">×</a></div>
            </div>
        </div>
    </div>
</div>