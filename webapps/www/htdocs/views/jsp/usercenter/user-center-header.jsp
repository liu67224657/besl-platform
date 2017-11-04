<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<!-- 导航 开始 -->
<div class="header-box">
    <div class="navbar navbar-default" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <ul class="nav navbar-nav web-hide joyme-nav-l">
                    <li>
                        <a href="${URL_WWW}">
                            返回着迷首页&gt;&gt;
                        </a>
                    </li>
                    <li>
                        <a href="${URL_WWW}/news/official/">
                            手游资讯
                        </a>
                    </li>
                    <li>
                        <a href="${URL_WWW}/news/reviews/">
                            着迷评测
                        </a>
                    </li>
                    <li>
                        <a href="http://wiki.${DOMAIN}">
                            着迷WIKI
                        </a>
                    </li>
                    <li>
                        <a href="${URL_WWW}/gift">
                            礼包中心
                        </a>
                    </li>
                    <li>
                        <a href="${URL_WWW}/news/blue/">
                            精品推荐
                        </a>
                    </li>
                    <li><a target="_blank" href="http://wanba.joyme.com/">精彩问答</a></li>
                </ul>
                <div class="nav navbar-nav  joyme-nav-r navbar-right">
                    <script>
                        document.write(unescape("%3Cscript src='http://passport.${DOMAIN}/auth/header/userinfo?t=wiki%26v=" + Math.random() + "'  type='text/javascript'%3E%3C/script%3E"));
                    </script>
                    <!-- 登陆之后 结束-->
                </div>
                <a class="navbar-brand visible-xs joyme-logo" href="${URL_M}">
                                    <i class="fa fa-angle-left"></i>返回着迷网</a>
            </div>
        </div>
    </div>
</div>
<!-- 导航 结束 -->